package com.fish.live.home.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.lifecycle.LifecycleOwner;

import com.fish.live.home.view.MainCotract;
import com.fish.live.service.AppService;
import com.nucarf.base.retrofit.RetrofitUtils;
import com.nucarf.base.retrofit.RxSchedulers;
import com.nucarf.base.retrofit.api.BaseHttp;
import com.nucarf.base.ui.mvp.BasePAV;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;


import java.util.Map;

/**
 * Created by yuwenming on 2019/10/21.
 */
public class MianPresenter extends BasePAV<MainCotract.View> implements MainCotract.Presenter {
    @Override
    public void loadData(boolean isRefresh) {
        if (isRefresh) {
            mView.showDialog();
        }
        Map<String, String> tokenParams = BaseHttp.getTokenParams();
        RetrofitUtils.INSTANCE.getRxjavaClient(AppService.class)
                .rxGetWaitRechargeList(BaseHttp.getBaseParams(),tokenParams)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) mView)))
                .subscribe(data -> {
                    mView.dismissDialog();
                    mView.setData(data);

                }, throwable -> {
                    mView.dismissDialog();

                });
    }


    public MianPresenter(MainCotract.View view) {
        mView = view;
    }
}
