package com.laka.androidlib.util;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ImageView;
import java.lang.reflect.Field;

/**
 * @ClassName: ViewUtils
 * @Description: View工具类
 * @Author: chuan
 * @Date: 23/01/2018
 */

public final class ViewUtils {

    private ViewUtils() {
        throw new UnsupportedOperationException("do not instantiate me , please.");
    }

    /**
     * 获取ImageView的宽
     *
     * @param imageView ImageView
     * @return 宽
     */
    public static int getWidth(@NonNull ImageView imageView) {
        int width = 0;
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        if (params != null) {
            width = params.width;
        }

        if (width <= 0) {
            width = getImageViewFieldValue(imageView, "mMaxWidth");
        }

        return width;
    }

    /**
     * 获取ImageView的高
     *
     * @param imageView ImageView
     * @return 高
     */
    public static int getHeight(@NonNull ImageView imageView) {
        int height = 0;

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        if (params != null) {
            height = params.height;
        }

        if (height <= 0) {
            height = getImageViewFieldValue(imageView, "mMaxHeight");
        }

        return height;
    }

    /**
     * 通过反射得到ImageView的属性
     *
     * @param object    要获取属性的ImageView
     * @param fieldName 要获取的属性名
     * @return 所获取属性的值
     */
    public static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;

        try {
            Field e = ImageView.class.getDeclaredField(fieldName);
            e.setAccessible(true);
            int fieldValue = (int) e.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    /**
     * 隐藏软键盘
     */
    public static void hideInputMethod( Activity activity) {

        try{
            if (activity != null) {

                InputMethodManager imm = (InputMethodManager) activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive() && activity.getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                            .getWindowToken(), 0);
                }
            }

        }catch (Exception e) {

        }
    }

}
