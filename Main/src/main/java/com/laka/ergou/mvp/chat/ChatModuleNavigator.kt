package com.laka.ergou.mvp.chat

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.ergou.mvp.chat.constant.ChatConstant
import com.laka.ergou.mvp.chat.view.activity.ChatActivity
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.order.OrderModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils

/**
 * @Author:Rayman
 * @Date:2019/2/13
 * @Description:购小二模块路由类
 */
object ChatModuleNavigator {

    private fun loginHandle(activity: Context): Boolean {
        return if (!UserUtils.isLogin()) {
            LoginModuleNavigator.startLoginActivity(activity)
            false
        } else {
            true
        }
    }

    fun startChatActivity(context: Context, contactId: String, content: String = "") {
        if (loginHandle(context)) {
            val bundle = Bundle()
            bundle.putString(ChatConstant.CHAT_CONTACT_ID, contactId)
            bundle.putString(ChatConstant.CHAT_CONTENT, content)
            BaseActivityNavigator.startActivity(context, ChatActivity::class.java, bundle)
        }
    }

    fun startMyOrderActivity(activity: Activity) {
        if (loginHandle(activity)) {
            OrderModuleNavigator.startOrderActivity(activity)
        }
    }

}