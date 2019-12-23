package com.laka.ergou.mvp.login


import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.laka.androidlib.base.activity.BaseActivity
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.PackageUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.view.activity.*
import com.laka.ergou.mvp.main.view.activity.HomeActivity
import com.laka.ergou.mvp.user.UserUtils.UserUtils

/**
 * @Author:Rayman
 * @Date:2018/12/19
 * @Description:HOME模块页面跳转类
 */
object LoginModuleNavigator {

    fun startTaoBaoAuthorActivity(activity: Context) {
        if (PackageUtils.isAppInstalled(activity, PackageUtils.TAO_BAO)) {
            BaseActivityNavigator.startActivity(activity, TaobaoAuthorActivity::class.java)
        } else {
            ToastHelper.showToast("请安装手机淘宝")
        }
    }

    fun startLoginActivity(activity: Context) {
        BaseActivityNavigator.startActivity(activity, LoginActivity::class.java)
    }

    fun startLoginActivityForResult(activity: Activity,requestCode: Int){
        BaseActivityNavigator.startActivityForResult(activity,LoginActivity::class.java,null,requestCode)
    }

    /**清除当前任务栈的所有activity,并创建新的任务栈打开activity*/
    fun startHomeActivityClearTask(activity: Activity) {
        BaseActivityNavigator.startActivityClearTask(activity, HomeActivity::class.java)
    }

    fun startHomeActivity(activity: Activity) {
        BaseActivityNavigator.startActivity(activity, HomeActivity::class.java)
    }

    fun startHomeActivityFinish(activity: Activity) {
        BaseActivityNavigator.startActivityFinish(activity, HomeActivity::class.java, null)
    }

    /**进入邀请码页面*/
    fun startInvitationCodeActivityFinish(activity: Activity, loginType: Int) {
        BaseActivityNavigator.startActivityFinish(activity, BindInvitationCodeActivity::class.java, getLoginParams(loginType))
    }

    fun startInvitationCodeActivity(activity: Activity, loginType: Int) {
        BaseActivityNavigator.startActivity(activity, BindInvitationCodeActivity::class.java, getLoginParams(loginType))
    }

    /**进入手机号绑定页面*/
    fun startPhoneInputActivityFinish(activity: Activity, loginType: Int, invitationCode: String) {
        val bundle = Bundle()
        bundle.putInt(LoginConstant.LOGIN_TYPE, loginType)
        bundle.putString(LoginConstant.INVITATION_CODE, invitationCode)//注册才会有
        BaseActivityNavigator.startActivityFinish(activity, PhoneInputActivity::class.java, bundle)
    }

    fun startPhoneInputActivity(activity: Activity, loginType: Int, tmpToken: String) {
        val bundle = Bundle()
        bundle.putInt(LoginConstant.LOGIN_TYPE, loginType)
        bundle.putString(LoginConstant.TMP_TOKEN, tmpToken)//微信登录才会有
        BaseActivityNavigator.startActivity(activity, PhoneInputActivity::class.java, bundle)
    }

    /**进入验证码输入页面*/
    fun startVerificationCodeInputActivityFinish(activity: Activity, bundle: Bundle) {
        BaseActivityNavigator.startActivityFinish(activity, VerificationCodeInputActivity::class.java, bundle)
    }

    private fun getLoginParams(loginType: Int): Bundle {
        val bundle = Bundle()
        bundle.putInt(LoginConstant.LOGIN_TYPE, loginType)
        return bundle
    }

    fun startScanQRCodeActivityForResult(activity: Activity, requestCode: Int) {
        BaseActivityNavigator.startActivityForResult(activity, ScanQRCodeActivity::class.java, requestCode)
    }

    /**手机登录和淘宝授权*/
    fun startPhoneLoginActivity(activity: LoginActivity) {
        BaseActivityNavigator.startActivity(activity, PhoneLoginActivity::class.java)
    }

    fun loginHandle(context: Context): Boolean {
        return if (!UserUtils.isLogin()) {
            startLoginActivity(context)
            false
        } else {
            true
        }
    }


    //========================== 保存登录入口activity，用于后续回退栈清除 ==========================
    private var mLoginChoiceActivity: BaseActivity? = null

    fun bindLoginRootActivity(activity: BaseActivity) {
        mLoginChoiceActivity = activity
    }

    fun clearLoginRootActivity() {
        mLoginChoiceActivity?.let {
            if (!mLoginChoiceActivity?.isFinishing!! && !mLoginChoiceActivity?.isDestroyed!!) {
                mLoginChoiceActivity?.finish()
            }
        }
        mLoginChoiceActivity = null
    }

}