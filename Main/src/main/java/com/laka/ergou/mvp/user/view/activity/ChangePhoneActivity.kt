package com.laka.ergou.mvp.user.view.activity

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.StringUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.SelectorButton
import com.laka.ergou.R
import com.laka.ergou.mvp.login.model.bean.VerificationCodeData
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.contract.IUserChangePhoneContract
import com.laka.ergou.mvp.user.presenter.UserChangePhonePresenter
import kotlinx.android.synthetic.main.activity_change_phone.*
import java.util.*

/**
 * @Author:Rayman
 * @Date:2019/1/19
 * @Description:更换手机号页面
 */
class ChangePhoneActivity : BaseMvpActivity<Objects>(), IUserChangePhoneContract.IUserChangePhoneView, View.OnClickListener {

    private var textWatcher = InputTextWatcher()

    /**
     * description:数据设置
     **/
    private var userPhone: String? = ""
    private lateinit var mChangePhonePresenter: IUserChangePhoneContract.IUserChangePhonePresenter

    override fun setContentView(): Int {
        return R.layout.activity_change_phone
    }

    override fun initIntent() {

    }

    override fun initViews() {
        title_bar.setTitle(ResourceUtils.getString(R.string.change_phone_title))
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)
    }

    override fun initData() {
        userPhone = UserUtils?.getUserPhone()!!
    }

    override fun initEvent() {
        setClickView<ImageView>(R.id.iv_change_phone_delete)
        setClickView<SelectorButton>(R.id.btn_change_phone)
        et_change_phone_input.addTextChangedListener(textWatcher)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_change_phone_delete -> {
                et_change_phone_input.setText("")
                cl_change_phone_error_hint.visibility = View.GONE
            }
            R.id.btn_change_phone -> {
                val text = et_change_phone_input.text.toString()
                if (!StringUtils.isPhoneNumber(text)) {
                    cl_change_phone_error_hint.visibility = View.VISIBLE
                } else {
                    if (TextUtils.equals(userPhone, et_change_phone_input.text.toString())) {
                        ToastHelper.showToast(ResourceUtils.getString(R.string.change_phone_same_error_hint))
                    } else {
                        mChangePhonePresenter.getVerifyCode(text)
                    }
                }
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
        // 跳转到验证码验证页面
        UserModuleNavigator.startChangePhoneVerifyCodeActivity(this, et_change_phone_input.text.toString())
    }

    override fun onChangePhoneSuccess() {
        // 当前页面不需要使用
    }

    inner class InputTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!TextUtils.isEmpty(et_change_phone_input?.text)) {
                if (iv_change_phone_delete.visibility == View.GONE) {
                    iv_change_phone_delete.visibility = View.VISIBLE
                }
                if (cl_change_phone_error_hint.visibility == View.VISIBLE) {
                    cl_change_phone_error_hint.visibility = View.GONE
                }
            } else {
                iv_change_phone_delete.visibility = View.GONE
            }
        }
    }
}