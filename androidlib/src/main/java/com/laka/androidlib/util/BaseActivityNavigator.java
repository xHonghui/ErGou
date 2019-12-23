package com.laka.androidlib.util;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * @Author:Rayman
 * @Date:2018/12/19
 * @Description:基础路由类，封装常规的跳转方式
 */

public class BaseActivityNavigator {

    /**
     * 启动Activity，指定类
     *
     * @param context
     * @param cls
     */
    public static void startActivity(@NonNull Context context, Class<?> cls) {
        startActivity(context, cls, null);
    }

    /**
     * 启动Activity，指定类，指定Bundle
     *
     * @param context
     * @param cls
     * @param bundle
     */
    public static void startActivity(@NonNull Context context, Class<?> cls, @Nullable Bundle bundle) {

        Intent intent = new Intent(context, cls);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        // 假若Application、Service类型的context，需要加上new_task
        if (context instanceof Application || context instanceof Service) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 清空当前任务栈并打开activity
     */
    public static void startActivityClearTask(@NonNull Context context, Class<?> cls) {
        startActivityClearTask(context, cls, null);
    }

    public static void startActivityClearTask(@NonNull Context context, Class<?> cls, @Nullable Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 启动Activity，使用FLAG=NEW_TASK方式跳转
     *
     * @param context
     * @param cls
     * @param bundle
     */
    public static void startActivityNewTask(@NonNull Context context, Class<?> cls, @Nullable Bundle bundle) {
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 启动Activity，启动后finish当前页面
     *
     * @param activity
     * @param cls
     * @param bundle
     */
    public static void startActivityFinish(@NonNull Activity activity, Class<?> cls, @Nullable Bundle bundle) {
        startActivity(activity, cls, bundle);
        activity.finish();
    }


    /**
     * startActivityForResult
     */
    public static void startActivityForResult(@NonNull Activity context, Class<?> cls, @Nullable Bundle bundle, @Nullable int requestCode) {
        Intent intent = new Intent(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 为了兼容fragment
     */
    public static void startActivityForResultOnFragment(@NonNull Activity context, Fragment fragment, Class<?> cls, @Nullable Bundle bundle, @Nullable int requestCode) {
        Intent intent = new Intent(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(@Nullable Activity context, Class<?> cls, @Nullable int requestCode) {
        startActivityForResult(context, cls, null, requestCode);
    }

}
