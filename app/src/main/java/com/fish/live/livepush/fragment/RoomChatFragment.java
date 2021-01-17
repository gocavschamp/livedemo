package com.fish.live.livepush.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fish.live.LiveApplication;
import com.fish.live.R;
import com.fish.live.home.bean.MsgEvent;
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
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

import static android.app.Activity.RESULT_OK;
import static com.tencent.imsdk.v2.V2TIMMessage.V2TIM_ELEM_TYPE_TEXT;

/**
 * @Description TODO
 * @Author yuwenming
 * @Date 2021/1/12 15:55
 */
public class RoomChatFragment extends BaseLazyFragment implements TCInputTextMsgDialog.OnTextSendListener {
    private static final String TYPE = "chat";
    private Unbinder unbinder;
    private Button send;
    private Button more;
    private TextView et_input;
    private RecyclerView rvlog;
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
            rvlog.scrollToPosition(msgAdapter.getData().size() - 1);
            rvlog.smoothScrollToPosition(msgAdapter.getData().size() - 1);
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
        conversation.getMessage(20, null, new TIMValueCallBack<List<TIMMessage>>() {
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
                            rvlog.scrollToPosition(msgAdapter.getData().size() - 1);
                            rvlog.smoothScrollToPosition(msgAdapter.getData().size() - 1);
                        });
            }
        });
    }

    @Override
    protected void initView() {
        registerEventBus();
        unbinder = ButterKnife.bind(this, mRootView);
        send = mActivity.findViewById(R.id.btn_send);
        more = mActivity.findViewById(R.id.btn_more);
        et_input = mActivity.findViewById(R.id.et_message_input);
        rvlog = mActivity.findViewById(R.id.rv_log);
        ScreenUtil.setRecycleviewLinearLayout(mActivity, rvlog, true);
        msgAdapter = new MsgAdapter(R.layout.msg_layout);
        rvlog.setAdapter(msgAdapter);
        et_input.setOnClickListener(v -> {
            showInputMsgDialog();
        });
        more.setOnClickListener(v -> {
            new LFilePicker().withSupportFragment(this)
                    .withRequestCode(REQUESTCODE_FROM_FRAGMENT)
                    .withTitle("Open From Fragment")
                    .withMutilyMode(false)
                    .withChooseMode(true)
                    .withFileFilter(new String[]{".ppt", ".pptx"})
                    .withIsGreater(true)
                    .withFileSize(10 * 1024)
                    .start();

        });
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
                rvlog.scrollToPosition(msgAdapter.getData().size() - 1);
                rvlog.smoothScrollToPosition(msgAdapter.getData().size() - 1);
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
