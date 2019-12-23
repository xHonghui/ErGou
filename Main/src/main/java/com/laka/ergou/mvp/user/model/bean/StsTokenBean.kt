package com.laka.ergou.mvp.user.model.bean

/**
 * @Author:summer
 * @Date:2019/1/14
 * @Description:
 */
data class StsTokenBean(
        val bucket: String,
        val endpoint: String,
        val id: String,
        val secret: String,
        val token: String,
        val url: String
)