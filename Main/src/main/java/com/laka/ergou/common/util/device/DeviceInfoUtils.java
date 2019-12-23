package com.laka.ergou.common.util.device;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.laka.androidlib.util.PermissionUtils;

import java.util.UUID;

/**
 * @Author:summer
 * @Date:2019/6/25
 * @Description:
 */
public class DeviceInfoUtils {

    public static String androidID = "";//androidID
    public static String mSerial = "";//SIM卡的序列号
    public static String deviceID = "";//设备ID
    public static String androidUuidID = "";//Uuid加密的android设备唯一标识
    public static String phoneProducer = "";//手机厂商
    public static String IMEI = "";//手机IM
    public static String phoneModel = "";//手机型号
    public static String systemVersion = "";//手机系统版本号
    public static String SDKVersion = "";//SDK版本
    public static String versionName = "";//软件版本

    public static Activity context;

    /**
     * 本工具类初始化
     *
     * @param context
     */
    public static void init(Activity context) {
        DeviceInfoUtils.context = context;
    }

    /**
     * 未加密
     *
     * @return 设备ID
     */
    public static String getAndroidId() {
        try {
            if (TextUtils.isEmpty(androidID) && isLegalContext()) {
                androidID = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return androidID;
    }

    @SuppressLint("ByteOrderMark")
    public static String getDeviceId() {
        try {
            if (TextUtils.isEmpty(deviceID) && isLegalContext()) {
                final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    deviceID = "" + tm.getDeviceId();
                    mSerial = "" + tm.getSimSerialNumber();
                } else {
                    String[] permissionList = {Manifest.permission.READ_PHONE_STATE};
                    PermissionUtils.requestPermission(context, permissionList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceID;
    }


    /**
     * 生成设备的UUID
     */
    public static String getUUIDAndroidId() {
        try {
            if (TextUtils.isEmpty(androidUuidID) && isLegalContext()) {
                if (TextUtils.isEmpty(deviceID) || TextUtils.isEmpty(mSerial)) {
                    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        deviceID = "" + tm.getDeviceId();
                        mSerial = "" + tm.getSimSerialNumber();
                    } else {
                        String[] permissionList = {Manifest.permission.READ_PHONE_STATE};
                        PermissionUtils.requestPermission(context, permissionList);
                    }
                }
                if (!TextUtils.isEmpty(deviceID) && !TextUtils.isEmpty(mSerial)) {
                    getUUIDAndroidID(getAndroidId(), deviceID, mSerial);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return androidUuidID;
    }

    /**
     * UUID 加密过后
     *
     * @return android唯一标识
     */
    private static String getUUIDAndroidID(String androidID, String deviceID, String tmSerial) {
        UUID deviceUuid = new UUID(androidID.hashCode(), ((long) deviceID.hashCode() << 32) | tmSerial.hashCode());
        androidUuidID = deviceUuid.toString();
        return androidUuidID;
    }

    /**
     * 获取手机厂商加型号
     */
    public static String getPhoneProducerAndModel() {
        return getPhoneProducer() + "_" + getPhoneModel();
    }

    /**
     * 手机厂商
     *
     * @return
     */
    public static String getPhoneProducer() {
        if (TextUtils.isEmpty(phoneProducer)) {
            phoneProducer = android.os.Build.BRAND;
        }
        return phoneProducer;
    }

    /**
     * 手机IM
     *
     * @return
     */
    public static String getIMEI() {
        try {
            if (TextUtils.isEmpty(IMEI) && isLegalContext()) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
                if (tm != null) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        IMEI = tm.getDeviceId();
                    } else {
                        String[] permissionList = {Manifest.permission.READ_PHONE_STATE};
                        PermissionUtils.requestPermission(context, permissionList);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return IMEI;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        if (TextUtils.isEmpty(phoneModel)) {
            phoneModel = android.os.Build.MODEL;
        }
        return phoneModel;
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getSystemVersion() {
        if (TextUtils.isEmpty(systemVersion)) {
            systemVersion = android.os.Build.VERSION.RELEASE;
        }
        return systemVersion;
    }

    /**
     * SDK 版本
     *
     * @return
     */
    public static String getSDKVersion() {
        if (TextUtils.isEmpty(SDKVersion)) {
            SDKVersion = android.os.Build.VERSION.SDK;
        }
        return SDKVersion;
    }

    /**
     * 当前软件版本
     *
     * @return
     */
    private String getAppVersionName() {
        try {
            PackageManager packageManager = context.getPackageManager();
            //context.getPackageName() 当前软件包名
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            //packageInfo.versionCode
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static boolean isLegalContext() {
        if (context == null || context.isDestroyed() || context.isFinishing()) {
            return false;
        }
        return true;
    }

    //释放activity
    public static void release() {
        if (context != null) {
            context = null;
        }
    }

}
