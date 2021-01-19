package com.fish.live.photo.bean;

import java.util.List;

/**
 * Created by WANG on 2016/2/24.
 */
public class PhotoEvent {
    private String photoPath = null;
    private String mClassName = null;
    //canceled选择照片取消 ,ok 选择ok
    private String mActionName = null;
    private List<String> mSelectPhotoPath = null;

    public PhotoEvent(String photoPath, String pClassName, String pActionName) {
        this.photoPath = photoPath;
        this.mClassName = pClassName;
        this.mActionName = pActionName;
    }

    public PhotoEvent(String photoPath, String mClassName) {
        this.photoPath = photoPath;
        this.mClassName = mClassName;
    }

    public PhotoEvent(List<String> mSelectPhotoPath, String mClassName) {
        this.mClassName = mClassName;
        this.mSelectPhotoPath = mSelectPhotoPath;
    }

    public List<String> getmSelectPhotoPath() {
        return mSelectPhotoPath;
    }

    public void setmSelectPhotoPath(List<String> mSelectPhotoPath) {
        this.mSelectPhotoPath = mSelectPhotoPath;
    }

    public String getmClassName() {
        if(null==mClassName){
            mClassName = "";
        }
        return mClassName;
    }

    public void setmClassName(String mClassName) {
        this.mClassName = mClassName;
    }

    public PhotoEvent(String pphotoPath) {
        this.photoPath = pphotoPath;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getmActionName() {
        return mActionName;
    }

    public void setmActionName(String mActionName) {
        this.mActionName = mActionName;
    }
}
