package com.laka.androidlib.net.utils.parse.anno;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.laka.androidlib.net.utils.parse.GsonUtil;
import com.laka.androidlib.util.LogUtils;

/**
 * @Author:Rayman
 * @Date:2018/12/21
 * @Description:基于注解的Gson过滤器
 */

public class AnnotationExclusion implements ExclusionStrategy {

    public static final String TAG = GsonUtil.TAG;

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        // 判断使用Exclude注解的变量，就直接排除
        boolean isShouldSkip = f.getAnnotation(Exclude.class) != null;
        //LogUtils.info(TAG, "输出当前字段：" + f.getName() + "是否排除：" + isShouldSkip);
        return isShouldSkip;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        boolean isShouldSkip = clazz.getAnnotation(Exclude.class) != null;
        //LogUtils.info(TAG, "输出当前类名：" + clazz.getName() + "是否排除：" + isShouldSkip);
        return isShouldSkip;
    }
}
