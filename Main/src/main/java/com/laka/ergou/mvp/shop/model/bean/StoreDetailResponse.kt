package com.laka.ergou.mvp.shop.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant

/**
 * @Author:summer
 * @Date:2019/3/29
 * @Description:商家详情
 */
data class StoreDetailRespons(val viewType: Int = ShopDetailConstant.SHOP_DETAIL_STORE_DETAIL):MultiItemEntity{
    override fun getItemType(): Int {
        return viewType
    }
}