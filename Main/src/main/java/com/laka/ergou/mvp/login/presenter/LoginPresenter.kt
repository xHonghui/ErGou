package com.laka.ergou.mvp.login.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.text.TextUtils
import cn.jpush.android.api.JPushInterface
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.common.util.MetaDataUtils
import com.laka.ergou.common.util.rx.RxSubscriber
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.contract.ILoginContract
import com.laka.ergou.mvp.login.contract.IPhoneLoginContract
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.login.model.bean.VerificationCodeDataBean
import com.laka.ergou.mvp.login.model.repository.LoginModel
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import io.reactivex.disposables.Disposable
import org.json.JSONObject

/**
 * @Author:summer
 * @Date:2019/3/9
 * @Description:微信登录 、手机注册
 */
class LoginPresenter : ILoginContract.ILoginPresenter {

    private lateinit var mView: ILoginContract.ILoginView
    private var mModel: ILoginContract.ILoginModel = LoginModel()

    override fun setView(view: ILoginContract.ILoginView) {
        this.mView = view
        mModel.setView(view)
    }

    override fun onViewCreate() {
    }

    override fun onViewDestroy() {
        mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun onWechatLogin(context: Context, code: String, tmpToken: String?, phone: String) {
        mView.showLoading()
        val params = HashMap<String, String>()
        val registerId = JPushInterface.getRegistrationID(context)
        val channel = MetaDataUtils.getMateDataForApplicationInfo("UMENG_CHANNEL")
        params[LoginConstant.PLATFORM] = LoginConstant.ANDROID_PLATFROM
        params[LoginConstant.JPUSH_REGISTER_CHANNEL] = channel
        params[LoginConstant.PHONE] = "$phone"
        params[LoginConstant.CODE] = "$code"
        if (!TextUtils.isEmpty(registerId)) {
            params[LoginConstant.JPUSH_REGISTER_ID] = registerId
        }
        if (!TextUtils.isEmpty(tmpToken)) {
            params[LoginConstant.TMP_TOKEN] = "$tmpToken"
        }
        mModel.onWechatLogin(params, object : ResponseCallBack<UserBean> {
            override fun onSuccess(t: UserBean) {
                UserUtils.updateUserInfo(UserInfoBean(t, null))
                mView.dismissLoading()
                mView.onLoginSuccess(t)
            }

            override fun onFail(e: BaseException?) {
                mView.onVerificationCodeFail("${e?.errorMsg}")
                ToastHelper.showCenterToast("${e?.errorMsg}")
            }
        })
    }

    override fun onUserRegister(context: Context, agentCode: String?, phone: String, code: String, type: Int) {
        mView.showLoading()
        val params = HashMap<String, String>()
        val registerId = JPushInterface.getRegistrationID(context)
        val channel = MetaDataUtils.getMateDataForApplicationInfo("UMENG_CHANNEL")
        if (!TextUtils.isEmpty(registerId)) {
            params[LoginConstant.JPUSH_REGISTER_ID] = registerId
        }
        params[LoginConstant.PLATFORM] = LoginConstant.ANDROID_PLATFROM
        params[LoginConstant.JPUSH_REGISTER_CHANNEL] = channel
        params[LoginConstant.PHONE] = phone
        params[LoginConstant.CODE] = code
        params[LoginConstant.TYPE] = "$type"
        if (!TextUtils.isEmpty(agentCode)) {
            params[LoginConstant.AGENT_CODE] = "$agentCode"
        }
        mModel.onUserRegister(params, object : ResponseCallBack<UserBean> {
            override fun onSuccess(t: UserBean) {
                UserUtils.updateUserInfo(UserInfoBean(t, null))
                mView.dismissLoading()
                mView.onRegisterSuccess(t)
            }

            override fun onFail(e: BaseException?) {
                mView.onVerificationCodeFail("${e?.errorMsg}")
                ToastHelper.showCenterToast("${e?.errorMsg}")
            }
        })
    }

    override fun onGetVerificationCode(phone: String, type: Int) {
        val params = HashMap<String, String>()
        params[LoginConstant.PHONE] = phone
        params[LoginConstant.VERIFICATION_CODE_TYPE] = "$type"
        mModel?.onGetVerificationCode(params, object : ResponseCallBack<JSONObject> {
            override fun onSuccess(t: JSONObject) {
                mView.onGetVerificationCodeSuccess()
            }

            override fun onFail(e: BaseException?) {
                ToastHelper.showCenterToast("${e?.errorMsg}")
            }
        })
    }

    /**
     * 手机登录
     * */
    override fun onPhoneLogin(context: Context, phone: String, verificationCode: String, type: Int) {
        mView.showLoading()
        val registerId = JPushInterface.getRegistrationID(context)
        val channel = MetaDataUtils.getMateDataForApplicationInfo("UMENG_CHANNEL")
        LogUtils.info("registerId：$registerId")
        val params = HashMap<String, String>()
        params[LoginConstant.PHONE] = phone
        params[LoginConstant.CODE] = verificationCode
        params[LoginConstant.TYPE] = "$type"
        params[LoginConstant.OS_VERSION] = android.os.Build.VERSION.RELEASE
        params[LoginConstant.PLATFORM] = LoginConstant.ANDROID_PLATFROM
        params[LoginConstant.JPUSH_REGISTER_CHANNEL] = channel
        if (!TextUtils.isEmpty(registerId)) {
            params[LoginConstant.JPUSH_REGISTER_ID] = registerId
        }
        mModel.onPhoneLogin(params, object : ResponseCallBack<UserBean> {
            override fun onSuccess(t: UserBean) {
                // 登陆的时候，更新UserUtils数据
                UserUtils.updateUserInfo(UserInfoBean(t, null))
                mView.onPhoneLoginSuccess(t)
                mView.dismissLoading()
            }

            override fun onFail(e: BaseException?) {
                ToastHelper.showCenterToast("${e?.errorMsg}")
            }
        })
    }
}