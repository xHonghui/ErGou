package com.laka.ergou.mvp.invitationrecord.model.bean

/**
 * @Author:summer
 * @Date:2019/3/13
 * @Description:邀请记录
 */
data class InvitationRecordResponse(
        val records: ArrayList<InvitationRecord>,
        val total: Int = 0
)

data class InvitationRecord(
        val avatar: String = "",
        val create_time: String = "",
        val earn: String = "",
        val id: String = "",
        val nickname: String = "",
        val order_id: String = "",
        val word: String = ""
)