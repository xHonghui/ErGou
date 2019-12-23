package com.laka.ergou.mvp.teamaward.view.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.teamaward.model.bean.Order

class TearmAwardAdapter : BaseQuickAdapter<Order, BaseViewHolder> {

    constructor(layoutResId: Int, data: MutableList<Order>?) : super(layoutResId, data)

    override fun convert(helper: BaseViewHolder, item: Order) {
        R.layout.item_tearm_award
        val layoutParams = helper?.itemView?.layoutParams as? RecyclerView.LayoutParams
        if (helper?.position == 0) {
            layoutParams?.topMargin = ScreenUtils.dp2px(10f)
        } else {
            layoutParams?.topMargin = ScreenUtils.dp2px(0f)
        }
        setColorText(helper, item)
        setStatus(helper, item)
        helper.setText(R.id.tv_status, item.subsidy_word)
                .setText(R.id.tv_type, item.earn)

    }

    private fun setStatus(helper: BaseViewHolder, item: Order) {
        when (item.order_status) {
            12, 14 -> {
                //待结补贴
                helper.setBackgroundRes(R.id.tv_type, R.drawable.bg_tearm_award)
                        .setTextColor(R.id.tv_type, mContext.resources.getColor(R.color.color_txt_red))
                        .setText(R.id.tvDate, "付款时间：${item.create_time}")
            }
            3, 30 -> {
                //已结算
                helper.setBackgroundRes(R.id.tv_type, R.drawable.bg_tearm_award_g)
                        .setTextColor(R.id.tv_type, mContext.resources.getColor(R.color.color_05a585))
                        .setText(R.id.tvDate, "结算时间：${item.earning_time}")
            }
            13 -> {
                //已失效
                helper.setBackgroundRes(R.id.tv_type, R.drawable.bg_tearm_award_gray)
                        .setTextColor(R.id.tv_type, mContext.resources.getColor(R.color.color_929292))
                        .setText(R.id.tvDate, "付款时间：${item.create_time}")
            }
        }
    }

    private fun setColorText(helper: BaseViewHolder, item: Order) {
        val tvTitle = helper.getView<TextView>(R.id.tv_title)
        var name = "战友[${item.nickname}]"
        var context = "购物补贴提成${item.earn_rate}"
        var allContext = name + context
        val spannableString = SpannableString(allContext)
        spannableString.setSpan(ForegroundColorSpan(mContext.resources.getColor(R.color.color_main)), 2, name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTitle.text = spannableString
    }


}