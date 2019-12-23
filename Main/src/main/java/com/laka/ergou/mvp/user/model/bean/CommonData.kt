package com.laka.ergou.mvp.user.model.bean

/**
 * @Author:summer
 * @Date:2019/1/15
 * @Description:
 */
data class CommonData(
        val nickname: String?,
        val gender: String?,
        val avatar: String?,
        val alipay_username: String?,
        val alipay_realname: String?,
        val isOpenGouXiaoEr: Int = 0
)