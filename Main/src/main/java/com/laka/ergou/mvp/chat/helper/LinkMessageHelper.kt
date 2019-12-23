package com.laka.ergou.mvp.chat.helper

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.laka.androidlib.util.image.ImageLoader
import com.laka.ergou.R
import com.laka.ergou.mvp.chat.model.bean.LinkMessage
import com.laka.ergou.mvp.chat.model.bean.Message
import com.lqr.adapter.LQRViewHolderForRecyclerView

/**
 * @Author:summer
 * @Date:2019/2/19
 * @Description:
 */
object LinkMessageHelper {

    @JvmStatic
    fun setLinkStatus(helper: LQRViewHolderForRecyclerView, item: Message, position: Int) {

    }

    @JvmStatic
    fun setLinkMessageView(context: Context, helper: LQRViewHolderForRecyclerView, item: Message, position: Int) {
        // 设置UI数据
        var linkMessage = item.msgContent
        if (linkMessage is LinkMessage) {
            var linkTitle = helper.getView<TextView>(R.id.tv_link_title)
            var linkDescription = helper.getView<TextView>(R.id.tv_content)
            var linkThumb = helper.getView<ImageView>(R.id.iv_icon)
            linkTitle.text = linkMessage.title
            linkDescription.text = linkMessage.description
            ImageLoader.getInstance()
                    .with(context)
                    .placeholder(R.drawable.default_img)
                    .load(linkMessage.thumbUrl)
                    .into(linkThumb)
        }
    }
}