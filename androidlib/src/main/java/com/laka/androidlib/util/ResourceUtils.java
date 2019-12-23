package com.laka.androidlib.util;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;

/**
 * @ClassName: ResourceUtils
 * @Description: 系统资源文件工具
 * @Author: chuan
 * @Date: 09/01/2018
 */

public final class ResourceUtils {

    private ResourceUtils() {
        throw new UnsupportedOperationException("do not instantiate me , please.");
    }

    /**
     * 获取int资源，资源不存在时返回0
     *
     * @param resId 资源id
     * @return int型资源或0
     */
    public static int getInteger(@IntegerRes int resId) {
        return getInteger(resId, 0);
    }

    /**
     * 获取int资源，资源不存在时返回指定值
     *
     * @param resId    资源id
     * @param defValue 默认值
     * @return int资源或默认值
     */
    public static int getInteger(@IntegerRes int resId, int defValue) {
        if (resId <= 0) {
            return defValue;
        }

        return ApplicationUtils.getApplication().getResources().getInteger(resId);
    }

    /**
     * 获取dimen资源，资源不存在时返回0
     *
     * @param resId 资源id
     * @return dimen资源或0
     */
    public static int getDimen(@DimenRes int resId) {
        return getDimen(resId, 0);
    }

    /**
     * 获取dimen资源，资源不存在时返回指定值
     *
     * @param resId    资源id
     * @param defValue 默认值
     * @return dimen资源或默认值
     */
    public static int getDimen(@DimenRes int resId, int defValue) {
        if (resId <= 0) {
            return defValue;
        }

        return ApplicationUtils.getApplication().getResources().getDimensionPixelSize(resId);
    }

    /**
     * 获取颜色资源
     *
     * @param resId 资源id
     * @return 颜色值或0
     */
    public static int getColor(@ColorRes int resId) {
        return getColor(resId, 0);
    }

    /**
     * 获取颜色资源
     *
     * @param resId    资源id
     * @param defValue 默认值
     * @return 颜色值或defValue
     */
    public static int getColor(@ColorRes int resId, int defValue) {
        if (resId <= 0) {
            return defValue;
        }

        return ApplicationUtils.getApplication().getResources().getColor(resId);
    }

    /**
     * 获取颜色值列表
     *
     * @param resId 资源id
     * @return {@link ColorStateList} or null
     */
    public static ColorStateList getColorStateList(int resId) {
        if (resId <= 0) {
            return null;
        }

        return ApplicationUtils.getApplication().getResources().getColorStateList(resId);
    }

    /**
     * 获取String资源
     *
     * @param resId 资源id
     * @return String资源或null
     */
    public static String getString(@StringRes int resId) {
        return getString(resId, null);
    }

    /**
     * 获取String资源
     *
     * @param resId    资源id
     * @param defValue 默认值
     * @return String资源或默认值
     */
    public static String getString(@StringRes int resId, String defValue) {
        if (resId <= 0) {
            return defValue;
        }

        return ApplicationUtils.getApplication().getResources().getString(resId);
    }

    /**
     * 获取StringGroup资源
     *
     * @param resId    资源id
     * @return String资源或默认值
     */
    public static String[] getStringGroup(@ArrayRes int resId) {
        if (resId <= 0) {
            return new String[]{};
        }

        return ApplicationUtils.getApplication().getResources().getStringArray(resId);
    }

    /**
     * 获取String资源
     *
     * @param resId      资源id
     * @param formatArgs 参数
     * @return String资源或null
     */
    public static String getStringWithArgs(@StringRes int resId, Object... formatArgs) {
        return ApplicationUtils.getApplication().getResources().getString(resId, formatArgs);
    }

//    /**
//     * 获取String资源
//     *
//     * @param resId      资源id
//     * @param defValue   默认值
//     * @param formatArgs 参数
//     * @return String资源或默认值
//     */
//    public static String getStringWithArgs(@StringRes int resId, String defValue, Object... formatArgs) {
//        if (resId <= 0) {
//            return defValue;
//        }
//
//        return ApplicationUtils.getApplication().getResources().getString(resId, formatArgs);
//    }

