package com.laka.ergou.mvp.commission.model.bean

/**
 * @Author:summer
 * @Date:2019/1/19
 * @Description:
 */
data class CommissionDetailBean(
        val current_page: Int,
        val `data`: ArrayList<CommissionDetailData>,
        val first_page_url: String,
        val from: Int,
        val last_page: Int,
        val last_page_url: String,
        val next_page_url: String,
        val path: String,
        val per_page: Int,
        val prev_page_url: Any,
        val to: Int,
        val total: Int
)

data class CommissionDetailData(
        val id: Int,
        val user_id: Int,
        val phone: String,
        val to_wxid: String,
        val robot_wxid: String,
        val wx_hongbao_id: String,
        val type_id: Int,
        val send_status: Int,
        val receive_status: Int,
        val send_time: Long,
        val receive_time: Long,
        val create_time: Long,
        val money: String,
        val withdraw_status: Int,
        val system_send_time: Long,
        val alipay_realname: String,
        val alipay_username: String,
        val title: String

)