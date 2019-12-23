package com.laka.ergou.mvp.activityproduct.model.respository

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.activityproduct.constant.ActivityProductConstant

/**
 * @Author:summer
 * @Date:2019/1/21
 * @Description:
 */
class ActivityProductRetrofixHelper {

    // 双重验证同步锁单例模式
    companion object {

        val instance: ActivityProductService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            mainShopService
        }

        private var mainShopService: ActivityProductService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(ActivityProductConstant.ACTIVITY_ROOT_URL)
                .setNetWorkInterceptor(true)
                .setTokenRequest(true)
                .setApiType(ApiType.CUSTOM_API)
                .build()
                .create(ActivityProductService::class.java)
    }
}