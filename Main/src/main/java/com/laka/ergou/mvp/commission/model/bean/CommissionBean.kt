package com.laka.ergou.mvp.commission.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/1/19
 * @Description:
 */
data class CommissionBean(
        val btn_status: Int = -1,
        val frozen: String = "", //冻结佣金
        val reach: String = "",
        val total: String = "",
        val usable: String = "", //可用佣金
        val withdrawing: Int = -1,
        val withdrawing_money: String = "",
        val withdrew: String = "", //已提现佣金
        val tip_word: String = "",
        val help_url: String = "",
        val balance_word: String = "",
        @SerializedName("settlement_order_type")
        val settlementWay: String = "",
        @SerializedName("subsidy_word")
        val subsidyWord: String = "",
        @SerializedName("balance")
        val balance: String = "", //结算补贴
        @SerializedName("current_month_balance")
        val currentMonthBalance: String = "", //本月结算补贴
        @SerializedName("upper_month_balance")
        val upperMonthBalance: String = "" //上月结算补贴
)