package com.nucarf.base.utils;

import android.content.Context;

/**
 * Created by WANG on 2016/4/27.
 */
public class SharePreUtils {
    public static SharePreUtils instance;

    public static synchronized SharePreUtils getInstance() {
        if (null == instance) {
            instance = new SharePreUtils();
        }
        return instance;
    }

    // 请求时url 拼接添加验证
    public static String getSign(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getString(SharedKeys.SIGN, "");

    }

    public static void setSign(Context context, String sign) {
        SharedPreferencesUtil.setValue(context, SharedKeys.SIGN, sign);

    }

    // 是否是第一次进入app
    public static boolean getIsNewUser(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getBoolean(SharedKeys.IsNewUser, true);
    }

    public static void setIsNewUser(Context context, boolean isfIRST) {
        SharedPreferencesUtil.setValue(context, SharedKeys.IsNewUser, isfIRST);

    }

//    public static boolean getisFirstInstall(Context context) {
//        return SharedPreferencesUtil.getSharedPreferences(context).getBoolean(SharedKeys.ISFIRST, true);
//    }
//
//    public static void setIsFirstInstall(Context context, boolean isfIRST) {
//        SharedPreferencesUtil.setValue(context, SharedKeys.ISFIRST, isfIRST);
//
//    }

    //是否登录 true ,false
    public static boolean getIsLogin(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getBoolean(SharedKeys.IS_LOGIN, false);
    }

    public static void setLoginStatus(Context context, boolean isLogin) {
        SharedPreferencesUtil.setValue(context, SharedKeys.IS_LOGIN, isLogin);

    }


    public static String getjwt_token(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getString(SharedKeys.JWT_TOKEN, "");
    }

    public static void setjwt_token(Context context, String jwt_token) {
        SharedPreferencesUtil.setValue(context, SharedKeys.JWT_TOKEN, jwt_token);

    }

    public static void setName(Context context, String name) {
        SharedPreferencesUtil.setValue(context, SharedKeys.NAME, name);
    }

    public static String getName(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getString(SharedKeys.NAME, "");
    }

    public static void setUserName(Context context, String username) {
        SharedPreferencesUtil.setValue(context, SharedKeys.USER_NAME, username);
    }

    public static String getUserName(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getString(SharedKeys.USER_NAME, "");
    }

    public static void setUserCode(Context context, String usercode) {
        SharedPreferencesUtil.setValue(context, SharedKeys.USER_CODE, usercode);
    }

    public static String getUserCode(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getString(SharedKeys.USER_CODE, "");
    }

    public static void setMemberId(Context context, String memberId) {
        SharedPreferencesUtil.setValue(context, SharedKeys.MEMBER_ID, memberId);
    }

    public static String getMemberId(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getString(SharedKeys.MEMBER_ID, "");
    }

    public static String getBalance(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getString(SharedKeys.BALANCE, "");
    }

    public static void setBalance(Context context, String balance) {
        SharedPreferencesUtil.setValue(context, SharedKeys.BALANCE, balance);

    }

    public static void setRegion(Context context, String region) {
        SharedPreferencesUtil.setValue(context, SharedKeys.REGION, region);

    }

    public static String getRegion(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getString(SharedKeys.REGION, "");
    }

    public static void setCar_no(Context context, String car_no) {
        SharedPreferencesUtil.setValue(context, SharedKeys.CAR_NO, car_no);

    }

    public static String getCar_no(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getString(SharedKeys.CAR_NO, "");
    }

    public static void setLatLong(Context context, String latlong) {
        SharedPreferencesUtil.setValue(context, SharedKeys.LAT_LONG, latlong);
    }

    public static String getLatLong(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getString(SharedKeys.LAT_LONG, "");
    }

    public static void setProvinceCity(Context context, String provinceCity) {
        SharedPreferencesUtil.setValue(context, SharedKeys.PROVINCE_CITY, provinceCity);
    }

    public static void setProvinceAddress(Context context, String address) {
        SharedPreferencesUtil.setValue(context, SharedKeys.ADDRESS, address);
    }

    public static String getProvinceAddress(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getString(SharedKeys.ADDRESS, "");
    }

    public static String getProvinceCity(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getString(SharedKeys.PROVINCE_CITY, "");
    }


    public static void setNoPass(Context context, String not_pass) {
        SharedPreferencesUtil.setValue(context, SharedKeys.NOT_PASS, not_pass);

    }

    public static String getNoPass(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getString(SharedKeys.NOT_PASS, "");

    }

    public static void setSetPayPass(Context context, String pay_pass) {
        SharedPreferencesUtil.setValue(context, SharedKeys.PAY_PASS, pay_pass);
    }

    public static String getSetPayPass(Context context) {
        return SharedPreferencesUtil.getSharedPreferences(context).getString(SharedKeys.PAY_PASS, "");
    }

    public static void setDownLoadApkPath(String path) {
        SharedPreferencesUtil.setValue(BaseAppCache.getContext(), SharedKeys.SP_DOWNLOAD_PATH, path);
    }

    public static String getDownLoadApkPath() {
        return SharedPreferencesUtil.getSharedPreferences(BaseAppCache.getContext()).getString(SharedKeys.SP_DOWNLOAD_PATH, "");

    }

    public static void setIsSetAlias(boolean isSetAlias) {
        SharedPreferencesUtil.setValue(BaseAppCache.getContext(), SharedKeys.ISSETALIAS, isSetAlias);
    }
    public static boolean getIsSetAlias( ) {
       return SharedPreferencesUtil.getBooleanValue(BaseAppCache.getContext(), SharedKeys.ISSETALIAS,false);
    }
}
