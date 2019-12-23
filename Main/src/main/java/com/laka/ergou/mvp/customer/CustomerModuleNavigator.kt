package com.laka.ergou.mvp.customer

import android.content.Context
import android.os.Bundle
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.ergou.mvp.customer.constant.CustomerConstant
import com.laka.ergou.mvp.customer.view.activity.CustomerActivity
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils

/**
 * @Author:summer
 * @Date:2019/3/13
 * @Description:邀请记录
 */
object CustomerModuleNavigator {

    fun startCustomerActivity(context: Context, title: String, url: String) {
        if (loginHandle(context)) {
            val bundle = Bundle()
            bundle.putString(CustomerConstant.KEY_CUSTOMER_TITLE, title)
            bundle.putString(CustomerConstant.KEY_CUSTOMER_URL, url)
            BaseActivityNavigator.startActivity(context, CustomerActivity::class.java, bundle)
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