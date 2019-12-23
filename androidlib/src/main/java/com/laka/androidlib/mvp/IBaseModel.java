package com.laka.androidlib.mvp;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * @Author:summer
 * @Date:2019/1/18
 * @Description:
 */

public interface IBaseModel<V> {

    void setView(@NonNull V v);

    void onViewDestory();
}