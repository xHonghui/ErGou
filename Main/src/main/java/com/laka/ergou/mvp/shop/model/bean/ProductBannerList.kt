package com.laka.ergou.mvp.shop.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/4/15
 * @Description:产品详情banner列表
 */
class ProductBannerList(var imageList: ArrayList<ProductBannerBean>, var uiType: Int
) : MultiItemEntity {
    override fun getItemType(): Int {
        return uiType
    }
}