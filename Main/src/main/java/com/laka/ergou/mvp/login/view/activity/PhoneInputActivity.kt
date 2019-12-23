package com.laka.ergou.mvp.login.view.activity

import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.StatusBarUtil
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.contract.IPhoneLoginContract
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.login.model.bean.VerificationCodeDataBean
import com.laka.ergou.mvp.login.presenter.PhoneLoginPresenter
import kotlinx.android.synthetic.main.activity_phone_input.*

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:手机号输入
 */
class PhoneInputActivity : BaseMvpActivity<UserBean>(), IPhoneLoginContract.ILoginView {

    private var mLoginType = -1 //登录类型
    private var tmpToken: String? = "" //微信授权的tmpToken
    private var mPhoneNumber: String = ""//获取验证码的手机号码
    private var mInvitationCode: String? = "" //邀请码，注册进入才会有
    private lateinit var mPresenter: IPhoneLoginContract.ILoginPresenter

    override fun createPresenter(): IBasePresenter<*>? {
        mPresenter = PhoneLoginPresenter()
        return mPresenter
    }

    override fun setContentView(): Int {
        return R.layout.activity_phone_input
    }

    override fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.color_theme_bg), 0)
            StatusBarUtil.setLightModeNotFullScreen(this, true)
        } else {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, color), 0)
        }
    }

    override fun initIntent() {
        mLoginType = intent.extras.getInt(LoginConstant.LOGIN_TYPE, -1)
        mInvitationCode = intent.extras.getString(LoginConstant.INVITATION_CODE, "")
        tmpToken = intent.extras.getString(LoginConstant.TMP_TOKEN, "")
    }

    override fun initViews() {

    }

    override fun initData() {
    }

    override fun initEvent() {
        btn_confirm_phone_code.setOnClickListener { onGetVerificationCode() }
        btn_confirm_phone_code.isEnabled = false
        iv_back.setOnClickListener { finish() }
        et_phone_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(et_phone_input.text.toString())) {
                    iv_txt_remove.visibility = View.GONE
                } else {
                    iv_txt_remove.visibility = View.VISIBLE
                }
                if (et_phone_input.text.toString().length == 11) {
                    btn_confirm_phone_code.isEnabled = true
                    btn_confirm_phone_code.setBackGroundColor(R.color.color_main)
                } else {
                    btn_confirm_phone_code.isEnabled = false
                }
                LogUtils.info("phone_input_activity--------isEnable:${btn_confirm_phone_code.isEnabled}----length=${et_phone_input.text.toString().length}")
            }
        })
        iv_txt_remove.setOnClickListener {
            et_phone_input.setText("")
        }
    }

    /**获取验证码*/
    private fun onGetVerificationCode() {
        mPhoneNumber = et_phone_input?.text.toString().trim()
        if (TextUtils.isEmpty(mPhoneNumber)) {
            ToastHelper.showCenterToast("请输入手机号码")
            return
        }
        if (mPhoneNumber.length != 11) {
            ToastHelper.showCenterToast("请输入正确的手机号")
            return
        }
        when (mLoginType) {
            LoginConstant.LOGIN_TYPE_WECAHT -> {
                mPresenter.onGetVerificationCode(mPhoneNumber, LoginConstant.VERIFICATION_TYPE_WECHAT_LOGIN)
            }
            LoginConstant.LOGIN_TYPE_REGISTER -> {
                mPresenter.onGetVerificationCode(mPhoneNumber, LoginConstant.VERIFICATION_TYPE_REGISTER)
            }
            LoginConstant.LOGIN_TYPE_PHONE -> {
                mPresenter.onGetVerificationCode(mPhoneNumber, LoginConstant.VERIFICATION_TYPE_LOGIN)
            }
        }
    }

    //===================================== V层接口实现 =========================================
    override fun onLoginSuccess(userBean: UserBean) {
    }

    override fun onGetVerificationCodeSuccess(verificationCode: VerificationCodeDataBean) {
        ToastHelper.showCenterToast("获取验证码成功")
        val bundle = Bundle()
        bundle.putInt(LoginConstant.LOGIN_TYPE, mLoginType)
        bundle.putString(LoginConstant.PHONE, mPhoneNumber)
        bundle.putString(LoginConstant.TMP_TOKEN, "$tmpToken")
        bundle.putString(LoginConstant.INVITATION_CODE, "$mInvitationCode")
        LogUtils.info("tmpToken:$tmpToken  mPhoneNumber:$mPhoneNumber")
        LoginModuleNavigator.startVerificationCodeInputActivityFinish(this, bundle)
    }

    //获取验证码太频繁
    override fun onGetVerificationCodeFrequently() {
        val bundle = Bundle()
        bundle.putInt(LoginConstant.LOGIN_TYPE, mLoginType)
        bundle.putString(LoginConstant.PHONE, mPhoneNumber)
        bundle.putString(LoginConstant.TMP_TOKEN, "$tmpToken")
        bundle.putString(LoginConstant.INVITATION_CODE, "$mInvitationCode")
        LogUtils.info("tmpToken:$tmpToken  mPhoneNumber:$mPhoneNumber")
        LoginModuleNavigator.startVerificationCodeInputActivityFinish(this, bundle)
    }

    override fun showData(data: UserBean) {

    }

    override fun showErrorMsg(msg: String?) {

    }

}