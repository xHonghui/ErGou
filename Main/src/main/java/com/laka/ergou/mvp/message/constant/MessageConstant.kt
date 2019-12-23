package com.laka.ergou.mvp.message.constant

import com.laka.ergou.BuildConfig

/**
 * @Author:summer
 * @Date:2019/3/8
 * @Description:
 */
object MessageConstant {
    /*消息列表*/
    const val MESSAGE_LIST_URL: String = "msg/messages-list"
    const val MESSAGE_BASE_HOSTL: String = BuildConfig.ERGOU_BASE_HOST
    const val ITEM_OTHER_MESSAGE_TYPE_NORMAL: Int = 1
    const val ITEM_OTHER_MESSAGE_TYPE_SHOPPING: Int = 2
    const val ITEM_OTHER_MESSAGE_TYPE_NOTICE: Int = 3
    const val MESSAGE_TYPE: String = "type"
    const val MESSAGE_PAGE_KEY: String = "page"
    const val MESSAGE_PAGE_SIZE_KEY: String = "pageSize"
    const val PAGE_SIZE: String = "20"
    const val TOKEN: String = "token"
    const val MESSAGE_COMMISSION_TYPE: Int = 1
    const val MESSAGE_OTHER_TYPE: Int = 2
    //补贴消息的补贴类型
    const val FROZEN_COMMISSION_TYPE: Int = 1 //待结补贴
    const val ENBALE_COMMISSION_TYPE: Int = 2//可用补贴
    const val INVALID_COMMISSION_TYPE: Int = 3//失效补贴
    //补贴消息的类型，1：订单付款返利补贴，2：订单结算返利补贴，3：订单失效和扣除补贴，4：提现补贴失败，5：提现补贴成功，6：邀请战友奖励补贴，7：战友购物提成补贴
    const val MESSAGE_COMMISSION_ORDER_PAY = 1  //订单付款返利补贴
    const val MESSAGE_COMMISSION_ORDER_SETTLEMENT = 2  //订单结算返利补贴
    const val MESSAGE_COMMISSION_ORDER_INVALID = 3    //订单失效和扣除补贴
    const val MESSAGE_COMMISSION_WITHDRAWAL_FAIL = 4   //提现补贴失败
    const val MESSAGE_COMMISSION_WITHDRAWAL_SUCCESS = 5  //提现补贴成功
    const val MESSAGE_COMMISSION_INVITATION_REWARD = 6    //邀请战友奖励补贴
    const val MESSAGE_COMMISSION_LOWER_LEVELS_REWARD = 7  //战友购物提成补贴
    //其他消息（消息场景） // 场景：1：邀请好友，2：其他消息，3：我的订单，4：商品详情，5：H5链接
    const val SCENE_INVITATION_FRIEND: Int = 1
    const val SCENE_OTHER_MESSAGE: Int = 2
    const val SCENE_MY_ORDER: Int = 3
    const val SCENE_PRODUCT_DETAIL: Int = 4
    const val SCENE_H5_URL: Int = 5
    //推送面向类型
    const val PUSH_TYPE_ALL: Int = 1 //全部
    const val PUSH_TYPE_PART: Int = 2 //部分

}