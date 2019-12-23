package com.laka.ergou.mvp.main.model.bean

import com.google.gson.annotations.SerializedName
import com.laka.ergou.mvp.shopping.center.model.bean.Promotion

/**
 * @Author:Rayman
 * @Date:2019/3/12
 * @Description:AppUrl信息类
 */
data class AppUrlsInfo(
        @SerializedName("privacypolicy")
        var privacyUrl: String = "",
        @SerializedName("home")
        var aboutLaka: String = "",
        @SerializedName("rebate")
        var earnCommissionUrl: String = "",
        @SerializedName("scan_download")
        var scanDownloadUrl: String = "",
        @SerializedName("invitation")
        var invitationUrl: String = "",
        @SerializedName("landing")
        var invitationShareUrl: String = "",
        @SerializedName("poster")
        var invitationPosterUrl: String = "",
        @SerializedName("taobaologout")
        var taobaoLogoutUrl: String = "",
        @SerializedName("tmall_prefix")
        var tmallPrefix: String = "",
        @SerializedName("tmall_detail_url")
        var tmallPrefixDetailUrl: ArrayList<PerfixDetailUrl> = ArrayList(), //天猫超市--产品详情页匹配url
        @SerializedName("tutorial")
        var userTutorialUrl: String = "",
        @SerializedName("wx_moment")
        var wechatMoment: String = "",
        @SerializedName("customer")
        var customerUrl: String = "", //客服
        @SerializedName("zero_buy")
        var zeroBuy: String = "",  // 0元购教程
        @SerializedName("majordomo_service")
        var majordomoService: String = "",  // 大管家
        @SerializedName("wx_pay_referer")
        var wxPayReferer: String = "",
        @SerializedName("wx_pay_url")
        var wxPayUrl: String = "",
        @SerializedName("war_team")
        var warTeam: String = "",  //战队说明
        @SerializedName("team_invite")
        var teamInvite: String = "",  //战队邀请
        @SerializedName("promotion") //活动弹窗
        var promotion: Promotion
)