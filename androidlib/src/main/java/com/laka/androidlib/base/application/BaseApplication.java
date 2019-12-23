package com.laka.androidlib.base.application;

import android.app.Application;
import android.util.Log;

import com.laka.androidlib.BuildConfig;
import com.laka.androidlib.util.AppStatusTracker;
import com.laka.androidlib.util.ApplicationUtils;
import com.laka.androidlib.util.Utils;
import com.tencent.smtt.sdk.QbSdk;

/**
 * @Author:Rayman
 * @Date:2018/12/15
 * @Description:项目
 **/
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 工具类初始化
        Utils.initSeriesUtil(this, BuildConfig.DEBUG ? true : false);
        initX5WebView();
        initAppTracker();
    }

    private void initX5WebView() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.e("BaseApplication", "isX5WebView kernel WebView init Success：" + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    private void initAppTracker() {
        registerActivityLifecycleCallbacks(new AppStatusTracker());
    }
}
