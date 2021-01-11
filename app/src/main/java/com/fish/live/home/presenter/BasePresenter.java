package com.fish.live.home.presenter;
/**
 * @author lixiaoxue
 * @date On 2019/8/8
 */
public class BasePresenter<T> {
    protected T view;
    public void detach(){
        view  = null;
    }

}
