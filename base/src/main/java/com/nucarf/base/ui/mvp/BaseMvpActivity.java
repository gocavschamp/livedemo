package com.nucarf.base.ui.mvp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nucarf.base.utils.DialogUtils;
import com.nucarf.base.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by yuwenmingon 2019/1/17
 * <p>
 * MVP BaseMvpActivity
 *
 * @author yuwenming
 */
public abstract class BaseMvpActivity<T extends BasePersenter> extends AppCompatActivity implements BaseView {


    //    @Inject //drager
    @Nullable
    public T mPresenter;

    protected Unbinder unbinder;
    protected Context mContext;
    private Dialog alertDialog;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(getLayout());
        unbinder = ButterKnife.bind(this);
        ButterKnife.bind(this);
        registerEventBus();
        initInject();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initViewAndData();
    }

    /**
     * 注册EventBus通信组件
     */
    protected void registerEventBus() {
        EventBus.getDefault().register(this);
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
     * 取消注册EventBus通信组件
     */
    protected void unRegisterEventBus() {
        EventBus.getDefault().unregister(this);
    }
//    protected ActivityComponent getActivityComponent() {
//        return DaggerActivityComponent.builder()
//                .appComponent(App.getAppComponent())
//                .activityModule(new ActivityModule(this))
//                .build();
//    }


    protected abstract int getLayout();

    protected abstract void initInject();

    protected abstract void initViewAndData();

    /**
     * show loading view
     */
    public void showDialog() {
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
     * hide loading view
     */
    public void dismissDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    /**
     * hide loading view
     */
    public boolean isDialogShowing() {
        if (alertDialog != null) {
            return alertDialog.isShowing();
        } else
            return false;
    }

    @Override
    public void onSucess() {

    }

    @Override
    public void showToast(String toast) {
        ToastUtils.show_middle(this, toast, 0);
    }

    @Override
    public void onFail() {
//        ToastUtils.showShort("获取数据失败");
    }

    @Override
    public void onNetError() {
//        ToastUtils.showShort("请检查网络是否连接");
    }

    @Override
    public void onReLoad() {

    }

    public void addListener() {

    }

    public void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    public void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterEventBus();
        unSubscribe();
        dismissDialog();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (unbinder != null&& unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
            unbinder = null;
        }
    }
}
