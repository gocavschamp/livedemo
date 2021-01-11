package com.nucarf.base.ui.mvp;

import io.reactivex.disposables.Disposable;

/**
 * Created by yuwenmingon 2019/1/23
 * BaseView
 *
 * @author yuwenming
 */
public interface BaseView {
    //解绑rxjava
    void addSubscribe(Disposable subscription);

    /**
     * 显示加载框
     */
    void showDialog();


    /**
     * 关闭加载框
     */
    void dismissDialog();


    /**
     * 请求成功所做处理
     */
    void onSucess();

    /**
     * 吐司
     */
    void showToast(String toa);


    /**
     * 请求失败所做处理
     */
    void onFail();


    /**
     * 网络异常
     */
    void onNetError();

    /**
     * 重新加载
     */
    void onReLoad();
}
