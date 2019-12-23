package com.laka.ergou.mvp.circle

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.ergou.mvp.advert.AdvertNavigator
import com.laka.ergou.mvp.advert.activity.AdvertDetailX5WebActivity
import com.laka.ergou.mvp.circle.constant.CircleConstant
import com.laka.ergou.mvp.circle.view.activity.CircleHelperActivity
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.user.UserUtils.UserUtils

object CircleNavigator {

    fun startCircleHelperActivity(activity: Context, title: String, url: String) {
        if (loginHandle(activity)) {
            val bundle = Bundle()
            bundle.putString(HomeConstant.WEB_TITLE, title)
            bundle.putString(HomeConstant.WEB_URL, url)
            BaseActivityNavigator.startActivity(activity, CircleHelperActivity::class.java, bundle)
        }
    }

    fun startCircleHelperActivityForResult(activity: Activity, title: String, url: String, type: Int = 0, requestCode: Int) {
        if (loginHandle(activity)) {
            val bundle = Bundle()
            bundle.putString(HomeConstant.WEB_TITLE, title)
            bundle.putString(HomeConstant.WEB_URL, url)
            bundle.putInt(CircleConstant.TYPE_LOGIN, type)
            BaseActivityNavigator.startActivityForResult(activity, CircleHelperActivity::class.java, bundle, requestCode)
        }
    }

    fun loginHandle(context: Context): Boolean {
        return if (!UserUtils.isLogin()) {
            LoginModuleNavigator.startLoginActivity(context)
            false
        } else {
            true
        }
    }
}