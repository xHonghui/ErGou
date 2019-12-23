package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:Rayman
 * @Date:2019/1/15
 * @Description:商品列表主页---精选商品Banner--数据Bean
 */
data class ProductFavorite(
        @SerializedName("favorites_id") var favoritesId: String,
        @SerializedName("favorites_title") var favoritesTitle: String,
        @SerializedName("type") var productType: String,
        @SerializedName("banner") var productPic: String)
