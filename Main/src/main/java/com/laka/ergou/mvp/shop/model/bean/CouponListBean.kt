package com.laka.ergou.mvp.shop.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant

/**
 * @Author:summer
 * @Date:2019/1/9
 * @Description: 商品详情，推荐列表数据类
 */
data class CouponListBean(
        val tbk_dg_item_coupon_get_response: TbkDgItemCouponGetResponse,
        val error_response: ErrorResponse
)

data class TbkDgItemCouponGetResponse(
        val request_id: String,
        val results: CouponListResult,
        val total_results: Int
)

data class CouponListResult(
        val tbk_coupon: ArrayList<TbkCoupon>
)

data class TbkCoupon(
        val category: Int,
        val commission_rate: String,
        val coupon_click_url: String,
        val coupon_end_time: String,
        val coupon_info: String,
        val coupon_remain_count: Int,
        val coupon_start_time: String,
        val coupon_total_count: Int,
        val item_description: String,
        val item_url: String,
        val nick: String,
        val num_iid: Long,
        val pict_url: String,
        val seller_id: Long,
        val shop_title: String,
        val small_images: CouponListSmallImages,
        val title: String,
        val user_type: Int,
        val volume: Int,
        val zk_final_price: String,
        val uiType: Int = ShopDetailConstant.SHOP_DETAIL_RECOMMEND_ITEM
) : MultiItemEntity {
    override fun getItemType(): Int {
        return uiType
    }
}

data class CouponListSmallImages(
        val string: List<String>
)