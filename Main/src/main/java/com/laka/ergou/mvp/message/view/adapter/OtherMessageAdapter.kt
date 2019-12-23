package com.laka.ergou.mvp.message.view.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.message.constant.MessageConstant
import com.laka.ergou.mvp.message.model.bean.Msg
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:其他消息
 */
class OtherMessageAdapter : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    constructor(data: MutableList<MultiItemEntity>?) : super(data) {
        addItemType(MessageConstant.ITEM_OTHER_MESSAGE_TYPE_NORMAL, R.layout.item_other_message_type_normal)
        addItemType(MessageConstant.ITEM_OTHER_MESSAGE_TYPE_SHOPPING, R.layout.item_other_message_type_shopping)
        addItemType(MessageConstant.ITEM_OTHER_MESSAGE_TYPE_NOTICE, R.layout.item_other_message_type_notice)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        val itemBean = item as? Msg
        itemBean?.let {
            when (helper?.itemViewType) {
                MessageConstant.ITEM_OTHER_MESSAGE_TYPE_NORMAL -> {
                    normalConvert(helper, itemBean)
                }
                MessageConstant.ITEM_OTHER_MESSAGE_TYPE_NOTICE -> {
                    noticeConvert(helper, itemBean)
                }
                MessageConstant.ITEM_OTHER_MESSAGE_TYPE_SHOPPING -> {
                    recommendConvert(helper, itemBean)
                }
            }
        }
    }

    //普通消息
    private fun normalConvert(helper: BaseViewHolder, item: Msg) {
        val tvTitle = helper.getView<TextView>(R.id.tv_title)
        val tvDes = helper.getView<TextView>(R.id.tv_des)
        val tvTime = helper.getView<TextView>(R.id.tv_time)
        onFormatDate(item, tvTime, helper)
        tvTitle?.text = item.title
        tvDes?.text = item.context

    }

    //公告消息
    private fun noticeConvert(helper: BaseViewHolder, item: Msg) {
        val ivAvatar = helper.getView<ImageView>(R.id.iv_avatar)
        val tvTitle = helper.getView<TextView>(R.id.tv_title)
        val tvDes = helper.getView<TextView>(R.id.tv_des)
        val tvTime = helper.getView<TextView>(R.id.tv_time)
        GlideUtil.loadImage(mContext, item.img_url, R.drawable.default_img, R.drawable.default_img, ivAvatar)
        onFormatDate(item, tvTime, helper)
        tvTitle.text = item.title
        tvDes.text = item.context
    }

    //推荐消息（商品详情）
    private fun recommendConvert(helper: BaseViewHolder, item: Msg) {
        val ivAvatar = helper.getView<ImageView>(R.id.iv_avatar)
        val tvTitle = helper.getView<TextView>(R.id.tv_title)
        val tvDes = helper.getView<TextView>(R.id.tv_des)
        val tvTime = helper.getView<TextView>(R.id.tv_time)
        GlideUtil.loadImage(mContext, item.img_url, R.drawable.default_img, R.drawable.default_img, ivAvatar)
        onFormatDate(item, tvTime, helper)
        tvTitle.text = item.title
        tvDes.text = item.context
    }


    /**格式化时间，对比当前item时间和前一条时间是否在同一天*/
    private fun onFormatDate(item: Msg, tvTime: TextView, helper: BaseViewHolder) {
        if (item?.create_time == null) {
            return
        }
        val sdf = SimpleDateFormat("yyyy.MM.dd")
        val date = Date(item?.create_time!! * 1000)
        val dateStr = sdf.format(date)
        tvTime?.text = dateStr
        LogUtils.info("helper?.position-----==${helper?.position}----layoutPosition==${helper?.layoutPosition}-------headerLayoutCount===$headerLayoutCount")
        if (helper?.position!! >= headerLayoutCount + 1) {
            val preItem = getItem(helper?.position!! - headerLayoutCount - 1) as? Msg
            val currCalendar = Calendar.getInstance()
            val preCalendar = Calendar.getInstance()
            currCalendar.time = date
            preCalendar.time = Date(preItem?.create_time!! * 1000)
            if (preCalendar.timeInMillis - currCalendar.timeInMillis >= 1000 * 60 * 60 * 24) {
                tvTime?.visibility = View.VISIBLE   //大于一天
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