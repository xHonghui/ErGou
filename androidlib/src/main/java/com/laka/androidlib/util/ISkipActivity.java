package com.laka.androidlib.util;

import android.app.Activity;
import android.os.Bundle;

/**
 * @Author:Rayman
 * @Date:2018/6/6
 * @Description:页面跳转协议
 */

public interface ISkipActivity {

    /**
     * Activity跳转
     *
     * @param cls
     * @param bundle
     */
    void startActivity(Class<?> cls, Bundle bundle);

    /**
     * 跳转FragmentActivity，默认传递page
     *
     * @param cls
     * @param page
     * @param bundle
     */
    void startFragmentActivity(Class<?> cls, int page, Bundle bundle);

    /**
     * 跳转页面，并且关闭当前页面
     *
     * @param cls
     * @param bundle
     */
    void startActivityFinish(Class<?> cls, Bundle bundle);

    /**
     * 跳转到Web页面，默认传递url
     *
     * @param cls
     * @param url
     * @param bundle
     */
    void startWebActivity(Class<?> cls, String url, Bundle bundle);

    /**
     * Activity跳转并返回result
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode);
}
