package com.laka.ergou.mvp.login.view.activity

import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.eventbus.Event
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.*
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.BuildConfig
import com.laka.ergou.R
import com.laka.ergou.common.util.JVerificationHelper
import com.laka.ergou.common.util.SpannableStringUtils
import com.laka.ergou.common.util.share.WxHelper
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.contract.IWechatLoginContract
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.login.presenter.WechatAuthorPresenter
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import kotlinx.android.synthetic.main.activity_login_choice.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Author:summer
 * @Date:2019/3/6
 * @Description:登录页面（注册/微信登录/手机登录）
 */
class LoginActivity : BaseMvpActivity<UserBean>(), IWechatLoginContract.IWechatAuthorView, View.OnClickListener {

    private lateinit var mPresenter: IWechatLoginContract.IWechatAutorPresenter
    private lateinit var mJverficationHelper: JVerificationHelper

    override fun setContentView(): Int {
        return R.layout.activity_login_choice
    }

    override fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setTranslucentForImageView(this, 0, iv_back)
            StatusBarUtil.setLightMode(this)
        }
    }

    override fun initIntent() {
        onClearUserInfo()
    }

    private fun onClearUserInfo() {
        //SPHelper.clearByFileName(LoginConstant.USER_INFO_FILENAME)
        //UserUtils.clearUserInfo()
        //EventBusManager.postEvent(UserEvent(UserConstant.LOGOUT_EVENT))
        //EventBusManager.postEvent(UserEvent(UserConstant.EDIT_USER_INFO))
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = WechatAuthorPresenter()
        mPresenter.setView(this)
        return mPresenter
    }

    override fun initViews() {
        tv_normal_login.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        tv_normal_login.paint.isAntiAlias = true
        tv_user_agreement.movementMethod = LinkMovementMethod.getInstance()
        val userAgreementTet = tv_user_agreement.text.toString().trim()
        val start = userAgreementTet.indexOf("《")
        val end = userAgreementTet.lastIndexOf("》") + 1
        val spahnUserArgument = SpannableStringUtils.makeColorSpannableString(start, end, R.color.color_login_blue, userAgreementTet)
        tv_user_agreement.text = spahnUserArgument
        LoginModuleNavigator.bindLoginRootActivity(this)

        //TODO 屏幕高度适配
        val height = ScreenUtils.getScreenHeight()
        val width = ScreenUtils.getScreenWidth()
        if (BigDecimalUtils.divi(height.toString(), width.toString()).toDouble() >= 2) {
            val layoutParams: ConstraintLayout.LayoutParams = tv_normal_login.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.bottomMargin = ScreenUtils.dp2px(80F)
            tv_normal_login.layoutParams = layoutParams
            iv_login_logo.visibility = View.GONE

            //适配背景图片
            val ivbgHeight = width * 1.2
            val ivBgLayoutParams = iv_bg.layoutParams
            ivBgLayoutParams.width = width
            ivBgLayoutParams.height = ivbgHeight.toInt()
            iv_bg.layoutParams = ivBgLayoutParams
            //加载长图
            Glide.with(this)
                    .load(R.drawable.login_bg_height)
                    .asBitmap()
                    .format(DecodeFormat.PREFER_RGB_565)
                    .placeholder(R.drawable.default_img)
                    .into(iv_bg)
        } else {
            //加载普通背景图
//            Glide.with(this)
//                    .load(R.drawable.login_bg)
//                    .asBitmap()
//                    .format(DecodeFormat.PREFER_RGB_565)
//                    .placeholder(R.drawable.default_img)
//                    .into(iv_bg)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LoginModuleNavigator.clearLoginRootActivity()
    }

    override fun initData() {

    }

    override fun initEvent() {
        iv_back.setOnClickListener(this)
        sb_wechat_login.setOnClickListener(this)
        sb_phone_login.setOnClickListener(this)
        tv_normal_login.setOnClickListener(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: Event) {
        when (event?.name) {
            LoginConstant.EVENT_LOGIN_WX -> {
                showLoading()
                sb_wechat_login.postDelayed({
                    dismissLoading()
                    val response = event.data as? SendAuth.Resp
                    if (response?.errCode == BaseResp.ErrCode.ERR_OK) {
                        LogUtils.info("code:" + response.code)
                        mPresenter.onWechatAuthor("${response?.code}", false)
                    } else {
                        ToastHelper.showCenterToast("微信授权失败")
                    }
                }, 500)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> finish()
            R.id.sb_wechat_login -> {//微信登录
                onWechatLogin()
            }
            R.id.sb_phone_login -> { //和v1.2.0 之前的登录流程相同，不需要修改
                //LoginModuleNavigator.startPhoneInputActivity(this, LoginConstant.LOGIN_TYPE_PHONE, "")
                showLoading()
                if (!::mJverficationHelper.isInitialized) {
                    mJverficationHelper = JVerificationHelper()
                }
                mJverficationHelper.getToken(this, { code, content, operator ->
                    if (code == JVerificationHelper.VERIFICATION_LOGIN_SUCCESS) {
                        mPresenter.onLoginTokenVerify(content)
                    }
                }, { msg ->
                    dismissLoading()
                    if (!TextUtils.isEmpty(msg) && BuildConfig.DEBUG) {
                        //ToastHelper.showToast(msg)
                    }
                })
            }
            R.id.tv_normal_login -> {//注册
                LoginModuleNavigator.startInvitationCodeActivity(this, LoginConstant.LOGIN_TYPE_REGISTER)
                //测试
                //onTest()
            }
        }
    }

    /**
     * 测试*/
    private fun onTest() {
        val bundle = Bundle()
        bundle.putInt(LoginConstant.LOGIN_TYPE, LoginConstant.LOGIN_TYPE_REGISTER)
        bundle.putString(LoginConstant.PHONE, "13570594365")
        LoginModuleNavigator.startVerificationCodeInputActivityFinish(this, bundle)
    }

    private fun onWechatLogin() {
        //是否存在微信
        if (PackageUtils.isAppInstalled(this, PackageUtils.WEI_XIN)) {
            showLoading()
            sb_wechat_login.postDelayed({
                dismissLoading()
                LogUtils.info("wechat login!")
                WxHelper(this@LoginActivity).onWechatAuthor()
            }, 500)
        } else {
            ToastHelper.showCenterToast(getString(R.string.alert_no_wechat_msg))
        }
    }

    override fun finish() {
        KeyboardHelper.hideKeyBoard(this, cl_root_view)
        super.finish()
    }

    //====================================== V层接口实现 ========================================
    //授权成功
    override fun onWechatAuthorSuccess(result: UserBean) {
        if (result.is_first == 1) {//首次使用该微信登录
            LoginModuleNavigator.startPhoneInputActivity(this, LoginConstant.LOGIN_TYPE_WECAHT, result.tmp_token)
        } else {
            //不是首次授权
            UserUtils?.updateUserInfo(UserInfoBean(result, null))
            EventBusManager.postEvent(UserEvent(UserConstant.LOGIN_EVENT))
            if (result.can_fill_agent) {//未绑定邀请码，且登录未超过24小时
                LoginModuleNavigator.startInvitationCodeActivityFinish(this, LoginConstant.LOGIN_TYPE_WECAHT)
            } else {
                ToastHelper.showCenterToast("登录成功")
                finish()
            }
        }
    }

    //一键登录loginToken认证成功
    override fun onLoginTokenVerifySuccess(result: UserBean) {
        ToastHelper.showToast("一键登录成功")
        finish()
    }

    override fun showData(data: UserBean) {

    }

    override fun showErrorMsg(msg: String?) {

    }


}