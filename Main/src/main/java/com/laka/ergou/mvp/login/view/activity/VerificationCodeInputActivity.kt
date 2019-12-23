package com.laka.ergou.mvp.login.view.activity

import android.os.Handler
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.KeyboardHelper
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.contract.ILoginContract
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.login.presenter.LoginPresenter
import com.laka.ergou.mvp.user.constant.UserConstant
import com.lqr.utils.OsUtils
import kotlinx.android.synthetic.main.activity_verification_code.*

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:验证码页面
 */
class VerificationCodeInputActivity : BaseMvpActivity<UserBean>(), ILoginContract.ILoginView {

    private var mLoginType = -1 //登录类型
    private var mTmpToken: String? = "" //微信授权获取的tmp_token
    private var mPhoneNumber: String = ""//手机号
    private var mInvitationCode: String? = "" //邀请码（注册进来时，才会在绑定手机前有邀请码）
    private lateinit var mPresenter: ILoginContract.ILoginPresenter
    private var mCountDownMillisecond = 60
    private var mHandler = Handler()
    private var mRunnable = object : Runnable {
        override fun run() {
            if (mCountDownMillisecond <= 0) {
                mCountDownMillisecond = 60
                tv_get_verification_code.text = "重新获取验证码"
                tv_get_verification_code.isEnabled = true
                tv_get_verification_code.setTextColor(ContextCompat.getColor(this@VerificationCodeInputActivity, R.color.color_main))
                mHandler.removeCallbacks(this)
                return
            }
            mCountDownMillisecond--
            tv_get_verification_code.text = "重新获取（${mCountDownMillisecond}s）"
            tv_get_verification_code.setTextColor(ContextCompat.getColor(this@VerificationCodeInputActivity, R.color.color_aaaaaa))
            mHandler.postDelayed(this, 1000)
        }
    }

    override fun finish() {
        super.finish()
        mHandler.removeCallbacks(mRunnable)
    }

    override fun showErrorMsg(msg: String?) {
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = LoginPresenter()
        return mPresenter
    }

    override fun setContentView(): Int {
        return R.layout.activity_verification_code
    }

    override fun initIntent() {
        mLoginType = intent.extras.getInt(LoginConstant.LOGIN_TYPE, -1)
        mTmpToken = intent.extras.getString(LoginConstant.TMP_TOKEN)
        mPhoneNumber = intent.extras.getString(LoginConstant.PHONE)
        mInvitationCode = intent.extras.getString(LoginConstant.INVITATION_CODE)
    }

    override fun initViews() {
        tv_get_verification_code.text = "重新获取（${mCountDownMillisecond}s）"
        tv_get_verification_code.isEnabled = false
        val content = "二购已将验证码已发送至手机 $mPhoneNumber"
        tv_alert_msg.text = content
        mHandler.postDelayed(mRunnable, 1000)
    }

    override fun initData() {
        KeyboardHelper.openKeyBoard(this, et_verify.currEditText)
    }

    override fun initEvent() {
        cl_back.setOnClickListener { finish() }
        btn_confirm_verification_code.setOnClickListener { onSureVerificationCode() }
        tv_get_verification_code.setOnClickListener {
            when (mLoginType) {
                LoginConstant.LOGIN_TYPE_WECAHT -> {
                    mPresenter.onGetVerificationCode(mPhoneNumber, LoginConstant.VERIFICATION_TYPE_WECHAT_LOGIN)
                }
                LoginConstant.LOGIN_TYPE_REGISTER -> {
                    mPresenter.onGetVerificationCode(mPhoneNumber, LoginConstant.VERIFICATION_TYPE_REGISTER)
                }
                LoginConstant.LOGIN_TYPE_PHONE -> { //普通手机登录
                    mPresenter.onGetVerificationCode(mPhoneNumber, LoginConstant.VERIFICATION_TYPE_LOGIN)
                }
            }
        }
        et_verify.setInputCompleteListener { _, content ->
            if (!TextUtils.isEmpty(content)) {
                onSureVerificationCode()
            }
        }
        initKeyBoardListener()
    }

    private fun initKeyBoardListener() {
        //监听键盘，获取键盘高度
        cl_root_view.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            LogUtils.info("left:$left，top:$top，right:$right，bottom:$bottom，oldLeft:$oldLeft，oldTop:$oldTop，oldRight:$oldRight，oldBottom:$oldBottom")
            if (oldBottom != -1 && oldBottom > bottom) {
                LogUtils.info("登录页面键盘弹起")
                OsUtils.getKeyboardHeight(this)
            }
        }
    }

    private fun onSureVerificationCode() {
        val verificationCode = et_verify.content
        if (TextUtils.isEmpty(verificationCode)) {
            ToastHelper.showCenterToast("请输入验证码")
            return
        }
        if (verificationCode.length != 4 && verificationCode.length != 6) {
            ToastHelper.showCenterToast("请输入正确的验证码")
            return
        }
        when (mLoginType) {
            LoginConstant.LOGIN_TYPE_WECAHT -> {
                LogUtils.info("tmpToken:$mTmpToken  mPhoneNumber:$mPhoneNumber")
                mPresenter.onWechatLogin(this, verificationCode, mTmpToken, mPhoneNumber)
            }
            LoginConstant.LOGIN_TYPE_REGISTER -> mPresenter.onUserRegister(this, mInvitationCode, mPhoneNumber, verificationCode, LoginConstant.LOGIN_TYPE_REGIS)
            LoginConstant.LOGIN_TYPE_PHONE -> mPresenter.onPhoneLogin(this, mPhoneNumber, verificationCode, LoginConstant.LOGIN_TYPE_PHONE_LOGIN)
        }
    }

    private fun onNextPage(userBean: UserBean) {
        //发送event，通知UserInfoActivity更新数据
        EventBusManager.postEvent(UserEvent(UserConstant.LOGIN_EVENT))
        when (mLoginType) {
            LoginConstant.LOGIN_TYPE_REGISTER -> {//注册
                //LoginModuleNavigator.startHomeActivityFinish(this)
                finish()
            }
            LoginConstant.LOGIN_TYPE_WECAHT -> {//微信授权登录
                if (userBean.can_fill_agent) {//未绑定邀请码，且登录未超过24小时
                    LoginModuleNavigator.startInvitationCodeActivityFinish(this, mLoginType)
                } else {//登录成功
                    //LoginModuleNavigator.startHomeActivityFinish(this)
                    finish()
                }
            }
            LoginConstant.LOGIN_TYPE_PHONE -> { //手机登录
                finish()
            }
        }
        //清除登录 activity
        LoginModuleNavigator.clearLoginRootActivity()
    }


    //======================================== V层接口实现 ========================================
    override fun showData(data: UserBean) {

    }

    override fun onLoginSuccess(userBean: UserBean) {
        onNextPage(userBean)
    }

    override fun onRegisterSuccess(userBean: UserBean) {
        onNextPage(userBean)
    }

    override fun onGetVerificationCodeSuccess() {
        ToastHelper.showCenterToast("获取验证码成功")
        tv_get_verification_code.text = "重新获取（${mCountDownMillisecond}s）"
        tv_get_verification_code.isEnabled = false
        tv_get_verification_code.setTextColor(ContextCompat.getColor(this@VerificationCodeInputActivity, R.color.color_aaaaaa))
        mHandler.postDelayed(mRunnable, 1000)
    }

    override fun onVerificationCodeFail(msg: String) {
        tv_verification_alert.visibility = View.VISIBLE
        tv_verification_alert.text = msg
    }

    override fun onPhoneLoginSuccess(userBean: UserBean) {
        onNextPage(userBean)
    }
}