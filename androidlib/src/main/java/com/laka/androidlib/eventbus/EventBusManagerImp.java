package com.laka.androidlib.eventbus;

import android.view.View;

import org.greenrobot.eventbus.EventBus;

/**
 * @Author:Rayman
 * @Date:2018/12/17
 * @Description: 事件管理的实现处
 */

public class EventBusManagerImp implements IEventBusManager {

    @Override
    public void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    @Override
    public void unRegister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    @Override
    public void postEvent(String name) {
        postEvent(name, null);
    }

    @Override
    public void postEvent(Object o) {
        EventBus.getDefault().post(o);
    }

    @Override
    public void postSticky(String name) {
        postSticky(name, null);
    }

    @Override
    public void postEvent(String name, Object data) {
        EventBus.getDefault().post(new Event(name, data));
    }

    @Override
    public void postEvent(String name, Object data, View transitionView) {
        EventBus.getDefault().post(new Event(name, data, transitionView));
    }

    @Override
    public Event postSticky(String name, Object data) {
        Event event = new Event(name, data);
        EventBus.getDefault().postSticky(event);
        return event;
    }

    @Override
    public void removeStickyEvent(Event event) {
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void removeStickyEvent(String name) {
        //根据当前的类型去判断移除
        Event event = EventBus.getDefault().getStickyEvent(Event.class);
        if (event.isEquals(name)) {
            EventBus.getDefault().removeStickyEvent(event);
        }
    }

}
