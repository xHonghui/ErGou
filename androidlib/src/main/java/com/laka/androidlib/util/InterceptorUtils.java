package com.laka.androidlib.util;

import java.util.ArrayList;

import okhttp3.Interceptor;

/**
 * @Author:summer
 * @Date:2019/3/25
 * @Description:androidlibs 模块的拦截器初始化工具类
 */
public class InterceptorUtils {

    private static ArrayList<Interceptor> mInterceptorList = new ArrayList<>();

    public static boolean isInit() {
        return mInterceptorList.size() > 0 ? true : false;
    }

    public static void addCommonInterceptor(Interceptor commonInterceptor) {
        mInterceptorList.add(commonInterceptor);
    }

    public static ArrayList<Interceptor> getInterceptorList(){
        return mInterceptorList;
    }

}
