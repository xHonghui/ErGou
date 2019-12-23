package com.laka.ergou.mvp.circle.view.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.widget.ImageView
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.common.ext.loadImage
import com.laka.ergou.common.widget.SpaceItemDecoration
import com.laka.ergou.mvp.circle.model.bean.CircleArticle
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils
import java.text.SimpleDateFormat
import java.util.*

class CircleListAdapter(var context: Context) : BaseQuickAdapter<CircleArticle, BaseViewHolder>(R.layout.item_circle) {

    private var mCallback: Callback? = null

    var dividerLine: SpaceItemDecoration? = null
    fun setPictureClickCallback(callback: Callback): CircleListAdapter {
        mCallback = callback
        return this
    }

    override fun convert(helper: BaseViewHolder, item: CircleArticle) {
        with(item) {
            admin?.let {
                helper.setText(R.id.tv_name, "${it.nickname}")
                        .getView<ImageView>(R.id.iv_avatar).loadImage(it.avatar)
            }
            helper.setText(R.id.tv_time, "${getFormatTime(send_time * 1000)}")
                    .setText(R.id.tv_content, "$content")
                    .setText(R.id.tv_comment, "$comment")
                    .addOnClickListener(R.id.tv_copy)
                    .addOnClickListener(R.id.tv_one_send)
                    .addOnClickListener(R.id.tv_share)
                    .addOnClickListener(R.id.cl_goods_detail)
                    .setGone(R.id.tv_content, if (content.isNotBlank()) {
                        true
                    } else {
                        false
                    })
                    .setGone(R.id.ll_comment, if (comment.isNotBlank()) {
                        true
                    } else {
                        false
                    })
                    .setText(R.id.tv_one_send, when (send_status) {
                        "1" -> "发圈中"
                        "2" -> "已发送"
                        else -> "一键发圈"
                    })
                    .setBackgroundRes(R.id.tv_one_send, when (send_status) {
                        "1" -> R.drawable.btn_circle_comment_normal
                        "2" -> R.drawable.btn_circle_comment_normal
                        else -> R.drawable.selector_share_btn_yjfx
                    })
            setNineLayout(helper, item)
            product?.let {
                var bonus_amount = ResourceUtils.getStringWithArgs(R.string.product_commission, BigDecimalUtils.numberFormat(it.bonus_amount))
                helper.setText(R.id.tv_title, "${it.title}")
                        .setText(R.id.tv_coupon_price, "券后价 ¥${it.actual_price}")
                        .setText(R.id.tv_product_min_coupon, "¥${it.coupon_amount}")
                        .setText(R.id.tv_product_commission, "$bonus_amount")
                        .getView<ImageView>(R.id.iv_goods_pic)
                        .loadImage(it.pict_url)
            } ?: helper.setGone(R.id.cl_goods_detail, false)

        }
    }

    fun setNineLayout(helper: BaseViewHolder, item: CircleArticle) {
        var rlGoods = helper.getView<RecyclerView>(R.id.rl_goods)
//        var mAdapter: BaseQuickAdapter<String, BaseViewHolder>? = null
        var images = item.images
        if (images != null && images.size > 0) {
            helper.setGone(R.id.ll_nine_layout, true)
            if (dividerLine == null) {
                dividerLine = SpaceItemDecoration(mContext).setSpace(4).setSpaceColor(-0x131314)
            }
            rlGoods.removeItemDecoration(dividerLine)
            rlGoods.addItemDecoration(dividerLine)
            var mAdapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_circle_img, images) {
                var imageList = SparseArray<ImageView>()
                override fun convert(helper: BaseViewHolder, item: String) {
                    var image = helper.getView<ImageView>(R.id.iv_pic)
                    imageList.put(helper.layoutPosition, image)
                    helper.addOnClickListener(R.id.iv_pic)
                    image.loadImage(item)
                }
            }
            rlGoods.adapter = mAdapter
            mAdapter.setOnItemChildClickListener { adapter, view, position ->
                mCallback?.onThumbPictureClick(item, view as ImageView, mAdapter.imageList, xList(images))
            }
            var size = when (images.size) {
                //四张图片
                4 -> 2
                else -> 3
            }
            val layoutManager = GridLayoutManager(context, size)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (images.size == 1) {
                        2
                    } else {
                        1
                    }
                }
            }
            rlGoods.layoutManager = layoutManager
            if (images.size == 4) {
                var layoutParams = rlGoods.layoutParams
                layoutParams.width = (ScreenUtils.getScreenWidth() * 0.6).toInt()
            } else {
                var layoutParams = rlGoods.layoutParams
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
            }
        } else {
            helper.setGone(R.id.ll_nine_layout, false)
        }
    }

    interface Callback {
        fun onThumbPictureClick(mCircleArticle: CircleArticle, i: ImageView, imageGroupList: SparseArray<ImageView>, urlList: List<Uri>)
    }

    private fun xList(images: List<String>): MutableList<Uri> {
        var list = mutableListOf<Uri>()
        images.forEach {
            list.add(Uri.parse(it))
        }
        return list
    }


    /**
     * 时间戳格式转换
     */
    fun getFormatTime(timesamp: Long): String {
        var result = ""
        val todayCalendar = Calendar.getInstance()
        val otherCalendar = Calendar.getInstance()
        otherCalendar.setTimeInMillis(timesamp)

        val timeFormat = SimpleDateFormat("M月d日 HH:mm")
        val yearTimeFormat = SimpleDateFormat("yyyy年M月d日 HH:mm")
        val hourAndMinFormat = SimpleDateFormat("HH:mm")

        val yearTemp = todayCalendar.get(Calendar.YEAR) === otherCalendar.get(Calendar.YEAR)
        if (yearTemp) {
            val todayMonth = todayCalendar.get(Calendar.MONTH)
            val otherMonth = otherCalendar.get(Calendar.MONTH)
            if (todayMonth == otherMonth) {//表示是同一个月
                val temp = todayCalendar.get(Calendar.DATE) - otherCalendar.get(Calendar.DATE)
                when (temp) {
                    0 -> result = "今天 ${hourAndMinFormat.format(Date(timesamp))}"
                    1 -> result = "昨天 ${hourAndMinFormat.format(Date(timesamp))}"

                    else -> result = timeFormat.format(Date(timesamp))
                }
            } else {
                result = timeFormat.format(Date(timesamp))
            }
        } else {
            result = yearTimeFormat.format(Date(timesamp))
        }
        return result
    }

}