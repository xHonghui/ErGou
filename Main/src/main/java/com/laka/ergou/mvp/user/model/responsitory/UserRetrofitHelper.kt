package com.laka.ergou.mvp.user.model.responsitory

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.user.constant.UserApiConstant
import com.laka.ergou.mvp.user.constant.UserConstant

/**
 * @Author:summer
 * @Date:2019/1/12
 * @Description:
 */
class UserRetrofitHelper {

    // 双重验证同步锁单例模式
    companion object {

        val instance: UserService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            userService
        }

        private var userService: UserService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(UserApiConstant.SHOPPING_BASE_HOST)
                .setDownloadRequest(true)
                .setNetWorkInterceptor(true)
                .setTokenRequest(true)
                .setApiType(ApiType.TAOBAO_API)
                .build()
                .create(UserService::class.java)
    }
}