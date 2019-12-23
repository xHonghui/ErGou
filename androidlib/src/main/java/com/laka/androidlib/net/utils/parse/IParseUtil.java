package com.laka.androidlib.net.utils.parse;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @Author Lyf
 * @CreateTime 2018/2/26
 * @Description
 **/
public interface IParseUtil {

    /**
     * Converts a json string into a T bean.
     */
    <T> T parseJson(String json, Class<T> tClass);

    /**
     * Converts a json string into a T bean.
     */
    <T> T parseJson(String json, Type type);

    /**
     * Converts an object into a string.
     */
    String toJson(Object object);

    /**
     * Converts a json into an List.
     *
     * @param json  a string json contains a key which has an array value.
     * @param key   the key whose value is an array.
     * @param clazz the class of T.
     * @param <T>   a certain type of array.
     * @return
     */
    <T> List<T> parseJsonToList(String json, String key, final Class<T> clazz);

    /**
     * Converts a json into an List.
     *
     * @param json  a string json contains an array of T.
     * @param clazz the class of T.
     * @param <T>   a certain type of array.
     * @return
     */
    <T> List<T> parseJsonToList(String json, final Class<T> clazz);

    /**
     * Converts a json into an Map.
     *
     * @param json    a string json contains an array of T.
     * @param typeOfT see{@link com.google.gson.reflect.TypeToken<T>}
     * @return
     */
    <T> T parseJsonToType(String json, final Type typeOfT);
}
