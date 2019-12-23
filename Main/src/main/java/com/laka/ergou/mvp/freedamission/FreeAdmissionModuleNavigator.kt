package com.laka.ergou.mvp.freedamission

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.ergou.mvp.freedamission.constant.FreeAdmissionContant
import com.laka.ergou.mvp.freedamission.view.activity.FreeAdmissionActivity
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils

/**
 * @Author:summer
 * @Date:2019/3/13
 * @Description:邀请记录
 */
object FreeAdmissionModuleNavigator {

    fun startFreeAdmissionActivity(context: Context, title: String, bigImageUrl: String) {
        if (loginHandle(context)) {
            val bundle = Bundle()
            bundle.putString(FreeAdmissionContant.KEY_FREE_ADMISSION_BANNER_URL, bigImageUrl)
            bundle.putString(FreeAdmissionContant.KEY_TITLE_FREE_ADMISSION, title)
            BaseActivityNavigator.startActivity(context, FreeAdmissionActivity::class.java, bundle)
        }
    }

    fun startFreeAdmissionActivityForResult(context: Activity, title: String, bigImageUrl: String, requestCode: Int) {
        if (loginHandle(context)) {
            val bundle = Bundle()
            bundle.putString(FreeAdmissionContant.KEY_FREE_ADMISSION_BANNER_URL, bigImageUrl)
            bundle.putString(FreeAdmissionContant.KEY_TITLE_FREE_ADMISSION, title)
            BaseActivityNavigator.startActivityForResult(context, FreeAdmissionActivity::class.java, bundle, requestCode)
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