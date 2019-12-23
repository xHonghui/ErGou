package com.laka.ergou.mvp.commission.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.commission.constant.CommissionConstant

data class CommissionNewBean(
    val balance_word: String,
    val btn_status: Int,
    val btn_word: String,
    val help_url: String,
    val reach: String,
    val settlement_order_type: String,
    val subsidy: Subsidy,
    val tip_word: String,
    val usable: String,
    val withdrawing: Int,
    val withdrawing_money: String,
    val ywx_user: Int
)

data class Subsidy(
    val agent: Agent,
    val other: Other,
    val promotion: Promotion,
    val taobao: Taobao,
    val total: Total
)

data class Agent(
    val balance: String,
    val count: String,
    val frozen: String
)

data class Promotion(
    val balance: String,
    val count: String,
    val frozen: String
)

data class Taobao(
    val balance: String,
    val count: String,
    val frozen: String
)

data class Other(
    val balance: String,
    val frozen: String,
    val count: String
)

data class Total(
    val balance: String,
    val frozen: String
)