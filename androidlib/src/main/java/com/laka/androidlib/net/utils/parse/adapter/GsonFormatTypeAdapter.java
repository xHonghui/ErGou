package com.laka.androidlib.net.utils.parse.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.laka.androidlib.util.LogUtils;

import java.util.List;

/**
 * @Author:Rayman
 * @Date:2018/12/21
 * @Description:GSON针对后端Number字段,String字段，null转换工厂类
 */

public class GsonFormatTypeAdapter implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        //LogUtils.info("输出当前Gson对象的Type：" + type.getType() + "\nrawType：" + type.getRawType());
        Class<T> rawType = (Class<T>) type.getRawType();
        if (rawType == Integer.class || rawType == int.class) {
            return (TypeAdapter<T>) new IntegerDefaultFormatAdapter();
        } else if (rawType == Long.class || rawType == long.class) {
            return (TypeAdapter<T>) new LongDefaultFormatAdapter();
        } else if (rawType == Double.class || rawType == double.class) {
            return (TypeAdapter<T>) new DoubleDefaultFormatAdapter();
        } else if (rawType == Float.class || rawType == float.class) {
            return (TypeAdapter<T>) new FloatDefaultFormatAdapter();
        } else if (rawType == String.class) {
            return (TypeAdapter<T>) new StringDefaultFormatAdapter();
        } else {
            return null;//返回null，由其他Factory创建TypeAdapter
        }
    }
}