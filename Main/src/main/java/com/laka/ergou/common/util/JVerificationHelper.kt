package com.laka.ergou.common.util

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.widget.RelativeLayout
import android.widget.TextView
import cn.jiguang.verifysdk.api.JVerificationInterface
import cn.jiguang.verifysdk.api.JVerifyUIConfig
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.BuildConfig
import com.laka.ergou.R
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.constant.LoginConstant

/**
 * @Author:summer
 * @Date:2019/6/20
 * @Description:
 */
class JVerificationHelper {

    companion object {
        const val VERIFICATION_LOGIN_SUCCESS: Int = 6000  //授权成功
        const val VERIFICATION_LOGIN_CANCEL: Int = 6002  //取消获取loginToken
        const val GETTOKEN_SUCCESS: Int = 2000  //获取认证token成功
        const val PRE_LOGIN_SUCCESS: Int = 7000  //获取一键登录预取号成功
        const val TIME_INTERVAL: Long = 60 * 60 * 1000 //时间间隔
        var times_into_page_count: Long = if (BuildConfig.DEBUG) 2000 else 3 //进入一键登录限制次数
        //用户连续取消一键登录的次数
        var sCancelTimes: Int = 0
        //第一次进入一键登录页面时的时间
        var sFirstIntoTime: Long = 0

    }

    fun getToken(context: Activity, callBack: ((code: Int, content: String, operator: String) -> Unit), failCallBack: ((msg: String) -> Unit)) {
        JVerificationInterface.getToken(context, 10000) { code, _, _ ->
            if (code == GETTOKEN_SUCCESS) {
                preLogin(context, callBack, failCallBack)
            } else {
                failCallBack.invoke("获取认证token失败，code=$code")
                LoginModuleNavigator.startPhoneInputActivity(context, LoginConstant.LOGIN_TYPE_PHONE, "")
            }
        }
    }

    private fun preLogin(context: Activity, callBack: ((code: Int, content: String, operator: String) -> Unit), failCallBack: ((msg: String) -> Unit)) {
        //缓存预取号
        JVerificationInterface.preLogin(context, 10000) { code, content ->
            if (code == PRE_LOGIN_SUCCESS) {
                verificationLogin(context, callBack, failCallBack)
            } else {
                failCallBack.invoke("获取缓存预取号失败，code=$code")
                LoginModuleNavigator.startPhoneInputActivity(context, LoginConstant.LOGIN_TYPE_PHONE, "")
            }
        }
    }

