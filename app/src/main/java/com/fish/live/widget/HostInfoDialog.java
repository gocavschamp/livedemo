package com.fish.live.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.fish.live.R;
import com.nucarf.base.bean.StringBean;
import com.nucarf.base.widget.CircleImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ${yuwenming} on 2020-12-11 16:53:20.
 */

public class HostInfoDialog extends DialogFragment {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.position)
    TextView position;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.office)
    TextView office;
    @BindView(R.id.hospital)
    TextView hospital;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.ll_layout)
    RelativeLayout llLayout;
    @BindView(R.id.head)
    CircleImageView head;
    @BindView(R.id.tv_close)
    ImageView tvClose;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    @BindView(R.id.rl_card_list)
    RelativeLayout rlCardList;
    private View rootView;
    private Activity context;
    private OnDialogClickListener onDialogClickListener;
    private View view;
    private Unbinder unbinder;
    private StringBean data;

    @Override
    public void onStart() {
        super.onStart();
        //设置dialog的 进出 动画
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.host_info_card_dialog, container);
        data = (StringBean) getArguments().getSerializable("data");
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void initData() {


    }


    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @OnClick(R.id.tv_close)
    public void onViewClicked() {
        dismiss();
    }


    public interface OnDialogClickListener {
        void onConfirmClick(int position);
    }
}
