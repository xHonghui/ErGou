package com.laka.androidlib.util;

import android.content.Intent;

import com.laka.androidlib.util.toast.ToastHelper;

/**
 * @Author:Rayman
 * @Date:2019/2/23
 * @Description:第三方App跳转
 */

public class ThirdAppNavigator {

    public static void startTaobaoApp() {
        if (PackageUtils.isAppInstalled(ApplicationUtils.getApplication(), PackageUtils.TAO_BAO)) {
            Intent intent = new Intent();
            intent.setAction("Android.intent.action.VIEW");
            intent.setClassName("com.taobao.taobao", "com.taobao.tao.welcome.Welcome");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ApplicationUtils.getApplication().startActivity(intent);
        } else {
            ToastHelper.showToast("请先安装手掏客户端");
        }
    }

    public static void startTaobaoMainActivity() {
        if (PackageUtils.isAppInstalled(ApplicationUtils.getApplication(), PackageUtils.TAO_BAO)) {
            Intent intent = new Intent();
            intent.setAction("Android.intent.action.VIEW");
            intent.setClassName("com.taobao.taobao", "com.taobao.tao.homepage.MainActivity3");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ApplicationUtils.getApplication().startActivity(intent);
        } else {
            ToastHelper.showToast("请先安装手掏客户端");
        }
    }
}
