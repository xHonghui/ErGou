package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:Rayman
 * @Date:2019/1/15
 * @Description:主页商品带有折扣类型Bean(当前主要运用在商品列表主页---除了精选之外的分类)
 */
data class ProductWithCoupon(
        @SerializedName("click_url") val itemUrl: String = "",
        @SerializedName("category_id") val categoryId: String = "",
        @SerializedName("category_name") val categoryName: String = "",
        @SerializedName("commission_rate") val commissionRate: String = "",
        @SerializedName("commission_type") var commissionType: String = "",
        @SerializedName("coupon_end_time") val couponEndTime: String = "",
        @SerializedName("coupon_click_url") var couponUrl: String = "",
        @SerializedName("high_volume_coupon_url") val highVolumeCouponUrl: String = "",
        @SerializedName("coupon_id") val couponId: String = "",
        @SerializedName("coupon_info") val couponInfo: String = "",
        @SerializedName("coupon_remain_count") val couponRemainCount: String = "",
        @SerializedName("coupon_share_url") val couponShareUrl: String = "",
        @SerializedName("event_start_time") val couponStartTime: String = "",
        @SerializedName("shop_title") val sellerShopName: String = "",
        @SerializedName("status") val status: Int = 0,
        @SerializedName("zk_final_price_wap") val zk_final_price_wap: String = "",
        @SerializedName("fanli") val commissionPrice: String = "",
        @SerializedName("has_coupon") val hasCoupon: Int = 0, //是否有优惠券 1：是，2：否
        @SerializedName("large_coupon") val largeCoupon: Int = 0, //是否有大额券  1：是，2：否
        @SerializedName("coupon_money") val couponMoney: String = "",
        @SerializedName("subsidy") val subsidy: String = "",//首单补贴/补贴
        @SerializedName("actual_price") val actualPrice: String = "", //0元购页面商品原价
        var isFirst: Int = 1,//是否是首单补贴  1：是，2：否  ,0元购使用
        @SerializedName("type") val type: Int = 0,
        @SerializedName("labels") val labels: MutableList<TagItemBean>
) : BaseProduct() {
    override fun toString(): String {
        return "ProductWithCoupon(itemUrl='$itemUrl', categoryId='$categoryId', categoryName='$categoryName', commissionRate='$commissionRate', commissionType='$commissionType', couponEndTime='$couponEndTime', couponUrl='$couponUrl', highVolumeCouponUrl='$highVolumeCouponUrl', couponId='$couponId', couponInfo='$couponInfo', couponRemainCount='$couponRemainCount', couponShareUrl='$couponShareUrl', couponStartTime='$couponStartTime', sellerShopName='$sellerShopName', status=$status, zk_final_price_wap='$zk_final_price_wap', commissionPrice='$commissionPrice', type=$type)," +
                "\nBaseProduct(favoritesId=$productId, productName='$productName', productUrl='$productUrl', productPic='$productPic', productPics=$productPics, productPrice='$productPrice', originPrice='$originPrice', place='$place', sellCount='$sellCount', sellerType='$sellerType', sellerName='$sellerName', sellerId=$sellerId, uiType=$uiType')"
    }


}
