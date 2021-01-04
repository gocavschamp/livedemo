package com.nucarf.base.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;


import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by wang on 2017/2/17.
 */

public class AppUtil {
    public static AppUtil instance;

    public static AppUtil getInstance() {
        if (null == instance) {
            instance = new AppUtil();
        }

        return instance;
    }
    //Todo 获取应用程序的版本号
    public int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            Log.e("AndroidUtil", "Cannot find package and its version info.");
            return -1;
        }
    }

    //Todo 获取应用程序的包名
    public String getVersionName(Context pContext) {
        // PackageManager 可以获取清单文件中的所有信息
        PackageManager manager = pContext.getPackageManager();
        try {
            // 获取到一个应用程序的信息
            // getPackageName 获取到当前程序的包名
            PackageInfo packageInfo = manager.getPackageInfo(pContext.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param activity 要判断的Activity
     * @return 是否在前台显示
     */
    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }
    //Todo 判断应用当前是否运行中，包括后台运行
    public boolean isApplicationRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo runningTaskInfo : taskList) {
            if (runningTaskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    //todo 判断应用程序是否安装
    public boolean isInstallByread(Context context, String pPackgeName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pPackgeName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            packageInfo = null;
        }
        if (packageInfo != null) {
            return true;
        } else {
            return false;
        }
    }
    //todo 获得状态栏的高度
    public int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    //todo 获取当前屏幕截图，包含状态栏
    public  Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = ScreenUtil.getScreenWidth(activity);
        int height = ScreenUtil.getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }


    //todo 获取当前屏幕截图，不包含状态栏
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = ScreenUtil.getScreenWidth(activity);
        int height = ScreenUtil.getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    public void setAlpha(Activity pContext,boolean ishow) {
        WindowManager.LayoutParams params = pContext.getWindow().getAttributes();
        if (ishow) {
            params.alpha = 0.7f;
        } else {
            params.alpha = 1f;
        }

        pContext.getWindow().setAttributes(params);
    }
}
