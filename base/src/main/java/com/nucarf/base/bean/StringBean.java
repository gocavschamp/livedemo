package com.nucarf.base.bean;

import java.io.Serializable;

public class StringBean implements Serializable {
    private String pay_code;
    private String company_name;
    private String cart_no;
    private String code;
    private boolean isChoice = false;
    private boolean is_alert;
    private String name;
    private String headimg;
    private String id;
    private String moi_id;//订单ID
    private String status;//路线收藏状态 0未收藏 1已经收藏
    private String type;
    private String token;
    private String mp3;//语音播报url
    private String lbsToken;//lbs位置监控token
    private int recharge_wait_num;//司机待领取金额条数

    public String getMoi_id() {
        return moi_id;
    }

    public void setMoi_id(String moi_id) {
        this.moi_id = moi_id;
    }

    public int getRecharge_wait_num() {
        return recharge_wait_num;
    }

    public void setRecharge_wait_num(int recharge_wait_num) {
        this.recharge_wait_num = recharge_wait_num;
    }

    public boolean isIs_alert() {
        return is_alert;
    }

    public void setIs_alert(boolean is_alert) {
        this.is_alert = is_alert;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLbsToken() {
        return lbsToken;
    }

    public void setLbsToken(String lbsToken) {
        this.lbsToken = lbsToken;
    }

    public String getCart_no() {
        return cart_no;
    }

    public void setCart_no(String cart_no) {
        this.cart_no = cart_no;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoice() {
        return isChoice;
    }

    public void setChoice(boolean choice) {
        isChoice = choice;
    }


    public String getPay_code() {
        return pay_code;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }
}
