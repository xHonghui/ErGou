package com.laka.ergou.mvp.circle.model.repository

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.circle.constant.CircleConstant
import com.laka.ergou.mvp.commission.constant.CommissionConstant

/**
 * @Author:sming
 * @Date:2019/8/8
 * @Description:
 */
class CircleRetrofixHelper {

    // 双重验证同步锁单例模式
    companion object {

        val instance: CircleService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            mainShopService
        }

        private var mainShopService: CircleService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(CircleConstant.CIRCLE_BASE_HOSTL)
                .setNetWorkInterceptor(true)
                .setTokenRequest(true)
                .setApiType(ApiType.CUSTOM_API)
                .build()
                .create(CircleService::class.java)
    }

}