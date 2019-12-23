package com.laka.ergou.mvp.shop.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.laka.ergou.mvp.shopping.center.model.bean.TagItemBean

/**
 * @Author:summer
 * @Date:2019/1/24
 * @Description:产品详情
 */
data class CustomProductDetail(
        var code: Int = 0,//有时后台未处理到的淘宝客异常，后台返回来的数据外层code=0，内层code!=0，所以添加code用于区别
        var sub_msg: String = "",//code 对应的错误信息
        @SerializedName("post")
        var freeShipping: Int = -1, // 1：包邮  2：不包邮
        @SerializedName("user_type")
        var traderType: Int = -1,  //商家类型 1：天猫商家  0：淘宝商家
        @SerializedName("seller")
        var seller: SellerBean = SellerBean(), //商家详细信息
        @SerializedName("videos")
        var productVideo: ProductDetailVideos = ProductDetailVideos(), //产品详情video
        @SerializedName("share")
        var share: WechatShareBean = WechatShareBean(), //微信分享数据
        @SerializedName("labels")
        val labels: MutableList<TagItemBean> = ArrayList(),
        var productImageDetailUrl: String = "",  //产品详情图片url
        var actual_price: String = "",
        var cat_leaf_name: String = "",
        var cat_name: String = "",
        var category_id: String = "",
        var coupon_click_url: String = "",
        var coupon_end_time: String = "",
        var coupon_info: String = "",
        var coupon_remain_count: Long = 0,
        var coupon_start_time: String = "",
        var coupon_total_count: Long = 0,
        var coupon_type: Int = 0,
        var fanli: String = "",
        var image_detail: ArrayList<ImageDetail> = ArrayList(),
        var item_id: String = "",
        var item_url: String = "",
        var store_detail: StoreDetailRespons = StoreDetailRespons(),
        var material_lib_type: String = "",
        var max_commission_rate: String = "",
        var nick: String = "",
        var num_iid: String = "",
        var pict_url: String = "",
        var provcity: String = "",
        var reserve_price: String = "",
        var seller_id: String = "",
        var small_images: SmallImages = SmallImages(ArrayList(), -1),
        var title: String = "",
        var volume: Long = 0,
        var zk_final_price: String = "",
        var coupon_money: String = "",
        var uiType: Int = -1
) : MultiItemEntity {
    override fun getItemType(): Int {
        return uiType
    }
}
