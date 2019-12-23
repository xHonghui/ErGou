package com.laka.ergou.mvp.shop.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant

/**
 * @Author:summer
 * @Date:2019/4/1
 * @Description:商家信息响应体
 */
data class SellerBean(
        var evaluates: ArrayList<Evaluate> = ArrayList(),
        var shopName: String = "",
        var shopIcon: String = "",
        var viewType: Int = ShopDetailConstant.SHOP_DETAIL_STORE_DETAIL
) : MultiItemEntity {
    override fun getItemType(): Int {
        return viewType
    }
}

data class Evaluate(
        var score: String = "",
        var title: String = "",
        var level: String = "",  //评分等级
        var levelText: String = "高",  //评分等级描述
        var levelTextColor: String = "#fa5e5e",
        var levelBackgroundColor: String = "#fa5e5e"
)