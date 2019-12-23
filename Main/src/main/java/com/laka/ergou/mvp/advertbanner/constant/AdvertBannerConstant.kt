package com.laka.ergou.mvp.advertbanner.constant

import com.laka.ergou.BuildConfig

/**
 * @Author:summer
 * @Date:2019/7/27
 * @Description:
 */
object AdvertBannerConstant {

    const val API_BASE_HOST:String = BuildConfig.ERGOU_BASE_HOST
    const val API_GET_ADVERT_BANNER_URL:String = "system/banner"

    //banner位置:1首页,2个人中心,3补贴消息列表,4订单列表,5商品详情,6其他消息列表
    const val TYPE_BANNER_CLASSID_HOME:String = "1"
    const val TYPE_BANNER_CLASSID_USERCENTER:String = "2"
    const val TYPE_BANNER_CLASSID_MESSAGE_COMMISSION:String = "3"
    const val TYPE_BANNER_CLASSID_ORDER_LIST:String = "4"
    const val TYPE_BANNER_CLASSID_PRODUCT_DETAIL:String = "5"
    const val TYPE_BANNER_CLASSID_MESSAGE_OTHER:String = "6"

}