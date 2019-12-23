package com.laka.ergou.mvp.login.model.repository

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.login.constant.LoginConstant

/**
 * @Author:summer
 * @Date:2019/1/23
 * @Description:使用 JsonResponseBodyConverter 转换器
 */
interface CustomLoginRetrofixHelper {

    // 双重验证同步锁单例模式
    companion object {

        val instance: PhoneLoginService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            phoneLoginService
        }

        private var phoneLoginService: PhoneLoginService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(LoginConstant.LOGIN_BASE_HOST)
                .setNetWorkInterceptor(true)
                .setTokenRequest(true)
                .setApiType(ApiType.CUSTOM_API)
                .build()
                .create(PhoneLoginService::class.java)
    }
}