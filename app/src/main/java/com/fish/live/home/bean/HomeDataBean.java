package com.fish.live.home.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Author
 * @Date 2021/1/11 16:26
 */
public class HomeDataBean implements Serializable, MultiItemEntity {

    public static final int ONE = 1;//banner
    public static final int TWO = 2;//近期直播
    private int itemType;
    private String title;
    private String url;
    private String time;
    private String status;
    private String icon;
    private String id;
    private String roomid;
    private List<String> banners;
    private int total_count;

    public List<String> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<String> banners) {
        this.banners = banners;
    }

    public HomeDataBean(int itemType) {
        this.itemType = itemType;
    }

    public HomeDataBean() {
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }
}
