package com.laka.androidlib.util;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.laka.androidlib.BuildConfig;
import com.laka.androidlib.eventbus.EventBusManager;
import com.laka.androidlib.eventbus.EventBusManagerImp;
import com.laka.androidlib.eventbus.IEventBusManager;
import com.laka.androidlib.util.toast.ICustomToast;
import com.laka.androidlib.util.toast.ToastHelper;
import com.orhanobut.logger.Logger;

/**
 * @Author Lyf
 * @CreateTime 2018/3/6
 * @Description 当前项目共用工具类初始化类
 * 主要初始化Application、LogUtil、ToastHelper和EventBusManager
 * 屏幕兼容类
 **/
public class Utils {

    /**
     * 初始化一系列AndroidLib中的配置
     *
     * @param application
     */
    public static void initSeriesUtil(Application application, boolean isDebug) {
        initApplication(application);
        initLogUtils(isDebug);
        initToastHelper(application, null);
        initEventBusManager(new EventBusManagerImp());
    }

    /**
     * 初始化其它工具之前，必须先调用这个。
     */
    private static void initApplication(Application application) {
        ApplicationUtils.init(application);
    }

    /**
     * 初始化LogUtils
     *
     * @param isDebug 是否是Debug环境
     */
    private static void initLogUtils(boolean isDebug) {
        Logger.init("ErGou").setMethodCount(3);
        ApplicationUtils.initDebug(isDebug);
    }

    /**
     * @param application 上下文
     * @param customToast 自定义的Toast样式
     */
    private static void initToastHelper(Application application, @Nullable ICustomToast customToast) {
        ToastHelper.initToastHelper(application, customToast);
    }

    /**
     * 初始化EventBus
     *
     * @param iEventBusManager
     */
    private static void initEventBusManager(IEventBusManager iEventBusManager) {
        EventBusManager.initEventBusManager(iEventBusManager);
    }

    /**
     * 初始化Fresco
     *
     * @param context
     */
    private static void initFresco(Context context) {
        Fresco.initialize(context);
    }
}
