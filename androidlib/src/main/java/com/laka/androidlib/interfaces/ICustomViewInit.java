package com.laka.androidlib.interfaces;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @ClassName: ICustomViewInit
 * @Description: 自定义控件实现的接口
 * @Author: chuan
 * @Date: 07/02/2018
 */

public interface ICustomViewInit<T> {

    /**
     * 初始化UI
     */
    void initViews(Context context, @Nullable AttributeSet attrs);

    /**
     * 初始化属性
     */
    void initProperty(Context context, AttributeSet attrs);

    /**
     * 初始化数据
     */
    void updateData(T data);
}
