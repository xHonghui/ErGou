package com.laka.androidlib.eventbus;

/**
 * @Author Lyf
 * @CreateTime 2018/4/24
 * @Description Event事件名
 **/
public interface BaseEventName {

    /**
     * 示例
     */
    String TEST = "TEST";

    /**
     * 用户变化。
     */
    String USER_CHANGED = "USER_CHANGED";

    /**
     * 登录成功
     */
    String LOGIN_SUCCESS = "LOGIN_SUCCESS";

    /**
     * 网络状态改变
     */
    String NET_WORK_CHANGED = "NET_WORK_CHANGED";

    /**
     * 软键盘状态改变
     */
    String SOFT_KEYBOARD_CHANGED = "SOFT_KEYBOARD_CHANGED";

    /**
     * 申请摄像头和存储权限
     */
    String REQUEST_TAKE_PHOTO_PERMISSION = "request_take_photo_permission";
}
