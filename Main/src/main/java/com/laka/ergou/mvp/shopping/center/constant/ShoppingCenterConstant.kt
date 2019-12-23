package com.laka.ergou.mvp.shopping.center.constant

import android.support.annotation.IntDef

/**
 * @Author:Rayman
 * @Date:2018/12/21
 * @Description:商品模块常量类
 */
object ShoppingCenterConstant {


    /**
     * description:主页商品列表父分类常量传递
     **/
    const val LIST_TYPE_ID = "LIST_TYPE_ID"
    const val LIST_TYPE_NAME = "LIST_TYPE_NAME"
    const val LIST_TYPE_FAVORITE = "LIST_TYPE_FAVORITE"
    const val LIST_TYPE_IS_SHOW_BANNER = "LIST_TYPE_IS_SHOW_BANNER"

    /**
     * description:跳转喜欢页面常量传递
     **/
    const val FAVORITE_TITLE = "FAVORITE_TITLE"
    const val FAVORITE_ID = "FAVORITE_ID"

    /**
     * description:当前模块EventBus传递
     **/
    const val EVENT_LIST_UI_TYPE_NORMAL = "EVENT_LIST_UI_TYPE_NORMAL"
    const val EVENT_LIST_UI_TYPE_GRID = "EVENT_LIST_UI_TYPE_GRID"

    /**
     * description:本地SP数据存储
     **/
    const val SP_SHOPPING_FILE_NAME = "SP_SHOPPING_FILE_NAME"
    const val SP_KEY_SHOPPING_CATEGORY = "SP_KEY_SHOPPING_CATEGORY"
    const val SP_KEY_SHOPPING_HOME = "SP_KEY_SHOPPING_HOME"
    const val SP_KEY_ADVERT = "SP_KEY_ADVERT"
    const val SP_KEY_ADVERT_PATH = "SP_KEY_ADVERT_PATH"
    const val SP_KEY_ADVERT_IMG_NAME = "laka_advert.jpg"

    /**
     * description:主页商品列表样式
     **/
    const val LIST_UI_TYPE_COMMON = 1
    const val LIST_UI_TYPE_GRID = 2

    @IntDef(LIST_UI_TYPE_COMMON, LIST_UI_TYPE_GRID)
    annotation class ProductListUiType

    /**
     * description:跳转商品专题页面常量传递
     * */
    const val SPECIAL_TITLE: String = "SPECIAL_TITLE"
    const val SPECIAL_ID: String = "SPECIAL_ID"
    const val SPECIAL_BIG_IMAGE: String = "SPECIAL_BIG_IMAGE"
    /**
     * description:专题商品列表类型（ui层面）
     * */
    const val SPECIAL_NINE_YUAN_NINE_FREE_SHIPPING:String = "10" //九块九包邮
    const val SPECIAL_SELL_WELL_STYLE:String = "20" //全网爆款
    const val SPECIAL_NEW_USER_VIP:String = "30" //新人专享
    const val SPECIAL_HIGH_COMMISSON_BOUTIQUE:String = "40" //高佣精品
    const val SPECIAL_ERGOU_STRICT_CHOISE:String = "50" //二购严选
    const val SPECIAL_HIGH_DISCOUNT:String = "60" //超值优惠

}