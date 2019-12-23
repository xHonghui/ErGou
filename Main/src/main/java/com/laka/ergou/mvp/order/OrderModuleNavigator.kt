package com.laka.ergou.mvp.order

import android.content.Context
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.order.view.activity.MyAllOrderActivity
import com.laka.ergou.mvp.user.UserUtils.UserUtils

/**
 * @Author:summer
 * @Date:2019/1/22
 * @Description:
 */
object OrderModuleNavigator {

    fun startOrderActivity(context: Context) {
        if (loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, MyAllOrderActivity::class.java)
        }
    }

    private fun loginHandle(context: Context): Boolean {
        return if (!UserUtils.isLogin()) {
            LoginModuleNavigator.startLoginActivity(context)
            false
        } else {
            true
        }
    }

}