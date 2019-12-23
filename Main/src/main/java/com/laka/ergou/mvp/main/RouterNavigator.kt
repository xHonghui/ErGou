package com.laka.ergou.mvp.main

import android.app.Activity
import android.content.Context
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.mvp.activityproduct.ActivityProductModuleNavigator
import com.laka.ergou.mvp.advert.AdvertNavigator
import com.laka.ergou.mvp.freedamission.FreeAdmissionModuleNavigator
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_ACTIVITY_TEARM_AWARD
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_ACTIVITY_WEB
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_ACTIVITY_PRODUCT
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_COMMISSION_MESSAGE
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_FREE_ADMISSION
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_GOOD_DETAIL
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_GOOD_SEARCH
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_INVITATION
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_INVITATION_RECORD
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_MY_APP_SHARE
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_MY_BIND_ORDER
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_MY_COMMISSION
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_MY_HELPER
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_MY_ORDER
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_MY_OTHER_REWARD
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_MY_POSTER_SHARE
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_MY_ROBOT
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_MY_SETTING
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_MY_SUBORDINATE
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_OTHER_MESSAGE
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_PRODUCT_SPECIAL
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_SHOP_CART
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_TMALL_SUPERMARKET
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_WEB
import com.laka.ergou.mvp.shop.ShopDetailModuleNavigator
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shopping.ShoppingModuleNavigator
import com.laka.ergou.mvp.shopping.search.constant.ShoppingSearchConstant
import com.laka.ergou.mvp.tmall.TmallModuleNavigator
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.circle.CircleNavigator
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant.NAV_IM_PUSH_MESSAGE

/**
 * @Author:Rayman
 * @Date:2019/1/7
 * @Description:项目路由跳转类
 */
object RouterNavigator {

    /**
     * description:本地跳转映射表
     **/
    @JvmField
    open val bannerRouterReflectMap = HashMap<Int, String>()

    init {
        //1：邀请好友，2：其他消息，3：我的订单，4：商品详情，5：H5链接，6：补贴消息，7：IM推送，8：商品专题，9：天猫超市&聚划算等H5页面，10:0元购，11：活动页h5跳转
        //12：活动产品专题，13：
        bannerRouterReflectMap[1] = NAV_INVITATION
        bannerRouterReflectMap[2] = NAV_OTHER_MESSAGE
        bannerRouterReflectMap[3] = NAV_MY_ORDER
        bannerRouterReflectMap[4] = NAV_GOOD_DETAIL
        bannerRouterReflectMap[5] = NAV_WEB
        bannerRouterReflectMap[6] = NAV_COMMISSION_MESSAGE
        bannerRouterReflectMap[7] = NAV_IM_PUSH_MESSAGE
        bannerRouterReflectMap[8] = NAV_PRODUCT_SPECIAL
        bannerRouterReflectMap[9] = NAV_TMALL_SUPERMARKET
        bannerRouterReflectMap[10] = NAV_FREE_ADMISSION
        bannerRouterReflectMap[11] = NAV_ACTIVITY_WEB
        bannerRouterReflectMap[12] = NAV_ACTIVITY_PRODUCT
    }

