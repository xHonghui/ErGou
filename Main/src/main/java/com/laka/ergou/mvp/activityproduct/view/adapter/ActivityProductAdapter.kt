package com.laka.ergou.mvp.activityproduct.view.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon

/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:
 */
class ActivityProductAdapter : BaseQuickAdapter<ProductWithCoupon, BaseViewHolder> {

    private var productListItem: ActivityProductListItem = ActivityProductListItem(this)

    constructor(layoutResId: Int, data: MutableList<ProductWithCoupon>?) : super(layoutResId, data)

    override fun convert(helper: BaseViewHolder?, item: ProductWithCoupon?) {
        productListItem.convert(helper, item)
    }

}