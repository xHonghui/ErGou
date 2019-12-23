package com.laka.ergou.mvp.armsteam.model.repository

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.armsteam.constant.MyArmsLevelsConstant

/**
 * @Author:summer
 * @Date:2019/1/21
 * @Description:
 */
class MyArmsLevelsRetrofixHelper {

    // 双重验证同步锁单例模式
    companion object {

        val instance: MyArmsLevelsService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            mainShopService
        }

        private var mainShopService: MyArmsLevelsService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(MyArmsLevelsConstant.MY_LOWER_LEVELS_BASE_HOSTL)
                .setNetWorkInterceptor(true)
                .setTokenRequest(true)
                .setApiType(ApiType.CUSTOM_API)
                .build()
                .create(MyArmsLevelsService::class.java)
    }
}