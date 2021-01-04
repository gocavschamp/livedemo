package com.nucarf.base.mvp;

import android.content.Context;

/**
 * Created by hzy on 2019/1/23
 * BaseView
 *
 * @author hzy
 *
 * */
public interface BaseView {

    /**
     * 显示加载框
     */
    void showLoading();


    /**
     * 关闭加载框
     */
    void closeLoading();


    /**
     * 请求成功所做处理
     */
    void onSucess();


    /**
     * 请求失败所做处理
     */
    void onFail();


    /**
     * 网络异常
     */
    void onNetError(int errorCode,String errorMsg);

    /**
     * 重新加载
     */
    void onReLoad();
}
