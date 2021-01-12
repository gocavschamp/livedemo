package com.fish.live.livepush.presenter;

import androidx.annotation.NonNull;

import com.fish.live.livepush.view.LivePushCotract;
import com.nucarf.base.ui.mvp.BasePAV;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yuwenming on 2019/10/21.
 */
public class LivePushPresenter extends BasePAV<LivePushCotract.View> implements LivePushCotract.Presenter {
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


    public LivePushPresenter(LivePushCotract.View view) {
        mView = view;
    }
}
