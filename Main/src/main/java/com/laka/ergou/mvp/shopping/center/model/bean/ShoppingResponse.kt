package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:Rayman
 * @Date:2018/12/25
 * @Description:商品模块--商品列表响应类
 */
data class ShoppingResponse(@SerializedName("results")
                            var baseProductList: ArrayList<ProductWithCoupon>,
                            @SerializedName("search")
                            var searchKey: String = "",
                            @SerializedName("total_results")
                            var total_result: Int = 0)