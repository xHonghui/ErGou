package com.laka.androidlib.util;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

import com.laka.androidlib.BuildConfig;
import com.laka.androidlib.util.screen.ScreenUtils;
import com.laka.androidlib.util.toast.ToastHelper;

import java.io.File;
import java.lang.reflect.Field;

/**
 * @Author Lyf
 * @CreateTime 2018/4/26
 * @Description 系统信息
 **/

public class SystemUtils {

    private static Application application = ApplicationUtils.getApplication();

    // 状态栏的高度
    private static int sStatusBarHeight;

    // 应用版本号，字符串类型 如:1.0.0
    public String app_version;

    // device_token => iOS或Android中用于推送的Token，若未从系统获取到则返回空字符串，字符串类型
    // 如:a22241adab6c68b3687a9f0f086c540341f4b3f010546d4af4834ada32281615
    public String device_token;

    // 设备型号，字符串类型 如:iPhone 6
    public String device_model;

    // 系统类型，如ios、android，等字符串类型 如:ios
    public String system_type;

    // 应用版本号，字符串类型 如:8.0
    public String system_version;

    // 设备唯一标识，字符串类型 如:FC408F8B-9598-48B6-A740-B9037ADCXXXE
    public String device_id;

    // 运营商名称，若未获取到则返回none，字符串类型 如:中国移动
    public String operator;

    // 当前网络连接类型，如 2g、3g、4g、wifi 等，字符串类型 如:wifi
    public String connection_type;

    // 当前手机系统，xiaomi、huawei
    public String platform_type;

    // 小米或华为的tokencode
    public String target;


    /**
     * 获取版本号---文案版本
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName() {
        try {
            PackageManager manager = application.getPackageManager();
            PackageInfo info = manager.getPackageInfo(application.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return BuildConfig.VERSION_NAME;
        }
    }

    /**
     * 获取版本号---数字版本
     *
     * @return
     */
    public static int getVersionCode() {
        try {
            PackageManager manager = application.getPackageManager();
            PackageInfo info = manager.getPackageInfo(application.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return BuildConfig.VERSION_CODE;
        }
    }

    /**
     * @return 获取状态栏高度
     */
    public static int getStatusBarHeight() {
        return getStatusBarHeight(application);
    }

    private static int getStatusBarHeight(Context context) {
        if (sStatusBarHeight > 0) {
            return sStatusBarHeight;
        }
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            sStatusBarHeight = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e) {
            sStatusBarHeight = guessStatusBarHeight(context);

            e.printStackTrace();
        }
        return sStatusBarHeight;
    }

    private static int guessStatusBarHeight(Context context) {
        try {
            if (context != null) {
                final int statusBarHeightDP = 25;
                float density = context.getResources().getDisplayMetrics().density;
                return Math.round(density * statusBarHeightDP);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取某个属性的下标
     *
     * @param attrs
     * @param index
     * @return
     */
    public static int getAttrsIndex(AttributeSet attrs, int index) {

        //获取AttributeSet中所有的XML属性的数量
        int count = attrs.getAttributeCount();
        //遍历AttributeSet中的XML属性
        for (int i = 0; i < count; i++) {
            //获取attr的资源ID
            int attrResId = attrs.getAttributeNameResource(i);
            if (index == attrResId) {
                return i;
            }
        }
        return -1;
    }


    public static boolean getAttributeBooleanValue(AttributeSet attrs, int attrResId, boolean defaultValue) {

        int index = getAttrsIndex(attrs, attrResId);

        if (index != -1) {
            return attrs.getAttributeBooleanValue(index, defaultValue);
        }

        return defaultValue;
    }

    public static int getAttributeResourceValue(AttributeSet attrs, int attrResId, int defaultValue) {

        int index = getAttrsIndex(attrs, attrResId);

        if (index != -1) {
            return attrs.getAttributeResourceValue(index, defaultValue);
        }

        return defaultValue;
    }


    /**
     * 复制内容到剪切板
     *
     * @param strToCopy 要被复制的内容
     */
    public static void copyToClipboard(String strToCopy) {

        ClipboardManager clipboard = (ClipboardManager)
                ApplicationUtils.getApplication()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText(null, strToCopy));
            ToastHelper.showCenterToast("已复制到剪切板");
        }
    }

    public static Bitmap getBitmapByView(View view) {

        int h = view.getMeasuredHeight();

        Bitmap bitmap = null;

        int maxHeight = ScreenUtils.getScreenHeight() * 2;

        if (h > maxHeight) {
            h = maxHeight;
        }

        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(view.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public static void notifyPhotoChanged(Context context, File file) {
        try {

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);

        } catch (Exception e) {

        }
    }

    /**
     * 打开桌面
     */
    public static void openSystemLuncher(Context context) {

        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(home);
    }
}
