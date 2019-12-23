package com.laka.androidlib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;

import com.laka.androidlib.net.utils.parse.ParseUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: SharedPreferenceHelper
 * @Description: SharePreference帮助类
 * @Author: chuan
 * @Date: 08/01/2018
 */

public final class SPHelper {

    private static SharedPreferences mSharedPreferences;

    private final static String EMPTY = "";

    /**
     * 单列模式。获取SharedPreferences实例。
     *
     * @return SharedPreferences实例
     */
    private static SharedPreferences getSharedPreferences() {
        if (mSharedPreferences == null) {
            synchronized (SPHelper.class) {
                if (mSharedPreferences == null) {
                    mSharedPreferences = ApplicationUtils.getApplication().getSharedPreferences(
                            ApplicationUtils.getApplication().getPackageName(), Context.MODE_PRIVATE);
                }
            }
        }
        return mSharedPreferences;
    }

    /**
     * 获取SP文件，如果fileName为空，则使用默认文件名
     */
    private static SharedPreferences getSharedPreferences(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return getSharedPreferences();
        }
        return ApplicationUtils.getApplication().getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }


    /**
     * 私有的构造函数
     */
    private SPHelper() {
        // 避免被反射获取
        throw new UnsupportedOperationException("You can't initialize me!");
    }

    /**
     * 存入long型数据
     *
     * @param keyString 键
     * @param object    值
     */
    public static void putLong(String keyString, long object) {
        getSharedPreferences().edit().putLong(keyString, object).apply();
    }

    /**
     * 存入boolean型数据
     *
     * @param keyString 键
     * @param object    值
     */
    public static void putBoolean(String keyString, boolean object) {
        getSharedPreferences().edit().putBoolean(keyString, object).apply();
    }

    /**
     * 存入int型数据
     *
     * @param keyString 键
     * @param object    值
     */
    public static void putInt(String keyString, int object) {
        getSharedPreferences().edit().putInt(keyString, object).apply();
    }

    /**
     * 存入String型数据
     *
     * @param keyString 键
     * @param object    值
     */
    public static void putString(String keyString, String object) {
        getSharedPreferences().edit().putString(keyString, object).apply();
    }

    /**
     * 存入float型数据
     *
     * @param keyString 键
     * @param object    值
     */
    public static void putFloat(String keyString, float object) {
        getSharedPreferences().edit().putFloat(keyString, object).apply();
    }

    /**
     * 读取String型数据
     *
     * @param keyString 键
     * @param def       默认值
     * @return 相关数据或def。
     */
    public static String getString(String keyString, String def) {
        return getSharedPreferences().getString(keyString, def);
    }

    /**
     * 读取int型数据
     *
     * @param keyString 键
     * @param def       默认值
     * @return 相关数据或def。
     */
    public static int getInt(String keyString, int def) {
        return getSharedPreferences().getInt(keyString, def);
    }

    /**
     * 读取long型数据
     *
     * @param keyString 键
     * @param def       默认值
     * @return 相关数据或def。
     */
    public static long getLong(String keyString, long def) {
        return getSharedPreferences().getLong(keyString, def);
    }

    /**
     * 读取boolean型数据
     *
     * @param keyString 键
     * @param def       默认值
     * @return 相关数据或def。
     */
    public static boolean getBoolean(String keyString, boolean def) {
        return getSharedPreferences().getBoolean(keyString, def);
    }

    /**
     * 读取float型数据
     *
     * @param keyString 键
     * @param def       默认值
     * @return 相关数据或def。
     */
    public static float getFloat(String keyString, float def) {
        return getSharedPreferences().getFloat(keyString, def);
    }


    /**
     * 保存一个对象。先将其转成json，再存到SP中。
     *
     * @param keyString 键
     * @param object    对象
     */
    public static void saveObject(String keyString, Object object) {
        try {
            getSharedPreferences().edit().putString(keyString,
                    encryptCaching(ParseUtil.toJson(object))).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定文件名称保存一个对象
     */
    public static void saveObject(String fileName, String keyString, Object object) {
        try {
            getSharedPreferences(fileName).edit().putString(keyString,
                    encryptCaching(ParseUtil.toJson(object))).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定文件名称保存一个集合
     */
    public static void saveArray(String fileName, String keyString, Object object) {
        try {
            getSharedPreferences(fileName).edit().putString(keyString,
                    encryptCaching(ParseUtil.toJson(object))).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一个对象。
     *
     * @param keyString 键
     * @param tClass    对象的类型
     * @param <T>       泛型，由{@param tClass} 指定
     * @return 返回一个T类型的对象
     */
    @Nullable
    public static <T> T getObject(@NonNull String keyString, Class<T> tClass) {
        String json = getSharedPreferences().getString(keyString, null);
        if (json == null) {
            return null;
        }
        return ParseUtil.parseJson(decryptCaching(json), tClass);
    }

    /**
     * 指定文件名称获取一个对象
     */
    @Nullable
    public static <T> T getObject(@NonNull String fileName, @NonNull String keyString, Class<T> tClass) {
        String json = getSharedPreferences(fileName).getString(keyString, null);
        if (json == null) {
            return null;
        }
        return ParseUtil.parseJson(decryptCaching(json), tClass);
    }

    /**
     * 指定文件名称获取一个集合
     */
    @Nullable
    public static <T> List<T> getArray(@NonNull String fileName, @NonNull String keyString, Class<T> tClass) {
        String json = getSharedPreferences(fileName).getString(keyString, null);
        if (json == null) {
            return null;
        }
        return ParseUtil.parseJsonToList(decryptCaching(json), tClass);
    }

    /**
     * 使用Base64加密Json
     */
    private static String encryptCaching(String json) throws IOException {
        return new String(Base64.encode(json.getBytes(), Base64.DEFAULT));
    }

    /**
     * 使用Base64解密Json
     */
    private static String decryptCaching(String json) {
        return new String(Base64.decode(json, Base64.DEFAULT));
    }

    /**
     * 删除指定文件
     */
    public static void clearByFileName(String fileName) {
        SharedPreferences sp = ApplicationUtils.getApplication().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

}
