package com.laka.ergou.mvp.shopping.center.view.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon

/**
 * @Author:summer
 * @Date:2019/4/29
 * @Description:商品专题Grid样式适配器
 */
class ShoppingSpecialGridAdapter : BaseQuickAdapter<ProductWithCoupon, BaseViewHolder> {

    private var productGridItem: ProductGridItem = ProductGridItem(this)

    constructor(layoutResId: Int, data: MutableList<ProductWithCoupon>?) : super(layoutResId, data)

    override fun convert(helper: BaseViewHolder?, item: ProductWithCoupon?) {
        productGridItem.convert(helper,item)
    }

}