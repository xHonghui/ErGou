package com.laka.ergou.mvp.message

import android.content.Context
import android.text.TextUtils
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.main.util.RouterParamsConverter
import com.laka.ergou.mvp.message.constant.MessageConstant
import com.laka.ergou.mvp.message.model.bean.Msg
import org.json.JSONObject

/**
 * @Author:summer
 * @Date:2019/3/15
 * @Description:消息路由
 */
object MessageModuleRouter {

    /**补贴消息路由*/
    fun startCommissionMessageRouter(activity: Context, msgItem: Msg) {
        MessageModuleNavigator.startCommissionActivity(activity)
    }

    /***/
    fun startOtherMessageRouter(activity: Context, otherMsg: Msg) {
        //其他消息里面的  其他消息 无点击响应
        if (otherMsg.scene_id == MessageConstant.SCENE_OTHER_MESSAGE) {
            return
        }
        var params = HashMap<String, String>()
        if (!TextUtils.isEmpty(otherMsg.scene_extra)) {
            params = RouterParamsConverter.convertParamsToMap(JSONObject(otherMsg.scene_extra))
        }
        params[HomeNavigatorConstant.ROUTER_VALUE] = otherMsg.scene_value
        val target = RouterNavigator.bannerRouterReflectMap[otherMsg.scene_id].toString()
        RouterNavigator.handleAppInternalNavigator(activity, target, params)
    }

}