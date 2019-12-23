package com.laka.androidlib.interfaces;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * desc:
 * BasePopupWindow接口
 *
 * @author Rayman
 * @date 2017/11/6
 */

public interface BasePopup {

    /**
     * 主要显示的Layout
     *
     * @return
     */
    @NonNull
    @LayoutRes
    int initLayout();

    /**
     * Layout的内容ContentView（提供点击dismiss）
     *
     * @return
     */
    @IdRes
    int initContentView();

    /**
     * 执行动画的具体View（具体窗体）
     *
     * @return
     */
    @IdRes
    int initAnimationView();

    /**
     * 进入动画
     *
     * @return
     */
    @AnimRes
    int initEnterAnimation();

    /**
     * 退出动画
     *
     * @return
     */
    @AnimRes
    int initExitAnimation();
}
