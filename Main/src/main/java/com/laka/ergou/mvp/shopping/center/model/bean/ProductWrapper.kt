package com.laka.ergou.mvp.shopping.center.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @Author:Rayman
 * @Date:2018/12/25
 * @Description:商品Bean外部包裹类，主要用来设置ViewType
 */
class ProductWrapper(var data: List<BaseProduct>) : MultiItemEntity {

    override fun getItemType(): Int {
        return itemType
    }


}