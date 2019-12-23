package com.laka.ergou.mvp.commission.view.adapter

import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.util.ApplicationUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.commission.constant.CommissionConstant
import com.laka.ergou.mvp.commission.constract.ICommissionDetailConstract
import com.laka.ergou.mvp.commission.model.bean.CommissionDetailData
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author:summer
 * @Date:2019/1/19
 * @Description: 补贴明细列表
 */
class CommissionDetailListAdapter : BaseQuickAdapter<CommissionDetailData, CommissionDetailListAdapter.CommissionDetailListHolder> {


    constructor(layoutResId: Int, data: MutableList<CommissionDetailData>?) : super(layoutResId, data) {
        R.layout.item_commission_detail_list
    }

    override fun convert(helper: CommissionDetailListHolder?, item: CommissionDetailData?) {
        helper?.showData(item)
    }

    class CommissionDetailListHolder : BaseViewHolder {
        var mTvCashDetail: TextView? = null
        var mTvCashWithdrawalTime: TextView? = null
        var mTvOperationCash: TextView? = null
        var mTvEnableCommission: TextView? = null

        constructor(view: View?) : super(view) {
            mTvCashDetail = view?.findViewById(R.id.tv_cash_detail)
            mTvCashWithdrawalTime = view?.findViewById(R.id.tv_cash_withdrawal_time)
            mTvOperationCash = view?.findViewById(R.id.tv_operation_cash)
            mTvEnableCommission = view?.findViewById(R.id.tv_enable_commission)
        }

        fun showData(item: CommissionDetailData?) {
            val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
            mTvCashDetail?.text = "${item?.title}"
            mTvOperationCash?.text = "${item?.money}"
            if (item?.create_time != null) {
                mTvCashWithdrawalTime?.text = "${sdf.format(Date(item?.create_time * 1000))}"
            } else {
                mTvCashWithdrawalTime?.text = "${sdf.format(Date(0))}"
            }
            when (item?.withdraw_status) {
                CommissionConstant.WITHDRAWING -> {
                    mTvEnableCommission?.text = "审核中"
                    mTvEnableCommission?.setTextColor(ContextCompat.getColor(ApplicationUtils.getContext(), R.color.color_black_05a585))
                }
                CommissionConstant.AUDIT_PASS -> {
                    mTvEnableCommission?.text = "已通过"
                    mTvEnableCommission?.setTextColor(ContextCompat.getColor(ApplicationUtils.getContext(), R.color.color_black_05a585))
                }
                CommissionConstant.REFUSE -> {
                    mTvEnableCommission?.text = "已拒绝"
                    mTvEnableCommission?.setTextColor(ContextCompat.getColor(ApplicationUtils.getContext(), R.color.color_common_text_main))
                }
            }
        }
    }
}