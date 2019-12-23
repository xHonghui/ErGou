package com.laka.ergou.mvp.shopping.center.model.bean

/**
 * @Author:Rayman
 * @Date:2018/12/24
 * @Description:商品列表页筛选Bean
 */
data class ProductFilter(
        var productName: String,
        var productLocation: String,
        var productMinPrice: Float,
        var productMaxPrice: Float)