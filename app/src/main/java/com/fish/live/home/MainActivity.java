package com.fish.live.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.viewpager.widget.ViewPager;

import com.fish.live.R;
import com.fish.live.home.adapter.HomeViewPagerAdapter;
import com.fish.live.home.presenter.MianPresenter;
import com.fish.live.home.view.MainCotract;
import com.fish.live.search.SearchActivity;
import com.fish.live.widget.CardFilterPopupWindow;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gyf.barlibrary.ImmersionBar;
import com.nucarf.base.bean.StringBean;
import com.nucarf.base.ui.mvp.BaseMvpActivity;
import com.nucarf.base.utils.AndroidUtil;
import com.nucarf.base.utils.LogUtils;
import com.nucarf.base.utils.UiGoto;
import com.nucarf.base.widget.ViewPagerSlide;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseMvpActivity<MianPresenter> implements MainCotract.View, OnTabSelectListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.stl_main)
    SlidingTabLayout stlMain;
    @BindView(R.id.vp_main)
    ViewPagerSlide vpMain;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.search)
    ImageView search;
    private HomeViewPagerAdapter homeViewPagerAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initInject() {
        setTheme(R.style.AppTheme);
        fixOppoAssetManager();
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


    @OnClick({R.id.add, R.id.search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add:
                ArrayList<StringBean> stringBeans = new ArrayList<>();
                stringBeans.add(new StringBean());
                stringBeans.add(new StringBean());
                stringBeans.add(new StringBean());
                stringBeans.add(new StringBean());
                stringBeans.add(new StringBean());
                stringBeans.add(new StringBean());
                stringBeans.add(new StringBean());
                stringBeans.add(new StringBean());
                stringBeans.add(new StringBean());
                stringBeans.add(new StringBean());
                stringBeans.add(new StringBean());
                stringBeans.add(new StringBean());
                stringBeans.add(new StringBean());
                showPop(stringBeans);
                break;
            case R.id.search:
                UiGoto.startAty(mContext, SearchActivity.class);
                break;
        }
    }

    private void showPop(List<StringBean> list) {
        CardFilterPopupWindow cardFilterPopupWindow = new CardFilterPopupWindow(mContext, list);
        cardFilterPopupWindow.setOnItemClickListener(new CardFilterPopupWindow.OnItemClickListener() {
            @Override
            public void clickItem(int position) {
//                category = filterlist.get(position).getCategory();
//                tvOilType.setText(filterlist.get(position).getCategory_name());
//                getCardList();
            }
        });
        cardFilterPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        cardFilterPopupWindow.showAtBottom(stlMain);

    }


    /**
     * fix 部分OPPO机型 AssetManager.finalize() timed out
     */
    private void fixOppoAssetManager() {
        String device = AndroidUtil.getDeviceName();
        LogUtils.d(device);
        if (!TextUtils.isEmpty(device)) {
            if (device.contains("OPPO R9") || device.contains("OPPO A5")) {
                try {
                    // 关闭掉FinalizerWatchdogDaemon
                    Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
                    Method method = clazz.getSuperclass().getDeclaredMethod("stop");
                    method.setAccessible(true);
                    Field field = clazz.getDeclaredField("INSTANCE");
                    field.setAccessible(true);
                    method.invoke(field.get(null));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
