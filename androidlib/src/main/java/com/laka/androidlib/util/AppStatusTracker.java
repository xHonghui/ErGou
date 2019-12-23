package com.laka.androidlib.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * @Author:Rayman
 * @Date:2019/3/20
 * @Description:监听当前App的一些状态，例如前后台的切换
 */

public class AppStatusTracker implements Application.ActivityLifecycleCallbacks {

    private static int refCount = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        refCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        refCount--;
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public static boolean isAppForeground() {
        return refCount > 0;
    }
}
