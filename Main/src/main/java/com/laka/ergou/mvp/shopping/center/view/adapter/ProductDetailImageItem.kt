package com.laka.ergou.mvp.shopping.center.view.adapter

import android.text.TextUtils
import android.widget.ImageView
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.base.adapter.MultipleAdapterItem
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.shop.model.bean.ImageDetail
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils

/**
 * @Author:summer
 * @Date:2019/4/28
 * @Description:产品详情图片
 */
class ProductDetailImageItem : MultipleAdapterItem<ImageDetail> {

    override fun convert(helper: BaseViewHolder, item: ImageDetail?) {
        R.layout.item_shop_detail_image_detail
        val imageView = helper?.itemView?.findViewById<ImageView>(R.id.image_view_img)
        item?.let {
            if (!TextUtils.isEmpty(it?.imageHeight) && !TextUtils.isEmpty(it?.imageWidth) && it?.imageHeight != "0" && it?.imageWidth != "0") {
                val width = ScreenUtils.getScreenWidth()
                val rotate = BigDecimalUtils.divi("${it?.imageHeight}", "${it?.imageWidth}").toDouble()
                val height = BigDecimalUtils.multi("$width", rotate.toString()).toInt()
                val params: LinearLayout.LayoutParams = imageView?.layoutParams as LinearLayout.LayoutParams
                params?.width = width
                params?.height = height
                imageView?.layoutParams = params
                GlideUtil.loadImageNoCrop(helper?.convertView?.context, "${it?.imageUrl}", R.drawable.default_img, imageView)
            } else {
                GlideUtil.downloadBitmapForAdapterImg(helper?.convertView?.context, "${it?.imageUrl}", R.drawable.default_img, imageView)
            }
        }
    }
}