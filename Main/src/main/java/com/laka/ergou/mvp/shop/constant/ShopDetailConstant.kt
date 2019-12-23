package com.laka.ergou.mvp.shop.constant

import com.laka.ergou.BuildConfig

/**
 * @Author:summer
 * @Date:2018/12/20
 * @Description:
 */
interface ShopDetailConstant {
    companion object {
        /*淘宝客总接口，通过method参数区分不同子接口*/
        const val TAOBAOKE_SHOP_URL: String = "tbkGetRequest"
        /*产品详情图片*/
        const val TAOBAO_DETAIL_IMAGE: String = "taobaoke/image-detail"
        /*产品详情*/
        //const val CUSTOM_PRODUCT_URL: String = "taobaoke/product-detail"
        const val CUSTOM_PRODUCT_URL: String = "taobaoke/product-detail-no-tao-h5"
        /*猜您喜欢*/
        //const val GUESS_LIKE_URL: String = "taobaoke/guess-like"
        const val GUESS_LIKE_URL: String = "taobaoke/tbk-dg-material-optional"
        /*淘宝客接口的method参数*/
        const val SHOP_DETAIL_BEAN: String = "shop_detail_bean"

        //获取高佣链接
        const val API_TBK_PRIVILEGE_GET = "taobaoke/tbk-privilege-get"
        //创建淘口令
        const val API_TBK_CREATE_TPWD = "taobaoke/tbk-tpwd-create"

        //淘宝客二维码url
        const val API_TBK_QRCODE_URL = "taobaoke/tkl-url"

        //淘宝相关推荐
        const val API_PRODUCT_RECOMMEND = "product/related-recommend"

        // 多样式显示 type
        const val SHOP_DETAIL_RECOMMEND_ITEM: Int = 0x1001
        const val SHOP_DETAIL_BASIC: Int = 0x1002
        const val SHOP_DETAIL_MORE: Int = 0x1003
        const val SHOP_DETAIL_RECOMMEND_TITLE: Int = 0x1004
        const val SHOP_DETAIL_BANNER: Int = 0x1005
        const val SHOP_DETAIL_IMAGE_DETAIL: Int = 0x1006
        const val SHOP_DETAIL_STORE_DETAIL: Int = 0x1007
        const val SHOP_DETAIL_ADVERT_BANNER: Int = 0x1008

        // 请求key
        const val OS: String = "os"
        const val ADZONE_ID: String = "adzone_id"
        const val IP: String = "ip"
        const val USER_AGENT: String = "ua"
        const val NET: String = "net"
        const val SHOPPING_BASE_HOST: String = BuildConfig.ERGOU_BASE_HOST
        const val METHOD: String = "method"
        const val TAOBAOKE_ADZONE_ID: String = "73683250470"
        const val PAGE_NO: String = "page_no"
        const val PAGE_SIZE: Int = 10
        const val PAGE_SIZE_KEY: String = "page_size"
        const val PRODUCT_CATEGORY_ID: String = "product_category_id"
        const val PRODUCT_CATEGORY: String = "q"
        const val USER_ID: String = "user_id"
        const val TEXT: String = "text"
        const val URL: String = "url"
        const val LOGO: String = "logo"
        const val PRODUCT_ID: String = "product_id"
        const val TOKEN: String = "token"
        const val ITEM_ID: String = "item_id"
        const val PLATFORM: String = "platform"
        const val NUM_IIDS: String = "num_iids"
        const val TPWD_CREATE: String = "tpwd_create"
        const val APP_KEY: String = "25355294"
        const val APP_SECRECT: String = "856d2e0da001b5718357d4074b0a0453"
        const val BACK_URL: String = "tbopen://m.laka.com"
        const val HAS_USERABLE_COUPON: Int = -1
        const val NO_USABLE_COUPON: Int = 1

        // 详情模块事件类型
        const val RECOMMEND_ITEM_CLICK_EVENT: Int = 1001
        const val SHOP_DETAIL_MORE_CLICK_EVENT: Int = 1002

        // 詳情页入口区分
        const val ENTRANCE: String = "entrance"
//        const val ORDER_ITEM_CLICK_INTO = 1  //订单页面点击进入（不使用）
//        const val PRODUCT_ITEM_CLICK_NORMAL = 2  //目前没有使用到（不使用）
        const val PRODUCT_ITEM_CLICK_SKELETON = 3  //商品列表点击进入
        // 高额补贴调用区分
        const val RECEIVE_COUPON: Int = 1
        const val CREATE_TBPWD: Int = 2
        //加载高佣是否刷新
        val LOAD_HIGH_VOLUME_NO_REFRESH: Int = 1
        val LOAD_HIGH_VOLUME_REFRESH: Int = 0
        //加载/更新 高佣优惠券信息
        const val TYPE_LOAD_COUPONINFO_FOR_RECEIVE: Int = 1  //领券类型
        const val TYPE_LOAD_COUPONINFO_FOR_SHARE: Int = 2  //分享
        const val TYPE_LOAD_COUPONINFO_FOR_NORMAL: Int = 3  //普通加载高佣优惠券

    }
}