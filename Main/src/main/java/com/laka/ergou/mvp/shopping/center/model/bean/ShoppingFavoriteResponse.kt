package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:Rayman
 * @Date:2019/1/15
 * @Description:商品列表主页精选数据Response
 */
data class ShoppingFavoriteResponse(@SerializedName("home_page") var homePage: ProductFavorite,
                                    @SerializedName("banners") var bannerList: ArrayList<ProductFavorite>,
                                    @SerializedName("cate") var categoryList: ArrayList<String>)