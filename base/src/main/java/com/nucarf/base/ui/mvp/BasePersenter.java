package com.nucarf.base.ui.mvp;

/**
 * Created by yuwenmingon 2019/1/23
 * BasePersenter
 *
 * @author Administrator
 */
public interface BasePersenter<T extends BaseView> {

    /**
     * attachView 绑定view
     *
     * @param view
     */
    void attachView(T view);

    /**
     * detachView 解除绑定
     */
    void detachView();
}
