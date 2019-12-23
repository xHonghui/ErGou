package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/4/29
 * @Description:商品列表响应体
 */
class GoodsListResponse(
        @SerializedName("is_more")
        var isMore: Int = 1,
        @SerializedName("search")
        var searchKey: String = "",
        @SerializedName("results")
        var results: ArrayList<ProductWithCoupon>)