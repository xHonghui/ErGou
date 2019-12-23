package com.laka.ergou.mvp.invitationrecord.model.repository

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.invitationrecord.constant.InvitationRecordConstant

/**
 * @Author:summer
 * @Date:2019/1/21
 * @Description:
 */
class InvitationRecordRetrofixHelper {

    // 双重验证同步锁单例模式
    companion object {

        val instance: InvitationRecordService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            mainShopService
        }

        private var mainShopService: InvitationRecordService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(InvitationRecordConstant.INVITATION_RECORD_ROOT_URL)
                .setNetWorkInterceptor(true)
                .setTokenRequest(true)
                .setApiType(ApiType.CUSTOM_API)
                .build()
                .create(InvitationRecordService::class.java)
    }
}