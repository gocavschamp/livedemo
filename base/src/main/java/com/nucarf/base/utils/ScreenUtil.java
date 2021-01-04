package com.nucarf.base.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;

import com.google.android.material.tabs.TabLayout;

import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by Administrator on 2018/4/25.
 */

public class ScreenUtil {
    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context activity) {
        WindowManager w = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        return widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight(Context activity) {
        WindowManager manager = ((Activity) activity).getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.heightPixels;
        return width;
    }

    public static int dip2px(float dpValue) {
        float res = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, BaseAppCache.getContext().getResources().getDisplayMetrics());
        return (int) res;
    }

    public static int sp2px(float spValue) {
        float res = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, BaseAppCache.getContext().getResources().getDisplayMetrics());
        return (int) res;
    }

    //(x,y)是否在view的区域内
    public static boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        return y >= top && y <= bottom && x >= left
                && x <= right;
    }

    public static int getRealWidth(Context context, int uiSize) {
        int resSize = 0;
        int screenWidth = getScreenWidth(context);
        float precent = (float) uiSize / (float) getUIWidth(context);
        resSize = (int) (precent * screenWidth);
        return resSize;
    }

    public static int getRealHeight(Context context, int uiSize) {
        int resSize = 0;
        int screenHeight = getScreenHeight(context);
        float precent = (float) uiSize / (float) getUIHeight(context);
        resSize = (int) (precent * screenHeight);
        return resSize;
    }

    /**
     * 获取设计图宽尺寸
     *
     * @param context
     * @return
     */
    private static int getUIWidth(Context context) {
        int resWidth = 0;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            resWidth = appInfo.metaData.getInt("design_width");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resWidth;
    }

    /**
     * 获取设计图高尺寸
     *
     * @param context
     * @return
     */
    private static int getUIHeight(Context context) {
        int resWidth = 0;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            resWidth = appInfo.metaData.getInt("design_height");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resWidth;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public static Point getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the right
        if (appUsableSize.x < realScreenSize.x) {
            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
        }

        // navigation bar is not present
        return new Point();
    }

    public static boolean isNavigationBarShow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            boolean result = realSize.y != size.y;
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * Desc: 获取虚拟按键高度 放到工具类里面直接调用即可
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        LogUtils.e("虚拟键盘高度" + result);
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId); // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else {
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            }
        }
        return size;
    }

    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    /**
     * 设置RelativeLayout中的view宽高
     *
     * @param view   :视图
     * @param width  :宽
     * @param height :高
     */
    public static void setRelativeLayoutParams(View view, int width, int height) {
        if (width == 0 || height == 0) return;

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
        lp.width = width;
        lp.height = height;
        view.setLayoutParams(lp);
    }

    public static void setRelativeLayoutParamsMATCH_PARENT(View view) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();

        lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        lp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(lp);
    }


    /**
     * 设置FrameLayout中的view宽高
     *
     * @param view   :视图
     * @param width  :宽
     * @param height :高
     */
    public void setFrameLayoutParams(View view, int width, int height) {
        if (width == 0 || height == 0) return;

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
        lp.width = width;
        lp.height = height;
        view.setLayoutParams(lp);
    }

    /**
     * 设置ViewGroup中的view宽高
     *
     * @param view   :视图
     * @param width  :宽
     * @param height :高
     */
    public static void setViewGroupParams(View view, int width, int height) {
        if (width == 0 || height == 0) return;

        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        lp.height = height;
        view.setLayoutParams(lp);
    }

    public static void setRecyleviewParams(View view, int width, int height) {
        if (width == 0 || height == 0) return;

        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        lp.width = width;
        lp.height = height;
        view.setLayoutParams(lp);
    }

    /**
     * 设置LinearLayout中的view宽高
     *
     * @param view   :视图
     * @param width  :宽
     * @param height :高
     */
    public static void setLinearLayoutParams(View view, int width, int height) {
        if (width == 0 || height == 0) return;

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        lp.width = width;
        lp.height = height;
        view.setLayoutParams(lp);
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /*
     * 禁止硬件加速
     * */
    public static void disableHardwareAccelerated(View view) {
        if (view == null) {
            return;
        }
        //是否开启了硬件加速,如开启将其禁掉
        if (!view.isHardwareAccelerated()) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }


    /**
     * 得到单个字的高度
     *
     * @param paint 画笔
     * @return 高度
     */
    public static float getPaintFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * 得到一段文本的宽度
     *
     * @param paint 画笔
     * @param str   文本
     * @return 文本宽度
     */
    public static float getTextWidth(Paint paint, String str) {
        if (str.length() == 0) return 0.0f;
        return paint.measureText(str, 0, str.length());
    }
}
