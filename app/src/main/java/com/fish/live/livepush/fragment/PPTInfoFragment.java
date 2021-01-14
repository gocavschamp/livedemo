package com.fish.live.livepush.fragment;

import android.os.Bundle;

import com.fish.live.R;
import com.nucarf.base.ui.BaseLazyFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Description TODO
 * @Author yuwenming
 * @Date 2021/1/12 15:55
 */
public class PPTInfoFragment extends BaseLazyFragment {
    private static final String TYPE = "ppt";
    private Unbinder unbinder;

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

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, mRootView);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
