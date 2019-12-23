package com.laka.androidlib.util;

import android.Manifest;
import android.content.Context;

import com.laka.androidlib.widget.CallPopupWindow;

/**
 * @Author:Rayman
 * @Date:2018/5/4
 * @Description:电话呼叫类
 */

public class CallUtil {

    private static long firstTime = 0;
    private static long time_interval = 1000;

    public static void makePhoneCall(Context context, String phone) {
        makePhoneCall(context, phone, false);
    }

    public static void makePhoneCall(Context context, String phone, boolean isEncry) {
        if (PermissionUtils.checkPermission(Manifest.permission.CALL_PHONE)) {
            if (System.currentTimeMillis() - firstTime > time_interval) {
                CallPopupWindow popupWindow = new CallPopupWindow(context, phone, isEncry);
                popupWindow.show();
                firstTime = System.currentTimeMillis();
            }
        }
    }
}
