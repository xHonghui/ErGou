package com.laka.ergou.mvp.shop.model.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @Author:summer
 * @Date:2019/5/27
 * @Description:微信分享
 */
data class WechatShareBean(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("content")
        val content: String = "",
        @SerializedName("content_tkl")
        val contentTkl: String = "",
        @SerializedName("content_link")
        val contentDownloadLink: String = "",
        @SerializedName("content_invite")
        val contentInviteCode: String = "",
        @SerializedName("tkl_url")
        var tklShareUrl: String = "",  //分享的二维码内容
        var couponMoney: String = "",  //优惠券金额
        var zkFinalPrice: String = "", //原价
        var volume: Int = 0, //销量
        var actualPrice: String = "", //券后价
        var productId: String = ""  //商品id
) : Serializable