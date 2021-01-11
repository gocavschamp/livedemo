package com.fish.live.home;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.fish.live.R;
import com.fish.live.home.adapter.HomeViewPagerAdapter;
import com.fish.live.home.presenter.MianPresenter;
import com.fish.live.home.view.MainCotract;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gyf.barlibrary.ImmersionBar;
import com.nucarf.base.ui.mvp.BaseMvpActivity;
import com.nucarf.base.widget.ViewPagerSlide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseMvpActivity<MianPresenter> implements MainCotract.View, OnTabSelectListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.stl_main)
    SlidingTabLayout stlMain;
    @BindView(R.id.vp_main)
    ViewPagerSlide vpMain;
    private HomeViewPagerAdapter homeViewPagerAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInject() {
        setTheme(R.style.AppTheme);
        ImmersionBar.with(this).statusBarDarkFont(false, 0.2f).init();
        mPresenter = new MianPresenter(this);
    }

    @Override
    protected void initViewAndData() {
        addListener();
        homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        vpMain.setAdapter(homeViewPagerAdapter);
        mPresenter.loadData(true);

    }

    @Override
    public void addListener() {
        stlMain.setOnTabSelectListener(this);
        vpMain.setSlidAble(false);
        vpMain.addOnPageChangeListener(this);

    }

    @Override
    public void setData(ArrayList<String> data) {
        vpMain.setOffscreenPageLimit(data.size());
        homeViewPagerAdapter.setData(data);
        stlMain.setViewPager(vpMain, (String[]) data.toArray(new String[data.size()]));

    }

    @Override
    public void getMoneySuccess() {

    }

    @Override
    public void onTabSelect(int position) {

    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void addSubscribe(Disposable subscription) {
        super.addSubscribe(subscription);
    }
}
