package com.laka.androidlib.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

import com.laka.androidlib.util.toast.ToastHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/09/01
 * @Description:权限工具
 */
public final class PermissionUtils {

    private static int REQUEST_CODE_PERMISSION = 0x999;

    private final static List<String> PERMISSIONS = getPermissions();  //app声明的权限列表


    /**
     * 获取app声明的权限列表
     *
     * @return app声明的权限列表
     */
    public static List<String> getPermissions() {
        return getPermissions(ApplicationUtils.getApplication().getPackageName());
    }

    /**
     * 获取指定包名下声明的权限列表
     *
     * @param packageName 包名
     * @return 权限列表
     */
    public static List<String> getPermissions(String packageName) {
        PackageManager pm = ApplicationUtils.getApplication().getPackageManager();

        try {
            return Arrays.asList(
                    pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                            .requestedPermissions);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

            return Collections.emptyList();
        }
    }

    /**
     * 判断当前app是否获得权限
     *
     * @param permission 相关权限
     * @return true , 表示具备该权限；false,不具备该权限
     */
    public static boolean checkPermission(String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            if (PermissionChecker.checkSelfPermission(ApplicationUtils.getApplication(), permission)
                    == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        } else {
            PackageManager pm = ApplicationUtils.getApplication().getPackageManager();
            if (pm.checkPermission(permission, ApplicationUtils.getApplication().getPackageName())
                    == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 判断当前app是否获得权限
     *
     * @param permissions 相关权限
     * @return true , 表示具备该权限；false,不具备该权限
     */
    public static boolean checkPermission(String... permissions) {
        for (String permission : permissions) {
            if (!checkPermission(permission)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 请求权限
     *
     * @param permissions
     */
    public static void requestPermission(Activity mActivity, String[] permissions) {
        //重新请求权限
        List<String> needPermissions = getDeniedPermissions(permissions);
        if (needPermissions != null && !needPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(mActivity,
                    needPermissions.toArray(new String[needPermissions.size()]),
                    REQUEST_CODE_PERMISSION);
        }
    }

    /**
     * 请求权限
     *
     * @param permissions
     */
    public static void requestAllPermissions(Activity mActivity, String[] permissions) {
        //重新请求权限
        ActivityCompat.requestPermissions(mActivity, permissions, REQUEST_CODE_PERMISSION);
    }

    /**
     * 获取权限集中需要申请权限的列表(筛选没通过的权限)
     *
     * @param permissions
     * @return
     */
    private static List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(ApplicationUtils.getApplication(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 系统请求权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults
            , PermissionCallback permissionCallback) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (verifyPermissions(grantResults, permissions)) {
                if (permissionCallback != null) {
                    permissionCallback.permissionSuccess(REQUEST_CODE_PERMISSION);
                }
            } else {
                if (permissionCallback != null) {
                    permissionCallback.permissionFail(REQUEST_CODE_PERMISSION);
                }
            }
        }
    }

    /**
     * 确认所有的权限是否都已授权
     *
     * @param grantResults
     * @return
     */
    private static boolean verifyPermissions(int[] grantResults, @NonNull String[] permissions) {
        for (int i = 0; i < grantResults.length; i++) {
            int grantResult = grantResults[i];
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                if (permissions[i].equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    ToastHelper.showToast("请授权读写SD权限!");
                } else if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ToastHelper.showToast("请授权读写SD权限!");
                } else if (permissions[i].equals(Manifest.permission.CAMERA)) {
                    ToastHelper.showToast("请授权打开摄像头权限!");
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 打开应用具体设置
     */
    public static void openAppSettings() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + ApplicationUtils.getApplication().getPackageName()));
        ApplicationUtils.getApplication().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public interface PermissionCallback {
        /**
         * 获取权限成功
         *
         * @param requestCode
         */
        void permissionSuccess(int requestCode);

        /**
         * 权限获取失败
         *
         * @param requestCode
         */
        void permissionFail(int requestCode);
    }
}
