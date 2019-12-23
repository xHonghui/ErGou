package com.laka.ergou.mvp.login.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.common.util.MetaDataUtils
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.contract.IWechatLoginContract
import com.laka.ergou.mvp.login.model.bean.User
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.login.model.repository.WechatAuthorModel
import com.laka.ergou.mvp.user.UserUtils.UserUtils

/**
 * @Author:summer
 * @Date:2019/3/9
 * @Description:
 */
class WechatAuthorPresenter : IWechatLoginContract.IWechatAutorPresenter {

    private lateinit var mView: IWechatLoginContract.IWechatAuthorView
    private var mModel: IWechatLoginContract.IWechatAuthorModel = WechatAuthorModel()

    override fun setView(view: IWechatLoginContract.IWechatAuthorView) {
        this.mView = view
        this.mModel.setView(view)
    }

    override fun onViewCreate() {
    }

    override fun onViewDestroy() {
        mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun onWechatAuthor(code: String, hasToken: Boolean) {
        mView.showLoading()
        val params = HashMap<String, String>()
        val registerId = JPushInterface.getRegistrationID(ApplicationUtils.getContext())
        val channel = MetaDataUtils.getMateDataForApplicationInfo("UMENG_CHANNEL")
        if (!TextUtils.isEmpty(registerId)) {
            params[LoginConstant.JPUSH_REGISTER_ID] = registerId
        }
        params[LoginConstant.WECHAT_AUTHOR_CODE] = code
        params[LoginConstant.JPUSH_REGISTER_CHANNEL] = channel
        params[LoginConstant.PLATFORM] = LoginConstant.ANDROID_PLATFROM
        mModel.onWechatAuthor(params, object : ResponseCallBack<UserBean> {
            override fun onSuccess(t: UserBean) {
                mView.dismissLoading()
                mView.onWechatAuthorSuccess(t)
            }

            override fun onFail(e: BaseException?) {
                mView.dismissLoading()
                ToastHelper.showCenterToast(e?.errorMsg)
                e?.let {
                    if (e.code == LoginConstant.CODE_WECHAT_ALREADY_BIND) {//微信已经绑定其他账号

                    }
                }
            }
        })
    }

    //手机一键登录
    override fun onLoginTokenVerify(loginToken: String) {
        mView.showLoading()
        val params = HashMap<String, String>()
        val registerId = JPushInterface.getRegistrationID(ApplicationUtils.getContext())
        val channel = MetaDataUtils.getMateDataForApplicationInfo("UMENG_CHANNEL")
        if (!TextUtils.isEmpty(registerId)) {
            params[LoginConstant.JPUSH_REGISTER_ID] = registerId
        }else{
            params[LoginConstant.JPUSH_REGISTER_ID] = "registerId"
        }
        params[LoginConstant.JPUSH_REGISTER_CHANNEL] = channel
        params[LoginConstant.KEY_LOGIN_TOKEN] = loginToken
        mModel.onLoginTokenVerify(params, object : ResponseCallBack<UserBean> {
            override fun onSuccess(t: UserBean) {
                // 登陆的时候，更新UserUtils数据
                UserUtils.updateUserInfo(UserInfoBean(t, null))
                mView.onLoginTokenVerifySuccess(t)
                mView.dismissLoading()
            }

            override fun onFail(e: BaseException?) {
                mView.dismissLoading()
                ToastHelper.showToast("${e?.errorMsg}")
            }
        })
    }
}