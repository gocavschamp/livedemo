package com.fish.live.home.adapter;

import android.util.Log;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fish.live.R;
import com.fish.live.home.bean.HomeDataBean;
import com.nucarf.base.utils.LogUtils;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnPageChangeListener;

import java.util.List;

public class HomeNormalAdapter extends BaseMultiItemQuickAdapter<HomeDataBean, BaseViewHolder> {
    public HomeNormalAdapter(List<HomeDataBean> data) {
        super(data);
        /**
         * addItemType中的type种类，必须和接收到的种类数目一模一样。
         * 种类：有几种type，就要写几个addItemType。少写或者错写，会直接报错！！！
         *  (android.content.res.Resources$NotFoundException: Resource ID *******)
         *  时刻注意！！！！
         *  例如：这个type有10种！。（type=1,2,3...10）你就得写
         *     addItemType(1, R.layout.item_test_one);
         *     addItemType(2, R.layout.item_test_two);
         *     addItemType(3, R.layout.item_test_two);
         *     ....
         */
        try {
            addItemType(HomeDataBean.ONE, R.layout.home_banner_layout);
            addItemType(HomeDataBean.TWO, R.layout.home_live_layout);
        } catch (Exception ex) {
            Log.d("tag", ex.getMessage());
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeDataBean item) {

        switch (item.getItemType()) {
            case HomeDataBean.ONE:
                Banner banner = helper.getView(R.id.banner);
                ImageAdapter adapter = new ImageAdapter(item.getBanners());
                banner.setAdapter(adapter)//设置适配器
                        .setIndicator(new CircleIndicator(this.mContext))//设置指示器
                        .setOnBannerListener((data, position) -> {
                            LogUtils.d("position：" + position);
                        });//设置点击事件,传this也行
                banner.getViewPager2().setOffscreenPageLimit(item.getBanners().size());
                break;
            case HomeDataBean.TWO:
                helper.setGone(R.id.ll_top, helper.getLayoutPosition() == 1);
                break;
        }
    }

}