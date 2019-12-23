package com.lqr.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * @Author:summer
 * @Date:2019/2/21
 * @Description:
 */
public class OsUtils {

    public static final String SHARE_PREFERENCE_NAME = "EmotionKeyBoard";
    public static final String SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "sofe_input_height";
    public static final String SHARE_PREFERENCE_VIRTUAL_KEY_HEIGHT = "share_preference_deviation_height";
    public static final String MIUI_VIRTUAL_KEY_EXIT_KEY = "force_fsg_nav_bar";
    private static final String MIUI_OS_KEY = "ro.miui.ui.version.name";
    private static final String NAVIGATION_BAR_HEIGHT = "navigation_bar_height";
    private static final String CONFIG_SHOW_NAVIGATION_BAR = "config_showNavigationBar";
    private static final java.lang.String ANDROID_SYSTEM_PROPERTIES = "android.os.SystemProperties";
    private static final Object QEMU_HW_MAINKEYS = "qemu.hw.mainkeys";

    public static boolean isMIUI() {
        return !TextUtils.isEmpty(getSystemProperty(MIUI_OS_KEY));
    }

    /**
     * 判断当前手机系统
     *
     * @param propName
     * @return
     */
    private static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            java.lang.Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }

    /**
     * 一般机型获取虚拟按键高度的方法
     * */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier(NAVIGATION_BAR_HEIGHT, "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 获取底部虚拟按键栏的高度
     * 如果软键盘弹起的情况下调用，获取的高度将是不准确的
     * @return虚拟按键的高度
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getSoftButtonsBarHeight(Activity activity) {
        int resultHeight = 0;
        DisplayMetrics metrics = new DisplayMetrics();
        //获取屏幕可用空间的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实物理高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            resultHeight = realHeight - usableHeight;
        }
        int navigationBarHeight = getNavigationBarHeight(activity);
        if (navigationBarHeight <= 0) {
            return resultHeight;
        }
        //两者都不为0，返回小的一个
        return resultHeight < navigationBarHeight ? resultHeight : navigationBarHeight;
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
        int resourceId = res.getIdentifier(CONFIG_SHOW_NAVIGATION_BAR, "bool", "android");
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
                Class c = Class.forName(ANDROID_SYSTEM_PROPERTIES);
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, QEMU_HW_MAINKEYS);
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

    /**
     * 适配小米手机的方法，判断小米手机是否开启虚拟按键
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isOpenMiUiVirtualBar(Activity activity) {
        if (Settings.Global.getInt(activity.getContentResolver(), OsUtils.MIUI_VIRTUAL_KEY_EXIT_KEY, 0) != 0) {
            return false;//开启手势
        } else {
            return true;//开启虚拟按键
        }
    }

    /**
     * 通过：屏幕高度 - rootView.bottom = 虚拟按键高度 + keyboard height  获取并保存
     * 键盘弹起，则获取到的高度是：虚拟按键height（如果有） + keyboard height
     * 键盘未弹起，则获取的高度是：虚拟按键height（如果有）
     */
    public static int getScreenBottomToRootViewBottomDistance(Activity activity) {
        Rect r = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
        int softInputHeight = screenHeight - r.bottom;
        return softInputHeight;
    }

    /**
     * 注意：在键盘未弹起状态下，获取虚拟按键（如果有）
     * 在该项目中，首页是没有键盘弹起的，所以在HomeActivity 的onResume 中获取虚拟按键的高度
     */
    public static int getAndSaveVirtualKeyHeight(Activity activity) {
        SharedPreferences sp = activity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        int softInputHeight = getScreenBottomToRootViewBottomDistance(activity);
        Log.i(SHARE_PREFERENCE_NAME, "首页获取虚拟按键高度1：" + softInputHeight);
        if (softInputHeight > 0) {//虚拟按键高度
            Log.i(SHARE_PREFERENCE_NAME, "首页获取虚拟按键高度2：" + softInputHeight);
            sp.edit().putInt(SHARE_PREFERENCE_VIRTUAL_KEY_HEIGHT, softInputHeight).commit();
        }
        return softInputHeight;
    }


    /**
     * 获取键盘高度（键盘弹出后才能够准确获取）
     */
    public static int getKeyboardHeight(Activity activity) {
        SharedPreferences sp = activity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        int softInputHeight = getScreenBottomToRootViewBottomDistance(activity);
        /**
         * 某些机型，没有显示软键盘时减出来的高度也不为零，这是因为高度是包括了虚拟按键栏的(例如华为、小米系列)，
         * 所以判断当前机型是否存在虚拟按键，我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (OsUtils.hasNavBar(activity)) {
            // 开启虚拟按键
            if (OsUtils.isMIUI()) {//小米适配，针对通过正常方法获取虚拟键盘不正确的机型做的适配，在键盘未显示时，通过: 屏幕高度 - 屏幕可用高度 = 虚拟按键高度  来获取
                if (isOpenMiUiVirtualBar(activity)) {
                    //开启虚拟按键
                    int virtualKeyHeight = sp.getInt(SHARE_PREFERENCE_VIRTUAL_KEY_HEIGHT, 0);
                    if (virtualKeyHeight > 0) { //本地里已存在想虚拟按键高度（准确）
                        softInputHeight -= virtualKeyHeight;
                    } else {//本地未存在虚拟按键高度（某些机型，获取的不准确，所以尽量在聊天页面前，获取到虚拟按键高度并保存）
                        softInputHeight -= getSoftButtonsBarHeight(activity);
                    }
                }
            } else {
                //不需要适配的机型，直接通过 getSoftButtonsBarHeight 即可获得准确的虚拟按键高度
                softInputHeight -= OsUtils.getSoftButtonsBarHeight(activity);
            }
        }
        //保存键盘高度到本地
        if (softInputHeight > 0) {
            Log.i(SHARE_PREFERENCE_NAME, "登录页保存键盘高度：" + softInputHeight);
            sp.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).commit();
            return softInputHeight;
        }
        return 0;//获取不到键盘高度，直接返回 0
    }

}
