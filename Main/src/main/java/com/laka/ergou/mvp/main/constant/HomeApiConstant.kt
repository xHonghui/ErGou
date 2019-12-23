package com.laka.ergou.mvp.main.constant

import com.laka.ergou.BuildConfig
import com.laka.ergou.mvp.main.model.bean.PerfixDetailUrl

/**
 * @Author:Rayman
 * @Date:2019/1/22
 * @Description:主页接口常量
 */
object HomeApiConstant {

    const val HOME_API_HOST = BuildConfig.ERGOU_BASE_HOST

    /**
     * description:URL配置，根据后台接口获取。然后动态更新
     **/
    @JvmField
    var ABOUT_LAKA = "https://www.laka-inc.com"
    var ABOUT_PRIVACY = "https://h5.fanli.amia2012.com/privacypolicy"
    var EARN_COMMISSION = "https://h5.fanli.amia2012.com/rebate"
    var URL_INVITATION = "http://ergou-app.test.szsdiy.com/invitation.html"
    var URL_INVITATION_SHARE = "http://ergou-app.test.szsdiy.com/landing.html"
    var URL_INVITATION_POSTER = "http://ergou-app.test.szsdiy.com/poster.html"
    var URL_TAOBAO_LOGOUT = "https://login.m.taobao.com/logout.htm"
    var URL_USER_TUTORIAL = "http://ergou-app.test.lm1314.xyz/tutorial.html"
    //下载地址
    var URL_SCAN_DOWNLOAD = "http://ergou-app.test.lm1314.xyz/scanDownload.html"
    //天猫超市地址
    var URL_TMALL_PREFIX = "https://detail.m.tmall.com"
    //天猫超市产品详情匹配地址
    var URL_TMALL_PREFIX_LIST = ArrayList<PerfixDetailUrl>()
    //微信分享教程H5页面
    var URL_WECHAT_MOMENT = "http://ergou-app.test.lm1314.xyz/shareIntro.html"
    //战队说明
    var URL_WARTEAM_INFO = "http://ergou-app.test.lm1314.xyz/teamIntro.html"
    //战队邀请
    var URL_TEAM_INVITATION = "http://ergou-app.test.lm1314.xyz/teamInvite.html"
    //客服
    var URL_CUSTOMER = "https://tb.53kf.com/code/app/10189465/2?header=none&device=android"
    //0元购教程
    var URL_AERO_BUY = "http://ergou-app.test.lm1314.xyz"
    var URL_SEND_CIRCLE = ""
    var URL_WX_PAY_REFERER = ""
    var URL_WX_PAY = ""

    /**
     * description:H5下载地址
     **/
    const val H5_DOWNLOAD_URL = "https://ergou-api.lakatv.com/download/ergou-release.apk"

    // APP更新
    const val API_UPDATE = "system/app-update"

    // 微信分享文案获取
    const val API_WX_SHARE_OFFICIAL = "system/wx-share-info"

    // 抽奖微信分享文案获取
    const val API_WX_SHARE_PROMOTION = "user/share-promotion"
    //增加抽奖次数
    const val API_ADD_LOTTERY_TIME = "anniversary/prize-share"

    // 获取项目常量URLS
    const val API_GET_USER_BANNER_ADV = "system/h5-url"

    /**
     * description:参数定义
     **/
    const val VERSION = "version"
    const val BUILD = "build"           //数字版本
    const val APP_CHANNEL = "channel"
    const val APP_PLAT_FORM = "platform"
    const val APP_FLAG = "flag"
    const val PRODUCT_ID = "item_id"

    /**
     * ProductGridItem 根据不同列表类型，UI显示不同
     * */
    const val UI_TYPE_SEARCH_PRODUCT: Int = 1
    const val UI_TYPE_SEARCH_NORMAL: Int = 2
}