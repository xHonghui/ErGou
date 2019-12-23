package com.laka.ergou.common.fragmentstack;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.laka.androidlib.base.fragment.BaseFragment;


public class FragmentConfig {

    /**
     * 当前的根屏幕
     */
    public static String CurrentRootScreenName = "";
    /**
     * 当前顶部Fragment
     */
    public static BaseFragment mCurrentFragment;


    /**
     * 目前仅支持supportFragmentManager
     */
    public static android.support.v4.app.FragmentManager v4FragmentManager;

    /**
     * 每个需要使用该fragment任务栈的activity, 都需要在activity 中进行fragmentManager的初始化
     * fragmentManger是对应于所处的activity的,不同的activity使用不同的fragmentManger操作fragment.
     */
    public static void initFragmentManager(FragmentActivity activity) {
        v4FragmentManager = activity.getSupportFragmentManager();
    }

    public static void initFragmentManager(AppCompatActivity activity) {
        v4FragmentManager = activity.getSupportFragmentManager();
    }

    /**
     * 释放资源
     */
    public static void release() {
        if (mCurrentFragment != null) {
            mCurrentFragment = null;
        }
        if (v4FragmentManager != null) {
            v4FragmentManager = null;
        }
    }

}
