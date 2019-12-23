package com.laka.ergou.mvp.advert

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.mvp.advert.activity.AdvertDetailX5WebActivity
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.view.activity.HomeActivity
import com.laka.ergou.mvp.user.UserUtils.UserUtils

/**
 * @Author:Rayman
 * @Date:2018/12/19
 * @Description:
 */
object AdvertNavigator {

    fun startMainActivity(activity: Context) {
        BaseActivityNavigator.startActivity(activity, HomeActivity::class.java)
    }

    fun startAdvertWebActivity(activity: Context, title: String, url: String) {
        if (loginHandle(activity)/*&& handleAlibcLogin()*/) {
            val bundle = Bundle()
            bundle.putString(HomeConstant.WEB_TITLE, title)
            bundle.putString(HomeConstant.WEB_URL, url)
            BaseActivityNavigator.startActivity(activity, AdvertDetailX5WebActivity::class.java, bundle)
        }
    }

    fun startAdvertWebActivityForResult(activity: Activity, title: String, url: String, requestCode: Int) {
        if (loginHandle(activity)/*&& handleAlibcLogin()*/) {
            val bundle = Bundle()
            bundle.putString(HomeConstant.WEB_TITLE, title)
            bundle.putString(HomeConstant.WEB_URL, url)
            BaseActivityNavigator.startActivityForResult(activity, AdvertDetailX5WebActivity::class.java, bundle, requestCode)
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

    /**
     * 淘宝授权
     * */
    private fun handleAlibcLogin(): Boolean {
        if (!AlibcLogin.getInstance().isLogin) {
            //进入淘宝授权前，清空本地粘贴板内容，防止粘贴板含有淘口令从而引起淘宝弹窗搜索弹窗
            ClipBoardManagerHelper.getInstance.clearClipBoardContentForHasTkl()
            AlibcLogin.getInstance().showLogin(object : AlibcLoginCallback {
                override fun onSuccess(p0: Int, p1: String?, p2: String?) {
                    val userInfoBean = UserUtils.getUserInfoBean()
                    userInfoBean.session = AlibcLogin.getInstance().session
                    UserUtils.updateUserInfo(userInfoBean)
                    ToastHelper.showCenterToast("授权成功")
                }

                override fun onFailure(p0: Int, p1: String?) {
                    ToastHelper.showToast("授权失败 ")
                }
            })
            return false
        } else {
            return true
        }
    }
}