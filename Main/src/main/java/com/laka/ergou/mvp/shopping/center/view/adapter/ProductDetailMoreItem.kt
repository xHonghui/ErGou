package com.laka.ergou.mvp.shopping.center.view.adapter

import android.animation.ObjectAnimator
import android.text.SpannableString
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.base.adapter.MultipleAdapterItem
import com.laka.androidlib.util.TextViewHelper
import com.laka.ergou.R
import com.laka.ergou.common.util.SpannableStringUtils
import com.laka.ergou.mvp.shop.model.bean.CustomProductDetail
import com.laka.ergou.mvp.shop.model.bean.ProductBannerList
import com.laka.ergou.mvp.shop.model.bean.TitleTypeBean
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils
import com.laka.ergou.mvp.shop.utils.ShopStringUtils
import com.laka.ergou.mvp.shop.weight.ShopDetailBannerView

/**
 * @Author:summer
 * @Date:2019/4/28
 * @Description:
 */
class ProductDetailMoreItem : MultipleAdapterItem<TitleTypeBean> {

    override fun convert(helper: BaseViewHolder, item: TitleTypeBean?) {
        item?.let {
            val ivMore = helper.getView<ImageView>(R.id.image_view_more)
            if (it.open == 1) {
                val anim = ObjectAnimator.ofFloat(ivMore, "rotation", 90f)
                anim.duration = 300
                anim.start()
            }
        }
    }
}