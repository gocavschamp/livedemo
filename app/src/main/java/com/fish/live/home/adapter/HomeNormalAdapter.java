package com.fish.live.home.adapter;

import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fish.live.R;
import com.fish.live.home.bean.HomeDataBean;
import com.nucarf.base.utils.LogUtils;
import com.nucarf.base.widget.RoundImageView;
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
            addItemType(HomeDataBean.THREE, R.layout.home_live_layout);
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
                TextView tv_title_name = helper.getView(R.id.tv_title_name);
                TextView tv_more_info = helper.getView(R.id.tv_more_info);
                RoundImageView iv_pic = helper.getView(R.id.iv_pic);
                tv_title_name.setText("近期直播");
                Glide.with(mContext)
                        .load("https://pics3.baidu.com/feed/1e30e924b899a9016d606d0fcf072d730308f5c8.jpeg?token=0589927e0d10f1b5092e2b16f29d2483&s=0E71218C678A20E640B7C68A0300C09D")
                        .into(iv_pic);
                helper.addOnClickListener(R.id.tv_more_info);
                break;
            case HomeDataBean.THREE:
                helper.setGone(R.id.ll_top, getData().get(helper.getLayoutPosition() - 1).getItemType() == HomeDataBean.TWO);
                TextView tv_title_name1 = helper.getView(R.id.tv_title_name);
                RoundImageView iv_pic1 = helper.getView(R.id.iv_pic);
                Glide.with(mContext)
                        .load("https://pics3.baidu.com/feed/1e30e924b899a9016d606d0fcf072d730308f5c8.jpeg?token=0589927e0d10f1b5092e2b16f29d2483&s=0E71218C678A20E640B7C68A0300C09D")
                        .into(iv_pic1);
                tv_title_name1.setText("精彩推荐");
                helper.addOnClickListener(R.id.tv_more_info);
                break;
        }
    }

}
