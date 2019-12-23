package com.laka.ergou.mvp.main.model.repository

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.main.constant.HomeApiConstant

/**
 * @Author:Rayman
 * @Date:2019/1/22
 * @Description:主页API-RetrofitHelper
 */
class HomeRetrofitHelper {

    // 双重验证同步锁单例模式
    companion object {

        private var instance: HomeService? = null
        fun newInstance(): HomeService {
            if (instance == null) {
                synchronized(HomeService::class.java) {
                    if (instance == null) {
                        instance = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                                .setRequestHost(HomeApiConstant.HOME_API_HOST)
                                .setNetWorkInterceptor(true)
                                .setTokenRequest(true)
                                .setApiType(ApiType.TAOBAO_API)
                                .build()
                                .create(HomeService::class.java)
                    }
                }
            }
            return instance!!
        }

        private var downloadInstance: HomeService? = null
        fun newDownLoadInstance(): HomeService {
            if (downloadInstance == null) {
                synchronized(HomeService::class.java) {
                    if (downloadInstance == null) {
                        downloadInstance = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                                .setRequestHost(HomeApiConstant.HOME_API_HOST)
                                .setDownloadRequest(true)
                                .setNetWorkInterceptor(true)
                                .setTokenRequest(true)
                                .setApiType(ApiType.TAOBAO_API)
                                .build()
                                .create(HomeService::class.java)
                    }
                }
            }
            return downloadInstance!!
        }


        private var productInstance: HomeService? = null
        fun newProductInstance(): HomeService {
            if (productInstance == null) {
                synchronized(HomeService::class.java) {
                    if (productInstance == null) {
                        productInstance = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                                .setRequestHost(HomeApiConstant.HOME_API_HOST)
                                .setNetWorkInterceptor(true)
                                .setTokenRequest(true)
                                .setApiType(ApiType.CUSTOM_API)
                                .build()
                                .create(HomeService::class.java)
                    }
                }
            }
            return productInstance!!
        }
    }
}