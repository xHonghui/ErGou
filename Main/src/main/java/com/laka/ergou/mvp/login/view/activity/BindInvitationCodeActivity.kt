package com.laka.ergou.mvp.login.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.KeyboardHelper
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.common.util.regex.RegexUtils
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.contract.IBindInvitationCodeConstract
import com.laka.ergou.mvp.login.model.bean.AgentSimpleInfo
import com.laka.ergou.mvp.login.presenter.BindInvitationCodePresenter
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.lqr.utils.OsUtils
import kotlinx.android.synthetic.main.activity_invotition_code.*

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:绑定邀请码
 */
class BindInvitationCodeActivity : BaseMvpActivity<AgentSimpleInfo>(), IBindInvitationCodeConstract.IBindInvitationCodeView {

    private lateinit var clipBoardListener: ClipBoardManagerHelper.ClipBoardContentChangeListener
    private lateinit var mPresenter: IBindInvitationCodeConstract.IBindInvitationCodePresenter
    private var mAgentInfo: AgentSimpleInfo? = null
    private var mLoginType = -1
    private var mCurrentInvitationCode: String = ""
    private var mHandler = Handler()

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = BindInvitationCodePresenter()
        mPresenter.setView(this)
        return mPresenter
    }

    override fun setContentView(): Int {
        return R.layout.activity_invotition_code
    }

    override fun initIntent() {
        mLoginType = intent.extras.getInt(LoginConstant.LOGIN_TYPE, -1)
    }

    override fun initViews() {
        title_bar?.setLeftIcon(R.drawable.nav_btn_back_n)?.setTitleTextColor(R.color.black)
                ?.setRightTextColor(R.color.black_alpha_35)
                ?.setRightTextSize(14)
    }

    override fun initData() {
        KeyboardHelper.openKeyBoard(this, et_invitation_code)
    }

    override fun initEvent() {
        btn_confirm_invitation_code?.setOnClickListener {
            onInvitationCodeNext()
        }
        iv_scan?.setOnClickListener {
            onScanQRCode()
        }
        sb_skip.setOnClickListener {
            onNextPage("")
        }
        et_invitation_code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val code = et_invitation_code.text.toString().trim()
                if (code.length == 6) {
                    mHandler.removeCallbacks(mRunnable)
                    mHandler.postDelayed(mRunnable, 500)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        //粘贴板监听
        initClipBoardListener()
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

    private fun initClipBoardListener() {
        clipBoardListener = object : ClipBoardManagerHelper.ClipBoardContentChangeListener {
            override fun contentChange(content: String, isCommandValid: Boolean) {
                clipBoardInvitationCode(content)
            }
        }
        ClipBoardManagerHelper.getInstance.addListener(clipBoardListener)
    }

    private fun clipBoardInvitationCode(content: String) {
        if (content.length == 6) {
            if (et_invitation_code.text.toString() != content && mCurrentInvitationCode != content) {
                et_invitation_code.setText(content)
                et_invitation_code.setSelection(et_invitation_code.text.toString().length)
                mCurrentInvitationCode = content
                //todo 绑定邀请码时，不需要清除粘贴板
                //ClipBoardManagerHelper.getInstance.clearClipBoardContent()
                //ClipBoardManagerHelper.getInstance.setPreSearchKey()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        clipBoardInvitationCode(ClipBoardManagerHelper.getInstance.getClipboardContent())
    }

    override fun onDestroy() {
        super.onDestroy()
        ClipBoardManagerHelper.getInstance.removeListener(clipBoardListener)
    }

    private var mRunnable = Runnable {
        val code = et_invitation_code.text.toString().trim()
        if (code.length == 6) {
            mPresenter.onLoadUserAgentInfo(code)
        }
    }

    /**扫描二维码*/
    private fun onScanQRCode() {
        LoginModuleNavigator.startScanQRCodeActivityForResult(this, LoginConstant.REQUEST_SCAN_QR_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LoginConstant.REQUEST_SCAN_QR_CODE && resultCode == LoginConstant.RESULT_SCAN_QR_CODE) {
            val sourceStr = data?.getStringExtra(LoginConstant.RESULT_SCAN_QR)
            sourceStr?.let {
                val invitationCode = RegexUtils.findTergetStrForRegex(sourceStr, LoginConstant.MATCHER_INVITATION_CODE_REGEX, 1)
                if (!TextUtils.isEmpty(invitationCode) && invitationCode.length == 6) {
                    et_invitation_code.setText(invitationCode)
                    et_invitation_code.setSelection(et_invitation_code.text.toString().length)
                    mPresenter.onLoadUserAgentInfo(invitationCode)
                } else {
                    ToastHelper.showCenterToast(getString(R.string.scan_current_invitation_code))
                }
            }
        }
    }


    /**输入邀请码下一步*/
    private fun onInvitationCodeNext() {
        val invitationCode = et_invitation_code?.text.toString().trim()
        if (TextUtils.isEmpty(invitationCode)) {
            ToastHelper.showCenterToast("请输入邀请码")
            return
        }
        if (invitationCode.length != 6) {
            ToastHelper.showCenterToast(getString(R.string.please_input_correct_verification_code_alert))
            return
        }
        if (mAgentInfo == null) {
            ToastHelper.showCenterToast(getString(R.string.please_input_correct_verification_code_alert))
            return
        }
        if (mAgentInfo != null && mAgentInfo?.agent_code != invitationCode) {
            ToastHelper.showCenterToast(getString(R.string.please_input_correct_verification_code_alert))
            return
        }
        if (mLoginType == LoginConstant.LOGIN_TYPE_WECAHT) {
            val localCode = UserUtils.getAgentCode()
            if (localCode != invitationCode) {
                mPresenter.onBindSuperAgent(invitationCode)//绑定邀请码
            } else {
                onBindSuperAgentFail(getString(R.string.dont_invitation_self))
            }
        } else if (mLoginType == LoginConstant.LOGIN_TYPE_REGISTER) {
            //记录邀请码并传到下一页
            onNextPage(invitationCode)
        } else if (mLoginType == LoginConstant.LOGIN_TYPE_ONE_CLICK_LOGIN) { //手机一键登录进入

        }
    }

    /**根据不同的登录类型，跳转不同的页面*/
    private fun onNextPage(invitationCode: String) {
        when (mLoginType) {
            LoginConstant.LOGIN_TYPE_WECAHT -> {//微信
                //登录app，进入主页
                LoginModuleNavigator.startHomeActivityFinish(this)
                LoginModuleNavigator.clearLoginRootActivity()
            }
            LoginConstant.LOGIN_TYPE_REGISTER -> {//注册
                LoginModuleNavigator.startPhoneInputActivityFinish(this, mLoginType, invitationCode)
            }
            LoginConstant.LOGIN_TYPE_ONE_CLICK_LOGIN -> { //手机一键登录进入

            }
        }
    }

    private fun onShowAgentInfo(info: AgentSimpleInfo) {
        cl_user_info.visibility = View.VISIBLE
        tv_error_msg.visibility = View.GONE
        GlideUtil.loadFilletImage(this, info.avatar, R.drawable.default_img, R.drawable.default_img, iv_avatar)
        tv_user_name.text = info.nickname
    }

    //===================================== V 层接口实现 =========================================
    override fun showData(data: AgentSimpleInfo) {}

    override fun showErrorMsg(msg: String?) {}

    override fun onBindSuperAgentSuccess() {
        onNextPage("")
    }

    override fun onBindSuperAgentFail(msg: String) {
        cl_user_info.visibility = View.GONE
        tv_error_msg.visibility = View.VISIBLE
        tv_error_msg.text = msg
    }

    override fun onLoadUserAgentInfoSuccess(agentSimpleInfo: AgentSimpleInfo) {
        mAgentInfo = agentSimpleInfo
        onShowAgentInfo(agentSimpleInfo)
    }

}