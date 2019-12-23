package com.laka.ergou.mvp.teamaward.model.bean

data class TearmAwardBean(
        val orders: MutableList<Order>,
        val total: Int
)

data class Order(
        val create_time: String,
        val earn: String,
        val earn_rate: String,
        val id: Int,
        val nickname: String,
        val order_status: Int,
        val subsidy_word: String,
        val earning_time: String = ""
)