    /**
     * 获取bitmap资源
     *
     * @param resId 资源id
     * @return bitmap或null
     */
    public static Bitmap getBitmap(@DrawableRes int resId) {
        if (resId <= 0) {
            return null;
        }

        Drawable drawable = ApplicationUtils.getApplication().getResources().getDrawable(resId);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        return null;
    }

    /**
     * 获取drawable资源
     *
     * @param resId 资源id
     * @return drawable 或 null
     */
    public static Drawable getDrawable(@DrawableRes int resId) {
        if (resId <= 0) {
            return null;
        }

        return ApplicationUtils.getApplication().getResources().getDrawable(resId);
    }

    /**
     * 获取颜色drawable列表
     *
     * @param normalColorResId  正常颜色资源
     * @param pressedColorResId 按压颜色资源
     * @return {@link StateListDrawable}
     */
    public static StateListDrawable getColorDrawableStateList(
            @ColorRes int normalColorResId, @ColorRes int pressedColorResId) {

        int normalColor = getColor(normalColorResId);
        int pressedColor = getColor(pressedColorResId);
        return createColorDrawableStateList(normalColor, pressedColor);
    }

    /**
     * 获取颜色drawable列表
     *
     * @param normalColor  正常颜色值
     * @param pressedColor 按压颜色值
     * @return {@link StateListDrawable}
     */
    public static StateListDrawable createColorDrawableStateList(
            int normalColor, int pressedColor) {
        StateListDrawable dr = new StateListDrawable();
        dr.addState(new int[]{android.R.attr.state_pressed},
                new ColorDrawable(pressedColor));
        dr.addState(new int[]{android.R.attr.state_checked},
                new ColorDrawable(pressedColor));
        dr.addState(new int[]{}, new ColorDrawable(normalColor));
        return dr;
    }

    /**
     * 获取颜色drawable列表
     *
     * @param normalResId  正常图片资源
     * @param pressedResId 按压图片资源
     * @return {@link StateListDrawable}
     */
    public static StateListDrawable getDrawableStateList(
            @DrawableRes int normalResId, @DrawableRes int pressedResId) {
        Drawable normal = getDrawable(normalResId);
        Drawable pressed = getDrawable(pressedResId);

        StateListDrawable dr = new StateListDrawable();
        dr.addState(new int[]{android.R.attr.state_pressed}, pressed);
        dr.addState(new int[]{}, normal);
        return dr;
    }

    /**
     * 制作渐变色drawable
     *
     * @param startColor 开始颜色
     * @param midColor   中间颜色
     * @param endColor   结束颜色
     * @return 制作的drawable
     */
    public static Drawable createGradientDrawable(int startColor, int midColor,
                                                  int endColor) {
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, new int[]{
                startColor, midColor, endColor});
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        return gradientDrawable;
    }

    /**
     * 颜色调和
     *
     * @param colorA 颜色A
     * @param colorB 颜色B
     * @param bias   调和比例
     * @return 调和后的颜色值
     */
    public static int interpolateColor(int colorA, int colorB, float bias) {
        float[] hsvColorA = new float[3];
        Color.colorToHSV(colorA, hsvColorA);

        float[] hsvColorB = new float[3];
        Color.colorToHSV(colorB, hsvColorB);

        hsvColorB[0] = interpolate(hsvColorA[0], hsvColorB[0], bias);
        hsvColorB[1] = interpolate(hsvColorA[1], hsvColorB[1], bias);
        hsvColorB[2] = interpolate(hsvColorA[2], hsvColorB[2], bias);

        return Color.HSVToColor(hsvColorB);
    }

    /**
     * 计算值的调和
     *
     * @param a    值a
     * @param b    值b
     * @param bias 调和比例
     * @return 调和后的值
     */
    private static float interpolate(float a, float b, float bias) {
        return (a + ((b - a) * bias));
    }
}
