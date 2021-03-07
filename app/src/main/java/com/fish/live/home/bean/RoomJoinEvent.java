package com.fish.live.home.bean;

/**
 * @Description TODO
 * @Author
 * @Date 2021/1/15 17:30
 */
public class RoomJoinEvent {
    private boolean isLogin;
    public RoomJoinEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
