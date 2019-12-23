package com.laka.ergou.mvp.order.model.respository

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.order.constant.MyOrderConstant

/**
 * @Author:summer
 * @Date:2019/1/21
 * @Description:
 */
class MyOrderRetrofixHelper {

    // 双重验证同步锁单例模式
    companion object {

        val instance: MyOrderService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            mainShopService
        }

        private var mainShopService: MyOrderService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(MyOrderConstant.ORDER_BASE_HOSTL)
                .setNetWorkInterceptor(true)
                .setTokenRequest(true)
                .setApiType(ApiType.CUSTOM_API)
                .build()
                .create(MyOrderService::class.java)
    }
}