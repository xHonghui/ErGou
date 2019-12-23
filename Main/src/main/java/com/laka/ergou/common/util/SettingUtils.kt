package com.laka.ergou.common.util

import com.laka.androidlib.util.SPHelper
import com.laka.ergou.mvp.constant.MainConstant

/**
 * @Author:Rayman
 * @Date:2019/2/27
 * @Description:项目设置工具类 .
 * 例如设置一些App信息
 */
object SettingUtils {

    /**
     * description:设置是否开启购小二
     **/
    fun updateOpenGouXiaoEr(isOpen: Boolean) {
        SPHelper.putBoolean(MainConstant.IS_OPEN_GO_XIAO_ER, isOpen)
    }

    /**
     * description:是否开启购小二
     **/
    fun isOpenGouXiaoEr(): Boolean {
        val isOpen = SPHelper.getBoolean(MainConstant.IS_OPEN_GO_XIAO_ER, false)
        return isOpen
    }
}