package com.laka.ergou.mvp.user.model.responsitory

import com.laka.androidlib.util.rx.RxResponseComposer
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.bean.VerificationCodeData
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserApiConstant
import com.laka.ergou.mvp.user.contract.IUserChangePhoneContract
import io.reactivex.Observable
import org.json.JSONObject

/**
 * @Author:Rayman
 * @Date:2019/1/19
 * @Description:用户模块---更换手机号码Model实现层
 */
class UserChangePhoneModel : IUserChangePhoneContract.IUserChangePhoneModel {

    override fun getVerifyCode(phone: String): Observable<VerificationCodeData> {
        // 更换手机号验证码的type为2
        val params = HashMap<String, String?>()
        params[UserApiConstant.USER_PHONE] = phone
        params[LoginConstant.VERIFICATION_CODE_TYPE] = "2"
        return UserRetrofitHelper.instance.onGetVerificationCode(params)
                .compose(RxResponseComposer.flatResponse())
    }

    override fun changePhoneCommit(phone: String, code: String): Observable<JSONObject> {

        val params = HashMap<String, String?>()
        params[UserApiConstant.USER_PHONE] = phone
        params[UserApiConstant.VERIFY_CODE] = code
        return UserRetrofitHelper.instance.changeUserPhone(params = params)
                .compose(RxResponseComposer.flatResponse())
    }

}