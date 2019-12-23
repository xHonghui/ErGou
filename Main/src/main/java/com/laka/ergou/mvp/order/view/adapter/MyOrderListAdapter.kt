package com.laka.ergou.mvp.order.view.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.TextViewHelper
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.SelectorButton
import com.laka.ergou.R
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.mvp.order.constant.MyOrderConstant
import com.laka.ergou.mvp.order.model.bean.OrderDataBean
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils

/**
 * @Author:summer
 * @Date:2019/1/19
 * @Description:我的订单列表适配器（）
 */
class MyOrderListAdapter : BaseQuickAdapter<OrderDataBean, MyOrderListAdapter.MyOrderListViewHolder> {

    constructor(layoutResId: Int, data: MutableList<OrderDataBean>?) : super(layoutResId, data)

    override fun convert(helper: MyOrderListViewHolder?, item: OrderDataBean?) {
        helper?.showData(mContext, item)
//        val layoutParams = helper?.itemView?.layoutParams as? RecyclerView.LayoutParams
//        if (helper?.position == 0) {
//            layoutParams?.topMargin = ScreenUtils.dp2px(10f)
//        } else {
//            layoutParams?.topMargin = ScreenUtils.dp2px(0f)
//        }
        R.layout.item_my_order_list
    }

    class MyOrderListViewHolder : BaseViewHolder {
        private var mTvOrderPrice: TextView? = null
        private var mTvOrderNo: TextView? = null
        private var mTvShopTitle: TextView? = null
        private var mTvCommission: TextView? = null
        private var mTvOrderStatus: TextView? = null
        private var mSbCopy: SelectorButton? = null
        private var mTvDate: TextView? = null
        private var mTvShopCount: TextView? = null
        private var mIvIcon: ImageView? = null

        constructor(view: View?) : super(view) {
            mSbCopy = view?.findViewById(R.id.sb_copy)
            mTvOrderPrice = view?.findViewById(R.id.tv_order_price)
            mTvOrderNo = view?.findViewById(R.id.tv_order_no)
            mTvShopTitle = view?.findViewById(R.id.tv_shop_title)
            mTvOrderStatus = view?.findViewById(R.id.tv_order_status)
            mTvCommission = view?.findViewById(R.id.tv_commission)
            mIvIcon = view?.findViewById(R.id.iv_icon)
            mTvDate = view?.findViewById(R.id.tv_date)
            mTvShopCount = view?.findViewById(R.id.tv_shop_count)
        }

        fun showData(context: Context, result: OrderDataBean?) {
            GlideUtil.loadImage(context, "${result?.pic}", R.drawable.order_bg_good, mIvIcon)
            mTvShopTitle?.text = "${result?.title}"
            mTvOrderStatus?.text = "${result?.status_word}"
            mTvOrderNo?.text = "订单编号：${result?.order_id}"
            mTvShopCount?.text = "x${result?.number}"
            when (result?.type) {
                MyOrderConstant.ORDER_STATUS_ENABLE -> { // 已结算
                    mTvOrderStatus?.text = "已结算"
                    mTvOrderPrice?.text = "¥${result?.price}"
                    mTvCommission?.setTextColor(ContextCompat.getColor(ApplicationUtils.getContext(), R.color.color_05a585))
                    mTvCommission?.setBackgroundResource(R.drawable.shape_gray_hollow_rect)
                    mTvCommission?.text = "结算补贴¥${result?.commission}"
                    mTvDate?.text = "结算时间：${result?.earning_time}"
                }
                MyOrderConstant.ORDER_STATUS_SETTLEMENT -> { // 已结算
                    mTvOrderStatus?.text = "已结算"
                    mTvOrderPrice?.text = "¥${result?.price}"
                    mTvCommission?.setTextColor(ContextCompat.getColor(ApplicationUtils.getContext(), R.color.color_05a585))
                    mTvCommission?.setBackgroundResource(R.drawable.shape_gray_hollow_rect)
                    mTvCommission?.text = "可用补贴¥${result?.commission}"
                    mTvDate?.text = "结算时间：${result?.earning_time}"
                }
                MyOrderConstant.ORDER_STATUS_PAID -> {  // 已付款
                    mTvOrderStatus?.text = "已付款"
                    mTvOrderPrice?.text = "¥${result?.price}"
                    mTvCommission?.setTextColor(ContextCompat.getColor(ApplicationUtils.getContext(), R.color.color_f84272))
                    mTvCommission?.setBackgroundResource(R.drawable.shape_red_hollow_rect)
                    mTvCommission?.text = "待结补贴¥${result?.commission}"
                    mTvDate?.text = "付款时间：${result?.pay_time}"
                }
                MyOrderConstant.ORDER_STATUS_REFUND -> { // 已失效
                    mTvOrderStatus?.text = "已失效"
                    mTvOrderPrice?.text = "¥${result?.price}"
                    mTvCommission?.setTextColor(ContextCompat.getColor(ApplicationUtils.getContext(), R.color.color_929292))
                    mTvCommission?.setBackgroundResource(R.drawable.shape_929292_hollow_rect)
                    mTvCommission?.text = "失效补贴¥${result?.commission}"
                    mTvDate?.text = "付款时间：${result?.pay_time}"
                }
                else -> { //目前匹配不上状态的情况，统一当做是‘已结算’处理
                    mTvOrderStatus?.text = "已结算"
                    mTvOrderPrice?.text = "¥${result?.price}"
                    mTvCommission?.setTextColor(ContextCompat.getColor(ApplicationUtils.getContext(), R.color.color_05a585))
                    mTvCommission?.setBackgroundResource(R.drawable.shape_gray_hollow_rect)
                    mTvCommission?.text = "可用补贴¥${result?.commission}"
                    mTvDate?.text = "结算时间：${result?.earning_time}"
                }
            }

            mSbCopy?.setOnClickListener {
                ClipBoardManagerHelper.getInstance.writeToClipBoardContent("${result?.order_id}")
                ToastHelper.showCenterToast("复制成功")
            }
        }
    }
}