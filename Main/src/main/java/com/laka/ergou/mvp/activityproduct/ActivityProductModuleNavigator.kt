package com.laka.ergou.mvp.activityproduct

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.ergou.mvp.activityproduct.constant.ActivityProductConstant
import com.laka.ergou.mvp.activityproduct.view.activity.ActivityProductActivity
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils

/**
 * @Author:summer
 * @Date:2019/8/5
 * @Description:
 */
object ActivityProductModuleNavigator {

    fun startActivityProductActivity(context: Context, id: String, title: String, bigImageUrl: String) {
        if (loginHandle(context)) {
            val bundle = Bundle()
            bundle.putString(ActivityProductConstant.KEY_BANNER_URL, bigImageUrl)
            bundle.putString(ActivityProductConstant.KEY_ACTIVITY_ID, id)
            bundle.putString(ActivityProductConstant.KEY_TITLE, title)
            BaseActivityNavigator.startActivity(context, ActivityProductActivity::class.java, bundle)
        }
    }

    fun startActivityProductActivityForResult(context: Activity, id: String, title: String, bigImageUrl: String, requestCode: Int) {
        if (loginHandle(context)) {
            val bundle = Bundle()
            bundle.putString(ActivityProductConstant.KEY_BANNER_URL, bigImageUrl)
            bundle.putString(ActivityProductConstant.KEY_ACTIVITY_ID, id)
            bundle.putString(ActivityProductConstant.KEY_TITLE, title)
            BaseActivityNavigator.startActivityForResult(context, ActivityProductActivity::class.java, bundle, requestCode)
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