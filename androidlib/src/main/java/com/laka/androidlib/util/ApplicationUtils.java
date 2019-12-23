package com.laka.androidlib.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.laka.androidlib.BuildConfig;
import com.laka.androidlib.eventbus.Event;
import com.laka.androidlib.eventbus.EventBusManager;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName: ApplicationUtils
 * @Description: 用于初始化application Context
 * @Author: chuan
 * @Date: 27/02/2018
 */

public final class ApplicationUtils {
    @SuppressLint("StaticFieldLeak")
    private static Application sApplication;

    private static boolean sDebug = BuildConfig.DEBUG;
    public static List<Activity> activities = new LinkedList<>();

    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    private static Context mContext;//上下文
    private static long mMainThreadId;//主线程id
    private static Looper mMainLooper;//循环队列
    private static Handler mHandler;//主线程Handler

    //业务层数据
    private static boolean isShowUpdateDialog = false; //记录当前是否显示更新弹窗，需要配合淘宝客使用


    private ApplicationUtils() {
        throw new UnsupportedOperationException("do not instantiate me , please.");
    }

    /**
     * 设置Application
     *
     * @param application Application
     */
    public static void init(@NonNull Application application) {
        sApplication = application;
        mContext = sApplication;
        //对全局属性赋值
        mMainThreadId = android.os.Process.myTid();
        mHandler = new Handler();
    }

    /**
     * 获取Application
     *
     * @return Application实例
     * @throws NullPointerException 当Application为空时
     */
    public static Application getApplication() {
        if (sApplication == null) {
            throw new NullPointerException("application is null ,call init() please");
        }

        return sApplication;
    }

    /**
     * 初始化debug状态
     *
     * @param debug debug状态
     */
    public static void initDebug(boolean debug) {
        sDebug = debug;
    }

    /**
     * 判断是否是debug状态
     *
     * @return true ，debug状态；false ，非debug状态
     */
    public static boolean isDebug() {
        return sDebug;
    }

    /**
     * 完全退出
     * 一般用于“退出程序”功能
     */
    public static void exit() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }

    /**
     * 重启当前应用
     */
    public static void restart() {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    /**
     * 判断当前activity是否还在活动状态
     */
    public static boolean isVaildActivity(Activity activity) {
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return false;
        }
        return true;
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static void setMainThreadId(long mainThreadId) {
        mMainThreadId = mainThreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    public static void setMainThreadLooper(Looper looper) {
        mMainLooper = looper;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static void setMainHandler(Handler handler) {
        mHandler = handler;
    }

    public static boolean isIsShowUpdateDialog() {
        return isShowUpdateDialog;
    }

    public static void setIsShowUpdateDialog(boolean isShowUpdateDialog) {
        ApplicationUtils.isShowUpdateDialog = isShowUpdateDialog;
    }
}
