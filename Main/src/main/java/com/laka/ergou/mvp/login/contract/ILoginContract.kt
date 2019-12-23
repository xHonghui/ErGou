package com.laka.ergou.mvp.login.contract

import android.content.Context
import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.mvp.login.model.bean.UserBean
import org.json.JSONObject

/**
 * @Author:summer
 * @Date:2019/3/9
 * @Description:微信登录/用户注册登录
 */
interface ILoginContract {

    interface ILoginView : IBaseLoadingView<UserBean> {
        fun onLoginSuccess(userBean: UserBean)
        fun onRegisterSuccess(userBean: UserBean)
        fun onGetVerificationCodeSuccess()
        fun onVerificationCodeFail(msg: String)
        fun onPhoneLoginSuccess(userBean: UserBean)
    }

    interface ILoginPresenter : IBasePresenter<ILoginView> {
        fun onWechatLogin(context: Context, code: String, tmpToken: String?, phone: String)
        fun onUserRegister(context: Context, agentCode: String?, phone: String, code: String, type: Int)
        fun onGetVerificationCode(phone: String, type: Int)
        fun onPhoneLogin(context:Context,phone: String, verificationCode: String, type: Int)
    }

    interface ILoginModel : IBaseModel<ILoginView> {
        fun onWechatLogin(params: HashMap<String, String>, callBack: ResponseCallBack<UserBean>)
        fun onUserRegister(params: HashMap<String, String>, callBack: ResponseCallBack<UserBean>)
        fun onGetVerificationCode(params: HashMap<String, String>, callBack: ResponseCallBack<JSONObject>)
        fun onPhoneLogin(params: HashMap<String, String>, callBack: ResponseCallBack<UserBean>)
    }

}