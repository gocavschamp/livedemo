package com.fish.live.photo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class PhotoBean implements Parcelable {
    private String path;
    private boolean isLoad = false;
    private boolean isChoice = false;

    protected PhotoBean(Parcel in) {
        path = in.readString();
        isLoad = in.readByte() != 0;
        isChoice = in.readByte() != 0;
    }

    public PhotoBean() {
    }

    public static final Creator<PhotoBean> CREATOR = new Creator<PhotoBean>() {
        @Override
        public PhotoBean createFromParcel(Parcel in) {
            return new PhotoBean(in);
        }

        @Override
        public PhotoBean[] newArray(int size) {
            return new PhotoBean[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public boolean isChoice() {
        return isChoice;
    }

    public void setChoice(boolean choice) {
        isChoice = choice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeByte((byte) (isLoad ? 1 : 0));
        dest.writeByte((byte) (isChoice ? 1 : 0));
    }
}
