package com.nucarf.base.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loadingbox.LoadingBox;
import com.gyf.barlibrary.ImmersionBar;
import com.nucarf.base.R;
import com.nucarf.base.utils.DialogUtils;
import com.nucarf.base.widget.TitleLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public abstract class BaseActivityWithTitle extends AppCompatActivity {

    protected Context mContext;

    private Unbinder unbinder;
    private Dialog alertDialog;
    protected FrameLayout fl_root_content;
    protected TitleLayout titlelayout;
    protected LinearLayout ll_bar;
    private CompositeDisposable mCompositeDisposable;
    protected LoadingBox loadingBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        registerEventBus();
    }

    @Override
    public void setContentView(int layoutResID) {
        int layoutId = layoutResID;
        if (layoutId != -1) {
            LinearLayout content_view = (LinearLayout) View.inflate(this, R.layout.base_content_view, null);
            titlelayout = (TitleLayout) content_view.findViewById(R.id.titlelayout);
            ll_bar = (LinearLayout) content_view.findViewById(R.id.ll_bar);
            fl_root_content = content_view.findViewById(R.id.fl_root_content);
            fl_root_content.addView(View.inflate(this, layoutId, null));
            setContentView(content_view);
            initTitleBar();
            loadingBox = new LoadingBox(this, fl_root_content);

        }
//        ImmersionBar.with(this).statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
//                .flymeOSStatusBarFontColor(R.color.black)//修改 flyme OS 状态栏字体颜色
//                .keyboardEnable(true).init();
//        ImmersionBar.setTitleBar(this, findViewById(R.id.ll_bar));
        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).titleBar(ll_bar).init();

        unbinder = ButterKnife.bind(this);
        initData();
    }

    private void initTitleBar() {
        titlelayout.setLeftClickListener(v -> {
            onClickBack();
            finish();
        });

    }

    @Override
    public Resources getResources() {
        //需要升级到 v1.1.2 及以上版本才能使用 AutoSizeCompat
//        AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));//如果没有自定义需求用这个方法
        return super.getResources();
    }

    protected abstract void initData();

    /**
     * 注册EventBus通信组件
     */
    protected void registerEventBus() {
        EventBus.getDefault().register(this);
    }

    /**
     * show loading view
     */
    protected void showDialog() {
        if (alertDialog != null) {
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        } else {
            alertDialog = DialogUtils.dialogPro(mContext, "请稍后...", false);
        }
    }

    /**
     * hide loading view
     */
    protected void dismissDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    /**
     * hide loading view
     */
    protected boolean isDialogShowing() {
        if (alertDialog != null) {
            return alertDialog.isShowing();
        } else
            return false;
    }

    /**
     * 取消注册EventBus通信组件
     */
    protected void unRegisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * 退出应用
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onMessageEvent(String exitApp) {
//        if (exitApp.isExit()) {
//            finish();
//        }
    }

    /**
     * click back icon
     */
    protected void onClickBack() {

    }

    /**
     * show titlebar or not
     */
    protected void showTitleBar(boolean showTitleBar) {
        if (null != titlelayout) {
            titlelayout.setVisibility(showTitleBar ? View.VISIBLE : View.GONE);
        }
    }


    protected String getName() {
        return getClass().getSimpleName();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
//        MobclickAgent.onPageStart(getClass().getName());
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(getClass().getName());
//        MobclickAgent.onPause(this);
    }

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    @Override
    protected void onDestroy() {
        unRegisterEventBus();
        unSubscribe();
        if (null != unbinder) {
            unbinder.unbind();
        }
        dismissDialog();
        ImmersionBar.with(this).destroy();
        super.onDestroy();
    }
}