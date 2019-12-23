package com.laka.androidlib.util;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.ArrayMap;

/**
 * @ClassName: TypefaceHelper
 * @Description: 存储程序已经加载的Typeface
 * @Author: chuan
 * @Date: 10/01/2018
 */

public final class TypefaceHelper {
    private static TypefaceHelper mTypefaceHelper;
    private ArrayMap<String, Typeface> mTypefaceMap;

    /**
     * 单列模式
     *
     * @return TypefaceHelper实例
     */
    public static TypefaceHelper getInstance() {
        if (mTypefaceHelper == null) {
            synchronized (TypefaceHelper.class) {
                if (mTypefaceHelper == null) {
                    mTypefaceHelper = new TypefaceHelper();
                }
            }
        }

        return mTypefaceHelper;
    }

    /**
     * 私有构造函数，防止外部调用
     */
    private TypefaceHelper() {
        mTypefaceMap = new ArrayMap<>();
    }

    /**
     * 取出Typeface
     *
     * @param key 键
     * @return Typeface
     */
    public Typeface getTypeface(String key) {
        return mTypefaceMap.get(key);
    }

    /**
     * 存入Typeface
     *
     * @param key      键
     * @param typeface Typeface
     */
    public void putTypeface(String key, @NonNull Typeface typeface) {
        mTypefaceMap.put(key, typeface);
    }
}