    private fun verificationLogin(context: Activity, callBack: ((code: Int, content: String, operator: String) -> Unit), failCallBack: ((msg: String) -> Unit)) {
        val verifyEnable = JVerificationInterface.checkVerifyEnable(context)
        if (!verifyEnable) {
            //当前环境不支持认证，则直接跳转手机号登录
            failCallBack.invoke("当前网络环境不支持认证")
            LoginModuleNavigator.startPhoneInputActivity(context, LoginConstant.LOGIN_TYPE_PHONE, "")
            return
        }

        //记录进入一键登录页面的次数
        sFirstIntoTime = SPHelper.getLong(LoginConstant.TIME_FIRST_INTO_ONE_CLICK_LOGIN_PAGE, 0)
        sCancelTimes = SPHelper.getInt(LoginConstant.TIMES_CANCEL_ONE_CLICK_LOGIN_PAGE, 0)
        if (sCancelTimes >= times_into_page_count && System.currentTimeMillis() - sFirstIntoTime < TIME_INTERVAL) {
            //连续两次进入
            failCallBack.invoke("一个小时内只能进行两次该操作")
            LoginModuleNavigator.startPhoneInputActivity(context, LoginConstant.LOGIN_TYPE_PHONE, "")
            return
        } else if (sCancelTimes >= times_into_page_count && System.currentTimeMillis() - sFirstIntoTime >= TIME_INTERVAL) {
            //重新记录
            sCancelTimes = 0
            sFirstIntoTime = 0
            SPHelper.putInt(LoginConstant.TIMES_CANCEL_ONE_CLICK_LOGIN_PAGE, sCancelTimes)
            SPHelper.putLong(LoginConstant.TIME_FIRST_INTO_ONE_CLICK_LOGIN_PAGE, sFirstIntoTime)
        }
        val uiConfig = getFullScreenPortraitConfig(context)
        JVerificationInterface.setCustomUIWithConfig(uiConfig)
        JVerificationInterface.loginAuth(context) { code, content, operator ->
            if (code == VERIFICATION_LOGIN_SUCCESS) {
                callBack.invoke(code, "$content", "$operator")
            } else {
                when (code) {
                    VERIFICATION_LOGIN_CANCEL -> { //用户取消
                        ToastHelper.showToast("一键登录授权取消")
                        sCancelTimes += 1
                        SPHelper.putInt(LoginConstant.TIMES_CANCEL_ONE_CLICK_LOGIN_PAGE, sCancelTimes)
                        if (sCancelTimes == 1) {
                            sFirstIntoTime = System.currentTimeMillis()
                            SPHelper.putLong(LoginConstant.TIME_FIRST_INTO_ONE_CLICK_LOGIN_PAGE, sFirstIntoTime)
                        }
                    }
                    else -> {
                        ToastHelper.showToast("一键登录授权失败，请稍后再试或选择其他登录方式")
                    }
                }
                ToastHelper.showToast("一键登录授权取消")
                //failCallBack.invoke("一键登录操作失败----code=$code")
            }
        }
    }

    private fun initCustomView(context: Activity): TextView {
        val textView = TextView(context)
        textView.text = "其他号码登录"
        textView.setTextColor(ContextCompat.getColor(context, R.color.color_main))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        val mLayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        mLayoutParams.setMargins(0, 0, 0, ScreenUtils.dp2px(130f))
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        mLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
        textView.layoutParams = mLayoutParams
        return textView
    }

    private fun getFullScreenPortraitConfig(context: Activity): JVerifyUIConfig {
        val textView = initCustomView(context)
        val uiConfigBuilder = JVerifyUIConfig.Builder()
                .setAuthBGImgPath("main_bg")
                .setNavColor(ContextCompat.getColor(context, R.color.white))
                .setNavText("")
                .setNavTextColor(ContextCompat.getColor(context, R.color.white))
                .setNavReturnImgPath("seletor_nav_btn_back")
                .setLogoWidth(70)
                .setLogoHeight(70)
                .setLogoHidden(false)
                .setNumberColor(ContextCompat.getColor(context, R.color.color_303030))
                .setLogBtnText("本机号码一键登录")
                .setLogBtnTextColor(-0x1) //白色
                .setLogBtnImgPath("selector_bg_shape_login_main_color")
                .setAppPrivacyColor(ContextCompat.getColor(context, R.color.color_525252), ContextCompat.getColor(context, R.color.color_main))
                .setUncheckedImgPath("login_icon_notallow")
                .setSloganTextColor(ContextCompat.getColor(context, R.color.color_525252))
                .setLogoOffsetY(50)
                .setLogoImgPath("login_img_touxiang")
                .setNumFieldOffsetY(190)
                .setSloganOffsetY(220)
                .setLogBtnOffsetY(255)
                .setPrivacyOffsetY(35)
                .setCheckedImgPath("login_icon_allow")
                .addCustomView(textView, true) { ctx, view ->
                    LoginModuleNavigator.startPhoneInputActivity(context, LoginConstant.LOGIN_TYPE_PHONE, "")
                }
                .setPrivacyState(true)
                .setNavTransparent(true)
                .setPrivacyText("登录即同意《", "", "", "》并授权极光认证Demo获取本机号码")
                .setPrivacyCheckboxHidden(true)
                .setPrivacyTextCenterGravity(true)
                .setPrivacyTextSize(12)
        return uiConfigBuilder.build()
    }

}