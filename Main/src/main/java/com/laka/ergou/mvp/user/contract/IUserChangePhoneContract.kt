package com.laka.ergou.mvp.user.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.mvp.login.model.bean.VerificationCodeData
import io.reactivex.Observable
import org.json.JSONObject
import java.util.*

/**
 * @Author:Rayman
 * @Date:2019/1/19
 * @Description:用户模块---更换手机号Contract
 */
interface IUserChangePhoneContract {

    interface IUserChangePhoneModel {

        /**
         * description:获取验证码
         **/
        fun getVerifyCode(phone: String): Observable<VerificationCodeData>

        /**
         * description:更换手机号
         **/
        fun changePhoneCommit(phone: String, code: String): Observable<JSONObject>
    }

    interface IUserChangePhonePresenter : IBasePresenter<IUserChangePhoneView> {

        fun getVerifyCode(phone: String)

        fun changePhoneCommit(phone: String, code: String)
    }

    interface IUserChangePhoneView : IBaseLoadingView<Objects> {

        /**
         * description:验证码回调
         **/
        fun onGetVerificationCodeSuccess(verificationCode: VerificationCodeData)

        /**
         * description:更换手机号回调
         **/
        fun onChangePhoneSuccess()
    }
}