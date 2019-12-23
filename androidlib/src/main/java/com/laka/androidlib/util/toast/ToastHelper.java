package com.laka.androidlib.util.toast;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.ResourceUtils;
import com.laka.androidlib.util.StringUtils;

/**
 * @Author Lyf
 * @CreateTime 2018/3/7
 * @Description Toast辅助类
 **/
public class ToastHelper {

    private static Toast sToast;
    // 自定义样式的Toast
    private static Context sContext;
    private static ICustomToast sCustomToast;
    private static final int DEFAULT_GRAVITY = -1;

    public static void initToastHelper(Application application, @Nullable ICustomToast customToast) {
        sContext = application;
        sCustomToast = customToast;
    }

    public static void showToast(int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    public static void showToast(CharSequence text) {
        if (checkToast(text)) {
            showToast(text, Toast.LENGTH_SHORT);
        }
    }

    public static void showCenterToast(int resId){
        String message = ResourceUtils.getString(resId);
        showCenterToast(message);
    }

    public static void showCenterToast(final CharSequence text) {
        if (checkToast(text)) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    showToast(text, Toast.LENGTH_SHORT, Gravity.CENTER, 0, 0);
                }
            });
        }
    }

    public static void showCenterToastLong(final CharSequence text) {
        if (checkToast(text)) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    showToast(text, Toast.LENGTH_LONG, Gravity.CENTER, 0, 0);
                }
            });
        }
    }


    public static void showToast(int resId, int duration) {
        String message = ResourceUtils.getString(resId);
        showToast(message, duration);
    }

    public static void showToast(final CharSequence text, final int duration) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                showToast(text, duration, DEFAULT_GRAVITY, 0, 0);
            }
        });
    }


    // 这两个变量,用于避免在短时间内"重复显示"同一个toast。
    private static CharSequence sLastMsg = "";
    private static long lastToast = 0;


    /**
     * 避免短时间内重复显示同一个Toast
     */
    public static boolean checkToast(CharSequence msg) {

        // msg也有可能为空
        if (StringUtils.isEmpty(msg)) {
            return false;
        }

        if (sLastMsg.equals(msg)) {

            // 同一条消息的时候，需要判断两条消息的间隔时间
            if (System.currentTimeMillis() - lastToast > 2000) {

                sLastMsg = msg;
                lastToast = System.currentTimeMillis();
                return true;
            } else {
                return false;
            }

        } else {

            // 不同一条消息时,直接显示
            sLastMsg = msg;
            lastToast = System.currentTimeMillis();
            return true;
        }

    }

    public static void showToast(CharSequence text, int duration, int gravity, int xOffset, int yOffset) {

        if (TextUtils.isEmpty(text)) {
            return;
        }

        if (sCustomToast == null) {

            if (sToast == null) {
                sToast = Toast.makeText(sContext, text, duration);
            } else {
                sToast.setText(text);
                sToast.setDuration(duration);
            }
            if (DEFAULT_GRAVITY != gravity) {
                sToast.setGravity(gravity, xOffset, yOffset);
            }
            sToast.show();
        } else {
            sCustomToast.setText(text);
            sCustomToast.setDuration(duration);
            if (DEFAULT_GRAVITY != gravity) {
                sCustomToast.setGravity(gravity, xOffset, yOffset);
            }
            sCustomToast.show();
        }

    }

}
