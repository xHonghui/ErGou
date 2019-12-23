package com.laka.ergou.mvp.login.contract

import android.content.Context
import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.login.model.bean.VerificationCodeDataBean
import io.reactivex.Observable

/**
 * @Author:summer
 * @Date:2018/12/19
 * @Description:登录模块Contract
 */
interface IPhoneLoginContract {

    interface ILoginView : IBaseLoadingView<UserBean> {
        fun onLoginSuccess(userBean: UserBean)
        fun onGetVerificationCodeSuccess(verificationCode: VerificationCodeDataBean)
        fun onGetVerificationCodeFrequently()
    }

    interface ILoginPresenter : IBasePresenter<ILoginView> {
        fun onLogin(context: Context, phone: String, code: String, type: Int)
        fun onGetVerificationCode(phone: String, type: Int)
    }

    interface ILoginModel : IBaseModel<ILoginView> {
        fun onLogin(params: HashMap<String, String>, callBack: ResponseCallBack<UserBean>)
        fun onGetVerificationCode(params: HashMap<String, String>): Observable<VerificationCodeDataBean>
    }
}