package com.laka.ergou.mvp.login.view.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.NestedScrollView
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.KeyboardHelper
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.TextViewHelper
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.contract.IPhoneLoginContract
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.login.model.bean.VerificationCodeDataBean
import com.laka.ergou.mvp.login.presenter.PhoneLoginPresenter
import com.laka.ergou.mvp.user.constant.UserConstant
import com.lqr.utils.OsUtils

/**
 * @Author:summer
 * @Date:2019/1/7
 * @Description:手机登录
 */
class PhoneLoginFragment : BaseLazyLoadFragment(), IPhoneLoginContract.ILoginView {

    private lateinit var mFlBack: FrameLayout
    private lateinit var mIvBack: ImageView
    private lateinit var mEditTextPhone: EditText
    private lateinit var mEditTextVerificationCode: EditText
    private lateinit var mTextViewGetVerificationCode: TextView
    private lateinit var mTextViewPhoneLogin: TextView
    private lateinit var mRootView: NestedScrollView
    private val mPhoneLoginPresenter: IPhoneLoginContract.ILoginPresenter = PhoneLoginPresenter()
    private var mVerificationCodeCountDown = 60
    private var mPreGetVerificationCodeTime: Long = 0L
    private var mPreLoginClickTime: Long = 0L
    private var mTimeInterval = 1000

    override fun setContentView(): Int {
        return R.layout.fragment_phone_login
    }

    override fun initArgumentsData(arguments: Bundle?) {
    }

    override fun createPresenter(): IBasePresenter<*> {
        return mPhoneLoginPresenter
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        activity.lifecycle.addObserver(mPhoneLoginPresenter)
        mPhoneLoginPresenter.setView(this)
        mRootView = findViewById(R.id.sv_root_view)
        mFlBack = findViewById(R.id.fl_back)
        mIvBack = findViewById(R.id.iv_back)
        mEditTextPhone = findViewById(R.id.edit_text_phone)
        mEditTextVerificationCode = findViewById(R.id.edit_text_verification_code)
        mTextViewGetVerificationCode = findViewById(R.id.text_view_get_verification_code)
        mTextViewPhoneLogin = findViewById(R.id.text_view_phone_login)
    }

    override fun initDataLazy() {
        KeyboardHelper.openKeyBoard(activity, mEditTextPhone)
    }

    override fun initEvent() {
        mFlBack.setOnClickListener {
            activity.finish()
        }
        mTextViewGetVerificationCode.setOnClickListener {
            if (System.currentTimeMillis() - mPreGetVerificationCodeTime < mTimeInterval) {
                return@setOnClickListener
            }
            mPreGetVerificationCodeTime = System.currentTimeMillis()
            var phone = mEditTextPhone.text.trim()
            if (TextUtils.isEmpty(phone)) {
                ToastHelper.showToast("请输入手机号码！")
                return@setOnClickListener
            }
            mEditTextVerificationCode.requestFocus()
            mPhoneLoginPresenter?.onGetVerificationCode(phone.toString(), LoginConstant.VERIFICATION_TYPE_LOGIN)
        }
        mTextViewPhoneLogin.setOnClickListener {
            if (System.currentTimeMillis() - mPreLoginClickTime < mTimeInterval) {
                return@setOnClickListener
            }
            mPreLoginClickTime = System.currentTimeMillis()
            if (TextUtils.isEmpty(mEditTextPhone.text.toString())) {
                ToastHelper.showCenterToast("请输入手机号")
                return@setOnClickListener
            }
            if (mEditTextPhone.text.toString().length != 11) {
                ToastHelper.showCenterToast("请输入正确的手机号")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditTextVerificationCode.text)) {
                ToastHelper.showCenterToast("请输入验证码")
                return@setOnClickListener
            }
            onLogin()
        }
        //监听键盘，获取键盘高度
        mRootView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            LogUtils.info("left:$left，top:$top，right:$right，bottom:$bottom，oldLeft:$oldLeft，oldTop:$oldTop，oldRight:$oldRight，oldBottom:$oldBottom")
            if (oldBottom != -1 && oldBottom > bottom) {
                LogUtils.info("登录页面键盘弹起")
                OsUtils.getKeyboardHeight(activity)
                mHandler?.post({
                    TextViewHelper.recordFocus(mEditTextPhone, mEditTextVerificationCode)
                    //滑动到底部，会将焦点放在可见区域的底部组件上，如果没有任何组件可以获取光标，则光标被回收
                    mRootView.fullScroll(NestedScrollView.FOCUS_DOWN)
                    TextViewHelper.recoveryFocus(mEditTextPhone, mEditTextVerificationCode)
                })
            }
        }
    }

    private fun onLogin() {
        val phone = mEditTextPhone.text.toString()
        val code = mEditTextVerificationCode.text.toString()
        mPhoneLoginPresenter?.onLogin(activity, phone, code, LoginConstant.LOGIN_TYPE_PHONE_LOGIN)
    }

    override fun onLoginSuccess(userBean: UserBean) {
        ToastHelper.showCenterToast("登录成功")
        if (activity != null && !activity.isFinishing && !activity.isDestroyed) {
            // 先隐藏软键盘
            KeyboardHelper.hideKeyBoard(activity, mEditTextPhone)
            EventBusManager.postEvent(UserEvent(UserConstant.LOGIN_EVENT))
            mHandler.postDelayed({
                //LoginModuleNavigator.startHomeActivity(activity)
                //LoginModuleNavigator.clearLoginRootActivity()

                LoginModuleNavigator.clearLoginRootActivity()
                activity.finish()
            }, 100)
        }
    }

    override fun onGetVerificationCodeSuccess(verificationCode: VerificationCodeDataBean) {
        ToastHelper.showCenterToast("获取验证码成功")
        if (verificationCode.code == 0) {
            mHandler?.post(mRunnable)
            mTextViewGetVerificationCode.isEnabled = false
        }
    }

    private val mHandler = Handler()
    private val mRunnable = object : Runnable {
        override fun run() {
            if (mVerificationCodeCountDown <= 0) {
                mTextViewGetVerificationCode.text = "获取验证码"
                mTextViewGetVerificationCode.isEnabled = true
                mHandler?.removeCallbacksAndMessages(null)
                mVerificationCodeCountDown = 60
                return
            }
            mTextViewGetVerificationCode.text = mVerificationCodeCountDown.toString() + "s"
            mHandler?.postDelayed(this, 1000)
            mVerificationCodeCountDown--
        }
    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun showData(data: UserBean) {

    }

    override fun onGetVerificationCodeFrequently() {

    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showCenterToast(msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler?.removeCallbacksAndMessages(null)
    }
}