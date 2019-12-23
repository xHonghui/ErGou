package com.laka.ergou.mvp.message.view.adapter

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.ergou.R
import com.laka.ergou.mvp.message.constant.MessageConstant
import com.laka.ergou.mvp.message.model.bean.Msg
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:补贴消息
 */
class CommissionMessageAdapter : BaseQuickAdapter<Msg, BaseViewHolder> {

    private var tvCommissionReward: TextView? = null
    private var tvOrderNumber: TextView? = null
    private var tvAmount: TextView? = null
    private var tvAmountTxt: TextView? = null
    private var tvTime: TextView? = null

    constructor(layoutResId: Int, data: MutableList<Msg>?) : super(layoutResId, data)

    override fun convert(helper: BaseViewHolder?, item: Msg?) {
        R.layout.item_commission_message
        item?.let {
            tvCommissionReward = helper?.itemView?.findViewById(R.id.tv_commission_reward)
            tvOrderNumber = helper?.itemView?.findViewById(R.id.tv_order_number)
            tvAmount = helper?.itemView?.findViewById(R.id.tv_amount)
            tvAmountTxt = helper?.itemView?.findViewById(R.id.tv_amount_txt)
            tvTime = helper?.itemView?.findViewById(R.id.tv_time)

            tvCommissionReward?.text = item?.title
            tvOrderNumber?.text = item?.context
            when (item?.money_type) {
                MessageConstant.FROZEN_COMMISSION_TYPE -> {
                    tvAmountTxt?.text = "待结补贴"
                }
                MessageConstant.ENBALE_COMMISSION_TYPE -> {
                    tvAmountTxt?.text = "结算补贴"
                }
                MessageConstant.INVALID_COMMISSION_TYPE -> {
                    tvAmountTxt?.text = "失效补贴"
                }
            }

            initCommissionData(item)
            //格式化item消息的时间显示
            onFormatDate(item, helper)
        }
    }

    private fun initCommissionData(item: Msg?) {
        var resultStr = ""
        when (item?.msg_type) {
            MessageConstant.MESSAGE_COMMISSION_ORDER_PAY,
            MessageConstant.MESSAGE_COMMISSION_ORDER_SETTLEMENT,
            MessageConstant.MESSAGE_COMMISSION_WITHDRAWAL_FAIL,
            MessageConstant.MESSAGE_COMMISSION_INVITATION_REWARD,
            MessageConstant.MESSAGE_COMMISSION_LOWER_LEVELS_REWARD -> {
                resultStr = "+${item?.money}"
                tvAmount?.setTextColor(ContextCompat.getColor(mContext, R.color.color_05a585))
            }
            MessageConstant.MESSAGE_COMMISSION_ORDER_INVALID,//补贴减少
            MessageConstant.MESSAGE_COMMISSION_WITHDRAWAL_SUCCESS -> {
                resultStr = "-${item?.money}"
                tvAmount?.setTextColor(ContextCompat.getColor(mContext, R.color.color_main))
            }
        }
        tvAmount?.text = resultStr
    }

    /**格式化时间，对比当前item时间和前一条时间是否在同一天*/
    private fun onFormatDate(item: Msg?, helper: BaseViewHolder?) {
        if (item?.create_time == null) {
            return
        }
        val sdf = SimpleDateFormat("yyyy.MM.dd")
        val date = Date(item?.create_time!! * 1000)
        val dateStr = sdf.format(date)
        tvTime?.text = dateStr
        if (helper?.position!! >= headerLayoutCount + 1) {
            val preItem = getItem(helper?.position!! - headerLayoutCount - 1)
            val currCalendar = Calendar.getInstance()
            val preCalendar = Calendar.getInstance()
            preCalendar.time = Date(preItem?.create_time!! * 1000)
            currCalendar.time = date
            if (preCalendar.timeInMillis - currCalendar.timeInMillis >= 1000 * 60 * 60 * 24) {
                //大于一天
                tvTime?.visibility = View.VISIBLE
            } else {
                //时间值小于一天，但是日期不一定是同一天
                val currentDay = currCalendar.get(Calendar.DAY_OF_MONTH)
                val preDay = preCalendar.get(Calendar.DAY_OF_MONTH)
                if (preDay > currentDay) {
                    tvTime?.visibility = View.VISIBLE
                } else {
                    tvTime?.visibility = View.GONE
                }
            }
        } else {
            tvTime?.visibility = View.VISIBLE
        }
    }


}