    /**
     * description:处理常规App内路由跳转
     * @param requestCode:有传该参数，则使用startActivityForResult
     **/
    @JvmStatic
    fun handleAppInternalNavigator(context: Context, target: String, params: Map<String, String>, requestCode: Int = -1) {
        when (target) {
            NAV_WEB -> {
                // 解析出params的数据
                var title = ""
                if (params.containsKey(HomeConstant.WEB_TITLE)) {
                    title = params[HomeConstant.WEB_TITLE].toString()
                }

                if (params.containsKey(HomeConstant.TITLE)) {
                    title = params[HomeConstant.TITLE].toString()
                }

                var url = ""
                if (params.containsKey(HomeConstant.WEB_URL)) {
                    url = params[HomeConstant.WEB_URL].toString()
                }

                // 假若只存在RouterValue
                if (params.containsKey(HomeNavigatorConstant.ROUTER_VALUE)) {
                    url = params[HomeNavigatorConstant.ROUTER_VALUE].toString()
                }

                if (requestCode != -1) {
                    HomeModuleNavigator.startWebActivityForResult(context as Activity, title, url, requestCode)
                } else {
                    HomeModuleNavigator.startWebActivity(context, title, url)
                }
            }
            NAV_GOOD_DETAIL -> {
                var productId = ""
                if (params.containsKey(ShopDetailConstant.ITEM_ID)) {
                    productId = params[ShopDetailConstant.ITEM_ID].toString()
                }
                if (params.containsKey(HomeNavigatorConstant.ROUTER_VALUE)) {
                    productId = params[HomeNavigatorConstant.ROUTER_VALUE].toString()
                }
                LogUtils.info("输出productId：$productId")

                if (requestCode != -1) {
                    ShopDetailModuleNavigator.startShopDetailActivityForResult(context as Activity, productId, requestCode)
                } else {
                    ShopDetailModuleNavigator.startShopDetailActivity(context, productId)
                }
            }
            NAV_MY_ORDER -> {
                if (requestCode != -1) {
                    UserModuleNavigator.startMyOrderActivityForResult(context as Activity, requestCode)
                } else {
                    UserModuleNavigator.startMyOrderActivity(context)
                }
            }
            NAV_MY_COMMISSION -> {
                if (requestCode != -1) {
                    UserModuleNavigator.startMyCommissionActivityForResult(context as Activity, requestCode)
                } else {
                    UserModuleNavigator.startMyCommissionActivity(context)
                }
            }
            NAV_MY_BIND_ORDER -> {
                if (requestCode != -1) {
                    UserModuleNavigator.startBindOrderActivityForResult(context as Activity, requestCode)
                } else {
                    UserModuleNavigator.startBindOrderActivity(context)
                }
            }
            NAV_MY_HELPER -> {
                if (requestCode != -1) {
                    CircleNavigator.startCircleHelperActivityForResult(context as Activity, "发圈助手", HomeApiConstant.URL_SEND_CIRCLE, 0, requestCode)
                } else {
                    CircleNavigator.startCircleHelperActivity(context, "发圈助手", HomeApiConstant.URL_SEND_CIRCLE)
                }
            }

            NAV_MY_ROBOT -> {
                if (requestCode != -1) {
                    UserModuleNavigator.startMyRobotActivityForResult(context as Activity, requestCode)
                } else {
                    UserModuleNavigator.startMyRobotActivity(context)
                }
            }
            NAV_SHOP_CART -> {
                //阿里百川SDK打开页面
                UserModuleNavigator.startMyShopCartActivity(context as Activity)
            }
            NAV_MY_SETTING -> {
                if (requestCode != -1) {
                    UserModuleNavigator.startMySettingActivityForResult(context as Activity, requestCode)
                } else {
                    UserModuleNavigator.startMySettingActivity(context)
                }
            }
            NAV_INVITATION -> {
                if (requestCode != -1) {
                    UserModuleNavigator.startInvitationWebActivityForResult(context as Activity, requestCode)
                } else {
                    UserModuleNavigator.startInvitationWebActivity(context)
                }
            }
            NAV_MY_SUBORDINATE -> {
                if (requestCode != -1) {
                    UserModuleNavigator.startMyLowerLevelActivityForResult(context as Activity, requestCode)
                } else {
                    UserModuleNavigator.startMyLowerLevelActivity(context)
                }
            }
            NAV_OTHER_MESSAGE -> {
                if (requestCode != -1) {
                    UserModuleNavigator.startMessageCenterActivityForResult(context as Activity, 1, requestCode)
                } else {
                    UserModuleNavigator.startMessageCenterActivity(context, 1)
                }
            }
            NAV_COMMISSION_MESSAGE -> {
                if (requestCode != -1) {
                    UserModuleNavigator.startMessageCenterActivityForResult(context as Activity, 0, requestCode)
                } else {
                    UserModuleNavigator.startMessageCenterActivity(context, 0)
                }
            }
            NAV_INVITATION_RECORD -> {
                if (requestCode != -1) {
                    UserModuleNavigator.startInvitationRecordActivityForResult(context as Activity, requestCode)
                } else {
                    UserModuleNavigator.startInvitationRecordActivity(context)
                }
            }
            NAV_ACTIVITY_TEARM_AWARD -> { //战友提成
                if (requestCode != -1) {
                    UserModuleNavigator.startTearmAwardActivityForResult(context as Activity, requestCode)
                } else {
                    UserModuleNavigator.startTearmAwardActivity(context)
                }
            }
            NAV_MY_OTHER_REWARD -> {  //其他奖励
                if (requestCode != -1) {
                    UserModuleNavigator.startRewardListActivityForResult(context as Activity, requestCode)
                } else {
                    UserModuleNavigator.startRewardListActivity(context)
                }
            }
            NAV_MY_POSTER_SHARE -> {  //海报分享
                if (requestCode != -1) {
                    UserModuleNavigator.startInvitationPlaybillActivityForResult(context as Activity, requestCode)
                } else {
                    UserModuleNavigator.startInvitationPlaybillActivity(context)
                }
            }
            NAV_MY_APP_SHARE -> {  // app分享

            }
            NAV_PRODUCT_SPECIAL -> { //商品专题
                val title = "${params[HomeConstant.TITLE]}"
                val topicId = "${params[HomeNavigatorConstant.ROUTER_VALUE]}"
                val bigImageUrl = "${params[HomeConstant.TOPIC_BIG_IMAGE_URL]}"

                if (requestCode != -1) {
                    ShoppingModuleNavigator.startShoppingProductSpecialActivityForResult(context as Activity, title, topicId, bigImageUrl, requestCode)
                } else {
                    ShoppingModuleNavigator.startShoppingProductSpecialActivity(context, title, topicId, bigImageUrl)
                }
            }
            NAV_TMALL_SUPERMARKET -> { //天猫超市&聚划算
                // 使用外部传入 webView 的方式打开
                val title = "${params[HomeConstant.TITLE]}"
                val url = "${params[HomeNavigatorConstant.ROUTER_VALUE]}"

                if (requestCode != -1) {
                    TmallModuleNavigator.startTallWebActivityForResult(context as Activity, title, url, requestCode)
                } else {
                    TmallModuleNavigator.startTallWebActivity(context, title, url)
                }
            }
            NAV_FREE_ADMISSION -> { // 0元购
                val title = "${params[HomeConstant.TITLE]}"
                val bigImageUrl = "${params[HomeConstant.TOPIC_BIG_IMAGE_URL]}"

                if (requestCode != -1) {
                    FreeAdmissionModuleNavigator.startFreeAdmissionActivityForResult(context as Activity, title, bigImageUrl, requestCode)
                } else {
                    FreeAdmissionModuleNavigator.startFreeAdmissionActivity(context, title, bigImageUrl)
                }
            }
            NAV_ACTIVITY_WEB -> {  //活动H5页面
                val title = "${params[HomeConstant.TITLE]}"
                val url = "${params[HomeNavigatorConstant.ROUTER_VALUE]}"
                LogUtils.info("jpush---------url=$url")

                if (requestCode != -1) {
                    AdvertNavigator.startAdvertWebActivityForResult(context as Activity, title, url, requestCode)
                } else {
                    AdvertNavigator.startAdvertWebActivity(context, title, url)
                }
            }
            NAV_GOOD_SEARCH -> {
                val searchKey = "${params[ShoppingSearchConstant.KEY_SEARCH_KEYWORD]}"
                if (requestCode != -1) {
                    HomeModuleNavigator.startSearchActivityForResult(context as Activity, searchKey, requestCode)
                } else {
                    HomeModuleNavigator.startSearchActivity(context, searchKey)
                }
            }
            NAV_ACTIVITY_PRODUCT -> {
                val title = "${params[HomeConstant.TITLE]}"
                val value = "${params[HomeNavigatorConstant.ROUTER_VALUE]}"
                val bigImageUrl = "${params[HomeConstant.TOPIC_BIG_IMAGE_URL]}"
                if (requestCode != -1) {
                    ActivityProductModuleNavigator.startActivityProductActivityForResult(context as Activity, value, title, bigImageUrl, requestCode)
                } else {
                    ActivityProductModuleNavigator.startActivityProductActivity(context, value, title, bigImageUrl)
                }
            }
            else -> {
                // 假若不存在映射表中，不做任何跳转
            }
        }
    }

}