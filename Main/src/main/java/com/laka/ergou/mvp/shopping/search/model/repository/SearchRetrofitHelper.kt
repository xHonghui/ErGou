package com.laka.ergou.mvp.shopping.search.model.repository

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.shopping.center.constant.ShoppingApiConstant
import com.laka.ergou.mvp.shopping.center.model.repository.ShoppingService

/**
 * @Author:Rayman
 * @Date:2019/1/30
 * @Description:商品搜索模块Retrofit帮助类
 */
class SearchRetrofitHelper {

    // 双重验证同步锁单例模式
    companion object {

        val INSTANCE: ShoppingSearchService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            shoppingService
        }

        private var shoppingService: ShoppingSearchService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(ShoppingApiConstant.HOME_API_HOST)
                .setTokenRequest(true)
                .setNetWorkInterceptor(true)
                .setApiType(ApiType.TAOBAO_API)
                .build()
                .create(ShoppingSearchService::class.java)
    }
}