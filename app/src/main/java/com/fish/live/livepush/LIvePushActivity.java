package com.fish.live.livepush;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fish.live.Constants;
import com.fish.live.LiveApplication;
import com.fish.live.R;
import com.fish.live.SplashActivity;
import com.fish.live.home.bean.IMLoginEvent;
import com.fish.live.livepush.presenter.LivePushPresenter;
import com.fish.live.livepush.view.LivePushCotract;
import com.fish.live.livevideo.LiveVideoActivity;
import com.fish.live.livevideo.adapter.LivePagerAdapter;
import com.fish.live.tencenttic.core.TICManager;
import com.fish.live.tencenttic.core.TICVideoRootView;
import com.fish.live.tencenttic.core.impl.TICReporter;
import com.flyco.tablayout.SlidingTabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.nucarf.base.ui.mvp.BaseMvpActivity;
import com.nucarf.base.utils.LogUtils;
import com.nucarf.base.utils.SharePreUtils;
import com.nucarf.base.widget.TitleLayout;
import com.nucarf.base.widget.ViewPagerSlide;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupMemberRoleType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupSelfInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.liteav.basic.log.TXCLog;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerGlobalConfig;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.teduboard.TEduBoardController;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LIvePushActivity extends BaseMvpActivity<LivePushPresenter> implements LivePushCotract.View, TICManager.TICIMStatusListener, TICManager.TICMessageListener, TICManager.TICEventListener {


    private final String TAG = LIvePushActivity.this.getClass().getSimpleName();
    @BindView(R.id.title_layout)
    TitleLayout titleLayout;
    @BindView(R.id.player_content)
    FrameLayout playerContent;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp_main)
    ViewPagerSlide vpMain;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.tv_subscribe)
    TextView tvSubscribe;
    private boolean mDefaultVideo;
    private int mVideoCount;
    private boolean mVideoHasPlay;
    protected TICManager mTicManager;
    private int mRoomId;
    private String mUserID;
    private String mUserSig;
    private TRTCCloud mTrtcCloud;
    private TICVideoRootView mTrtcRootView;
    boolean mEnableAudio = true;
    boolean mEnableCamera = true;
    boolean mEnableFrontCamera = true;
    boolean mEnableAudioRouteSpeaker = true; //扬声器
    boolean mCanRedo = false;
    boolean mCanUndo = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_live_push;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String from = intent.getStringExtra("from");
        if (!TextUtils.isEmpty(from)) {
//            playExternalVideo();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void initInject() {
        mTicManager = ((LiveApplication) getApplication()).getTICManager();
        mTicManager.addIMStatusListener(this);
        getPermission();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ImmersionBar.with(this).statusBarDarkFont(false, 0.2f).titleBar(tabLayout).init();
        titleLayout.setLeftClickListener((v) -> finish());
        titleLayout.setTitleText("直播详情");
        LivePagerAdapter livePagerAdapter = new LivePagerAdapter(getSupportFragmentManager());
        vpMain.setAdapter(livePagerAdapter);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("简介");
        strings.add("文档");
        strings.add("聊天");
        livePagerAdapter.setData(strings);
        vpMain.setOffscreenPageLimit(strings.size());
        vpMain.setSlidAble(false);
        tabLayout.setViewPager(vpMain, (String[]) strings.toArray(new String[strings.size()]));

    }

    @SuppressLint("CheckResult")
    private void getPermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //用户同意使用权限
                        } else {
                            //用户不同意使用权限
                            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
                            Toast.makeText(mContext, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void initViewAndData() {
        mDefaultVideo = getIntent().getBooleanExtra(Constants.PLAYER_DEFAULT_VIDEO, true);
        mVideoHasPlay = false;
        mVideoCount = 0;

//        TXLiveBase.setAppID("1253131631");//官方默认id
        if (SharePreUtils.getName(mContext).equals("yuwenming1")) {
            mUserID = "yuwenming1";
            mUserSig = "eJwtzNsKgkAUheF3mVtDts7BFLorAknSsshLw0k2HjKdmiR690y9XN*C-0Pi3dF8yZZ4xDaBLMaNmawV3nDk-qllXWGdW-PbZUXaNJgRz2IAzGHCpdMj3w22cnDOuQ0Akyqs-iYsEOBwcOcK5kO821NxfWwuOlkvhYq3kdClCnpWRkV16oy7H1LDD84HGiYr8v0Bc3UzJA__";
        } else {
            mUserID = "yuwenming";
            mUserSig = "eJwtzMsKwjAUBNB-yVapt7UPLbhoFuIiLoJSENwUchMutaGkNb7w361tl3NmmA87i1Pg0bGcRQGw5ZhJoe1J08iv*wNtQ9bMZafqqm1JsTyMAeIsTrfrqcFnSw4HT5IkAoBJe2r*loaQwjCetSMzfBfS94Xwbm*uK4mX0murMiEXHcdjCZ6-lTYbfqhvopI79v0Bjpc0Aw__";
        }
        onLoginClick();
        initTrtc();//主播  或者 观看时看到
    }

    //---------trtc--------------

    private void initTrtc() {
        //1、获取trtc
        mTrtcCloud = mTicManager.getTRTCClound();

        if (mTrtcCloud != null) {
            //2、TRTC View
//            mTrtcRootView = (TICVideoRootView) findViewById(R.id.trtc_root_view);
            mTrtcRootView = new TICVideoRootView(mContext);
            mTrtcRootView.setUserId(mUserID);
            TXCloudVideoView localVideoView = mTrtcRootView.getCloudVideoViewByIndex(0);
            localVideoView.setUserId(mUserID);
            playerContent.addView(mTrtcRootView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        }
    }

    private void unInitTrtc() {
        if (mTrtcCloud != null) {
            //3、停止本地视频图像
            mTrtcCloud.stopLocalPreview();
            enableAudioCapture(false);
        }
    }

    private void startLocalVideo(boolean enable) {
        if (mTrtcCloud != null) {
            final String usrid = mUserID;
            TXCloudVideoView localVideoView = mTrtcRootView.getCloudVideoViewByUseId(usrid);
            localVideoView.setUserId(usrid);
            localVideoView.setVisibility(View.VISIBLE);
            if (enable) {
                mTrtcCloud.startLocalPreview(mEnableFrontCamera, localVideoView);
            } else {
                mTrtcCloud.stopLocalPreview();
            }
        }
    }

    private void enableAudioCapture(boolean bEnable) {
        if (mTrtcCloud != null) {
            if (bEnable) {
                mTrtcCloud.startLocalAudio();
            } else {
                mTrtcCloud.stopLocalAudio();
            }
        }

    }

    /**
     * 登录
     */
    public void onLoginClick() {
        mTicManager.login(mUserID, mUserSig, new TICManager.TICCallback() {
            @Override
            public void onSuccess(Object data) {
                LogUtils.e(mUserID + ":登录成功");
                onCreateClsssroomClick();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e(mUserID + ":登录失败, err:" + errCode + "  msg: " + errMsg);
            }
        });
    }

    private void joinRoom(int mRoomId) {
        final String desc = "board group";
        TIMGroupManager.getInstance().applyJoinGroup(mRoomId + "", desc + mRoomId, new TIMCallBack() {
            @Override
            public void onError(int errCode, String s) {
                if (errCode == 10013) { //you are already group member.
                    TXCLog.i(TAG, "TICManager: joinClassroom 10013 onSuccess");
                    TICReporter.report(TICReporter.EventId.joinGroup_end);
                    getUserRoomInfo();
                }
            }

            @Override
            public void onSuccess() {
                getUserRoomInfo();
            }
        });
    }

    private void getUserRoomInfo() {
        TIMGroupManager.getInstance().getSelfInfo(mRoomId + "", new TIMValueCallBack<TIMGroupSelfInfo>() {
            @Override
            public void onError(int i, String s) {
                LogUtils.e(TAG, s);
            }

            @Override
            public void onSuccess(TIMGroupSelfInfo timGroupSelfInfo) {
                LogUtils.e(TAG, timGroupSelfInfo.toString());
                if (timGroupSelfInfo.getRole() == TIMGroupMemberRoleType.ROLE_TYPE_ADMIN || timGroupSelfInfo.getRole() == TIMGroupMemberRoleType.ROLE_TYPE_OWNER) {
                    startLocalVideo(true);
                    enableAudioCapture(true);
                    mTrtcCloud.startPublishing("user_stream_001", TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
                } else {
                    startLocalVideo(false);
                    enableAudioCapture(false);
                }

            }
        });
    }

    private void initClassRoom() {
        int sdkAppId = 1400474693;
        String userId = "yuwenming";
        String userSig = "eJwtzMsKwjAUBNB-yVapt7UPLbhoFuIiLoJSENwUchMutaGkNb7w361tl3NmmA87i1Pg0bGcRQGw5ZhJoe1J08iv*wNtQ9bMZafqqm1JsTyMAeIsTrfrqcFnSw4HT5IkAoBJe2r*loaQwjCetSMzfBfS94Xwbm*uK4mX0murMiEXHcdjCZ6-lTYbfqhvopI79v0Bjpc0Aw__";

    }

    /**
     * 创建课堂
     */
    public void onCreateClsssroomClick() {
        mTicManager.addIMMessageListener(this);
        mTicManager.addEventListener(this);
        final int scence = TICManager.TICClassScene.TIC_CLASS_SCENE_VIDEO_CALL; //如果使用大房间，请使用 TIC_CLASS_SCENE_LIVE
        mRoomId = 1234;
        mTicManager.createClassroom(mRoomId, scence, new TICManager.TICCallback() {
            @Override
            public void onSuccess(Object data) {
                LogUtils.e("创建课堂 成功, 房间号：" + mRoomId);
                EventBus.getDefault().post(new IMLoginEvent(true));
                joinRoom(mRoomId);

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                if (errCode == 10021) {
                    LogUtils.e("该课堂已被他人创建，请\"加入课堂\"");
                    EventBus.getDefault().post(new IMLoginEvent(true));
                    joinRoom(mRoomId);
                } else if (errCode == 10025) {
                    LogUtils.e("该课堂已创建，请\"加入课堂\"");
                    EventBus.getDefault().post(new IMLoginEvent(true));
                } else {
                    LogUtils.e("创建课堂失败, 房间号：" + mRoomId + " err:" + errCode + " msg:" + errMsg);
                }

            }
        });
    }

    public void onDestroyClassroomClick(View v) {

        mTicManager.destroyClassroom(mRoomId, new TICManager.TICCallback() {
            @Override
            public void onSuccess(Object o) {
                LogUtils.e("销毁课堂成功: " + mRoomId);

                TEduBoardController board = mTicManager.getBoardController();
                if (board != null)
                    board.reset();
            }

            @Override
            public void onError(String s, int errCode, String errMsg) {
                LogUtils.e("销毁课堂失败: " + mRoomId + " err:" + errCode + " msg:" + errMsg);
            }
        });
    }

    /**
     * 进入课堂
     */
    public void onJoinClsssroomClick() {

        String roomInputId = mRoomId + "";
        if (TextUtils.isEmpty(roomInputId) || !TextUtils.isDigitsOnly(roomInputId)) {
            LogUtils.e("创建课堂失败, 房间号为空或者非数字:" + roomInputId);
            return;
        }
        mRoomId = Integer.valueOf(roomInputId);
        LogUtils.e("正在进入课堂，请稍等。。。");
    }

    @Override
    public void setData(ArrayList<String> data) {

    }

    @Override
    public void getMoneySuccess() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTicManager != null) {
            mTicManager.removeIMStatusListener(this);
            mTicManager.quitClassroom(false, null);
            mTicManager.removeIMMessageListener(this);
            mTicManager.removeEventListener(this);
        }
    }


    // ------------ FROM TICMessageListener ---------------------
    @Override
    public void onTICRecvTextMessage(String fromId, String text) {
        LogUtils.e(String.format("[%s]（C2C）说: %s", fromId, text));
    }

    @Override
    public void onTICRecvCustomMessage(String fromId, byte[] data) {
        LogUtils.e(String.format("[%s]（C2C:Custom）说: %s", fromId, new String(data)));
    }

    @Override
    public void onTICRecvGroupTextMessage(String fromId, String text) {
        LogUtils.e(String.format("[%s]（Group:Custom）说: %s", fromId, text));

    }

    @Override
    public void onTICRecvGroupCustomMessage(String fromUserId, byte[] data) {
        LogUtils.e(String.format("[%s]（Group:Custom）说: %s", fromUserId, new String(data)));
    }

    @Override
    public void onTICRecvMessage(TIMMessage message) {
        handleTimElement(message);
    }


    private void handleTimElement(TIMMessage message) {

        for (int i = 0; i < message.getElementCount(); i++) {
            TIMElem elem = message.getElement(i);
            switch (elem.getType()) {
                case Text:
                    LogUtils.e("This is Text message.");
                    EventBus.getDefault().post(message);
                    break;
                case Custom:
                    LogUtils.e("This is Custom message.");
                    break;
                case GroupTips:
                    LogUtils.e("This is GroupTips message.");
                    continue;
                default:
                    LogUtils.e("This is other message");
                    break;
            }
        }
    }
    //---------------------- TICEventListener-----------------

    @Override
    public void onTICUserVideoAvailable(String userId, boolean available) {
        Log.e(TAG, "onTICUserVideoAvailable:" + userId + "|" + available);
        if (available) {
            final TXCloudVideoView renderView = mTrtcRootView.onMemberEnter(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
            if (renderView != null) {
                // 启动远程画面的解码和显示逻辑，FillMode 可以设置是否显示黑边
                mTrtcCloud.setRemoteViewFillMode(userId, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT);
                mTrtcCloud.startRemoteView(userId, renderView);
                renderView.setUserId(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
            }
        } else {
            mTrtcCloud.stopRemoteView(userId);
            mTrtcRootView.onMemberLeave(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
        }
    }

    @Override
    public void onTICUserSubStreamAvailable(String userId, boolean available) {
        Log.e(TAG, "onTICUserSubStreamAvailable:" + userId + "|" + available);
        if (available) {
            final TXCloudVideoView renderView = mTrtcRootView.onMemberEnter(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
            if (renderView != null) {
                renderView.setUserId(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
                mTrtcCloud.setRemoteViewFillMode(userId, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT);
                mTrtcCloud.startRemoteSubStreamView(userId, renderView);
            }
        } else {
            mTrtcCloud.stopRemoteSubStreamView(userId);
            mTrtcRootView.onMemberLeave(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
        }
    }

    @Override
    public void onTICUserAudioAvailable(String userId, boolean available) {
        Log.e(TAG, "onTICUserAudioAvailable:" + userId + "|" + available);
        if (available) {
            final TXCloudVideoView renderView = mTrtcRootView.onMemberEnter(userId + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
            if (renderView != null) {
                renderView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onTICMemberJoin(List<String> userList) {
        Log.e(TAG, "onTICMemberJoin:");
        for (String user : userList) {
            // 创建一个View用来显示新的一路画面，在自已进房间时，也会给这个回调
            if (!user.equals(mUserID)) {
                TXCloudVideoView renderView = mTrtcRootView.onMemberEnter(user + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
                if (renderView != null) {
                    renderView.setVisibility(View.VISIBLE);
                }
                LogUtils.e(user + " join.");
            }
        }
    }

    @Override
    public void onTICMemberQuit(List<String> userList) {
        Log.e(TAG, "onTICMemberQuit:");

        for (String user : userList) {
            final String userID_Big = user.equals(mUserID) ? mUserID : user + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG;
            //停止观看画面
            mTrtcCloud.stopRemoteView(userID_Big);
            mTrtcRootView.onMemberLeave(userID_Big);
            final String userID_Sub = user.equals(mUserID) ? mUserID : user + TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB;
            mTrtcCloud.stopRemoteSubStreamView(userID_Sub);
            mTrtcRootView.onMemberLeave(userID_Sub);
            LogUtils.e(user + " quit.");
        }
    }

    @Override
    public void onTICForceOffline() {
        Log.e(TAG, "onTICForceOffline:");

        //1、退出TRTC
        if (mTrtcCloud != null) {
            mTrtcCloud.exitRoom();
        }

        //2.退出房间
        mTicManager.quitClassroom(false, new TICManager.TICCallback() {
            @Override
            public void onSuccess(Object data) {
                LogUtils.e("onForceOffline##quitClassroom#onSuccess: " + data);
//                Intent intent = new Intent(TICClassMainActivity.this, TICLoginActivity.class);
//                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e("onForceOffline##quitClassroom#onError: errCode = " + errCode + "  description " + errMsg);
            }
        });
    }

    @Override
    public void onTICUserSigExpired() {
        Log.e(TAG, "onTICUserSigExpired:");

    }

    @Override
    public void onTICVideoDisconnect(int errCode, String errMsg) {
        Log.e(TAG, "onTICVideoDisconnect:");

    }

    @Override
    public void onTICClassroomDestroy() {
        Log.e(TAG, "onTICClassroomDestroy:");

    }

    @Override
    public void onTICSendOfflineRecordInfo(int code, String desc) {

        //可以在此提示用户录制信息有误，然后让用户选择是否重新触发同步录制信息;
        new AlertDialog.Builder(this)
                .setTitle("同步录制信息失败")
                .setMessage("重试吗？")
                .setPositiveButton("要重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTicManager.sendOfflineRecordInfo();
                    }
                })
                .setNegativeButton("不重试", null)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_subscribe)
    public void onClick() {
    }
}
