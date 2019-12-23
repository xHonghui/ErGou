package com.laka.ergou.mvp.shop.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/1/21
 * @Description:
 */
data class HighVolumeInfoResponse(
        @SerializedName("small_images")
        var smallImages: SmallImages,
        @SerializedName("share")
        var share: WechatShareBean = WechatShareBean(),
        @SerializedName("user_type")
        var userType: Int = -1,
        @SerializedName("num_iid")
        var numIid: String,
        @SerializedName("has_coupon")
        var hasCoupon: Int = -1,
        @SerializedName("coupon_money")
        var couponMoney: String,
        var fanli: String = "",
        var actual_price: String = "",
        var zk_final_price: String = "",
        var coupon_click_url: String = "",
        var coupon_info: String = "",
        var large_coupon: Int = 0
)