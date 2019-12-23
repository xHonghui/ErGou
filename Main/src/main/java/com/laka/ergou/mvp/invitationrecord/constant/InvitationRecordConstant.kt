package com.laka.ergou.mvp.invitationrecord.constant

import com.laka.ergou.BuildConfig
import java.util.*

/**
 * @Author:summer
 * @Date:2019/3/13
 * @Description:
 */
object InvitationRecordConstant {
    private const val TITLE_INVITATION_ACTIVATION: String = "已激活"
    private const val TITLE_INVITATION_NOT_ACTIVE: String = "未激活"
    val INVITATION_RECORD_PAGE_TITLE: ArrayList<String> = arrayListOf(TITLE_INVITATION_NOT_ACTIVE, TITLE_INVITATION_ACTIVATION)
    /*邀请记录*/
    const val LOAD_INVITATION_RECORD_URL: String = "user/invite"
    const val INVITATION_RECORD_ROOT_URL: String = BuildConfig.ERGOU_BASE_HOST
    const val INVITATION_RECORD_TYPE: String = "invitation_record_type"
    const val INVITATION_NOT_ACTIVE: Int = 12 // 未激活
    const val INVITATION_ACTIVED: Int = 3 //已激活
    const val PAGE_NUMBER: String = "page"
    const val PAGE_SIZE_KEY: String = "pageSize"
    const val PAGE_SIZE: String = "20"
    const val INVITATION_TYPE: String = "type"
    const val TOKEN: String = "token"
    const val SOURCE_TYPE: String = "source_type" //来源
    const val APP_TYPE: Int = 1
    const val WECHAT_TYPE: Int = 2
}