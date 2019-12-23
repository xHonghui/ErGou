package com.laka.androidlib.eventbus;

import android.view.View;

/**
 * @Author Lyf
 * @CreateTime 2018/3/7
 * @Description 事件协议
 **/
public interface IEventBusManager<T> {
    /**
     * 注册
     */
    void register(Object subscriber);

    /**
     * 取消注册
     */
    void unRegister(Object subscriber);

    /**
     * 发送事件
     */
    void postEvent(String name);


    /**
     * 发送事件
     *
     * @param t
     */
    void postEvent(T t);

    /**
     * 发送事件
     */
    void postEvent(String name, Object data);

    /**
     * 发送带动画事件
     */
    void postEvent(String name, Object data, View transitionView);

    /**
     * 发送延时事件
     */
    void postSticky(String name);

    /**
     * 发送延时事件
     */
    Event postSticky(String name, Object data);

    /**
     * 移除延时事件
     *
     * @param event 具体事件
     */
    void removeStickyEvent(Event event);

    /**
     * 根据Name移除事件
     *
     * @param name 事件Name
     */
    void removeStickyEvent(String name);
}
