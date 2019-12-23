package com.laka.ergou.mvp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.google.gson.Gson
import com.laka.androidlib.net.utils.parse.ParseUtil
import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.main.util.RouterParamsConverter
import org.json.JSONObject

/**
 * @Author:Rayman
 * @Date:2019/3/14
 * @Description:极光推送Receiver
 */
class JPushReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val bundle = it.extras
            it.action?.let {
                when (it) {
                    JPushInterface.ACTION_REGISTRATION_ID -> {

                    }
                    JPushInterface.ACTION_CONNECTION_CHANGE -> {

                    }
                    JPushInterface.ACTION_MESSAGE_RECEIVED -> {

                    }

                    JPushInterface.ACTION_NOTIFICATION_RECEIVED -> {

                    }
                    JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION -> {
                    }
                    JPushInterface.ACTION_NOTIFICATION_OPENED -> {
                        val title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE)
                        val alertMsg = bundle.getString(JPushInterface.EXTRA_ALERT)
                        val extraData = bundle.getString(JPushInterface.EXTRA_EXTRA)
                        LogUtils.error("打开通知:$extraData+$title+$alertMsg")
                        LogUtils.error("打开通知：${intent.extras}")
                        LogUtils.error("打开通知intent:$intent")

                        extraData.let {
                            LogUtils.error("打开通知:$extraData")
                            handleRouter(extraData)
                        }
                    }
                    JPushInterface.ACTION_RICHPUSH_CALLBACK -> {

                    }
                }
            }
        }
    }

    private fun handleRouter(jsonStr: String) {
        LogUtils.info("输出Json：$jsonStr")
        // 跳转交由Router处理
        try {
            val pushData = parseJson(jsonStr)
            var target = ""
            pushData?.let {
                var paramsMap= it.scene_extra ?: HashMap()
                target = RouterNavigator.bannerRouterReflectMap[it.scene_id.toInt()].toString()
                paramsMap[HomeNavigatorConstant.ROUTER_VALUE] = it.scene_value
                LogUtils.info("jpush----------target=$target")
                LogUtils.info("jpush----------paramsMap=$paramsMap")
                RouterNavigator.handleAppInternalNavigator(ApplicationUtils.getApplication(), target, paramsMap)
            }
        } catch (e: Exception) {
            LogUtils.info("jpush----------exception=${e.message}")
        }
    }

    private fun parseJson(json: String): PushReceiverBean {
        val jsonObject = JSONObject(json)
        val pushReceiverBean = PushReceiverBean()
        try {
            if (jsonObject.has("scene_id") && jsonObject.has("scene_value")) {
                pushReceiverBean.scene_id = jsonObject.getString("scene_id")
                pushReceiverBean.scene_value = jsonObject.getString("scene_value")
                if (jsonObject.has("scene_extra")) {
                    val sceneExtraJSONObject = jsonObject.getJSONObject("scene_extra")
                    val keyIterator = sceneExtraJSONObject.keys()
                    if (keyIterator.hasNext()) {
                        val key = keyIterator.next()
                        pushReceiverBean.scene_extra[key] = sceneExtraJSONObject.getString(key)
                    }
                }
            }
            LogUtils.info("hashMap--------------:$pushReceiverBean")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        LogUtils.info("hashMap--------------:$pushReceiverBean")
        return pushReceiverBean
    }

}