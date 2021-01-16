package com.fish.live.livepush.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fish.live.LiveApplication;
import com.fish.live.R;
import com.fish.live.home.bean.IMLoginEvent;
import com.fish.live.home.bean.MsgEvent;
import com.fish.live.tencenttic.core.TICManager;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.nucarf.base.ui.BaseLazyFragment;
import com.nucarf.base.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * @Description TODO
 * @Author yuwenming
 * @Date 2021/1/12 15:55
 */
public class RoomChatFragment extends BaseLazyFragment {
    private static final String TYPE = "chat";
    private Unbinder unbinder;
    private Button send;
    private Button more;
    private EditText et_input;
    private TextView tvlog;
    private TICManager mTicManager;
    private String logMsg = "";
    private static final  int REQUESTCODE_FROM_FRAGMENT = 666;

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
        if (event instanceof MsgEvent) {
            MsgEvent imevent = (MsgEvent) event;
            addLog(imevent.getTextmsg());
        }
    }
    @Override
    protected void initView() {
        registerEventBus();
        unbinder = ButterKnife.bind(this, mRootView);
        send = mActivity.findViewById(R.id.btn_send);
        more = mActivity.findViewById(R.id.btn_more);
        et_input = mActivity.findViewById(R.id.et_message_input);
        tvlog = mActivity.findViewById(R.id.tv_log);
        send.setOnClickListener(v -> {
            sendGroupMessage(et_input.getText().toString());
        });
        more.setOnClickListener(v -> {
            new LFilePicker().withSupportFragment(this)
                    .withRequestCode(REQUESTCODE_FROM_FRAGMENT)
                    .withTitle("Open From Fragment")
                    .withMutilyMode(false)
                    .withChooseMode(true)
                    .withFileFilter(new String[]{".ppt", ".pptx"})
                    .start();

        });
        mTicManager = ((LiveApplication) mActivity.getApplication()).getTICManager();


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_FRAGMENT) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                EventBus.getDefault().post("ppt"+list.get(0));

            }
        }
    }

    private void sendGroupMessage(final String msg) {
        mTicManager.sendGroupTextMessage( msg, new TICManager.TICCallback() {
            @Override
            public void onSuccess(Object data) {
                LogUtils.e("[我]说: " + msg);
               addLog("[我]说: " + msg);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e("sendGroupMessage##onError##" + errMsg);

            }
        });
    }
    public void addLog(String logs) {
        if (tvlog != null) {
            logMsg = logMsg + "\r\n" + logs;
            tvlog.setText(logMsg + "\r\n");

            int offset = tvlog.getLineCount() * tvlog.getLineHeight();
            if (offset > tvlog.getHeight()) {
                tvlog.scrollTo(0, offset - tvlog.getHeight());
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
