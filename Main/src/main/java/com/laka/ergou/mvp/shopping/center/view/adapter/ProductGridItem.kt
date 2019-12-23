package com.laka.ergou.mvp.shopping.center.view.adapter

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.text.SpannableString
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.base.adapter.MultipleAdapterItem
import com.laka.androidlib.util.*
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.common.util.SpannableStringUtils
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils
import com.laka.ergou.mvp.shop.utils.ShopStringUtils
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.shopping.center.weight.TagLayout

/**
 * @Author:Rayman
 * @Date:2018/12/21
 * @Description:商品列表Grid类型
 */
class ProductGridItem : MultipleAdapterItem<ProductWithCoupon> {
    var adapter: BaseQuickAdapter<ProductWithCoupon, BaseViewHolder>? = null

    constructor(adapter: BaseQuickAdapter<ProductWithCoupon, BaseViewHolder>? = null) {
        this.adapter = adapter
    }

    override fun convert(helper: BaseViewHolder?, item: ProductWithCoupon?) {
        R.layout.item_product_grid
        GlideUtil.loadImage(helper?.convertView?.context,
                "${item?.getSmallPic()}",
                R.drawable.default_img,
                R.drawable.default_img,
                helper?.getView(R.id.iv_product_pic))
        LogUtils.info("imagePath-----${item?.getSmallPic()}")
        //helper?.setText(R.id.tv_product_name, item?.productName)
        val tvProductName = helper?.getView<TextView>(R.id.tv_product_name)
        var tvMinCoupon = helper?.getView<TextView>(R.id.tv_product_min_coupon)
        var clMinCoupon = helper?.getView<ConstraintLayout>(R.id.cl_product_min_coupon)
        val clMaxCoupon = helper?.getView<ConstraintLayout>(R.id.cl_product_max_coupon)
        val tvProductMaxCoupon = helper?.getView<TextView>(R.id.tv_product_max_coupon)
        val clProductCommissionRoot = helper?.getView<ConstraintLayout>(R.id.cl_product_commission_root)
        val clCouponRoot = helper?.getView<ConstraintLayout>(R.id.cl_coupon_root)
        val tvPrice = helper?.getView<TextView>(R.id.tv_product_price)
        val tvOriginPrice = helper?.getView<TextView>(R.id.tv_product_origin_price)
        val tvCommission = helper?.getView<TextView>(R.id.tv_product_commission)
        val tvVolumeCount = helper?.getView<TextView>(R.id.tv_product_sell_count)
        val tvStoreName = helper?.getView<TextView>(R.id.tv_store_name)
        val cardView = helper?.getView<CardView>(R.id.card_view)
        val tagLayout = helper?.getView<TagLayout>(R.id.tag_layout)
        adapter?.let {
            val headerCount = it.headerLayoutCount
            if (headerCount == 1) {
                if (helper?.layoutPosition == 1 || helper?.layoutPosition == 2) {
                    val lp = FrameLayout.LayoutParams(cardView!!.layoutParams)
                    lp.setMargins(0, -ScreenUtils.dp2px(20.0f), 0, 0)
                    cardView?.layoutParams = lp
                    LogUtils.info("layoutParamsTopMargin---------${lp.topMargin}")
                } else {
                    val lp = FrameLayout.LayoutParams(cardView!!.layoutParams)
                    lp.setMargins(0, 0, 0, 0)
                    cardView?.layoutParams = lp
                }
            }
        }
        val constraintLayoutParams: ConstraintLayout.LayoutParams = tvCommission?.layoutParams as ConstraintLayout.LayoutParams
        val commissionRootViewLayoutParams: ConstraintLayout.LayoutParams = clProductCommissionRoot?.layoutParams as ConstraintLayout.LayoutParams

        item?.let {
            tvProductName?.text = it.productName

            // 获取coupon的优惠数据，获取到具体的优惠价格
            // 提取差价，根据是否有coupon优惠信息显示或隐藏列表对应TAG
            var originalPrice = BigDecimalUtils.numberFormat(item.productPrice).toFloat()

            if (it.hasCoupon == 1) { //有优惠券
                val couponMoney = BigDecimalUtils.numberFormat(item.couponMoney).toFloat()
                handleCouponView(couponMoney, clMaxCoupon, clMinCoupon,
                        tvProductMaxCoupon, tvMinCoupon, constraintLayoutParams,
                        commissionRootViewLayoutParams, clCouponRoot?.id!!, it.largeCoupon)
            } else {
                handleEmptryCoupon(clMinCoupon, clMaxCoupon, constraintLayoutParams, commissionRootViewLayoutParams)
            }
            tvPrice?.text = BigDecimalUtils.numberFormat(it.actualPrice)
            TextViewHelper.setSpan(TextViewHelper.Builder()
                    .setTextView(tvOriginPrice)
                    .setText("¥${BigDecimalUtils.numberFormat(originalPrice)}")
                    .setStrike(true))

            //标签
            if (it.labels != null && it.labels.size > 0) {
                tagLayout?.visibility = View.VISIBLE
                tagLayout?.setTagNumsList(it.labels)
            } else {
                tagLayout?.visibility = View.GONE
            }

            if (TextUtils.isEmpty(item.commissionPrice)) {
                tvCommission?.visibility = View.GONE
            } else {
                tvCommission?.visibility = View.VISIBLE
                tvCommission?.text = ResourceUtils.getStringWithArgs(R.string.product_commission, BigDecimalUtils.numberFormat(item.commissionPrice))
            }

            if (!TextUtils.isEmpty(item.sellCount)) {
                val volume = NumberUtils.convertNumberToSellCount(BigDecimalUtils.numberFormat(item.sellCount).toInt())
                tvVolumeCount?.text = ResourceUtils.getStringWithArgs(R.string.product_sell_count, volume)
                tvVolumeCount?.visibility = View.VISIBLE
            } else {
                tvVolumeCount?.visibility = View.GONE
            }

            //店铺
            if (TextUtils.isEmpty(item.sellerShopName)) {
                tvStoreName?.visibility = View.GONE
            } else {
                tvStoreName?.visibility = View.VISIBLE
                //商家类型 1：天猫商家  0：淘宝商家
                var titleSpann = SpannableString(it.sellerShopName)
                if (it.sellerType == "0") { //淘宝商家
                    titleSpann = SpannableStringUtils.makeImageSpannableString(0, 6, R.drawable.label_icon_taobao, "[商家类型] " + it.sellerShopName)
                } else if (it.sellerType == "1") {//天猫商家
                    titleSpann = SpannableStringUtils.makeImageSpannableString(0, 6, R.drawable.label_icon_tmall, "[商家类型] " + it.sellerShopName)
                }
                tvStoreName?.text = titleSpann
            }
        }
    }

