package com.laka.ergou.mvp.message

import android.content.Context
import com.laka.ergou.mvp.commission.CommissionModelNavigator
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.order.OrderModuleNavigator
import com.laka.ergou.mvp.user.UserModuleNavigator

/**
 * @Author:summer
 * @Date:2019/1/22
 * @Description:
 */
object MessageModuleNavigator {

    fun startLoginActivity(context: Context) {
        LoginModuleNavigator.startLoginActivity(context)
    }

    /**我的补贴*/
    fun startCommissionActivity(activity: Context) {
        CommissionModelNavigator.startCommissionActivity(activity)
    }


}