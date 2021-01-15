package com.fish.live.livepush.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fish.live.LiveApplication;
import com.fish.live.R;
import com.fish.live.home.bean.IMLoginEvent;
import com.fish.live.tencenttic.core.TICClassroomOption;
import com.fish.live.tencenttic.core.TICManager;
import com.nucarf.base.ui.BaseLazyFragment;
import com.nucarf.base.utils.BaseAppCache;
import com.nucarf.base.utils.LogUtils;
import com.nucarf.base.utils.ToastUtils;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.rtmp.TXLog;
import com.tencent.teduboard.TEduBoardController;

import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.nucarf.base.utils.BaseAppCache.getApplication;

/**
 * @Description TODO
 * @Author yuwenming
 * @Date 2021/1/12 15:55
 */
public class PPTInfoFragment extends BaseLazyFragment implements TICManager.TICIMStatusListener, TICManager.TICMessageListener, TICManager.TICEventListener {
    private static final String TYPE = "ppt";
    @BindView(R.id.board_view_container)
    FrameLayout boardViewContainer;
    @BindView(R.id.last_page)
    TextView lastPage;
    @BindView(R.id.total_page)
    TextView totalPage;
    @BindView(R.id.next_page)
    TextView nextPage;
    @BindView(R.id.ll_page_control)
    LinearLayout llPageControl;
    private Unbinder unbinder;
    //    protected TICManager mTicManager;
    private TEduBoardController mBoard;
    private TICManager mTicManager;
    private boolean mHistroyDataSyncCompleted;

    //    TICMenuDialog moreDlg;
//    MySettingCallback mySettingCallback;
    boolean mEnableAudio = true;
    boolean mEnableCamera = true;
    boolean mEnableFrontCamera = true;
    boolean mEnableAudioRouteSpeaker = true; //扬声器
    boolean mCanRedo = false;
    boolean mCanUndo = false;
    private MyBoardCallback mBoardCallback;
    private int mRoomId;

    public PPTInfoFragment() {
    }

    public static PPTInfoFragment newInstance(String type) {
        PPTInfoFragment myFragment = new PPTInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.live_ppt_layout;
    }

