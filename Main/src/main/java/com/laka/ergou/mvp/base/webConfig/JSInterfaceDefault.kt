package com.laka.ergou.mvp.base.webConfig

import android.app.Activity
import android.webkit.JavascriptInterface
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.mvp.shop.ShopDetailModuleNavigator
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserApiConstant
import org.json.JSONObject

open class JSInterfaceDefault(val activity: Activity, val callBack: JSInterfaceCallBack) {

    @JavascriptInterface
    fun loginToken(): String {
        return UserUtils.getUserToken()
    }

    @JavascriptInterface
    fun loginUserID(): String {
        return UserUtils.getUserId().toString()
    }

    @JavascriptInterface
    fun jsCallBack(jsonObjectStr: String?) {
        LogUtils.info("输出StrJson：$jsonObjectStr")
        val jsonObject = JSONObject(jsonObjectStr)
        val action = jsonObject?.getString(UserApiConstant.JS_ACTION)
        when (action) {
            UserApiConstant.JS_ACTION_POSTER -> {
                UserModuleNavigator.startInvitationPlaybillActivity(activity)
            }
            UserApiConstant.JS_ACTION_MINE_COMMISSION -> {
                UserModuleNavigator.startMyCommissionActivity(activity)
            }
            UserApiConstant.JS_ACTION_POSTER -> {
                // 跳转到海报分享页面
                UserModuleNavigator.startInvitationPlaybillActivity(activity)
            }
            UserApiConstant.JS_ACTION_DETAIL -> {//productId
                if (jsonObject.has(UserApiConstant.JS_PARAMETERS)) {
                    val parameters = jsonObject?.getString(UserApiConstant.JS_PARAMETERS)
                    var jsonParameters = JSONObject(parameters)
                    val itemId = jsonParameters.getString(UserApiConstant.JS_ITEM_ID)
                    ShopDetailModuleNavigator.startShopDetailActivity(activity, itemId)
                }
            }
            else -> {
                callBack.jsSubCallBack(jsonObjectStr)
            }
        }
    }
}