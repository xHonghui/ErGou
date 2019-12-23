package com.laka.ergou.mvp.commission.constant

import com.laka.ergou.BuildConfig

/**
 * @Author:summer
 * @Date:2019/1/18
 * @Description:
 */
interface CommissionConstant {

    companion object {

        /*我的补贴*/
        const val MY_COMMISSION_URL = "order/my-commission"

        const val MY_NEW_COMMISSION_URL = "order/subsidy"
        /*补贴详情*/
        const val MY_COMMISSION_DETAIL_URL = "order/commission-list"
        /*提现记录*/
        const val MY_COMMISSION_WITHDRAW_URL = "order/withdraw"

        const val COMMISSON_BASE_HOSTL: String = BuildConfig.ERGOU_BASE_HOST
        const val TOKEN: String = "token"
        const val PAGE: String = "page"
        const val PAGE_SIZE_KEY: String = "pageSize"
        const val PAGE_SIZE: String = "20"
        const val WITHDRAWING: Int = 0 // 待审核
        const val AUDIT_PASS: Int = 1  // 审核通过
        const val REFUSE: Int = 2 // 拒绝
        const val NOTING: Int = 999  // 用户没有审核记录
        const val WITHDRAWAL_STATUS: String = "withdrawal_status"
        const val COMMISSION_SETTLEMENTWAY_ORDER: String = "0"//订单结算时结算
        const val COMMISSION_SETTLEMENTWAY_MONTH: String = "1"//每月固定时间结算
        //request code
        const val REQUEST_CODE_BIND_ALI_ACCOUNT: Int = 0x1001
        const val MONTH:String="1"
        const val LAST_MONTH:String="2"
        const val TODAY:String="3"
        const val YESTERDAY:String="4"
        const val COMMISSION_TYPE:String="COMMISSION_TYPE"

        const val TAOBAO :Int = 0
        const val AGENT :Int = 1
        const val PROMOTION :Int = 2
        const val OTHER :Int = 3
    }

}