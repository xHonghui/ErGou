package com.laka.ergou.mvp.shopping.center.model.repository

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.shopping.center.constant.ShoppingApiConstant

/**
 * @Author:summer
 * @Date:2019/4/2
 * @Description:
 */
class ShoppingCustomRetrofitHelper {


    // 双重验证同步锁单例模式
    companion object {

        val INSTANCE: ShoppingService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            shoppingService
        }

        private var shoppingService: ShoppingService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(ShoppingApiConstant.HOME_API_HOST)
                .setTokenRequest(true)
                .setNetWorkInterceptor(true)
                .setApiType(ApiType.CUSTOM_API)
                .build()
                .create(ShoppingService::class.java)
    }
}