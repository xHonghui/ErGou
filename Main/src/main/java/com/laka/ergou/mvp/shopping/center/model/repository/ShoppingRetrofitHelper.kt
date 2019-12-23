package com.laka.ergou.mvp.shopping.center.model.repository

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.shopping.center.constant.ShoppingApiConstant

/**
 * @Author:Rayman
 * @Date:2018/12/18
 * @Description:商品网络请求Retrofit，主要是初始化HOST，Service设置等 .
 */
class ShoppingRetrofitHelper {


    // 双重验证同步锁单例模式
    companion object {

        val INSTANCE: ShoppingService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            shoppingService
        }

        private var shoppingService: ShoppingService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(ShoppingApiConstant.HOME_API_HOST)
                .setTokenRequest(true)
                .setNetWorkInterceptor(true)
                .setApiType(ApiType.TAOBAO_API)
                .build()
                .create(ShoppingService::class.java)
    }
}