package com.laka.androidlib.eventbus;

import android.view.View;

/**
 * @Author Lyf
 * @CreateTime 2018/3/7
 * @Description 代理事件管理
 **/
public class EventBusManager {

    private static IEventBusManager sEventBusManager;

    public static void initEventBusManager(IEventBusManager iEventBusManager) {
        sEventBusManager = iEventBusManager;
    }

    /**
     * 注册
     */
    public static void register(Object subscriber) {
        sEventBusManager.register(subscriber);
    }

    /**
     * 反注册
     */
    public static void unRegister(Object subscriber) {
        sEventBusManager.unRegister(subscriber);
    }

    /**
     * 发送事件
     */
    public static void postEvent(String name, Object data) {
        sEventBusManager.postEvent(name, data);
    }

    /**
     * 发送带动画事件
     */
    public static void postEvent(String name, Object data, View transitionView) {
        sEventBusManager.postEvent(name, data, transitionView);
    }

    /**
     * 发送延时事件
     *
     * @return 返回当前延时事件
     */
    public static Event postStickyEvent(String name, Object data) {
        return sEventBusManager.postSticky(name, data);
    }

    /**
     * 移除延时事件
     *
     * @param event 移除的事件
     */
    public static void removeStickyEvent(Event event) {
        sEventBusManager.removeStickyEvent(event);
    }

    /**
     * @param name
     */
    public static void removeStickyEvent(String name) {
        sEventBusManager.removeStickyEvent(name);
    }


    /**
     * 发送事件
     */
    public static void postEvent(String name) {
        sEventBusManager.postEvent(name, null);
    }

    /**
     * 发送对象事件
     *
     * @param t
     * @param <T>
     */
    public static <T> void postEvent(T t) {
        sEventBusManager.postEvent(t);
    }

    /**
     * 发送延时事件
     */
    public static Event postStickyEvent(String name) {
        return sEventBusManager.postSticky(name, null);
    }

}
