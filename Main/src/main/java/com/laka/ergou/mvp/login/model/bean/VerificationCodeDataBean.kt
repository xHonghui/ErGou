package com.laka.ergou.mvp.login.model.bean

/**
 * @Author:summer
 * @Date:2019/1/7
 * @Description:
 */
data class VerificationCodeDataBean(
    val code: Int,
    val `data`: VerificationCodeData,
    val msg: String,
    val timestamp: Int
)

data class VerificationCodeData(val data:String)