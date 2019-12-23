package com.laka.ergou.mvp.shopping.center.view.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.ergou.R
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon

/**
 * @Author:Rayman
 * @Date:2018/12/17
 * @Description:商品列表Adapter
 */
class ProductListAdapter : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    /**
     * description:ListMode对应列表模式
     * itemType对应列表数据样式
     **/
    private var listMode = 0
    private var itemType = 0
    private var isChangeMode = false

    // 定义两个处理Item
    private var productListItem: ProductListItem
    private var productGridItem: ProductGridItem

    constructor(data: List<ProductWithCoupon>) : super(data) {
        addItemType(ShoppingCenterConstant.LIST_UI_TYPE_COMMON, R.layout.item_product_list)
        addItemType(ShoppingCenterConstant.LIST_UI_TYPE_GRID, R.layout.item_product_grid)
        productListItem = ProductListItem()
        productGridItem = ProductGridItem()
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        // 先根据ItemType判断，再判断当前列表
        when (itemType) {
            ShoppingCenterConstant.LIST_UI_TYPE_COMMON -> productListItem?.convert(helper, item as ProductWithCoupon?)
            ShoppingCenterConstant.LIST_UI_TYPE_GRID -> productGridItem?.convert(helper, item as ProductWithCoupon?)
        }
    }

    override fun getItemViewType(position: Int): Int {
        // 假若position大于当前列表size的（存在loadMoreView的情况。就返回super）
        itemType = if (position < data.size) {
            // 假若是手动设置的，就返回手动设置的type
            if (super.getItemViewType(position) == ShoppingCenterConstant.LIST_UI_TYPE_COMMON
                    || super.getItemViewType(position) == ShoppingCenterConstant.LIST_UI_TYPE_GRID) {
                listMode
            } else {
                super.getItemViewType(position)
            }
        } else {
            super.getItemViewType(position)
        }
        return itemType
    }

    /**
     * description:切换列表模式
     **/
    fun switchUIMode(@ShoppingCenterConstant.ProductListUiType uiType: Int) {
        mData?.forEach {
            if (it is ProductWithCoupon) {
                it.uiType = uiType
            }
        }
        listMode = uiType
        //LogUtils.error("输出listMode：$listMode")
        super.setSpanSizeLookup { _, _ ->
            if (uiType == ShoppingCenterConstant.LIST_UI_TYPE_COMMON) {
                2
            } else {
                1
            }
        }
        notifyDataSetChanged()
    }
}