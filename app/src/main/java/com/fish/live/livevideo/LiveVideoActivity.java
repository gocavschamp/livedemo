package com.fish.live.livevideo;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.fish.live.Constants;
import com.fish.live.LiveApplication;
import com.fish.live.R;
import com.fish.live.home.bean.IMLoginEvent;
import com.fish.live.home.bean.MsgEvent;
import com.fish.live.livevideo.adapter.LivePagerAdapter;
import com.fish.live.livevideo.presenter.LiveVideoPresenter;
import com.fish.live.livevideo.view.LiveVideoCotract;
import com.fish.live.tencenttic.core.TICManager;
import com.flyco.tablayout.SlidingTabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.nucarf.base.ui.mvp.BaseMvpActivity;
import com.nucarf.base.utils.LogUtils;
import com.nucarf.base.utils.SharePreUtils;
import com.nucarf.base.utils.ToastUtils;
import com.nucarf.base.widget.TitleLayout;
import com.nucarf.base.widget.ViewPagerSlide;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.liteav.demo.superplayer.SuperPlayerDef;
import com.tencent.liteav.demo.superplayer.SuperPlayerGlobalConfig;
import com.tencent.liteav.demo.superplayer.SuperPlayerView;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.teduboard.TEduBoardController;
import com.tencent.trtc.TRTCCloudDef;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.jessyan.autosize.utils.AutoSizeUtils;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LiveVideoActivity extends BaseMvpActivity<LiveVideoPresenter> implements LiveVideoCotract.View, SuperPlayerView.OnSuperPlayerViewCallback, TICManager.TICIMStatusListener, TICManager.TICMessageListener, TICManager.TICEventListener {


    private final String TAG = LiveVideoActivity.this.getClass().getSimpleName();
    @BindView(R.id.title_layout)
    TitleLayout titleLayout;
    @BindView(R.id.player_content)
    FrameLayout playerContent;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp_main)
    ViewPagerSlide vpMain;
    //    @BindView(R.id.superVodPlayerView)
    SuperPlayerView mSuperPlayerView;
    private boolean mDefaultVideo;
    private int mVideoCount;
    private boolean mVideoHasPlay;
    protected TICManager mTicManager;
    private int mRoomId;
    private String mUserID;
    private String mUserSig;

    @Override
    protected int getLayout() {
        return R.layout.activity_live_video;
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
        mSuperPlayerView = new SuperPlayerView(this);
        playerContent.addView(mSuperPlayerView, FrameLayout.LayoutParams.MATCH_PARENT, AutoSizeUtils.dp2px(mContext, 200));
        mSuperPlayerView.setPlayerViewCallback(this);

    }

    @Override
    protected void initViewAndData() {
        mDefaultVideo = getIntent().getBooleanExtra(Constants.PLAYER_DEFAULT_VIDEO, true);
        initSuperVodGlobalSetting();
        mVideoHasPlay = false;
        mVideoCount = 0;

        TXLiveBase.setAppID("1253131631");//官方默认id
//        TXLiveBase.setAppID(Constants.VOD_APPID+"");//我的id
        mSuperPlayerView.play(Constants.NORMAL_PLAY_URL);

        if (SharePreUtils.getName(mContext).equals("yuwenming1")) {
            mUserID = "yuwenming1";
            mUserSig = "eJwtzNsKgkAUheF3mVtDts7BFLorAknSsshLw0k2HjKdmiR690y9XN*C-0Pi3dF8yZZ4xDaBLMaNmawV3nDk-qllXWGdW-PbZUXaNJgRz2IAzGHCpdMj3w22cnDOuQ0Akyqs-iYsEOBwcOcK5kO821NxfWwuOlkvhYq3kdClCnpWRkV16oy7H1LDD84HGiYr8v0Bc3UzJA__";
        } else {
            mUserID = "yuwenming";
            mUserSig = "eJwtzMsKwjAUBNB-yVapt7UPLbhoFuIiLoJSENwUchMutaGkNb7w361tl3NmmA87i1Pg0bGcRQGw5ZhJoe1J08iv*wNtQ9bMZafqqm1JsTyMAeIsTrfrqcFnSw4HT5IkAoBJe2r*loaQwjCetSMzfBfS94Xwbm*uK4mX0murMiEXHcdjCZ6-lTYbfqhvopI79v0Bjpc0Aw__";
        }
        onLoginClick();
//        initTrtc();//主播  或者 观看时看到

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
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                if (errCode == 10021) {
                    LogUtils.e("该课堂已被他人创建，请\"加入课堂\"");
                    EventBus.getDefault().post(new IMLoginEvent(true));
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
        if (mSuperPlayerView.getPlayerState() == SuperPlayerDef.PlayerState.PAUSE) {
            Log.i(TAG, "onResume state :" + mSuperPlayerView.getPlayerState());
            mSuperPlayerView.onResume();
            if (mSuperPlayerView.getPlayerMode() == SuperPlayerDef.PlayerMode.FLOAT) {
                mSuperPlayerView.switchPlayMode(SuperPlayerDef.PlayerMode.WINDOW);
            }
        }
        if (mSuperPlayerView.getPlayerMode() == SuperPlayerDef.PlayerMode.FULLSCREEN) {
            //隐藏虚拟按键，并且全屏
            View decorView = getWindow().getDecorView();
            if (decorView == null) return;
            if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
                decorView.setSystemUiVisibility(View.GONE);
            } else if (Build.VERSION.SDK_INT >= 19) {
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause state :" + mSuperPlayerView.getPlayerState());
        if (mSuperPlayerView.getPlayerMode() != SuperPlayerDef.PlayerMode.FLOAT) {
            mSuperPlayerView.onPause();
        }
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
        if (mSuperPlayerView != null)
            mSuperPlayerView.release();
        if (mSuperPlayerView.getPlayerMode() != SuperPlayerDef.PlayerMode.FLOAT) {
            mSuperPlayerView.resetPlayer();
        }
//        VideoDataMgr.getInstance().setGetVideoInfoListListener(null);
    }

    @Override
    public void onStartFullScreenPlay() {
        // 隐藏其他元素实现全屏
        titleLayout.setVisibility(GONE);
//        if (mImageAdd != null) {
//            mImageAdd.setVisibility(GONE);
//        }
    }

    @Override
    public void onStopFullScreenPlay() {
        // 恢复原有元素
        titleLayout.setVisibility(VISIBLE);
//        if (mImageAdd != null) {
//            mImageAdd.setVisibility(VISIBLE);
//        }
    }

    @Override
    public void onClickFloatCloseBtn() {
        // 点击悬浮窗关闭按钮，那么结束整个播放
        mSuperPlayerView.resetPlayer();
        finish();
    }

    @Override
    public void onClickSmallReturnBtn() {
        // 点击小窗模式下返回按钮，开始悬浮播放
//        showFloatWindow();
    }

    @Override
    public void onStartFloatWindowPlay() {
        // 开始悬浮播放后，直接返回到桌面，进行悬浮播放
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    /**
     * 初始化超级播放器全局配置
     */
    private void initSuperVodGlobalSetting() {
        SuperPlayerGlobalConfig prefs = SuperPlayerGlobalConfig.getInstance();
        // 开启悬浮窗播放
        prefs.enableFloatWindow = true;
        // 设置悬浮窗的初始位置和宽高
        SuperPlayerGlobalConfig.TXRect rect = new SuperPlayerGlobalConfig.TXRect();
        rect.x = 0;
        rect.y = 0;
        rect.width = 810;
        rect.height = 540;
        prefs.floatViewRect = rect;
        // 播放器默认缓存个数
        prefs.maxCacheItem = 5;
        // 设置播放器渲染模式
        prefs.enableHWAcceleration = true;
        prefs.renderMode = TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION;
        //需要修改为自己的时移域名
        prefs.playShiftDomain = "liteavapp.timeshift.qcloud.com";
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
//        if (available) {
//            final TXCloudVideoView renderView = mTrtcRootView.onMemberEnter(userId+ TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
//            if (renderView != null) {
//                // 启动远程画面的解码和显示逻辑，FillMode 可以设置是否显示黑边
//                mTrtcCloud.setRemoteViewFillMode(userId, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT);
//                mTrtcCloud.startRemoteView(userId, renderView);
//                renderView.setUserId(userId+TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
//            }
//
//        } else {
//            mTrtcCloud.stopRemoteView(userId);
//            mTrtcRootView.onMemberLeave(userId+TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
//        }
    }

    @Override
    public void onTICUserSubStreamAvailable(String userId, boolean available) {
        Log.e(TAG, "onTICUserSubStreamAvailable:" + userId + "|" + available);
//        if (available) {
//            final TXCloudVideoView renderView = mTrtcRootView.onMemberEnter(userId+TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
//            if (renderView != null) {
//                renderView.setUserId(userId+TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
//                mTrtcCloud.setRemoteViewFillMode(userId, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT);
//                mTrtcCloud.startRemoteSubStreamView(userId, renderView);
//            }
//        }
//        else {
//            mTrtcCloud.stopRemoteSubStreamView(userId);
//            mTrtcRootView.onMemberLeave(userId+TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
//        }
    }

    @Override
    public void onTICUserAudioAvailable(String userId, boolean available) {
        Log.e(TAG, "onTICUserAudioAvailable:" + userId + "|" + available);

//        if (available) {
//            final TXCloudVideoView renderView = mTrtcRootView.onMemberEnter(userId+TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
//            if (renderView != null) {
//                renderView.setVisibility(View.VISIBLE);
//            }
//        }
    }

    @Override
    public void onTICMemberJoin(List<String> userList) {
        Log.e(TAG, "onTICMemberJoin:");

//        for (String user : userList) {
//            // 创建一个View用来显示新的一路画面，在自已进房间时，也会给这个回调
//            if (!user.equals(mUserID)) {
//                TXCloudVideoView renderView = mTrtcRootView.onMemberEnter(user+TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
//                if (renderView != null) {
//                    renderView.setVisibility(View.VISIBLE);
//                }
//                postToast(user + " join.", false);
//            }
//        }
    }

    @Override
    public void onTICMemberQuit(List<String> userList) {
        Log.e(TAG, "onTICMemberQuit:");

//        for (String user : userList) {
//            final String userID_Big = user.equals(mUserID) ? mUserID : user+TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG;
//            //停止观看画面
//            mTrtcCloud.stopRemoteView(userID_Big);
//            mTrtcRootView.onMemberLeave(userID_Big);
//
//            final String userID_Sub = user.equals(mUserID) ? mUserID : user+TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB;
//            mTrtcCloud.stopRemoteSubStreamView(userID_Sub);
//            mTrtcRootView.onMemberLeave(userID_Sub);
//
//            postToast(user + " quit.", false);
//        }
    }

    @Override
    public void onTICForceOffline() {
        Log.e(TAG, "onTICForceOffline:");

//        //1、退出TRTC
//        if (mTrtcCloud != null ) {
//            mTrtcCloud.exitRoom();
//        }
//
//        //2.退出房间
//        mTicManager.quitClassroom(false, new TICManager.TICCallback() {
//            @Override
//            public void onSuccess(Object data) {
//                postToast("onForceOffline##quitClassroom#onSuccess: " + data);
//                Intent intent = new Intent(TICClassMainActivity.this, TICLoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//
//            @Override
//            public void onError(String module, int errCode, String errMsg) {
//                postToast("onForceOffline##quitClassroom#onError: errCode = " + errCode + "  description " + errMsg);
//            }
//        });
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

    }
}
