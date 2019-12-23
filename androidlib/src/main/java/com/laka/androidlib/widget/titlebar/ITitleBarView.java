package com.laka.androidlib.widget.titlebar;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author:Rayman
 * @Date:2019/1/8
 * @Description: 标题栏的协议，以后重写复用，就照这协议来。
 */

public interface ITitleBarView {

    int POSITION_LEFT = 1;
    int POSITION_RIGHT = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POSITION_LEFT, POSITION_RIGHT})
    @interface TITLE_ICON_POSITION {

    }

    /**
     * 设置控件的背景颜色
     *
     * @param resColor
     * @return
     */
    ITitleBarView setBackGroundColor(int resColor);

    /**
     * 设置标题文字
     *
     * @param title
     */
    ITitleBarView setTitle(String title);

    /**
     * 设置标题文字大小
     *
     * @param titleTextSize
     * @return
     */
    ITitleBarView setTitleTextSize(int titleTextSize);

    /**
     * 设置标题文字颜色
     *
     * @param resColor 资源ID
     * @return
     */
    ITitleBarView setTitleTextColor(int resColor);


    /**
     * 设置左侧文字显示，文字和图片状态是对立的
     *
     * @param text
     */
    ITitleBarView setLeftText(String text);

    /**
     * 设置左侧文字大小
     *
     * @param textSize
     * @return
     */
    ITitleBarView setLeftTextSize(int textSize);

    /**
     * 设置左侧文字颜色
     *
     * @param resColor 资源文件
     * @return
     */
    ITitleBarView setLeftTextColor(int resColor);


    /**
     * 设置左侧Icon
     *
     * @param resId
     */
    ITitleBarView setLeftIcon(int resId);

    /**
     * 设置左侧显示Icon
     *
     * @param drawable
     * @return
     */
    ITitleBarView setLeftIcon(Drawable drawable);

    /**
     * 假若同时存在左侧文字与Icon，设置两者的显示顺序
     *
     * @param position
     * @return
     */
    ITitleBarView setLeftIconPosition(@TITLE_ICON_POSITION int position);

    /**
     * 假若同时存在左侧文字与Icon，设置两者之间的Margin
     *
     * @param margin
     * @return
     */
    ITitleBarView setLeftCompoundMargin(int margin);

    /**
     * 设置左侧Layout margin
     *
     * @param marginLeft
     * @param marginTop
     * @param marginRight
     * @param marginBottom
     * @return
     */
    ITitleBarView setLeftMargin(int marginLeft, int marginTop, int marginRight, int marginBottom);

    /**
     * 设置，标题栏左侧文字可见性
     *
     * @param visibility
     */
    void setLeftTextVisibility(int visibility);

    /**
     * 设置，标题栏左边的图片的可见性
     *
     * @param visibility
     */
    void setLeftIconVisibility(int visibility);

    /**
     * 标题栏左边的图片的点击事件
     */
    ITitleBarView setOnLeftClickListener(View.OnClickListener onLeftIconClickListener);

    /**
     * 设置右侧文字显示，文字和图片状态是对立的
     *
     * @param text
     */
    ITitleBarView setRightText(String text);

    /**
     * 设置右侧文字大小
     *
     * @param textSize
     * @return
     */
    ITitleBarView setRightTextSize(int textSize);

    /**
     * 设置右侧文字颜色
     *
     * @param resColor 资源文件
     * @return
     */
    ITitleBarView setRightTextColor(int resColor);

    /**
     * 设置右侧Icon
     *
     * @param resId
     */
    ITitleBarView setRightIcon(int resId);

    /**
     * 设置右侧显示Icon
     *
     * @param drawable
     * @return
     */
    ITitleBarView setRightIcon(Drawable drawable);

    /**
     * 假若同时存在右侧文字与Icon，设置两者的显示顺序
     *
     * @param position
     * @return
     */
    ITitleBarView setRightIconPosition(@TITLE_ICON_POSITION int position);

    /**
     * 假若同时存在右侧文字与Icon，设置两者之间的Margin
     *
     * @param margin
     * @return
     */
    ITitleBarView setRightCompoundMargin(int margin);

    /**
     * 设置左侧Layout margin
     *
     * @param marginLeft
     * @param marginTop
     * @param marginRight
     * @param marginBottom
     * @return
     */
    ITitleBarView setRightMargin(int marginLeft, int marginTop, int marginRight, int marginBottom);

    /**
     * 设置，标题栏右侧文字的可见性
     *
     * @param visibility
     */
    void setRightTextVisibility(int visibility);

    /**
     * 设置，标题栏右边的图片的可见性
     *
     * @param visibility
     */
    void setRightIconVisibility(int visibility);

    /**
     * 标题栏右边的图片的点击事件
     */
    ITitleBarView setOnRightClickListener(View.OnClickListener onRightIconClickListener);

    /**
     * 是否展示分割线
     *
     * @param isShow
     */
    ITitleBarView showDivider(boolean isShow);

    /**
     * 获取左侧View
     *
     * @return
     */
    View getLeftView();

    /**
     * 获取右侧View
     *
     * @return
     */
    View getRightView();
}