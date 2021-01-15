package com.fish.live.home.bean;

/**
 * @Description TODO
 * @Author
 * @Date 2021/1/15 17:30
 */
public class IMLoginEvent {
    private boolean isLogin;
    public IMLoginEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
