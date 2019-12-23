package com.laka.androidlib.net.utils.parse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.laka.androidlib.net.utils.parse.adapter.GsonFormatTypeAdapter;
import com.laka.androidlib.net.utils.parse.anno.AnnotationExclusion;
import com.laka.androidlib.util.LogUtils;

import org.json.JSONObject;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/12/20
 * @Description: Gson工具类
 **/
public class GsonUtil implements IParseUtil {

    public static final String TAG = "GsonUtils";

    /**
     * description:创建GSON的规则，String默认null转空字符串
     * Gson默认使用Expose注解
     **/
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new GsonFormatTypeAdapter())
            .setExclusionStrategies(new AnnotationExclusion())
            .addSerializationExclusionStrategy(new AnnotationExclusion())
            .addDeserializationExclusionStrategy(new AnnotationExclusion())
            .serializeNulls()
            .create();

    @Override
    public <T> T parseJson(String json, Class<T> tClass) {
        return GsonUtil.convertJson2Bean(json, tClass);
    }

    @Override
    public <T> T parseJson(String json, Type type) {
        return GsonUtil.convertJson2Bean(json, type);
    }

    @Override
    public String toJson(Object object) {
        return GsonUtil.convertObject2Json(object);
    }

    /**
     * Converts a json string into a T bean.
     */
    public static <T> T convertJson2Bean(String json, Class<T> tClass) {
        return gson.fromJson(json, tClass);
    }

    /**
     * Converts a json string into a T bean.
     */
    public static <T> T convertJson2Bean(String json, Type type) {
        return gson.fromJson(json, type);
    }

    /**
     * Converts an object into a string.
     */
    public static String convertObject2Json(Object object) {
        return gson.toJson(object);
    }


    @Override
    public <T> List<T> parseJsonToList(String json, String key, final Class<T> clazz) {

        try {
            Type type = new ParameterizedType() {
                @Override
                public Type getRawType() {
                    return ArrayList.class;
                }

                @Override
                public Type getOwnerType() {
                    return null;
                }

                @Override
                public Type[] getActualTypeArguments() {
                    Type[] type = new Type[1];
                    type[0] = clazz;
                    return type;
                }
            };

            String data = new JSONObject(json).optString(key);
            return gson.fromJson(data, type);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public <T> List<T> parseJsonToList(String json, final Class<T> clazz) {

        try {
            Type type = new ParameterizedType() {
                @Override
                public Type getRawType() {
                    return ArrayList.class;
                }

                @Override
                public Type getOwnerType() {
                    return null;
                }

                @Override
                public Type[] getActualTypeArguments() {
                    Type[] type = new Type[1];
                    type[0] = clazz;
                    return type;
                }
            };
            return gson.fromJson(json, type);
        } catch (Exception e) {
            LogUtils.log("parse=" + e.toString());
            return null;
        }

    }

    @Override
    public <T> T parseJsonToType(String json, Type typeOfT) {
        try {
            return gson.fromJson(json, typeOfT);
        } catch (Exception e) {
            return null;
        }
    }

    public Gson getDefaultGson() {
        return gson;
    }

    public static <T> String listToJson(List<T> list) {
        String json = gson.toJson(list);
        return json;
    }
}