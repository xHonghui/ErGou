package com.laka.ergou.mvp.freedamission.model.respository

import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.retrofit.RetrofitHelper
import com.laka.androidlib.util.rx.constant.ApiType
import com.laka.ergou.mvp.freedamission.constant.FreeAdmissionContant
import com.laka.ergou.mvp.invitationrecord.constant.InvitationRecordConstant

/**
 * @Author:summer
 * @Date:2019/1/21
 * @Description:
 */
class FreeAdmissionRetrofixHelper {

    // 双重验证同步锁单例模式
    companion object {

        val instance: FreeAdmissionService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            mainShopService
        }

        private var mainShopService: FreeAdmissionService = RetrofitHelper.getInstance(ApplicationUtils.getApplication())
                .setRequestHost(FreeAdmissionContant.FREE_ADMISSION_ROOT_URL)
                .setNetWorkInterceptor(true)
                .setTokenRequest(true)
                .setApiType(ApiType.CUSTOM_API)
                .build()
                .create(FreeAdmissionService::class.java)
    }
}