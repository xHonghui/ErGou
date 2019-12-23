package com.laka.ergou.mvp.shop.model.bean

/**
 * @Author:summer
 * @Date:2019/1/9
 * @Description: 统一错误消息格式
 */
data class ErrorResponse(
        val code: Int,
        val msg: String,
        val sub_msg: String,
        val sub_code: Int
)