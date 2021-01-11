package com.fish.live.home.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.fish.live.home.HomeNormalFragment;

import java.util.ArrayList;

/**
 * 说明：首页tab适配 <br>
 * 时间：2017/7/11 23:46<br>
 * 修改记录： <br>
 */

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> data;

    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
                return HomeNormalFragment.newInstance(data.get(position));
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
