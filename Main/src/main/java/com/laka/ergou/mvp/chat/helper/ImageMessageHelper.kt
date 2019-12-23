package com.laka.ergou.mvp.chat.helper

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.chat.constant.ChatConstant
import com.laka.ergou.mvp.chat.model.bean.ImageMessage
import com.laka.ergou.mvp.chat.model.bean.Message
import com.laka.ergou.mvp.chat.view.widget.BubbleImageView
import com.laka.ergou.mvp.share.ShopShareModuleNavigator
import com.lqr.adapter.LQRViewHolderForRecyclerView

/**
 * @Author:summer
 * @Date:2019/2/19
 * @Description:
 */
object ImageMessageHelper {

    @JvmStatic
    fun setImageStatus(helper: LQRViewHolderForRecyclerView, item: Message, position: Int) {
        val bivPic = helper.getView<BubbleImageView>(R.id.bivPic)
        val isSend = item.messageCreatorType == ChatConstant.CHAT_IDENTIFY_USER
        if (isSend) {
            val sentStatus = item.sendStatus
            if (sentStatus == ChatConstant.STATUS_SENDING_MESSAGE) {
                bivPic.setProgressVisible(true)
                //                    if (!TextUtils.isEmpty(item.getExtra()))
                //                        bivPic.setPercent(Integer.valueOf(item.getExtra()));
                bivPic.showShadow(true)
                helper.setViewVisibility(R.id.llError, View.GONE)

            } else if (sentStatus == ChatConstant.STATUS_FAILED_MESSAGE) {
                bivPic.setProgressVisible(false)
                bivPic.showShadow(false)
                helper.setViewVisibility(R.id.llError, View.VISIBLE)

            } else if (sentStatus == ChatConstant.STATUS_SUCCESS_MESSAGE) {
                bivPic.setProgressVisible(false)
                bivPic.showShadow(false)
                helper.setViewVisibility(R.id.llError, View.GONE)
            }
            // 发送成功
            bivPic.setProgressVisible(false)
            bivPic.showShadow(false)
            helper.setViewVisibility(R.id.llError, View.GONE)

        } else {
            bivPic.setProgressVisible(false)
            bivPic.showShadow(false)
            helper.setViewVisibility(R.id.llError, View.GONE)
        }
    }

    @JvmStatic
    fun setImageMessageView(context: Context, helper: LQRViewHolderForRecyclerView, item: Message, position: Int, data: MutableList<Message>) {
        val imageMessage = item.msgContent
        if (imageMessage is ImageMessage) {
            val imgBivPic = helper.getView<BubbleImageView>(R.id.bivPic)
            var width = ScreenUtils.dp2px(imageMessage.thumbWidth.toFloat())
            var height = ScreenUtils.dp2px(imageMessage.thumbHeight.toFloat())
            if (width <= 0 || height <= 0) {
                width = ChatConstant.DEFAULT_IMAGEVIEW_WIDTH
                height = ChatConstant.DEFAULT_IMAGEVIEW_HEIGHT
            }
            imgBivPic.setTag(R.id.chat_list_image_view_width, width)
            imgBivPic.setTag(R.id.chat_list_image_view_height, height)
            Glide.with(context).load(imageMessage.bigUrl)
                    .error(R.drawable.default_img)
                    .override(width, height)
                    .into(imgBivPic)

            //图片点击
            imgBivPic.setOnClickListener {
                val imageList = ArrayList<String>()
                var currentPos = 0
                var count = 0
                for (i in 0 until data.size) {
                    if (data[i].msgType == ChatConstant.CHAT_MSG_TYPE_IMAGE) {
                        val imageMessageContent = data[i].msgContent as? ImageMessage
                        imageMessageContent?.let {
                            count++
                            imageList.add(imageMessageContent.bigUrl)
                            if (item.chatId == data[i].chatId) { //当前ImageMessage 在所有ImageMessage中的位置
                                currentPos = count
                            }
                        }
                    }
                    LogUtils.info("imageMessage-------$data[i]")
                }
                currentPos = if ((count - currentPos) >= 0 && (count - currentPos) < count) count - currentPos else count - 1
                ShopShareModuleNavigator.startSeeBigImageActivity(context, imageList, currentPos)
            }
        }
    }


}