package com.laka.ergou.mvp.teamaward.model.respository

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.order.constant.MyOrderConstant
import com.laka.ergou.mvp.order.model.respository.MyOrderService
import com.laka.ergou.mvp.teamaward.constant.TearmAwardConstant

class TearmAwardRetrofixHelper{
    companion object{
        val instance:TearmAwardService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            mainShopService
        }

        private var mainShopService: TearmAwardService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(TearmAwardConstant.ORDER_BASE_HOSTL)
                .setNetWorkInterceptor(true)
                .setTokenRequest(true)
                .setApiType(ApiType.CUSTOM_API)
                .build()
                .create(TearmAwardService::class.java)
    }
}