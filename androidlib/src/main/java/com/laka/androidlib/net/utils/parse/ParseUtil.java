package com.laka.androidlib.net.utils.parse;


import com.laka.androidlib.net.response.BaseBean;
import com.laka.androidlib.net.response.Callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


/**
 * @Author Lyf
 * @CreateTime 2018/2/26
 * @Description
 **/
public class ParseUtil {

    private static IParseUtil INSTANCE;

    private static IParseUtil getParseUtil() {

        if (INSTANCE == null) {
            synchronized (ParseUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GsonUtil();
                }
            }
        }

        return INSTANCE;
    }

    public static <T> T parseJson(String json, Class<T> tClass) {
        return getParseUtil().parseJson(json, tClass);
    }

    public static <T> T parseJson(String json, Type type) {
        return getParseUtil().parseJson(json, type);
    }

    public static String toJson(Object object) {
        return getParseUtil().toJson(object);
    }

    public static <T> T parseJson(String json, Callback<T> responseCallback) {
        return parseJson(json, (((ParameterizedType)
                (responseCallback.getClass().getGenericInterfaces())[0])
                .getActualTypeArguments()[0]));
    }

    public static <T> List<T> parseJsonToList(String json, String key, final Class<T> clazz) {
        return getParseUtil().parseJsonToList(json, key, clazz);
    }

    public static <T> List<T> parseJsonToList(String json, final Class<T> clazz) {
        return getParseUtil().parseJsonToList(json, clazz);
    }

    public static <T> T parseJsonToType(String json, final Type type) {
        return getParseUtil().parseJsonToType(json, type);
    }
}