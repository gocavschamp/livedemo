package com.nucarf.base.retrofit.logiclayer;


import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.nucarf.base.retrofit.RetrofitConfig;
import com.nucarf.base.utils.LogUtils;
import com.nucarf.base.utils.ToastUtils;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;


/**
 * Creator: kakaluote.
 * Email  : kakaluote.com
 * 外观模式
 */
public abstract class HttpCallBack<T extends BaseResult> implements Callback<T> {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
//        LogUtils.e("onResponse", response.body().toString()+"");

        try {
            if (response.body() != null && response.body().isSuccessed()) {
                try {
                    succeeded(response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                failed(response.body().getCode());
                if (response.body().getCode().equals(RetrofitConfig.STATUS_GOTOLOGIN)) {
//                    SharePreUtils.setLoginStatus(StarshowApplication.getContext(),false);
//                    SharePreUtils.setjwt_token(StarshowApplication.getContext(),"");
//                    EventBus.getDefault().post(new LoginTypeEvent("islogin"));
//                    LoginActivity.launch2(StarshowApplication.getContext(),"code401");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            failed(RetrofitConfig.ERROR_PARSE);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable e) {
        if (e instanceof HttpException) {
            if (((HttpException) e).code() == UNAUTHORIZED || ((HttpException) e).code() == FORBIDDEN) {
                ToastUtils.showShort("您还没有登录，请先登录!");
            } else if (((HttpException) e).code() == NOT_FOUND) {
                ToastUtils.showShort("找不到指定的链接");
            } else {
                ToastUtils.showShort("网络错误");
            }
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            ToastUtils.showShort("解析错误");
//            SharePreUtils.setjwt_token(BaseAppCache.getContext(), "");
//            EventBus.getDefault().post(new LoginEvent());
        } else if (e instanceof SocketTimeoutException) {
            ToastUtils.showShort("请求超时，请检查您的网络状态");
        } else if (e instanceof ConnectException) {
            ToastUtils.showShort("连接服务器失败，请检查您的网络状态");
        } else if (e instanceof SocketException) {
            ToastUtils.showShort("网络中断，请检查您的网络状态");
        } else if (e instanceof UnknownHostException) {
            ToastUtils.showShort("连接服务器失败,请稍候重试");
        } else {
//            ToastUtils.showShort(e.getMessage());
            ToastUtils.showShort("服务器异常");
        }

        failed(RetrofitConfig.ERROR_NETWORK);
        LogUtils.e("错误日志", e.getMessage() + "错误");

    }


    //下面只是业务逻辑的成功和失败
    public abstract void succeeded(T t);

    public abstract void failed(String errorcode);


}
