package com.laka.ergou.mvp.commission.model.repository

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.commission.constant.CommissionConstant

/**
 * @Author:summer
 * @Date:2019/1/18
 * @Description:
 */
class CommissionRetrofixHelper {

    // 双重验证同步锁单例模式
    companion object {

        val instance: CommissionService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            mainShopService
        }

        private var mainShopService: CommissionService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(CommissionConstant.COMMISSON_BASE_HOSTL)
                .setNetWorkInterceptor(true)
                .setTokenRequest(true)
                .setApiType(ApiType.CUSTOM_API)
                .build()
                .create(CommissionService::class.java)
    }

}