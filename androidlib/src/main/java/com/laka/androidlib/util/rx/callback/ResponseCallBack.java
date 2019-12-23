package com.laka.androidlib.util.rx.callback;

import android.support.annotation.NonNull;

import com.laka.androidlib.util.rx.exception.BaseException;

/**
 * @Author:summer
 * @Date:2019/1/17
 * @Description:网络请求统一回调接口
 */
public interface ResponseCallBack<T> {
    void onSuccess(@NonNull T t);

    void onFail(BaseException e);
}
