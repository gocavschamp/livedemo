package com.nucarf.base.retrofit;


import com.nucarf.base.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * Creator: kakaluote.
 * Email  : kakaluote.com
 */
public enum OkHttpUtils {
    INSTANCE;

    OkHttpUtils() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设定10秒超时
        builder.connectTimeout(RetrofitConfig.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(RetrofitConfig.HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);

        builder.addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.e("OkHttp", message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY))
        ;//网络和日志拦截
        okHttpClient = builder.build();
    }



    private OkHttpClient okHttpClient;

    public OkHttpClient getClient() {
        return okHttpClient;
    }
}
