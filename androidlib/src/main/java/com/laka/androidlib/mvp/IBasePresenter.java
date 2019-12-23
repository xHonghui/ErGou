package com.laka.androidlib.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;


/**
 * @Author:Rayman
 * @Date:2018/12/17
 * @Description:Presenter的接口 .
 * 默认实现AndroidX LifeCycle组件
 **/
public interface IBasePresenter<T> extends LifecycleObserver {

    /**
     * 设置View
     *
     * @param view
     */
    void setView(@NonNull T view);

    /**
     * View层OnCreate事件回调
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onViewCreate();

    /**
     * View层onDestroy事件回调
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onViewDestroy();

    /**
     * 生命周期切换时候回调
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onLifeCycleChange(@NonNull LifecycleOwner owner,
                           @NonNull Lifecycle.Event event);

}

