package com.laka.ergou.mvp.advertbanner.model.respositroy

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.BuildConfig
import com.laka.ergou.mvp.advertbanner.constant.AdvertBannerConstant

/**
 * @Author:summer
 * @Date:2019/7/27
 * @Description:
 */
class AdvertBannerRetrofitHelper {

    // 双重验证同步锁单例模式
    companion object {

        val instance: AdvertBannerService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            advertBannerService
        }

        private var advertBannerService: AdvertBannerService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(AdvertBannerConstant.API_BASE_HOST)
                .setNetWorkInterceptor(true)
                .setTokenRequest(true)
                .setApiType(ApiType.CUSTOM_API)
                .build()
                .create(AdvertBannerService::class.java)
    }
}