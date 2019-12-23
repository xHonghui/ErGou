package com.laka.androidlib.util.screen;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;

/**
 * @Author:Rayman
 * @Date:2018/12/20
 * @Description:屏幕适配工具类
 */

public class DensityUtils {

    /**
     * description:系统默认Density
     **/
    private static float mDefaultDensity;
    private static float mDefaultScaledDensity;


    /**
     * 设置当前Application和Activity的兼容Density
     *
     * @param activity
     * @param application
     */
    public static void setCustomDensity(@NonNull Activity activity, @NonNull final Application application) {
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if (mDefaultDensity == 0) {
            mDefaultDensity = appDisplayMetrics.density;
            mDefaultScaledDensity = appDisplayMetrics.scaledDensity;

            // 注册配置回调，系统更换文字大小引起Density变化的时候回调更新Density
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        mDefaultScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        // 读取配置文件信息，计算出当前兼容的Density和scaledDensity
        CompatData metaConfig = getApplicationDesignDp(application);
        int designDp = metaConfig.getDesignDp();
        boolean isWidthCompat = metaConfig.isWidthCompat();
        // 默认使用XHDPI兼容
        if (designDp <= 0) {
            designDp = 320;
        }

        // 重新计算Metrics的参数
        final float targetDensity = appDisplayMetrics.widthPixels / designDp;
        final float targetScaledDensity = targetDensity * (mDefaultScaledDensity / mDefaultDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        // 同步更新到Activity的Metrics
        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    /**
     * 获取当前Manifest文件Application层配置
     */
    private static CompatData getApplicationDesignDp(@NonNull Application application) {
        CompatData compatData = new CompatData();
        try {
            ApplicationInfo appInfo = application.getPackageManager()
                    .getApplicationInfo(application.getPackageName(), PackageManager.GET_META_DATA);
            int designDp = appInfo.metaData.getInt("DESIGN_DP");
            boolean isWidthCompat = appInfo.metaData.getBoolean("COMPAT_WIDTH");
            compatData.setDesignDp(designDp);
            compatData.setWidthCompat(isWidthCompat);
            return compatData;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return compatData;
        }
    }

    /**
     * 兼容数据类
     */
    private static class CompatData {
        private int designDp = 0;
        private boolean isWidthCompat = true;

        public int getDesignDp() {
            return designDp;
        }

        public void setDesignDp(int designDp) {
            this.designDp = designDp;
        }

        public boolean isWidthCompat() {
            return isWidthCompat;
        }

        public void setWidthCompat(boolean widthCompat) {
            isWidthCompat = widthCompat;
        }
    }
}
