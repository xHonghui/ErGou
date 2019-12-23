package com.laka.ergou.mvp.shop.view.adapter

import android.support.v7.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.ergou.R
import com.laka.ergou.mvp.shopping.center.view.adapter.ProductAdvertBannerItem
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shop.model.bean.*
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.shopping.center.view.adapter.*


/**
 * @Author:summer
 * @Date:2018/12/25
 * @Description: 商品详情列表
 */
class ShopDetailListAdapter : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private var productGridItem: ProductGridItem? = null
    private var productDetailBannerItem: ProductDetailBannerItem? = null
    private var productDetailItem: ProductDetailItem? = null
    private var productDetailMoreItem: ProductDetailMoreItem? = null
    private var productDetailImageItem: ProductDetailImageItem? = null
    private var storeDetailItem: StoreDetailItem? = null
    private var mAdvertBannerItem: ProductAdvertBannerItem? = null

    constructor(data: ArrayList<MultiItemEntity>) : super(data) {
        addItemType(ShopDetailConstant.SHOP_DETAIL_BANNER, R.layout.item_shop_detail_banner)
        addItemType(ShopDetailConstant.SHOP_DETAIL_BASIC, R.layout.item_shop_detail_basic)
        addItemType(ShopDetailConstant.SHOP_DETAIL_MORE, R.layout.item_shop_detail_more)
        addItemType(ShopDetailConstant.SHOP_DETAIL_RECOMMEND_TITLE, R.layout.item_shop_detail_recommend_title)
        addItemType(ShopDetailConstant.SHOP_DETAIL_RECOMMEND_ITEM, R.layout.item_product_grid)
        addItemType(ShopDetailConstant.SHOP_DETAIL_IMAGE_DETAIL, R.layout.item_shop_detail_image_detail)
        addItemType(ShopDetailConstant.SHOP_DETAIL_STORE_DETAIL, R.layout.item_shop_detail_store_details)
        addItemType(ShopDetailConstant.SHOP_DETAIL_ADVERT_BANNER, R.layout.item_shop_detail_advert_banner)
        productGridItem = ProductGridItem()
        productDetailBannerItem = ProductDetailBannerItem()
        productDetailItem = ProductDetailItem()
        productDetailMoreItem = ProductDetailMoreItem()
        productDetailImageItem = ProductDetailImageItem()
        storeDetailItem = StoreDetailItem()
        mAdvertBannerItem = ProductAdvertBannerItem()
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        val layoutParams = helper?.itemView?.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams?.let {
            when (helper?.itemViewType) {
                ShopDetailConstant.SHOP_DETAIL_BANNER -> {
                    layoutParams.isFullSpan = true
                    productDetailBannerItem?.convert(helper, item as? ProductBannerList)
                }
                ShopDetailConstant.SHOP_DETAIL_BASIC -> {
                    layoutParams.isFullSpan = true
                    productDetailItem?.convert(helper, item as? CustomProductDetail)
                }
                ShopDetailConstant.SHOP_DETAIL_MORE -> {
                    layoutParams.isFullSpan = true
                    productDetailMoreItem?.convert(helper, item as? TitleTypeBean)
                }
                ShopDetailConstant.SHOP_DETAIL_RECOMMEND_TITLE -> {
                    layoutParams.isFullSpan = true
                }
                ShopDetailConstant.SHOP_DETAIL_RECOMMEND_ITEM -> {
                    layoutParams.isFullSpan = false
                    productGridItem?.convert(helper, item as? ProductWithCoupon)
                }
                ShopDetailConstant.SHOP_DETAIL_IMAGE_DETAIL -> {
                    layoutParams.isFullSpan = true
                    productDetailImageItem?.convert(helper, item as? ImageDetail)
                }
                ShopDetailConstant.SHOP_DETAIL_STORE_DETAIL -> {
                    layoutParams.isFullSpan = true
                    storeDetailItem?.convert(helper, item as? SellerBean)
                }
                ShopDetailConstant.SHOP_DETAIL_ADVERT_BANNER -> {
                    layoutParams.isFullSpan = true
                    mAdvertBannerItem?.convert(helper, item as? AdvertBannerListBean)
                }
                else -> {

                }
            }
        }
    }

    fun onStart() {
        mAdvertBannerItem?.onStart()
        productDetailBannerItem?.onStart()
    }

    fun onPause() {
        mAdvertBannerItem?.onPause()
        productDetailBannerItem?.onPause()
    }

    fun release() {
        productDetailBannerItem?.release()
        mAdvertBannerItem?.release()
    }

}