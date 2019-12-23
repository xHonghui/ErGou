package com.laka.androidlib.mvp;

import android.support.annotation.NonNull;

/**
 * @Author: chuan
 * @Date: 14/03/2018
 * @Description: View实现的接口, D：泛型，代表DataBean的类型。P：泛型，代表Presenter的类型。
 */
public interface IBaseView<D> {

    /**
     * 显示数据，一个页面，加载完数据的回调
     *
     * @param data 数据
     */
    void showData(@NonNull D data);

    /**
     * 显示错误，比如无数据、网络错误等等。
     *
     * @param msg 错误文案提示
     */
    void showErrorMsg(String msg);

    /**
     * 登录信息失效，重新登录
     * */
//     void authorInvalid();

}
