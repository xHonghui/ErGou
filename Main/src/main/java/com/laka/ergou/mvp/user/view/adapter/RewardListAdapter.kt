package com.laka.ergou.mvp.user.view.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.model.bean.RewardListBean

/**
 * @Author:summer
 * @Date:2019/6/26
 * @Description:
 */
class RewardListAdapter : BaseQuickAdapter<RewardListBean, BaseViewHolder> {

    constructor(layoutResId: Int, data: MutableList<RewardListBean>?) : super(layoutResId, data)

    override fun convert(helper: BaseViewHolder?, item: RewardListBean?) {
        R.layout.item_reward_list
        val layoutParams = helper?.itemView?.layoutParams as? RecyclerView.LayoutParams
        if (helper?.position == 0) {
            layoutParams?.topMargin = ScreenUtils.dp2px(10f)
        } else {
            layoutParams?.topMargin = ScreenUtils.dp2px(0f)
        }
        val tvDes = helper?.getView<TextView>(R.id.tv_des)
        val tvTitle = helper?.getView<TextView>(R.id.tv_title)
        val tvRecordTime = helper?.getView<TextView>(R.id.tv_record_time)
        val tvOperationCash = helper?.getView<TextView>(R.id.tv_operation_cash)
        item?.let {
            tvTitle?.text = it.title
            tvDes?.text = it.commisionWord
            tvRecordTime?.text = it.createTime
            tvOperationCash?.text = it.earnWord
            when (it.orderStatus) {
                UserConstant.COMMISSION_ENABLE -> { //可提现
                    tvOperationCash?.setTextColor(ContextCompat.getColor(mContext, R.color.color_05a585))
                    tvOperationCash?.setBackgroundResource(R.drawable.shape_gray_hollow_rect)
                }
                UserConstant.COMMISSION_SETTLED -> {  //待结算
                    tvOperationCash?.setTextColor(ContextCompat.getColor(mContext, R.color.color_f84272))
                    tvOperationCash?.setBackgroundResource(R.drawable.shape_red_hollow_rect)
                }
                else -> {
                    tvOperationCash?.setTextColor(ContextCompat.getColor(mContext, R.color.color_05a585))
                    tvOperationCash?.setBackgroundResource(R.drawable.shape_gray_hollow_rect)
                }
            }
        }
    }
}