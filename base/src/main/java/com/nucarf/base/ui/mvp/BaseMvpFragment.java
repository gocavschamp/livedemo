package com.nucarf.base.ui.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.nucarf.base.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.annotations.Nullable;

/**
 * Created by yuwenmingon 2019/1/17
 * <p>
 * MVP BaseMvpFragment
 *
 * @author Administrator
 *
 * */
public abstract class BaseMvpFragment<T extends BasePersenter> extends Fragment implements BaseView {
//    @Inject
    @Nullable
    protected T mPresenter;

    protected Unbinder unbinder;
    protected View mRootView, mErrorView, mEmptyView;
//    protected KProgressHUD mKProgressHUD;

    protected abstract int getLayoutId();

    protected abstract void initInject();

    protected abstract void initEventAndData();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        unbinder = ButterKnife.bind(this, mRootView);
        initInject();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        unbinder = ButterKnife.bind(this, view);
        initEventAndData();
    }

//    protected FragmentComponent getFragmentComponent() {
//        return DaggerFragmentComponent.builder()
//                .appComponent(App.getAppComponent())
//                .fragmentModule(new FragmentModule(this))
//                .build();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.detachView();
    }

    @Override
    public void onSucess() {

    }

    @Override
    public void onFail() {
        ToastUtils.showShort("获取数据失败");
    }

    @Override
    public void onNetError() {
        ToastUtils.showShort("请检查网络是否连接");
    }

    @Override
    public void onReLoad() {

    }
}
