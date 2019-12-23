package com.laka.ergou.mvp.teamaward.constant

import com.laka.ergou.BuildConfig
import com.laka.ergou.mvp.order.constant.MyOrderConstant

interface TearmAwardConstant {
    companion object {
        val ORDER_BASE_HOSTL: String = BuildConfig.ERGOU_BASE_HOST
        private const val TEARM_AWARD_ALL_PAGE = "全部"
        private const val TEARM_AWARD_PAID_PAGE = "待结算"
        private const val Tearm_Award_SETTLEMENT_PAGE = "已结算"
        private const val Tearm_Award_REFUND_PAGE = "已失效"
        // private const val MY_ORDER_REFUND_PAGE = "已退款"
        // private const val MY_ORDER_PROTECTION_PAGE = "已维权"
        val TEARM_AWARD_PAGE_TITLE: ArrayList<String> = arrayListOf(TEARM_AWARD_ALL_PAGE, TEARM_AWARD_PAID_PAGE, Tearm_Award_SETTLEMENT_PAGE, Tearm_Award_REFUND_PAGE)
        const val TYPE_APP: Int = 1
        const val TYPE_WECHAT: Int = 2
        const val PAGE_TYPE: String = "PAGE_TYPE"
        const val TEARM_AWARD_LIST_TYPE: String = "TEARM_AWARD_LIST_TYPE"
        const val TEARM_AWARD_ALL_TYPE = 99
        const val TEARM_AWARD_PAID_TYPE = 12
        const val Tearm_Award_SETTLEMENT_TYPE = 3
        const val Tearm_Award_REFUND_TYPE = 13
        const val COMRADE_SUBSIDY = "order/comrade-subsidy"
        const val PAGE_SIZE: Int = 20
    }
}