package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/4/29
 * @Description:商品列表数据响应体
 */
class ProductListResponse(
        @SerializedName("is_more")
        var isMore: Int = 1,
        @SerializedName("search")
        var searchKey: String = "",
        @SerializedName("img_path")
        val imgPath: String = "",
        @SerializedName("results")
        var results: ArrayList<ProductWithCoupon>)