    private fun handleCouponView(couponMoney: Float, clMaxCoupon: ConstraintLayout?, clMinCoupon: ConstraintLayout?,
                                 tvMaxCoupon: TextView?, tvMinCoupon: TextView?, constraintLayoutParams: ConstraintLayout.LayoutParams,
                                 commissionRootViewLayoutParams: ConstraintLayout.LayoutParams, targetId: Int, largeCoupon: Int) {
        if (largeCoupon == 1) { //有大额券
            clMaxCoupon?.visibility = View.VISIBLE
            clMinCoupon?.visibility = View.INVISIBLE
            tvMaxCoupon?.text = "¥${BigDecimalUtils.numberFormat(couponMoney)}"
        } else {
            clMaxCoupon?.visibility = View.INVISIBLE
            clMinCoupon?.visibility = View.VISIBLE
            tvMinCoupon?.text = "¥${BigDecimalUtils.numberFormat(couponMoney)}"
        }
        // 假若存在优惠券，将补贴靠右显示
        constraintLayoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
        constraintLayoutParams.leftToLeft = ConstraintLayout.LayoutParams.UNSET
        constraintLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        constraintLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        //
        commissionRootViewLayoutParams.leftToRight = targetId
    }

    private fun handleEmptryCoupon(mClCouponMin: ConstraintLayout?, mClCouponMax: ConstraintLayout?,
                                   constraintLayoutParams: ConstraintLayout.LayoutParams,
                                   commissionRootViewLayoutParams: ConstraintLayout.LayoutParams) {
        mClCouponMin?.visibility = View.INVISIBLE
        mClCouponMax?.visibility = View.INVISIBLE
        // 假若不存在优惠券，将补贴靠左显示
        constraintLayoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        constraintLayoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        constraintLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        constraintLayoutParams.rightToRight = ConstraintLayout.LayoutParams.UNSET
        //
        commissionRootViewLayoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
    }
}