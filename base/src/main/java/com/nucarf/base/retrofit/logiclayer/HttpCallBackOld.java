package com.nucarf.base.retrofit.logiclayer;

import com.nucarf.base.retrofit.RetrofitConfig;
import com.nucarf.base.utils.LogUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Creator: kakaluote.
 * Email  : kakaluote.com
 * 外观模式
 */
public abstract class HttpCallBackOld<T extends BaseResultOld> implements Callback<T> {


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        try {
            if (response.body().isSuccessed()) {
                try {
                    succeeded(response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
//                failed(response.body().getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            failed(RetrofitConfig.ERROR_PARSE);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
//        if (t.getMessage().contains("Failed to connect")) {
        failed(RetrofitConfig.ERROR_NETWORK);
//        }
        LogUtils.e("错误日志", t.getMessage() + "错误");
    }


    //下面只是业务逻辑的成功和失败
    public abstract void succeeded(T t);

    public abstract void failed(String errorcode);


}
