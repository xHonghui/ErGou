package com.laka.ergou.mvp.shopping.center.view.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon

/**
 * @Author:summer
 * @Date:2019/4/29
 * @Description:商品专题List样式适配器
 */
class ShoppingSpecialListAdapter : BaseQuickAdapter<ProductWithCoupon, BaseViewHolder> {

    private var productListItem:ProductListItem = ProductListItem(this)

    constructor(layoutResId: Int, data: MutableList<ProductWithCoupon>?) : super(layoutResId, data)

    override fun convert(helper: BaseViewHolder?, item: ProductWithCoupon?) {
        productListItem.convert(helper,item)
    }

}