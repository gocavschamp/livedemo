package com.fish.live.livevideo.adapter;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fish.live.livepush.fragment.HostInfoFragment;
import com.fish.live.livepush.fragment.PPTInfoFragment;
import com.fish.live.livepush.fragment.RoomChatFragment;

import java.util.ArrayList;

public class LivePagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> data;

    public LivePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HostInfoFragment.newInstance(data.get(position));
            case 1:
                if(data.size() == 2) {
                    return RoomChatFragment.newInstance(data.get(position));
                }
                return PPTInfoFragment.newInstance(data.get(position));
            case 2:
                return RoomChatFragment.newInstance(data.get(position));
        }
        return HostInfoFragment.newInstance(data.get(position));
    }

    @Override
    public int getCount() {
        return null == data ? 0 : data.size();
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
