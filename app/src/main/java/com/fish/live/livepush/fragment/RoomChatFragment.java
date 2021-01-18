package com.fish.live.livepush.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fish.live.LiveApplication;
import com.fish.live.R;
import com.fish.live.tencenttic.core.TICManager;
import com.fish.live.widget.TCInputTextMsgDialog;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.nucarf.base.ui.BaseLazyFragment;
import com.nucarf.base.utils.GlideUtils;
import com.nucarf.base.utils.LogUtils;
import com.nucarf.base.utils.ScreenUtil;
import com.nucarf.base.widget.CircleImageView;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;

import static android.app.Activity.RESULT_OK;

/**
 * @Description TODO
 * @Author yuwenming
 * @Date 2021/1/12 15:55
 */
public class RoomChatFragment extends BaseLazyFragment implements TCInputTextMsgDialog.OnTextSendListener {
    private static final String TYPE = "chat";
    @BindView(R.id.et_message_input)
    TextView etMessageInput;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.btn_more)
    Button btnMore;
    @BindView(R.id.btn_pic)
    Button btnPic;
    @BindView(R.id.btn_ppt)
    Button btnPpt;
    @BindView(R.id.rl_more_layout)
    RelativeLayout rlMoreLayout;
    @BindView(R.id.ll_input)
    LinearLayout llInput;
    @BindView(R.id.rv_log)
    RecyclerView rvLog;
    private Unbinder unbinder;
    private TICManager mTicManager;
    private String logMsg = "";
    private static final int REQUESTCODE_FROM_FRAGMENT = 666;
    private boolean isok;
    private MsgAdapter msgAdapter;
    private TCInputTextMsgDialog mInputTextMsgDialog;

    public RoomChatFragment() {
    }

    public static RoomChatFragment newInstance(String type) {
        RoomChatFragment myFragment = new RoomChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.live_room_layout;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    @Override
    protected void initData() {

    }

    @Subscribe
    public void onEvent(Object event) {
        if (event instanceof TIMMessage) {
            TIMMessage imevent = (TIMMessage) event;
            msgAdapter.addData(imevent);
            rvLog.scrollToPosition(msgAdapter.getData().size() - 1);
            rvLog.smoothScrollToPosition(msgAdapter.getData().size() - 1);
        }
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (isok) {
            return;
        }
        mTicManager = ((LiveApplication) mActivity.getApplication()).getTICManager();
        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, "1234");
        TIMMessage lastMsg = conversation.getLastMsg();
        conversation.getMessage(20, lastMsg, new TIMValueCallBack<List<TIMMessage>>() {
            @Override
            public void onError(int i, String s) {
                LogUtils.e("-----room---", s);
                isok = true;
            }

            @SuppressLint("CheckResult")
            @Override
            public void onSuccess(List<TIMMessage> timMessages) {
                LogUtils.e("-----room--", timMessages.size() + "");
                isok = true;
                Observable.fromIterable(timMessages)
                        .filter(v -> v.getElement(0).getType() == TIMElemType.Text)
                        .toList()
                        .subscribe(lists -> {
                            Collections.reverse(lists);
                            msgAdapter.addData(lists);
                            msgAdapter.notifyDataSetChanged();
                            if(lists.size()>0) {
                                rvLog.scrollToPosition(msgAdapter.getData().size() - 1);
                                rvLog.smoothScrollToPosition(msgAdapter.getData().size() - 1);
                            }
                        });
            }
        });
    }

    @Override
    protected void initView() {
        registerEventBus();
        unbinder = ButterKnife.bind(this, mRootView);
        rvLog = mActivity.findViewById(R.id.rv_log);
        ScreenUtil.setRecycleviewLinearLayout(mActivity, rvLog, true);
        msgAdapter = new MsgAdapter(R.layout.msg_layout);
        rvLog.setAdapter(msgAdapter);
        mInputTextMsgDialog = new TCInputTextMsgDialog(mActivity, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(this);


    }

    /**
     * 发消息弹出框
     */
    private void showInputMsgDialog() {
        WindowManager windowManager = mActivity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_FRAGMENT) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                EventBus.getDefault().post("ppt" + list.get(0));

            }
        }
    }

    private void sendGroupMessage(final String msg) {
        mTicManager.sendGroupTextMessage(msg, new TICManager.TICCallback() {
            @Override
            public void onSuccess(Object data) {
                msgAdapter.addData((TIMMessage) data);
                rvLog.scrollToPosition(msgAdapter.getData().size() - 1);
                rvLog.smoothScrollToPosition(msgAdapter.getData().size() - 1);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e("sendGroupMessage##onError##" + errMsg);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onTextSend(String msg, boolean tanmuOpen) {
        sendGroupMessage(msg);

    }

    @OnClick({R.id.et_message_input, R.id.btn_more, R.id.btn_pic, R.id.btn_ppt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_message_input:
                showInputMsgDialog();
                break;
            case R.id.btn_more:
                rlMoreLayout.setVisibility(rlMoreLayout.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
                break;
            case R.id.btn_pic:
                rlMoreLayout.setVisibility(View.GONE);
                new LFilePicker().withSupportFragment(this)
                        .withRequestCode(REQUESTCODE_FROM_FRAGMENT)
                        .withTitle("Open From Fragment")
                        .withMutilyMode(false)
                        .withChooseMode(true)
                        .withFileFilter(new String[]{".png", ".jpg", ".jpg"})
                        .withIsGreater(true)
                        .withFileSize(10 * 1024)
                        .start();
                break;
            case R.id.btn_ppt:
                rlMoreLayout.setVisibility(View.GONE);
                new LFilePicker().withSupportFragment(this)
                        .withRequestCode(REQUESTCODE_FROM_FRAGMENT)
                        .withTitle("Open From Fragment")
                        .withMutilyMode(false)
                        .withChooseMode(true)
                        .withFileFilter(new String[]{".ppt", ".pptx"})
                        .withIsGreater(true)
                        .withFileSize(10 * 1024)
                        .start();
                break;
        }
    }

    private class MsgAdapter extends BaseQuickAdapter<TIMMessage, BaseViewHolder> {
        public MsgAdapter(int layout) {
            super(layout);
        }

        @Override
        protected void convert(BaseViewHolder helper, TIMMessage item) {
            RelativeLayout rl_other = helper.getView(R.id.rl_other);
            CircleImageView head_other = helper.getView(R.id.head_other);
            TextView tv_other_text = helper.getView(R.id.tv_other_text);
            TextView tv_mine_name = helper.getView(R.id.tv_mine_name);
            RelativeLayout rl_mine = helper.getView(R.id.rl_mine);
            CircleImageView head_mine = helper.getView(R.id.head_mine);
            TextView tv_mine_text = helper.getView(R.id.tv_mine_text);
            TextView tv_other_name = helper.getView(R.id.tv_other_name);
            TIMTextElem textElem = (TIMTextElem) item.getElement(0);
            if (item.isSelf()) {
                rl_mine.setVisibility(View.VISIBLE);
                rl_other.setVisibility(View.GONE);
                tv_mine_name.setText("我");
                tv_mine_text.setText(textElem.getText() + "");
                GlideUtils.load(mContext, item.getSenderFaceUrl() + "", R.mipmap.ic_launcher, head_mine);
            } else {
                rl_mine.setVisibility(View.GONE);
                rl_other.setVisibility(View.VISIBLE);
                tv_other_name.setText(item.getSender() + "");
                tv_other_text.setText(textElem.getText() + "");
                GlideUtils.load(mContext, item.getSenderFaceUrl() + "", R.mipmap.ic_launcher, head_other);
            }
        }
    }
}
