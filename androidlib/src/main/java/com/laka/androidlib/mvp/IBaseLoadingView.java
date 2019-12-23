package com.laka.androidlib.mvp;

/**
 * @Author:Rayman
 * @Date:2018/6/20
 * @Description:带LoadingView
 */

public interface IBaseLoadingView<D> extends IBaseView<D> {

    /**
     * 显示加载框
     */
    void showLoading();

    /**
     * 隐藏加载框
     */
    void dismissLoading();
}
