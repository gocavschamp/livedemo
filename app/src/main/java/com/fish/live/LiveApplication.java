package com.fish.live;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.StrictMode;

import androidx.multidex.MultiDex;

import com.fish.live.database.MySQLiteOpenHelper;
import com.fish.live.database.db.DaoMaster;
import com.fish.live.database.db.DaoSession;
import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.liys.doubleclicklibrary.ViewDoubleHelper;
import com.nucarf.base.utils.ActivityHelper;
import com.nucarf.base.utils.BaseAppCache;
import com.nucarf.base.utils.LogUtils;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.smtt.sdk.QbSdk;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import me.jessyan.autosize.AutoSizeConfig;

public class LiveApplication extends Application {
    private static LiveApplication application;

    public static Context getContext() {
        return application;
    }

    public static LiveApplication get(Context context) {
        return (LiveApplication) context.getApplicationContext();
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BaseAppCache.setContext(this);
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        //去除9.0 弹框
        disableAPIDialog();
        // 屏幕适配
        AutoSizeConfig.getInstance().setExcludeFontScale(true);
        registerActivityLifecycleCallbacks(new ActivityHelper());
        //init tengxunlive
        initTengxun();
        //greendao 数据库
        initGreenDao();
        //初始化x5webview
        initX5WebView();

        // android 7.0系统解决拍照的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
        //防止点击抖动
        ViewDoubleHelper.init(this, 1000); //默认时间：1秒
    }

    private void initTengxun() {
        String licenceURL = "1fb9da81056d7adf3036673562de2768"; // 获取到的 licence url
        String licenceKey = "http://license.vod2.myqcloud.com/license/v1/e80f3d197ebcc7e519d0aa7fcf18d275/TXLiveSDK.licence"; // 获取到的 licence key
        TXLiveBase.getInstance().setLicence(this, licenceURL, licenceKey);
        TXLiveBase.setConsoleEnabled(true);

    }

    private void initXunFei() {
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        StringBuffer param = new StringBuffer();
        param.append("appid=5d5a1b6d");
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(this, param.toString());
    }



    /**
     * 反射 禁止弹窗
     */
    private void disableAPIDialog() {
        if (Build.VERSION.SDK_INT < 28) return;
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化GreenDao,直接在Application中进行初始化操作
     */
    private void initGreenDao() {
        // 初始化
        MigrationHelper.DEBUG = false; //如果你想查看日志信息，请将 DEBUG 设置为 true
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, "fish.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    private DaoSession daoSession;

    public DaoSession getDaoSession() {
        return daoSession;
    }

    /*使用腾讯x5 webview，解决安卓原生wenview不适配不同机型问题*/
    private void initX5WebView() {

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtils.d("onViewInitFinished", " onViewInitFinished is " + arg0);
                if(arg0){
                    LogUtils.d("onViewInitFinished", "腾讯X5内核加载成功");
                }else {
                    LogUtils.d("onViewInitFinished", "腾讯X5内核加载失败，使用原生安卓webview");
                }
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }
}
