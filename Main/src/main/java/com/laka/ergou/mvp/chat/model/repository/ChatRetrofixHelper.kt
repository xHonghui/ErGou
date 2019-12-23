package com.laka.ergou.mvp.chat.model.repository

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.chat.constant.ChatConstant

/**
 * @Author:summer
 * @Date:2019/1/18
 * @Description:
 */
class ChatRetrofixHelper {

    // 双重验证同步锁单例模式
    companion object {

        val instance: ChatService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            mainShopService
        }

        private var mainShopService: ChatService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(ChatConstant.CHAT_BASE_HOST)
                .setNetWorkInterceptor(true)
                .setTokenRequest(true)
                .setApiType(ApiType.CUSTOM_API)
                .build()
                .create(ChatService::class.java)
    }

}