package com.fish.live.home.bean;

/**
 * @Description TODO
 * @Author
 * @Date 2021/1/15 23:59
 */
public class MsgEvent {
    private String textmsg;
    public MsgEvent(String textmsg) {
        this.textmsg =textmsg;
    }

    public String getTextmsg() {
        return textmsg;
    }

    public void setTextmsg(String textmsg) {
        this.textmsg = textmsg;
    }
}
