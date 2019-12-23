package com.laka.androidlib.util.system;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.laka.androidlib.BuildConfig;
import com.laka.androidlib.util.ApplicationUtils;
import com.laka.androidlib.util.ChannelUtil;
import com.laka.androidlib.util.FileUtils;
import com.laka.androidlib.util.HardwareUtil;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.SPHelper;
import com.laka.androidlib.util.StringUtils;
import com.laka.androidlib.util.toast.ToastHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName: HardwareUtils
 * @Description: 系统辅助类，用于做一些系统级别的操作。
 * 比如，获取系统信息，安装应用。
 * @Author: chuan
 * @Date: 08/01/2018
 */
public class SystemHelper {

    private Context context = ApplicationUtils.getApplication();
    public static final String KEY_UUID = "UUid";
    private static final String SHARE_PREFERENCE_NAME_OLD = "user";
    private static AppInfo sAppInfo;
    private static DeviceInfo sDeviceInfo;

    private static SystemHelper INSTANCE = null;

    private SystemHelper() {
        init();
    }

    public static SystemHelper getInstance() {

        if (INSTANCE == null) {
            synchronized (SystemHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SystemHelper();
                }
            }
        }
        return INSTANCE;
    }


    private void init() {

        sAppInfo = new AppInfo();
        sAppInfo.marketChannel = ChannelUtil.getChannel(context);
        String apkVersionName;
        int apkVersionCode;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo != null) {
                apkVersionName = packageInfo.versionName;
                apkVersionCode = packageInfo.versionCode;
            } else {
                apkVersionCode = 21;
                apkVersionName = "3.0.0";
            }
        } catch (Exception e) {
            e.printStackTrace();
            apkVersionName = "2.6.0";
            apkVersionCode = 10;
        }
        sAppInfo.versionCode = apkVersionCode;
        sAppInfo.versionName = apkVersionName;
        sAppInfo.packageName = context.getPackageName();

        sDeviceInfo = new DeviceInfo();
        sDeviceInfo.sdkVersionName = Build.VERSION.RELEASE;
        sDeviceInfo.sdkVersionCode = Build.VERSION.SDK_INT;
        sDeviceInfo.deviceName = Build.MODEL;
        sDeviceInfo.manufacturer = Build.MANUFACTURER;
        sDeviceInfo.androidId = HardwareUtil.getAndroidId();
        sDeviceInfo.imei = HardwareUtil.getIMEI();
        sDeviceInfo.macAdr = HardwareUtil.getMacAddress();
        //sDeviceInfo.simNo = HardwareUtil.getSimNo();

        UUID deviceUuid = new UUID(sDeviceInfo.androidId.hashCode(), sDeviceInfo.imei.hashCode());
        sDeviceInfo.deviceId = deviceUuid.toString();

        String uuid = SPHelper.getString(KEY_UUID, null);
        if (StringUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            SPHelper.saveObject(KEY_UUID, uuid);
        }
        sDeviceInfo.uuid = uuid;
        if (BuildConfig.DEBUG == true) {
            Log.i("SystemHelper", "app info --> " + sAppInfo);
            Log.i("SystemHelper", "device info --> " + sDeviceInfo);
        }
    }

    public AppInfo getAppInfo() {
        return sAppInfo;
    }

    public DeviceInfo getDeviceInfo() {
        return sDeviceInfo;
    }

    /**
     * 安装应用
     *
     * @param context  上下文，可以用Application上下文
     * @param filePath apk对应的绝对路径
     */
    public static void installApk(Context context, String filePath) {

        File file = FileUtils.getAppFile(filePath, null, false);
        if (file == null) {
            ToastHelper.showToast("安装失败，文件不存在");
            return;
        }

        ToastHelper.showToast("更新成功");

        installApk(context, file);
    }


    /**
     * 安装应用
     *
     * @param context 上下文，可以用Application上下文
     * @param file    apk对应的File
     */
    public static void installApk(Context context, File file) {
        Uri contentUri;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android o 的权限问题
            boolean installAllowed = context.getPackageManager().canRequestPackageInstalls();
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            contentUri = FileProvider.getUriForFile(context, "com.laka.ergou.fileprovider", file);
            LogUtils.info("system-----是否拥有安装位置应用的权限-----installAllowed=$installAllowed");
            startInstallActivity(context, contentUri, intent);

//            android 8.0后对于应用内部安装设置了一个“应用内部安装未知来源apk”的权限
//            if (installAllowed) {
//                startInstallActivity(context, contentUri, intent);
//            } else {
//                ToastHelper.showCenterToast("已禁止未知来源应用安装，如需继续操作，请进入设置-->应用中开启相应权限");
//            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //android N的权限问题
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            contentUri = FileProvider.getUriForFile(context, "com.laka.ergou.fileprovider", file);//注意修改
            startInstallActivity(context, contentUri, intent);
        } else {
            contentUri = Uri.fromFile(file);
            startInstallActivity(context, contentUri, intent);
        }


//        Uri contentUri;
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        if (Build.VERSION.SDK_INT >= 24) {
//            //android N的权限问题
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            contentUri = FileProvider.getUriForFile(context, "com.laka.ergou.fileprovider", file);//注意修改
//        } else {
//            contentUri = Uri.fromFile(file);
//        }
//
//        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
//        context.startActivity(intent);

    }

    /**
     * 启动安装页面
     */
    private static void startInstallActivity(Context context, Uri contentUri, Intent intent) {
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
