package com.laka.ergou.mvp.login.model.bean

/**
 * @Author:summer
 * @Date:2019/3/9
 * @Description:
 */
data class WechatAuthorBean(
    val adzone: WechatAdzone,
    val agent_code: String,
    val alipay_realname: String,
    val alipay_username: String,
    val avatar: String,
    val bound_wx: Boolean,
    val created_time: String,
    val gender: String,
    val id: String,
    val is_first: Int,
    val nickname: String,
    val os_version: String,
    val phone: String,
    val platform: String,
    val token: String,
    val updated_time: Int,
    val ut_expire: String,
    val tmp_token:String
)

data class WechatAdzone(
    val adzone_id: String,
    val adzonepid: String
)