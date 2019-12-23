package com.laka.ergou.mvp.commission

import android.content.Context
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.ergou.mvp.commission.view.activity.CommissionActivity
import com.laka.ergou.mvp.commission.view.activity.CommissionDetailActivity
import com.laka.ergou.mvp.login.LoginModuleNavigator

/**
 * @Author:summer
 * @Date:2019/1/19
 * @Description:
 */
object CommissionModelNavigator {

    fun startCommissionDetailActivity(context: Context) {
        BaseActivityNavigator.startActivity(context, CommissionDetailActivity::class.java)
    }

    fun startCommissionActivity(context: Context) {
        if (LoginModuleNavigator.loginHandle(context)) {
            BaseActivityNavigator.startActivity(context, CommissionActivity::class.java)
        }
    }

}