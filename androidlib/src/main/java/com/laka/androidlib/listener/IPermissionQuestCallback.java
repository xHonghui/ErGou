package com.laka.androidlib.listener;

/**
 * @ClassName: IPermissionQuestCallback
 * @Description: 缺少系统权限的回调
 * @Author: chuan
 * @Date: 18/01/2018
 */

public interface IPermissionQuestCallback {
    /**
     * 需要权限
     *
     * @param permissions 所需权限列表
     */
    void questPermissions(String[] permissions);
}
