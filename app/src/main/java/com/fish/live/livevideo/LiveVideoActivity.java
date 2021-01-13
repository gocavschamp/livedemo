package com.fish.live.livevideo;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.fish.live.R;
import com.fish.live.home.adapter.HomeViewPagerAdapter;
import com.fish.live.livevideo.adapter.LivePagerAdapter;
import com.fish.live.livevideo.presenter.LiveVideoPresenter;
import com.fish.live.livevideo.view.LiveVideoCotract;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.nucarf.base.ui.mvp.BaseMvpActivity;
import com.nucarf.base.widget.TitleLayout;
import com.nucarf.base.widget.ViewPagerSlide;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveVideoActivity extends BaseMvpActivity<LiveVideoPresenter> implements LiveVideoCotract.View {


    @BindView(R.id.title_layout)
    TitleLayout titleLayout;
    @BindView(R.id.player_content)
    FrameLayout playerContent;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp_main)
    ViewPagerSlide vpMain;

    @Override
    protected int getLayout() {
        return R.layout.activity_live_video;
    }


    @Override
    protected void initInject() {
        ImmersionBar.with(this).statusBarDarkFont(false, 0.2f).init();
        titleLayout.setLeftClickListener((v) -> finish());
        titleLayout.setTitleText("直播详情");
        LivePagerAdapter livePagerAdapter = new LivePagerAdapter(getSupportFragmentManager());
        vpMain.setAdapter(livePagerAdapter);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("简介");
        strings.add("文档");
        strings.add("聊天");
        livePagerAdapter.setData(strings);
        vpMain.setOffscreenPageLimit(strings.size());
        tabLayout.setViewPager(vpMain, (String[]) strings.toArray(new String[strings.size()]));

    }

    @Override
    protected void initViewAndData() {

    }

    @Override
    public void setData(ArrayList<String> data) {

    }

    @Override
    public void getMoneySuccess() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
