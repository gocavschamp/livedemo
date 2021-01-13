package com.fish.live.livepush.fragment;

import android.os.Bundle;

import com.fish.live.R;
import com.nucarf.base.ui.BaseLazyFragment;

/**
 * @Description TODO
 * @Author yuwenming
 * @Date 2021/1/12 15:55
 */
public class RoomChatFragment extends BaseLazyFragment {
    private static final String TYPE = "chat";

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

    @Override
    protected void initView() {

    }
}
