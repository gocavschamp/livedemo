package com.nucarf.base.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.barlibrary.ImmersionBar;
import com.nucarf.base.ui.mvp.BaseMvpActivity;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/5/5.
 */

public abstract class BaseLazyFragment extends Fragment {
    protected Activity mActivity;
    protected View mRootView;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成
     * 当执行完onViewCreated方法后即为true
     */
    protected boolean mIsPrepare;

    /**
     * 是否加载完成
     * 当执行完onViewCreated方法后即为true
     */
    protected boolean mIsImmersion;

    protected ImmersionBar mImmersionBar;
    private Unbinder unbinder;
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (setLayoutId() != 0) {
            if (mRootView == null) {
                mRootView = inflater.inflate(setLayoutId(), container, false);
                unbinder = ButterKnife.bind(mRootView);
            } else {
                ViewGroup parent = (ViewGroup) mRootView.getParent();
                if (parent != null) {
                    parent.removeView(mRootView);
                }
            }
            return mRootView;
        }
        return null;
    }

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        if (isLazyLoad()) {
            mIsPrepare = true;
            mIsImmersion = true;
            onLazyLoad();
        } else {
            initData();
            if (isImmersionBarEnabled()) {
                initImmersionBar();
            }
        }
        setListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissDialog();
        unRegisterEventBus();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
            mImmersionBar = null;
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    /**
     * 是否懒加载
     *
     * @return the boolean
     */
    protected boolean isLazyLoad() {
        return true;
    }

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 用户可见时执行的操作
     */
    protected void onVisible() {
        onLazyLoad();
    }

    private void onLazyLoad() {
        if (mIsVisible && mIsPrepare) {
            mIsPrepare = false;
            initData();
        }
        if (mIsVisible && mIsImmersion && isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    /**
     * view与数据绑定
     */
    protected abstract void initView();

    /**
     * 设置监听
     */
    protected void setListener() {

    }

    /**
     * 用户不可见执行
     */
    protected void onInvisible() {

    }

    /**
     * show loading view
     */
    protected void showDialog() {
        if (mActivity != null) {
            if (mActivity instanceof BaseActivity) {
                ((BaseActivity) mActivity).showDialog();

            } else if (mActivity instanceof BaseActivityWithTitle) {
                ((BaseActivityWithTitle) mActivity).showDialog();

            } else if (mActivity instanceof BaseMvpActivity) {
                ((BaseMvpActivity) mActivity).showDialog();
            }
        }
    }

    /**
     * show loading view
     */
    protected boolean isDialogLoading() {
        if (mActivity != null) {
            if (mActivity instanceof BaseActivity) {
                return ((BaseActivity) mActivity).isDialogShowing();

            } else if (mActivity instanceof BaseActivityWithTitle) {
                return ((BaseActivityWithTitle) mActivity).isDialogShowing();

            } else if (mActivity instanceof BaseMvpActivity) {
                return ((BaseMvpActivity) mActivity).isDialogShowing();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * hide loading view
     */
    protected void dismissDialog() {
        if (mActivity != null) {
            if (mActivity instanceof BaseActivity) {
                ((BaseActivity) mActivity).dismissDialog();

            } else if (mActivity instanceof BaseActivityWithTitle) {
                ((BaseActivityWithTitle) mActivity).dismissDialog();

            } else if (mActivity instanceof BaseMvpActivity) {
                ((BaseMvpActivity) mActivity).dismissDialog();
            }
        }
    }


    /**
     * 找到activity的控件
     *
     * @param <T> the type parameter
     * @param id  the id
     * @return the t
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findActivityViewById(@IdRes int id) {
        return (T) mActivity.findViewById(id);
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

}
