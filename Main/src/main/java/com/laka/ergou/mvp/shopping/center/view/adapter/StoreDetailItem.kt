package com.laka.ergou.mvp.shopping.center.view.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.base.adapter.MultipleAdapterItem
import com.laka.androidlib.util.GlideUtil
import com.laka.ergou.R
import com.laka.ergou.mvp.shop.model.bean.SellerBean

/**
 * @Author:summer
 * @Date:2019/4/28
 * @Description:商家详情item
 */
class StoreDetailItem : MultipleAdapterItem<SellerBean> {

    @SuppressLint("Range")
    override fun convert(helper: BaseViewHolder, item: SellerBean?) {
        R.layout.item_shop_detail_store_details
        val llContent = helper.getView<View>(R.id.ll_content)
        val llSkeleton = helper.getView<View>(R.id.ll_skeleton)
        val ivIcon = helper.getView<ImageView>(R.id.iv_icon)
        val tvProductDes = helper.getView<TextView>(R.id.tv_product_des)
        val tvBusineServe = helper.getView<TextView>(R.id.tv_business_serve)
        val tvLogisticServe = helper.getView<TextView>(R.id.tv_logistics_serve)
        val tvStoreName = helper.getView<TextView>(R.id.tv_store_name)
        val tvProductDesScore = helper.getView<TextView>(R.id.tv_product_des_score)
        val tvProductDesLevel = helper.getView<TextView>(R.id.tv_product_des_level)
        val tvBusinessServeScore = helper.getView<TextView>(R.id.tv_business_serve_score)
        val tvBusinessServeLevel = helper.getView<TextView>(R.id.tv_business_serve_level)
        val tvLogisticsServeScore = helper.getView<TextView>(R.id.tv_logistics_serve_score)
        val tvLogisticsServeLevel = helper.getView<TextView>(R.id.tv_logistics_serve_level)
        item?.let {
            if (it.evaluates.size <= 0 && TextUtils.isEmpty(it.shopName)) {
                llContent.visibility = View.GONE
                llSkeleton.visibility = View.VISIBLE
                return
            } else {
                llContent.visibility = View.VISIBLE
                llSkeleton.visibility = View.GONE
            }
            GlideUtil.loadImage(helper?.convertView.context, it.shopIcon, R.drawable.default_img, R.drawable.default_img, ivIcon)
            tvStoreName.text = it.shopName
            val list = it.evaluates
            list?.let {
                if (list.size >= 1) {
                    val evaluate = it[0]
                    tvProductDes.text = "宝贝描述"
                    tvProductDesScore.text = "${evaluate.score}"
                    tvProductDesScore.setTextColor(Color.parseColor(evaluate.levelTextColor))
                    tvProductDesLevel.text = evaluate.levelText
                    tvProductDesLevel.setBackgroundColor(Color.parseColor(evaluate.levelTextColor))
                }
                if (list.size >= 2) {
                    val evaluate = it[1]
                    tvBusineServe.text = "卖家服务"
                    tvBusinessServeScore.text = "${evaluate.score}"
                    tvBusinessServeScore.setTextColor(Color.parseColor(evaluate.levelTextColor))
                    tvBusinessServeLevel.text = evaluate.levelText
                    tvBusinessServeLevel.setBackgroundColor(Color.parseColor(evaluate.levelTextColor))
                }
                if (list.size >= 3) {
                    val evaluate = it[2]
                    tvLogisticServe.text = "物流服务"
                    tvLogisticsServeScore.text = "${evaluate.score}"
                    tvLogisticsServeScore.setTextColor(Color.parseColor(evaluate.levelTextColor))
                    tvLogisticsServeLevel.text = evaluate.levelText
                    tvLogisticsServeLevel.setBackgroundColor(Color.parseColor(evaluate.levelTextColor))
                }
            }
        }
    }
}