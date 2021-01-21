package com.fish.live.livepush.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fish.live.Constants;
import com.fish.live.LiveApplication;
import com.fish.live.R;
import com.fish.live.bean.BoardPhotoEvent;
import com.fish.live.home.bean.IMLoginEvent;
import com.fish.live.photo.bean.PhotoBean;
import com.fish.live.tencenttic.core.TICClassroomOption;
import com.fish.live.tencenttic.core.TICManager;
import com.fish.live.tencenttic.core.impl.TICManagerImpl;
import com.nucarf.base.ui.BaseLazyFragment;
import com.nucarf.base.utils.BaseAppCache;
import com.nucarf.base.utils.LogUtils;
import com.nucarf.base.utils.SharePreUtils;
import com.nucarf.base.utils.StringUtils;
import com.nucarf.base.utils.ToastUtils;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberRoleType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupSelfInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.rtmp.TXLog;
import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.teduboard.TEduBoardController;

import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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
    private static String taskid;
    private int currentIndex;
    private int totalPage1;
    private boolean isdrawable;
    private boolean isSyncable;
    private String mUserID;
    private String mUserSig;
    private int mRomotCurrentIndex;

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
//                getUserRoomInfo();
            }
        } else if (event instanceof String) {
            String ppt = (String) event;
            if (ppt.startsWith("ppt")) {
                String ppt1 = ppt.replaceFirst("ppt", "");
                String toUTF8 = StringUtils.toUTF8(ppt1);
                TEduBoardController.TEduBoardTranscodeConfig transcodeConfig = new TEduBoardController.TEduBoardTranscodeConfig();
                transcodeConfig.isStaticPPT = true;
                mBoard.applyFileTranscode(toUTF8, transcodeConfig);
            }
        } else if (event instanceof BoardPhotoEvent) {
            BoardPhotoEvent pptPhoto = (BoardPhotoEvent) event;
            ArrayList<PhotoBean> photoData = pptPhoto.getData();
            LogUtils.e("PHOTO --- " + photoData.size());
            List<String> images = new ArrayList<>();
            mBoard.reset();
            for (PhotoBean p : photoData) {
                images.add(p.getPath());
                LogUtils.e("PHOTO --- current board--" +  mBoard.getCurrentBoard());
                mBoard.addBoard(p.getPath()+"--");
                LogUtils.e("PHOTO --- add   current  board--" +  mBoard.getCurrentBoard());
                mBoard.addImageElement(p.getPath());
                mBoard.nextBoard();
//                mBoard.setBackgroundImage(p.getPath(),TEduBoardController.TEduBoardImageFitMode.TEDU_BOARD_IMAGE_FIT_MODE_CENTER);
            }
//            mBoard.addImagesFile(images);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        unInitTrtc();
        removeBoardView();
        mTicManager.removeIMMessageListener(this);
        mTicManager.removeEventListener(this);
        quitClass();
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
        mBoard.setDrawEnable(false);
        mBoard.setDataSyncEnable(false);
//        mTicManager.addIMMessageListener(this);
//        mTicManager.addEventListener(this);

    }

    /**
     * 进入课堂
     */
    private void joinClass() {
        mRoomId = 1234;
        //1、设置白板的回调
        mBoardCallback = new MyBoardCallback(this);
        mBoard = mTicManager.getBoardController();
        //2、如果用户希望白板显示出来时，不使用系统默认的参数，就需要设置特性缺省参数，如是使用默认参数，则填null。
        TEduBoardController.TEduBoardInitParam initParam = new TEduBoardController.TEduBoardInitParam();
        initParam.brushColor = new TEduBoardController.TEduBoardColor(255, 0, 0, 255);
        initParam.smoothLevel = 0; //用于指定笔迹平滑级别，默认值0.1，取值[0, 1]
        initParam.dataSyncEnable = false;
        initParam.drawEnable = false;
        TICClassroomOption classroomOption = new TICClassroomOption();
        classroomOption.classId = mRoomId;
        classroomOption.boardCallback = mBoardCallback;
        classroomOption.boardInitPara = initParam;
        mTicManager.joinClassroom(classroomOption, new TICManager.TICCallback() {
            @Override
            public void onSuccess(Object data) {
                LogUtils.e("进入课堂成功:" + mRoomId);
                getUserRoomInfo();
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

                } else {
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
                mBoard.prevBoard();
                currentIndex--;
                if (currentIndex < 0) {
                    currentIndex = 0;
                    return;
                }
                this.totalPage.setText((currentIndex + 1) + "/" + totalPage1);
                break;
            case R.id.total_page:
                TEduBoardController.TEduBoardTranscodeFileResult t = new TEduBoardController.TEduBoardTranscodeFileResult("腾讯课堂介绍", "https://transcode-result-1259648581.file.myqcloud.com/g6lcd5qcpqrgit7mopob/", 9, "1766x987");
                mBoard.addTranscodeFile(t, true);
                totalPage1 = t.pages;
                currentIndex = 1;
                this.totalPage.setText(totalPage1 + "/" + currentIndex);
                break;
            case R.id.next_page:
                mBoard.nextBoard();
                if (currentIndex >= mRomotCurrentIndex) {
                    return;
                }
                currentIndex++;
                if (currentIndex >= totalPage1 - 1) {
                    currentIndex = totalPage1 - 1;
                    return;
                }
                this.totalPage.setText((currentIndex + 1) + "/" + totalPage1);

                break;
        }
    }

    private void openFile(String path) {
        Bundle bundle = new Bundle();
        //文件路径
        bundle.putString("filePath", path);
        //临时文件的路径，必须设置，否则会报错
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getAbsolutePath() + "腾讯文件TBS");
        //准备
        TbsReaderView tbsReaderView = new TbsReaderView(mActivity, null);
//        boolean result =(TbsReaderView) mBoard.getBoardRenderView().(getFileType(path), false);
//        if (result) {
        //预览文件
        tbsReaderView.openFile(bundle);
//        }
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
        Log.e(TAG, "onTICUserVideoAvailable:" + userId + "|" + available);
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
            Log.e(TAG, "onTEBError:" + code + "|" + msg);
        }

        @Override
        public void onTEBWarning(int code, String msg) {
            Log.e(TAG, "onTEBWarning:" + code + "|" + msg);
        }

        @Override
        public void onTEBInit() {
            Log.e(TAG, "onTEBInit:");
            PPTInfoFragment activity = mActivityRef.get();
            if (activity != null) {
                activity.addBoardView();
            }
        }

        @Override
        public void onTEBHistroyDataSyncCompleted() {
            Log.e(TAG, "onTEBHistroyDataSyncCompleted:");

            PPTInfoFragment activityEx = mActivityRef.get();
            if (activityEx != null) {
                activityEx.onTEBHistroyDataSyncCompleted();
            }
        }

        @Override
        public void onTEBSyncData(String data) {
            Log.e(TAG, "onTEBSyncData:" + data);

        }


        @Override
        public void onTEBAddBoard(List<String> boardId, final String fileId) {
            Log.e(TAG, "onTEBAddBoard:" + fileId);
        }

        @Override
        public void onTEBDeleteBoard(List<String> boardId, final String fileId) {
            Log.e(TAG, "onTEBDeleteBoard:" + fileId + "|" + boardId);
        }

        @Override
        public void onTEBGotoBoard(String boardId, final String fileId) {
            Log.e(TAG, "onTEBGotoBoard:" + fileId + "|" + boardId);
            TEduBoardController.TEduBoardFileInfo mBoardFileInfo = mActivityRef.get().mBoard.getFileInfo(fileId);
            mActivityRef.get().currentIndex = mBoardFileInfo.pageIndex;
            mActivityRef.get().mRomotCurrentIndex = mBoardFileInfo.pageIndex;
            mActivityRef.get().totalPage.setText((mBoardFileInfo.pageIndex + 1) + "/" + mBoardFileInfo.pageCount);
        }

        @Override
        public void onTEBGotoStep(int currentStep, int total) {
            Log.e(TAG, "onTEBGotoStep:" + currentStep + "|" + total);
        }

        @Override
        public void onTEBRectSelected() {
            Log.e(TAG, "onTEBRectSelected:");
        }

        @Override
        public void onTEBRefresh() {
            Log.e(TAG, "onTEBRefresh:");
        }

        @Override
        public void onTEBDeleteFile(String fileId) {
        }

        @Override
        public void onTEBSwitchFile(String fileId) {
            Log.e(TAG, "onTEBSwitchFile:" + fileId);
        }

        @Override
        public void onTEBAddTranscodeFile(String s) {
            Log.e(TAG, "onTEBAddTranscodeFile:" + s);
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
            Log.e(TAG, "onTEBFileUploadProgress:" + path + " percent:" + percent);
        }

        @Override
        public void onTEBFileUploadStatus(final String path, int status, int code, String statusMsg) {
            Log.e(TAG, "onTEBFileUploadStatus:" + path + " status:" + status);
        }

        @Override
        public void onTEBFileTranscodeProgress(String s, String s1, String s2, TEduBoardController.TEduBoardTranscodeFileResult tEduBoardTranscodeFileResult) {
            Log.e(TAG, "onTEBFileTranscodeProgress:" + tEduBoardTranscodeFileResult.progress);
            Log.e(TAG, "onTEBFileTranscodeProgress:" + s + 00 + s1 + 00 + s2);

            taskid = tEduBoardTranscodeFileResult.taskid;
            if (tEduBoardTranscodeFileResult.progress >= 100) {
                PPTInfoFragment activityEx = mActivityRef.get();
                activityEx.mBoard.addTranscodeFile(tEduBoardTranscodeFileResult, true);
            }
        }

        @Override
        public void onTEBH5FileStatusChanged(String fileId, int status) {
            Log.e(TAG, "onTEBH5FileStatusChanged:" + fileId);

        }

        @Override
        public void onTEBAddImagesFile(String fileId) {
            Log.e(TAG, "onTEBAddImagesFile:" + fileId);
            PPTInfoFragment activityEx = mActivityRef.get();
            TEduBoardController.TEduBoardFileInfo fileInfo = activityEx.mBoard.getFileInfo(fileId);
        }

        @Override
        public void onTEBVideoStatusChanged(String fileId, int status, float progress, float duration) {
            Log.e(TAG, "onTEBVideoStatusChanged:" + fileId + " | " + status + "|" + progress);
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
            Log.e(TAG, "onTEBImageStatusChanged:" + boardId + "|" + url + "|" + status);
        }

        @Override
        public void onTEBSetBackgroundImage(final String url) {
            Log.e(TAG, "onTEBSetBackgroundImage:" + url);
        }

        @Override
        public void onTEBAddImageElement(final String url) {
            Log.e(TAG, "onTEBAddImageElement:" + url);
        }

        @Override
        public void onTEBAddElement(String s, String s1) {
            Log.e(TAG, "onTEBAddElement:");

        }

        @Override
        public void onTEBBackgroundH5StatusChanged(String boardId, String url, int status) {
            Log.e(TAG, "onTEBBackgroundH5StatusChanged:" + boardId + " url:" + boardId + " status:" + status);
        }
    }

    void addBoardView() {
        View boardview = mBoard.getBoardRenderView();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        if (boardViewContainer != null) {
            boardViewContainer.addView(boardview, layoutParams);
        }

//        TEduBoardController.TEduBoardTranscodeFileResult transcodeFileResult = new TEduBoardController.TEduBoardTranscodeFileResult("欢迎新同学", "https://ppt2h5-1259648581.file.myqcloud.com/ghikv1979vq1bhl3jtpb/index.html", 23, "960x540");
//        mBoard.addTranscodeFile(transcodeFileResult, false);
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
        String currentBoard = mBoard.getCurrentFile();
        TEduBoardController.TEduBoardFileInfo fileInfo = mBoard.getFileInfo(currentBoard);
        this.totalPage.setText((fileInfo.pageIndex + 1) + "/" + fileInfo.pageCount);
        totalPage1 = fileInfo.pageCount;
        currentIndex = fileInfo.pageIndex;
        mRomotCurrentIndex = fileInfo.pageIndex;
        LogUtils.e("历史数据同步完成");
    }

}
