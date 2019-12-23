package com.laka.ergou.mvp.freedamission.view.adapter

import android.support.constraint.ConstraintLayout
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.base.adapter.MultipleAdapterItem
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.NumberUtils
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.TextViewHelper
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.freedamission.constant.FreeAdmissionContant
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon

/**
 * @Author:Rayman
 * @Date:2018/12/21
 * @Description:商品列表单列
 */
class FreeAdmissionProductListItem : MultipleAdapterItem<ProductWithCoupon> {
    var adapter: BaseQuickAdapter<ProductWithCoupon, BaseViewHolder>? = null

    constructor(adapter: BaseQuickAdapter<ProductWithCoupon, BaseViewHolder>? = null) {
        this.adapter = adapter
    }


    override fun convert(helper: BaseViewHolder?, item: ProductWithCoupon?) {
        GlideUtil.loadFilletImage(helper?.convertView?.context,
                item?.getSmallPic(),
                R.drawable.default_img,
                R.drawable.default_img,
                helper?.getView(R.id.iv_product_pic))

        R.layout.item_free_admission_product_list
        helper?.setText(R.id.tv_product_name, item?.productName)
        val clProductMinCoupon = helper?.getView<ConstraintLayout>(R.id.cl_product_min_coupon)
        val clProductMaxCoupon = helper?.getView<ConstraintLayout>(R.id.cl_product_max_coupon)
        val tvProductMaxCoupon = helper?.getView<TextView>(R.id.tv_product_max_coupon)
        val tvProductMinCoupon = helper?.getView<TextView>(R.id.tv_product_min_coupon)
        val tvProductPrice = helper?.getView<TextView>(R.id.tv_product_price)
        val tvTmallPrice = helper?.getView<TextView>(R.id.tv_product_tmall_price)
        val tvProductCommission = helper?.getView<TextView>(R.id.tv_product_commission)
        val tvVolumeCount = helper?.getView<TextView>(R.id.tv_volume)
        val ivIcon = helper?.getView<ImageView>(R.id.iv_icon)
        val tvStoreName = helper?.getView<TextView>(R.id.tv_store_name)
        val llContent = helper?.getView<ConstraintLayout>(R.id.ll_content)
        val constraintLayoutParams: ConstraintLayout.LayoutParams = tvProductCommission?.layoutParams as ConstraintLayout.LayoutParams
        adapter?.let {
            val headerCount = it.headerLayoutCount
            if (headerCount == 1) {
                if (helper?.layoutPosition == 1) {
                    val lp = ConstraintLayout.LayoutParams(llContent!!.layoutParams)
                    lp.setMargins(0, -ScreenUtils.dp2px(20.0f), 0, 0)
                    llContent?.layoutParams = lp
                } else {
                    val lp = ConstraintLayout.LayoutParams(llContent!!.layoutParams)
                    lp.setMargins(0, 0, 0, 0)
                    llContent?.layoutParams = lp
                }
            }

        }
        item?.let {
            // 获取coupon的优惠数据，获取到具体的优惠价格
            // 提取差价，根据是否有coupon优惠信息显示或隐藏列表对应TAG
            var originalPrice = BigDecimalUtils.numberFormat(item.productPrice).toFloat()

            if (it.hasCoupon == 1) { //有优惠券
                val couponMoney = BigDecimalUtils.numberFormat(item.couponMoney).toFloat()
                handleCouponView(couponMoney, clProductMaxCoupon, clProductMinCoupon,
                        tvProductMaxCoupon, tvProductMinCoupon,
                        it.largeCoupon, constraintLayoutParams)
            } else {
                handleEmptryCoupon(clProductMaxCoupon, clProductMinCoupon, constraintLayoutParams)
            }

            tvProductPrice?.text = BigDecimalUtils.numberFormat(it.actualPrice)
            TextViewHelper.setSpan(TextViewHelper.Builder()
                    .setTextView(tvTmallPrice)
                    .setText("原价¥${BigDecimalUtils.numberFormat(originalPrice)}")
                    .setStrike(true))

            // 0元购不适用 fanli 字段，这里使用 subsidy
            if (item.isFirst == 1) {  //首单
                tvProductCommission?.text = ResourceUtils.getStringWithArgs(R.string.first_product_commission, BigDecimalUtils.numberFormat("${item?.subsidy}"))
            } else {
                tvProductCommission?.text = ResourceUtils.getStringWithArgs(R.string.normal_product_commission, BigDecimalUtils.numberFormat("${item?.subsidy}"))
            }
            if (TextUtils.isEmpty(item.subsidy)) {
                tvProductCommission?.visibility = View.GONE
            } else {
                tvProductCommission?.visibility = View.VISIBLE
            }

            //有销量字段则显示，没有则隐藏  （0元购才有，现在0元购独立出去）
            if (!TextUtils.isEmpty(item.sellCount)) {
                val volume = NumberUtils.convertNumberToSellCount(BigDecimalUtils.numberFormat(item.sellCount).toInt())
                tvVolumeCount?.text = ResourceUtils.getStringWithArgs(R.string.product_sell_count, volume)
                tvVolumeCount?.visibility = View.VISIBLE
            } else {
                tvVolumeCount?.visibility = View.GONE
            }
            //商店名稱
            tvStoreName?.text = item.sellerShopName
            when (item.sellerType) {
                FreeAdmissionContant.TYPE_USER_TMALL -> { //天猫
                    ivIcon?.setImageResource(R.drawable.label_icon_taobao)
                }
                FreeAdmissionContant.TYPE_USER_TAOBAO -> {  //淘宝
                    ivIcon?.setImageResource(R.drawable.label_icon_tmall)
                }
                else -> {
                }
            }
        }
    }

    private fun handleCouponView(couponMoney: Float, clProductMaxCoupon: ConstraintLayout?, clProductMinCoupon: ConstraintLayout?,
                                 tvMaxCoupon: TextView?, tvMinCoupon: TextView?, largeCoupon: Int,
                                 constraintLayoutParams: ConstraintLayout.LayoutParams) {
        if (largeCoupon == 1) {  //有大额券
            clProductMaxCoupon?.visibility = View.VISIBLE
            clProductMinCoupon?.visibility = View.GONE
            tvMaxCoupon?.text = "¥${BigDecimalUtils.numberFormat(couponMoney)}"
        } else {
            clProductMaxCoupon?.visibility = View.GONE
            clProductMinCoupon?.visibility = View.VISIBLE
            tvMinCoupon?.text = "¥${BigDecimalUtils.numberFormat(couponMoney)}"
        }
        constraintLayoutParams.bottomToBottom = R.id.cl_coupon
        constraintLayoutParams.leftToRight = R.id.cl_coupon
        constraintLayoutParams.topToTop = R.id.cl_coupon
        constraintLayoutParams.leftMargin = ScreenUtils.dp2px(10f)
    }

    private fun handleEmptryCoupon(clProductMaxCoupon: ConstraintLayout?, clProductMinCoupon: ConstraintLayout?, constraintLayoutParams: ConstraintLayout.LayoutParams) {
        clProductMaxCoupon?.visibility = View.GONE
        clProductMinCoupon?.visibility = View.GONE
        // 假若不存在优惠券，将补贴靠左显示
        constraintLayoutParams.bottomMargin = ScreenUtils.dp2px(3f)
        constraintLayoutParams.leftMargin = ScreenUtils.dp2px(15f)
        constraintLayoutParams.leftToRight = R.id.iv_product_pic
        constraintLayoutParams.topToTop = ConstraintLayout.LayoutParams.UNSET
        constraintLayoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
    }


}