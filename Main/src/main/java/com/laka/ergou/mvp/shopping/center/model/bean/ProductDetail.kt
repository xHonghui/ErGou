package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName
import com.laka.ergou.mvp.login.model.bean.User


/**
 * @Author:Rayman
 * @Date:2018/12/20
 * @Description:
 */
data class ProductDetail(
        @SerializedName("product_info") var baseProduct: BaseProduct,
        @SerializedName("user_info") var user: User,
        @SerializedName("test_int") var testInt: Int,
        @SerializedName("test_float") var testFloat: Float,
        @SerializedName("test_double") var testDouble: Double,
        @SerializedName("test_long") var testLong: Long
)