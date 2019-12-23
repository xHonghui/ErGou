package com.laka.ergou.mvp.armsteam.model.bean


/**
 * @Author:summer
 * @Date:2019/3/8
 * @Description:
 */
data class MyLowerLevelsListBean(
    val sub_user: ArrayList<SubUser>,
    val total: Int
)

data class SubUser(
    val avatar: String,
    val created_time: Long,
    val earn: String,
    val id: String,
    val nickname: String
)