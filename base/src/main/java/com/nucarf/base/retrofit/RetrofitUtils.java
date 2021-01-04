package com.nucarf.base.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Creator: kakaluote.
 * Email  : kakaluote.com
 */
public enum RetrofitUtils {
    INSTANCE;

    private Retrofit retrofitsingleton;
    RetrofitUtils() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit.Builder builder = new Retrofit.Builder();
        //配置服务器路径(baseUrl和注解中url连接的”/”最好写在baseUrl的后面，而不是注解中url的前面，否则可能会出现不可预知的错误。)
        builder.baseUrl("http://tapi.nucarf.cn/V1/");
//        builder.baseUrl(RetrofitConfig.TEST_HOST_URL);
        //设置OKHttpClient为网络客户端
        builder.client(OkHttpUtils.INSTANCE.getClient());
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        retrofitsingleton = builder.build();

    }

    public <T> T getClient(Class<T> clazz) {
        return retrofitsingleton.create(clazz);
    }

    //基于特定的url生成的 RestAdapter
    public <T> T getSpecialClient(String url, Class<T> clazz) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(url);//配置服务器路径
        //设置OKHttpClient为网络客户端
        builder.client(OkHttpUtils.INSTANCE.getClient());
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit specialSingleton = builder.build();
        return specialSingleton.create(clazz);
    }

    //基于特定的url生成的 RestAdapter
    public <T> T getRxjavaClient(Class<T> clazz) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(RetrofitConfig.TEST_WAN_ANDROID);//配置服务器路径
        //设置OKHttpClient为网络客户端
        builder.client(OkHttpUtils.INSTANCE.getClient());
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit specialSingleton = builder.build();
        return specialSingleton.create(clazz);
    }

    //基于特定的url生成的 RestAdapter
    public <T> T getRxjavaClientCache(Class<T> clazz) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(RetrofitConfig.TEST_HOST_URL);//配置服务器路径
        //设置OKHttpClient为网络客户端
        builder.client(OkHttpUtilsCache.INSTANCE.getClient());
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit specialSingleton = builder.build();
        return specialSingleton.create(clazz);
    }
    public <T> T getClientCache(Class<T> clazz) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(RetrofitConfig.TEST_HOST_URL);//配置服务器路径
        //设置OKHttpClient为网络客户端
        builder.client(OkHttpUtilsCache.INSTANCE.getClient());
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit specialSingleton = builder.build();
        return specialSingleton.create(clazz);
    }
}

