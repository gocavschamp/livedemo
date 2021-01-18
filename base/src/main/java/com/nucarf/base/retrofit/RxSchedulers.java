package com.nucarf.base.retrofit;


import android.util.Log;

import com.nucarf.base.retrofit.logiclayer.BaseResult;

import java.nio.Buffer;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by hzy on 2019/1/10
 * 线程调度
 *
 * @author Administrator
 */
public class RxSchedulers {

    final static ObservableTransformer Stf = upstream -> upstream.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread());
    public static String TAG = "tag";

    static <T> ObservableTransformer<T, T> applySchedulers() {
        return Stf;
    }

    @SuppressWarnings("unchecked")
    public static <T> ObservableTransformer<T, T> io_main() {
        return (ObservableTransformer<T, T>) applySchedulers();
    }

    /**
     * 统一返回结果处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<BaseResult<T>, T> handleFlowableResult() {   //compose判断结果
        return new FlowableTransformer<BaseResult<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<BaseResult<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<BaseResult<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(BaseResult<T> tGankHttpResponse) {
                        if (!tGankHttpResponse.isSuccessed()) {
                            return createFlowableData(tGankHttpResponse.getData());
                        } else {
                            return Flowable.error(new ApiException("服务器返回error"));
                        }
                    }
                });
            }
        };
    }

    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
    /**
     * 统一返回结果处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<BaseResult<T>, T> handleResult() {   //compose判断结果
        return new ObservableTransformer<BaseResult<T>, T>() {
            @Override
            public Observable<T> apply(Observable<BaseResult<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<BaseResult<T>, Observable<T>>() {
                    @Override
                    public Observable<T> apply(BaseResult<T> tGankHttpResponse) {
                        if (tGankHttpResponse.isSuccessed()) {
                            return createData(tGankHttpResponse.getData());
                        } else {
                            return Observable.error(new Throwable("服务器 error"));
                        }
                    }
                });
            }
        };
    }
    /**
     * 统一返回结果处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<BaseResult<T>, T> handleResultTemp() {   //compose判断结果
        return new ObservableTransformer<BaseResult<T>, T>() {
            @Override
            public Observable<T> apply(Observable<BaseResult<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<BaseResult<T>, Observable<T>>() {
                    @Override
                    public Observable<T> apply(BaseResult<T> tGankHttpResponse) {
                        if (tGankHttpResponse.isSuccessed()) {
                            return createData(tGankHttpResponse.getData());
                        } else {
                            return Observable.error(new Throwable("服务器 error"));
                        }
                    }
                });
            }
        };
    }

    /**
     * Observable
     *
     * @param <T>
     * @return
     */
    public static <T> Observable<T> createData(final T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                    Log.d(RxSchedulers.TAG, ": " + e);
                }
            }
        });
    }

    /**
     * Observable
     *
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> createFlowableData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                    Log.d(RxSchedulers.TAG, ": " + e);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }
}
