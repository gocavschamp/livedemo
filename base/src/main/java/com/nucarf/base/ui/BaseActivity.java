package com.nucarf.base.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;
import com.nucarf.base.R;
import com.nucarf.base.utils.DialogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;

    private Unbinder unbinder;
    private CompositeDisposable mCompositeDisposable;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ImmersionBar.with(this).statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .flymeOSStatusBarFontColor(R.color.black)  //修改 flyme OS 状态栏字体颜色
                .keyboardEnable(true).init();
        registerEventBus();
//        PushAgent.getInstance(this).onAppStart();
    }
    @Override
    public Resources getResources() {
        //需要升级到 v1.1.2 及以上版本才能使用 AutoSizeCompat
//        AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()));//如果没有自定义需求用这个方法
        return super.getResources();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        unbinder = ButterKnife.bind(this);
        initData();
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
    protected abstract void initData();

    /**
     * 注册EventBus通信组件
     */
    protected void registerEventBus() {
        EventBus.getDefault().register(this);
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
    public void onEvent(String exitApp) {
//        if (exitApp.isExit()) {
//            finish();
//        }
    }


    protected String getName() {
        return getClass().getSimpleName();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
////        EventBus.getDefault().post("andServer-start");
//    }
    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    /**
     * show loading view
     */
    protected void showDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (isDestroyed()) {
                return;
            }
        } else {
            if (isFinishing()) {
                return;
            }
        }
        if (alertDialog != null) {
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        } else {
            alertDialog = DialogUtils.dialogPro(mContext, "请稍后...", false);
            alertDialog.show();
        }
    }

    /**
     * is show loading view
     */
    protected boolean isDialogShowing() {
        if (alertDialog != null) {
            return alertDialog.isShowing();
        } else {
            return false;
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
    @Override
    protected void onDestroy() {
        unSubscribe();
        unRegisterEventBus();
        if (null != unbinder) {
            unbinder.unbind();
        }
//        ImmersionBar.with(this).destroy();
        super.onDestroy();
    }
}