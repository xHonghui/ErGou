package com.laka.androidlib.util.screen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import com.laka.androidlib.util.ApplicationUtils;

import java.lang.reflect.Method;

/**
 * @ClassName: ScreenUtils
 * @Description: 屏幕工具
 * @Author: chuan
 * @Date: 09/01/2018
 */

public final class ScreenUtils {
    private static int mScreenWidth = 0;
    private static int mScreenHeight = 0;
    private static int mStatusBarHeight = 0;

    private ScreenUtils() {
        throw new UnsupportedOperationException("do not instantiate me , please.");
    }

    /**
     * 得到设备屏幕的宽度
     *
     * @return 屏幕宽度 pixels
     */
    public static int getScreenWidth() {
        if (mScreenWidth <= 0) {
            mScreenWidth = ApplicationUtils.getApplication().getResources().getDisplayMetrics().widthPixels;
        }
        return mScreenWidth;
    }

    /**
     * 得到设备屏幕的高度
     *
     * @return 屏幕高度 pixels
     */
    public static int getScreenHeight() {
        if (mScreenHeight <= 0) {
            mScreenHeight = ApplicationUtils.getApplication().getResources().getDisplayMetrics().heightPixels;
        }
        return mScreenHeight;
    }

    /**
     * 获取StatusBar高度
     *
     * @return StatusBar高度 pixels
     */
    public static int getStatusBarHeight() {
        if (mStatusBarHeight <= 0) {
            int resourceId = ApplicationUtils.getApplication().getResources().getIdentifier(
                    "status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                mStatusBarHeight = ApplicationUtils.getApplication().getResources().getDimensionPixelSize(resourceId);
            }
        }
        return mStatusBarHeight;
    }

    /**
     * 获取 虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getSoftInputHeight(Context context) {
        int totalHeight = getDpi(context);
        int contentHeight = getScreenHeight();
        return totalHeight - contentHeight;
    }

    public static int getNavigationBarHeight() {

        int result = 0;
        if (hasNavBar(ApplicationUtils.getApplication())) {
            Resources res = ApplicationUtils.getApplication().getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
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
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
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

    /**
     * 获取屏幕高度。不包括状态栏的高度
     *
     * @return 屏幕高度 pixels
     */
    public static int getScreenHeightWithoutStatusBar() {
        if (mScreenHeight <= 0) {
            getScreenHeight();
        }

        if (mStatusBarHeight <= 0) {
            getStatusBarHeight();
        }

        return mScreenHeight - mStatusBarHeight;
    }

    /**
     * dp值转化为px值
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(float dpValue) {
        final float scale = ApplicationUtils.getApplication().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px值转化为dp值
     *
     * @param pxValue px值
     * @return dp值
     */
    public static float px2dp(float pxValue) {
        final float scale = ApplicationUtils.getApplication().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px值转化为sp值
     *
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(float pxValue) {
        final float fontScale = ApplicationUtils.getApplication().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp值转化为px值
     *
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(float spValue) {
        final float fontScale = ApplicationUtils.getApplication().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static float getDensity(Activity context) {
        //获取屏幕密度
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.density;
    }
}
