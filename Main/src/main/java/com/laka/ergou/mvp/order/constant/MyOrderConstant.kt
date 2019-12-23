package com.laka.ergou.mvp.order.constant

import com.laka.ergou.BuildConfig

/**
 * @Author:summer
 * @Date:2019/1/19
 * @Description:
 */
interface MyOrderConstant {

    companion object {
        /*我的订单*/
        val ORDER_BASE_HOSTL: String = BuildConfig.ERGOU_BASE_HOST
        const val MY_ORDER_URL: String = "order/list"
        // 我的订单page标题
        private const val MY_ORDER_ALL_PAGE = "全部"
        private const val MY_ORDER_PAID_PAGE = "待结算"
        private const val MY_ORDER_SETTLEMENT_PAGE = "已结算"
        private const val MY_ORDER_REFUND_PAGE = "已失效"
        // private const val MY_ORDER_REFUND_PAGE = "已退款"
        // private const val MY_ORDER_PROTECTION_PAGE = "已维权"
        val MY_ORDER_PAGE_TITLE: ArrayList<String> = arrayListOf(MY_ORDER_ALL_PAGE, MY_ORDER_PAID_PAGE, MY_ORDER_SETTLEMENT_PAGE, MY_ORDER_REFUND_PAGE)
        const val MY_ORDER_STATUS: String = "my_order_status"
        // 订单状态
        const val ORDER_STATUS_ALL: Int = 99 // 全部
        const val ORDER_STATUS_PAID: Int = 12  // 已付款
        const val ORDER_STATUS_SETTLEMENT: Int = 3  // 已结算（上一个月以前）
        const val ORDER_STATUS_ENABLE: Int = 30  // 已结算（当前月份）
        const val ORDER_STATUS_REFUND: Int = 13  // 已退款  // 已维权
        const val TYPE: String = "type"
        const val TOKEN: String = "token"
        //来源类型 1:app  2：微信
        const val SOURCE_TYPE: String = "source_type"
        val PAGE_NUMER: String = "page"
        val PAGE_SIZE: String = "pageSize"
        val SIZE: String = "20"
        //订单来源类型
        const val APP_TYPE: Int = 1
        const val WECHAT_TYPE: Int = 2

    }
}