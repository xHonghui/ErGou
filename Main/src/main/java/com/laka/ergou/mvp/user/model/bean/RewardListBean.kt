package com.laka.ergou.mvp.user.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/6/26
 * @Description:
 */
class RewardListBean(
        @SerializedName("title")
        var title: String = "",
        @SerializedName("earn")
        var earn: String = "",
        @SerializedName("order_status")
        var orderStatus: String = "",//订单状态，3：可提现；30：待结算
        @SerializedName("create_time")
        var createTime: String = "",
        @SerializedName("earn_word")
        var earnWord: String = "",
        @SerializedName("commision_word")
        var commisionWord: String = ""
)