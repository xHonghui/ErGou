package com.laka.ergou.mvp.commission.view.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.ext.setNumTypeface
import com.laka.ergou.R
import com.laka.ergou.mvp.commission.constant.CommissionConstant
import com.laka.ergou.mvp.commission.model.bean.CommissionDIYBean

class CommissionListAdapter : BaseQuickAdapter<CommissionDIYBean, BaseViewHolder> {

    constructor(layoutResId: Int, data: MutableList<CommissionDIYBean>?) : super(layoutResId, data)

    override fun convert(helper: BaseViewHolder, item: CommissionDIYBean) {
        R.layout.item_commission_list
        with(item) {
            when (type) {
                CommissionConstant.TAOBAO -> {
                    helper.setText(R.id.tv_title, "付款订单预估收入")
                            .setText(R.id.tv_left_key, "付款订单数")
                            .setText(R.id.tv_right_key, "预估结算补贴")
                }
                CommissionConstant.AGENT -> {
                    helper.setText(R.id.tv_title, "战友购物提成预估收入")
                            .setText(R.id.tv_left_key, "提成订单数")
                            .setText(R.id.tv_right_key, "预估结算补贴")
                }
                CommissionConstant.PROMOTION -> {
                    helper.setText(R.id.tv_title, "邀请战友预估收入")
                            .setText(R.id.tv_left_key, "邀请战友数")
                            .setText(R.id.tv_right_key, "预估可用补贴")
                }
                CommissionConstant.OTHER -> {
                    helper.setText(R.id.tv_title, "其他奖励预估收入（活动、任务等）")
                            .setText(R.id.tv_left_key, "获得奖励次数")
                            .setText(R.id.tv_right_key, "预估可用补贴")
                }
                else -> {

                }
            }
            helper.setText(R.id.tv_left_value, leftNum)
                    .setText(R.id.tv_center_value, "¥$centerNum")
                    .setText(R.id.tv_right_value, "¥$rightNum")

            helper?.getView<TextView>(R.id.tv_detail).setOnClickListener {
                onItemClick(helper?.getView<TextView>(R.id.tv_detail), helper?.position)
            }
            helper?.getView<ImageView>(R.id.iv_detail).setOnClickListener {
                onItemClick(helper?.getView<ImageView>(R.id.iv_detail), helper?.position)
            }
        }
        setTextTypeFace(helper)
    }

    private fun setTextTypeFace(helper: BaseViewHolder) {
        helper.getView<TextView>(R.id.tv_left_value).setNumTypeface()
        helper.getView<TextView>(R.id.tv_right_value).setNumTypeface()
        helper.getView<TextView>(R.id.tv_center_value).setNumTypeface()
    }

    fun onItemClick(view: View, pos: Int) {
        if (onItemChildClickListener != null) {
            onItemChildClickListener?.onItemChildClick(this, view, pos)
        }
    }

}