    @Subscribe
    public void onEvent(Object event) {
        if (event instanceof IMLoginEvent) {
            IMLoginEvent imevent = (IMLoginEvent) event;
            if (imevent.isLogin()) {
                joinClass();
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mTicManager.removeIMStatusListener(this);
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, mRootView);
        registerEventBus();
        // 创建并初始化白板控制器
        //（1）鉴权配置
        mTicManager = ((LiveApplication) mActivity.getApplication()).getTICManager();
        mBoard = mTicManager.getBoardController();
//        mTicManager.addIMMessageListener(this);
//        mTicManager.addEventListener(this);

    }

    /**
     * 进入课堂
     */
    private void joinClass() {
        //1、设置白板的回调
        mBoardCallback = new MyBoardCallback(this);
        //2、如果用户希望白板显示出来时，不使用系统默认的参数，就需要设置特性缺省参数，如是使用默认参数，则填null。
        TEduBoardController.TEduBoardInitParam initParam = new TEduBoardController.TEduBoardInitParam();
        initParam.brushColor = new TEduBoardController.TEduBoardColor(255, 0, 0, 255);
        initParam.smoothLevel = 0; //用于指定笔迹平滑级别，默认值0.1，取值[0, 1]

        mRoomId = 1234;
        TICClassroomOption classroomOption = new TICClassroomOption();
        classroomOption.classId = 1234;
        classroomOption.boardCallback = mBoardCallback;
        classroomOption.boardInitPara = initParam;

        mTicManager.joinClassroom(classroomOption, new TICManager.TICCallback() {
            @Override
            public void onSuccess(Object data) {
                LogUtils.e("进入课堂成功:" + mRoomId);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                if (errCode == 10015) {
                    LogUtils.e("课堂不存在:" + mRoomId + " err:" + errCode + " msg:" + errMsg);
                } else {
                    LogUtils.e("进入课堂失败:" + mRoomId + " err:" + errCode + " msg:" + errMsg);
                }
            }
        });
    }

    private void quitClass() {

        //如果是老师，可以清除；
        //如查是学生一般是不要清除数据
        boolean clearBoard = false;
        mTicManager.quitClassroom(clearBoard, new TICManager.TICCallback() {
            @Override
            public void onSuccess(Object data) {
                LogUtils.e("quitClassroom#onSuccess: " + data);
//                finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e("quitClassroom#onError: errCode = " + errCode + "  description " + errMsg);
//                finish();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onTICForceOffline() {

    }

    @Override
    public void onTICUserSigExpired() {

    }

    @OnClick({R.id.board_view_container, R.id.last_page, R.id.total_page, R.id.next_page})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.board_view_container:
                llPageControl.setVisibility(llPageControl.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.last_page:
                TEduBoardController.TEduBoardTranscodeFileResult transcodeFileResult = new TEduBoardController.TEduBoardTranscodeFileResult("欢迎新同学", "https://ppt2h5-1259648581.file.myqcloud.com/ghikv1979vq1bhl3jtpb/index.html", 23, "960x540");
                mBoard.addTranscodeFile(transcodeFileResult, false);
                break;
            case R.id.total_page:
                break;
            case R.id.next_page:
                TEduBoardController.TEduBoardTranscodeFileResult t = new TEduBoardController.TEduBoardTranscodeFileResult("欢迎新同学", "https://ppt2h5-1259648581.file.myqcloud.com/ghikv1979vq1bhl3jtpb/index.html", 23, "960x540");
                mBoard.addTranscodeFile(t, true);
                break;
        }
    }

    private final static String TAG = "PPT FRAGMENT";


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

    @Override
    public void onTICUserVideoAvailable(String userId, boolean available) {
        Log.i(TAG, "onTICUserVideoAvailable:" + userId + "|" + available);
//        if (available) {
//            final TXCloudVideoView renderView = mTrtcRootView.onMemberEnter(userId+ TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
//            if (renderView != null) {
//                // 启动远程画面的解码和显示逻辑，FillMode 可以设置是否显示黑边
//                mTrtcCloud.setRemoteViewFillMode(userId, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT);
//                mTrtcCloud.startRemoteView(userId, renderView);
//                renderView.setUserId(userId+TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
//            }
//        } else {
//            mTrtcCloud.stopRemoteView(userId);
//            mTrtcRootView.onMemberLeave(userId+TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
//        }
    }

    @Override
    public void onTICUserSubStreamAvailable(String userId, boolean available) {

//        if (available) {
//            final TXCloudVideoView renderView = mTrtcRootView.onMemberEnter(userId+ TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
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

    }

    @Override
    public void onTICMemberJoin(List<String> userList) {

    }

    @Override
    public void onTICMemberQuit(List<String> userList) {

    }

    @Override
    public void onTICVideoDisconnect(int errCode, String errMsg) {

    }

    @Override
    public void onTICClassroomDestroy() {

    }

    @Override
    public void onTICSendOfflineRecordInfo(int code, String desc) {

    }


    //Board Callback
    static private class MyBoardCallback implements TEduBoardController.TEduBoardCallback {
        WeakReference<PPTInfoFragment> mActivityRef;

        MyBoardCallback(PPTInfoFragment activityEx) {
            mActivityRef = new WeakReference<>(activityEx);
        }

        @Override
        public void onTEBError(int code, String msg) {
            Log.i(TAG, "onTEBError:" + code + "|" + msg);
        }

        @Override
        public void onTEBWarning(int code, String msg) {
            Log.i(TAG, "onTEBWarning:" + code + "|" + msg);
        }

        @Override
        public void onTEBInit() {
            PPTInfoFragment activity = mActivityRef.get();
            if (activity != null) {
                activity.addBoardView();
            }
        }

        @Override
        public void onTEBHistroyDataSyncCompleted() {
            PPTInfoFragment activityEx = mActivityRef.get();
            if (activityEx != null) {
                activityEx.onTEBHistroyDataSyncCompleted();
            }
        }

        @Override
        public void onTEBSyncData(String data) {

        }


        @Override
        public void onTEBAddBoard(List<String> boardId, final String fileId) {
            Log.i(TAG, "onTEBAddBoard:" + fileId);
        }

        @Override
        public void onTEBDeleteBoard(List<String> boardId, final String fileId) {
            Log.i(TAG, "onTEBDeleteBoard:" + fileId + "|" + boardId);
        }

        @Override
        public void onTEBGotoBoard(String boardId, final String fileId) {
            Log.i(TAG, "onTEBGotoBoard:" + fileId + "|" + boardId);
        }

        @Override
        public void onTEBGotoStep(int currentStep, int total) {
            Log.i(TAG, "onTEBGotoStep:" + currentStep + "|" + total);
        }

        @Override
        public void onTEBRectSelected() {
            Log.i(TAG, "onTEBRectSelected:");
        }

        @Override
        public void onTEBRefresh() {
            Log.i(TAG, "onTEBRefresh:");
        }

        @Override
        public void onTEBDeleteFile(String fileId) {
        }

        @Override
        public void onTEBSwitchFile(String fileId) {
        }

        @Override
        public void onTEBAddTranscodeFile(String s) {
            Log.i(TAG, "onTEBAddTranscodeFile:" + s);
        }

        @Override
        public void onTEBUndoStatusChanged(boolean canUndo) {
            PPTInfoFragment activityEx = mActivityRef.get();
            if (activityEx != null) {
                activityEx.mCanUndo = canUndo;
            }
        }

        @Override
        public void onTEBRedoStatusChanged(boolean canredo) {
            PPTInfoFragment activityEx = mActivityRef.get();
            if (activityEx != null) {
                activityEx.mCanRedo = canredo;
            }
        }

        @Override
        public void onTEBFileUploadProgress(final String path, int currentBytes, int totalBytes, int uploadSpeed, float percent) {
            Log.i(TAG, "onTEBFileUploadProgress:" + path + " percent:" + percent);
        }

        @Override
        public void onTEBFileUploadStatus(final String path, int status, int code, String statusMsg) {
            Log.i(TAG, "onTEBFileUploadStatus:" + path + " status:" + status);
        }

        @Override
        public void onTEBFileTranscodeProgress(String s, String s1, String s2, TEduBoardController.TEduBoardTranscodeFileResult tEduBoardTranscodeFileResult) {

        }

        @Override
        public void onTEBH5FileStatusChanged(String fileId, int status) {

        }

        @Override
        public void onTEBAddImagesFile(String fileId) {
            Log.i(TAG, "onTEBAddImagesFile:" + fileId);
            PPTInfoFragment activityEx = mActivityRef.get();
            TEduBoardController.TEduBoardFileInfo fileInfo = activityEx.mBoard.getFileInfo(fileId);
        }

        @Override
        public void onTEBVideoStatusChanged(String fileId, int status, float progress, float duration) {
            Log.i(TAG, "onTEBVideoStatusChanged:" + fileId + " | " + status + "|" + progress);
        }

        @Override
        public void onTEBAudioStatusChanged(String s, int i, float v, float v1) {

        }

        @Override
        public void onTEBSnapshot(String s, int i, String s1) {

        }

        @Override
        public void onTEBH5PPTStatusChanged(int i, String s, String s1) {

        }

        @Override
        public void onTEBImageStatusChanged(String boardId, String url, int status) {
            Log.i(TAG, "onTEBImageStatusChanged:" + boardId + "|" + url + "|" + status);
        }

        @Override
        public void onTEBSetBackgroundImage(final String url) {
            Log.i(TAG, "onTEBSetBackgroundImage:" + url);
        }

        @Override
        public void onTEBAddImageElement(final String url) {
            Log.i(TAG, "onTEBAddImageElement:" + url);
        }

        @Override
        public void onTEBAddElement(String s, String s1) {

        }

        @Override
        public void onTEBBackgroundH5StatusChanged(String boardId, String url, int status) {
            Log.i(TAG, "onTEBBackgroundH5StatusChanged:" + boardId + " url:" + boardId + " status:" + status);
        }
    }

    void addBoardView() {
        View boardview = mBoard.getBoardRenderView();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        if(boardViewContainer!=null) {
            boardViewContainer.addView(boardview, layoutParams);
        }

        TEduBoardController.TEduBoardTranscodeFileResult transcodeFileResult = new TEduBoardController.TEduBoardTranscodeFileResult("欢迎新同学", "https://ppt2h5-1259648581.file.myqcloud.com/ghikv1979vq1bhl3jtpb/index.html", 23, "960x540");
        mBoard.addTranscodeFile(transcodeFileResult, false);
        //设置透明
        //boardview.setBackgroundColor(Color.TRANSPARENT);
        //boardview.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        //mBoard.setGlobalBackgroundColor(new TEduBoardController.TEduBoardColor(0, 0,0, 0));
        //mBoard.setBackgroundColor(new TEduBoardController.TEduBoardColor(0, 0,10, 0));

//        LogUtils.e("正在使用白板：" + TEduBoardController.getVersion(), true);
    }

    private void removeBoardView() {
        if (mBoard != null) {
            View boardview = mBoard.getBoardRenderView();
            if (boardViewContainer != null && boardview != null) {
                boardViewContainer.removeView(boardview);
            }
        }
    }

    private void onTEBHistroyDataSyncCompleted() {
        mHistroyDataSyncCompleted = true;
        LogUtils.e("历史数据同步完成");
    }

}
