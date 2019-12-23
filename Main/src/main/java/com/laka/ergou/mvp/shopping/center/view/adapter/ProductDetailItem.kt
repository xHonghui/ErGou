package com.laka.ergou.mvp.shopping.center.view.adapter

import android.text.SpannableString
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.base.adapter.MultipleAdapterItem
import com.laka.androidlib.util.TextViewHelper
import com.laka.ergou.R
import com.laka.ergou.common.util.SpannableStringUtils
import com.laka.ergou.mvp.shop.model.bean.CustomProductDetail
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils
import com.laka.ergou.mvp.shopping.center.weight.TagLayout

/**
 * @Author:summer
 * @Date:2019/4/28
 * @Description:产品详情页面---> 详情数据item
 */
class ProductDetailItem : MultipleAdapterItem<CustomProductDetail> {

    override fun convert(helper: BaseViewHolder, item: CustomProductDetail?) {
        R.layout.item_shop_detail_basic
        val tvTradeName = helper.itemView.findViewById<TextView>(R.id.text_view_trade_name)
        val tvVolumn = helper.itemView.findViewById<TextView>(R.id.text_view_volumn)
        val tvOriginPrice = helper.itemView.findViewById<TextView>(R.id.text_view_origin_price)
        val tvRealPrice = helper.itemView.findViewById<TextView>(R.id.text_view_real_price)
        val tvCommissionFanli = helper.itemView.findViewById<TextView>(R.id.tv_commission_fanli)
        val tagLayout = helper?.getView<TagLayout>(R.id.tag_layout)
        item?.let {
            tvVolumn.text = "${it.volume}人购买"
            TextViewHelper.setSpan(TextViewHelper.Builder()
                    .setTextView(tvOriginPrice)
                    .setText("原价¥${BigDecimalUtils.roundMode(it.zk_final_price)}")
                    .setStrike(true))
            tvCommissionFanli.text = "补贴 ¥${BigDecimalUtils.roundMode(it.fanli)}"
            tvRealPrice.text = "${it.actual_price}"

            var titleSpann = SpannableString(it.title)
            if (it.freeShipping == 1) { //包邮
                if (it.traderType == 0) { //淘宝商家
                    titleSpann = SpannableStringUtils.makeImageSpannableString(0, 6, R.drawable.label_icon_taobao, "[商家类型] [物流类型] " + it.title)
                } else if (it.traderType == 1) {//天猫商家
                    titleSpann = SpannableStringUtils.makeImageSpannableString(0, 6, R.drawable.label_icon_tmall, "[商家类型] [物流类型] " + it.title)
                }
                SpannableStringUtils.makeImageSpannableString(7, 13, R.drawable.default_icon_baoyou, titleSpann)
            } else { //不包邮
                if (it.traderType == 0) { //淘宝商家
                    titleSpann = SpannableStringUtils.makeImageSpannableString(0, 6, R.drawable.label_icon_taobao, "[商家类型] " + it.title)
                } else if (it.traderType == 1) {//天猫商家
                    titleSpann = SpannableStringUtils.makeImageSpannableString(0, 6, R.drawable.label_icon_tmall, "[商家类型] " + it.title)
                }
            }
            tvTradeName.text = titleSpann

            //标签
            if (it.labels != null && it.labels.size > 0) {
                tagLayout?.visibility = View.VISIBLE
                tagLayout?.setTagList(it.labels,12f)
            } else {
                tagLayout?.visibility = View.GONE
            }
        }
    }
}