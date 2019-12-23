package com.laka.androidlib.eventbus;

import android.view.View;

/**
 * @ClassName:
 * @Description:
 * @Author: chuan
 * @Date: 07/03/2018
 */
public class Event {

    // 事件名
    private String name;
    // 事件带的数据
    private Object data;
    private View transitionView;

    public Event(String name, Object data) {
        this.name = name;
        this.data = data;
    }

    public Event(String name, Object data, View transitionView) {
        this.name = name;
        this.data = data;
        this.transitionView = transitionView;
    }

    /**
     * 是否是当前事件
     */
    public boolean isEquals(String name) {
        return name.equals(this.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getData() {
        return data;
    }

    @SuppressWarnings("unchecked")
    public <R> R getGenericData(Class<R> tClass) {
        if(data == null){
            return null;
        }
        return (R) data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public View getTransitionView() {
        return transitionView;
    }

    public void setTransitionView(View transitionView) {
        this.transitionView = transitionView;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", data=" + data +
                ", transitionView=" + transitionView +
                '}';
    }
}
