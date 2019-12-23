package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/7/30
 * @Description:商品列表响应体
 */
data class ShoppingListResponse(
        @SerializedName("list")
        val list: ArrayList<ProductWithCoupon> = ArrayList(),
        @SerializedName("total_results")
        val totalResults: Int = 0,
        @SerializedName("timestamp")
        val timestamp: Long = 0
)