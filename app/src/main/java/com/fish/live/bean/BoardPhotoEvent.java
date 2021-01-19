package com.fish.live.bean;

import com.fish.live.photo.bean.PhotoBean;

import java.util.ArrayList;

/**
 * @Description TODO
 * @Author
 * @Date 2021/1/19 17:01
 */
public class BoardPhotoEvent {
    public ArrayList<PhotoBean> getData() {
        return data;
    }

    public void setData(ArrayList<PhotoBean> data) {
        this.data = data;
    }

    private ArrayList<PhotoBean> data;

    public BoardPhotoEvent(ArrayList<PhotoBean> data) {
        this.data = data;
    }
}
