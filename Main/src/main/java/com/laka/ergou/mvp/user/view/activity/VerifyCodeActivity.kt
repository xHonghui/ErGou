package com.laka.ergou.mvp.user.view.activity

import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.constant.AppConstant
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ActivityManager
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.TextViewHelper
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.SelectorButton
import com.laka.ergou.R
import com.laka.ergou.mvp.login.model.bean.VerificationCodeData
import com.laka.ergou.mvp.login.model.bean.VerificationCodeDataBean
import com.laka.ergou.mvp.user.constant.UserConstant.CHANG_PHONE_NUMBER
import com.laka.ergou.mvp.user.contract.IUserChangePhoneContract
import com.laka.ergou.mvp.user.presenter.UserChangePhonePresenter
import kotlinx.android.synthetic.main.activity_verify_code.*
import java.util.*

/**
 * @Author:Rayman
 * @Date:2019/1/19
 * @Description:更换手机---获取验证码页面
 */
class VerifyCodeActivity : BaseMvpActivity<Objects>(), IUserChangePhoneContract.IUserChangePhoneView, View.OnClickListener {

    /**
     * description:倒计时
     **/
    private var mVerificationCodeCountDown = AppConstant.GET_VERIFY_CODE_MAX_DURATION
    private var mHandler = Handler()
    private lateinit var mCountDownRunnable: Runnable

    /**
     * description:数据设置
     **/
    private var phone: String = ""
    private var verifyCode = 0
    private lateinit var mChangePhonePresenter: IUserChangePhoneContract.IUserChangePhonePresenter

    override fun setContentView(): Int {
        return R.layout.activity_verify_code
    }

    override fun initIntent() {
        intent?.extras?.let {
            phone = it.getString(CHANG_PHONE_NUMBER)
        }
    }

    override fun initViews() {
        title_bar.setTitle(ResourceUtils.getString(R.string.change_phone_verify_code_title))
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)
        tv_verify_code_phone.text = ResourceUtils.getStringWithArgs(R.string.change_phone_verify_code_hint, phone)
    }

    override fun initData() {
        mCountDownRunnable = object : Runnable {
            override fun run() {
                if (mVerificationCodeCountDown <= 0) {
                    tv_verify_code_get_code.text = "获取验证码"
                    tv_verify_code_get_code.isEnabled = true
                    mHandler?.removeCallbacksAndMessages(null)
                    mVerificationCodeCountDown = 60
                    tv_verify_code_get_code.setTextColor(ResourceUtils.getColor(R.color.color_05a585))
                    tv_verify_code_get_code.isEnabled = true
                    return
                }

                tv_verify_code_get_code.text = "重新发送(${mVerificationCodeCountDown}s)"
                mHandler?.postDelayed(this, 1000)
                mVerificationCodeCountDown--
            }
        }
        tv_verify_code_get_code.setTextColor(ResourceUtils.getColor(R.color.color_font))
        mHandler?.post(mCountDownRunnable)
    }

    override fun initEvent() {
        setClickView<TextView>(R.id.tv_verify_code_get_code)
        setClickView<SelectorButton>(R.id.btn_verify_code)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_verify_code_get_code -> {
                // 获取验证码
                mChangePhonePresenter.getVerifyCode(phone)
            }
            R.id.btn_verify_code -> {
                // 绑定手机号验证
                mChangePhonePresenter.changePhoneCommit(phone, "${et_verify_code_input.text}")
            }
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mChangePhonePresenter = UserChangePhonePresenter()
        return mChangePhonePresenter
    }

    override fun showData(data: Objects) {

    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast(msg)
    }

    override fun onGetVerificationCodeSuccess(verificationCode: VerificationCodeData) {
        ToastHelper.showCenterToast("获取验证码成功")
        if (!TextUtils.isEmpty(phone)) {
            tv_verify_code_phone.text = ResourceUtils.getStringWithArgs(R.string.change_phone_verify_code_hint, phone)
        }
        tv_verify_code_get_code.setTextColor(ResourceUtils.getColor(R.color.color_font))
        tv_verify_code_get_code.isEnabled = false
        mHandler?.post(mCountDownRunnable)
    }

    override fun onChangePhoneSuccess() {
        ToastHelper.showCenterToast("更改成功")
        this.finish()
        ActivityManager.getInstance().finishActivity(ChangePhoneActivity::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler?.removeCallbacksAndMessages(null)
    }
}