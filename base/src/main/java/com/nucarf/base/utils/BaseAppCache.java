package com.nucarf.base.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2017/menu_code_no_pressed/9.
 */

public class BaseAppCache {

    private static Context context;
    private static Application application;
    private static String channel_name;
    private static int version_code;

    public static String getVersion_code() {
        PackageManager manager = context.getPackageManager();
        try {
            // 获取到一个应用程序的信息
            // getPackageName 获取到当前程序的包名
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
//        return version_code;
    }

    public static void setVersion_code(int version_code) {
        BaseAppCache.version_code = version_code;
    }

    public static String getChannel_name() {
        return channel_name;
    }

    public static void setChannel_name(String channel_name) {
        BaseAppCache.channel_name = channel_name;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        BaseAppCache.context = context;
    }

    public static void setApplication(Application application) {
        BaseAppCache.application = application;
    }

    public static Application getApplication() {
        return application;
    }
}
