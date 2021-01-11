package com.fish.live.home.presenter;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.fish.live.home.MainActivity;
import com.fish.live.home.view.MainCotract;
import com.fish.live.login.LoginActivity;
import com.fish.live.service.AppService;
import com.nucarf.base.retrofit.RetrofitUtils;
import com.nucarf.base.retrofit.RxSchedulers;
import com.nucarf.base.retrofit.api.BaseHttp;
import com.nucarf.base.ui.mvp.BasePAV;
import com.nucarf.base.utils.SharePreUtils;
import com.nucarf.base.utils.UiGoto;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;


import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.Adler32;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yuwenming on 2019/10/21.
 */
public class MianPresenter extends BasePAV<MainCotract.View> implements MainCotract.Presenter {
    private Disposable mDisposable;

    @Override
    public void loadData(boolean isRefresh) {
        if (isRefresh) {
            mView.showDialog();
        }
//        Map<String, String> tokenParams = BaseHttp.getTokenParams();
//        RetrofitUtils.INSTANCE.getRxjavaClient(AppService.class)
//                .rxGetWaitRechargeList(BaseHttp.getBaseParams(),tokenParams)
//                .compose(RxSchedulers.rxSchedulerHelper())
//                .compose(RxSchedulers.handleResult())
//                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from((LifecycleOwner) mView)))
//                .subscribe(data -> {
//                    mView.dismissDialog();
//                    mView.setData(data);
//
//                }, throwable -> {
//                    mView.dismissDialog();
//
//                });
        mDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // 由于interval默认在新线程，所以我们应该切回主线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        if (aLong == 2) {
                            mView.dismissDialog();
                           ArrayList<String> data = new ArrayList<>();
                           data.add("推荐");
                           data.add("心血管");
                           data.add("儿科");
                           data.add("精神科");
                           data.add("骨科");
                           data.add("心里洁疾病");
                           data.add("推荐打发");
                            mView.setData(data);
                        }
                    }
                });
        mView.addSubscribe(mDisposable);
    }


    public MianPresenter(MainCotract.View view) {
        mView = view;
    }
}
