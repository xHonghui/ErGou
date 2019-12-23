package com.laka.ergou.mvp.test.model.responsitory

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitCustomHelper
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant

/**
 * @Author:summer
 * @Date:2018/12/20
 * @Description:
 */
class TestRetrofitHelper {


    // 双重验证同步锁单例模式
    companion object {

        val instance: TestService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            mainShopService
        }

        private var mainShopService: TestService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(ShopDetailConstant.SHOPPING_BASE_HOST)
                .setNetWorkInterceptor(true)
                .setTokenRequest(true)
                .setApiType(ApiType.CUSTOM_API)
                .build()
                .create(TestService::class.java)
    }
}