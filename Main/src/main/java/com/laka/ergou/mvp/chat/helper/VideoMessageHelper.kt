package com.laka.ergou.mvp.chat.helper

import android.content.Context
import android.widget.ImageView
import com.laka.ergou.R
import com.laka.ergou.mvp.chat.constant.ChatConstant
import com.laka.ergou.mvp.chat.model.bean.Message
import com.laka.ergou.mvp.chat.view.widget.BubbleImageView
import com.laka.ergou.mvp.chat.view.widget.CircularProgressBar
import com.lqr.adapter.LQRViewHolderForRecyclerView

/**
 * @Author:summer
 * @Date:2019/2/19
 * @Description:
 */
object VideoMessageHelper {

    @JvmStatic
    fun setVideoStatus(helper: LQRViewHolderForRecyclerView, item: Message, position: Int) {
        val bivPic = helper.getView<BubbleImageView>(R.id.bivPic)
        val isSend = item.messageCreatorType == ChatConstant.CHAT_IDENTIFY_USER
        val cpbLoading = helper.getView<CircularProgressBar>(R.id.cpbLoading)
        // 对于视频的处理
//        if (isSend) {
//            val sentStatus = item.status
//            if (sentStatus === Message.SentStatus.SENDING || fileMessage.getLocalPath() == null || fileMessage.getLocalPath() != null && !File(fileMessage.getLocalPath().getPath()).exists()) {
//                if (!TextUtils.isEmpty(item.getExtra())) {
//                    cpbLoading.max = 100
//                    cpbLoading.progress = Integer.valueOf(item.getExtra())
//                } else {
//                    cpbLoading.max = 100
//                    cpbLoading.progress = 0
//                }
//                helper.setViewVisibility(R.id.llError, View.GONE).setViewVisibility(R.id.cpbLoading, View.VISIBLE)
//                bivPic.showShadow(true)
//            } else if (sentStatus === Message.SentStatus.FAILED) {
//                helper.setViewVisibility(R.id.llError, View.VISIBLE).setViewVisibility(R.id.cpbLoading, View.GONE)
//                bivPic.showShadow(false)
//            } else if (sentStatus === Message.SentStatus.SENT) {
//                helper.setViewVisibility(R.id.llError, View.GONE).setViewVisibility(R.id.cpbLoading, View.GONE)
//                bivPic.showShadow(false)
//            }
//        } else {
//            val receivedStatus = item.getReceivedStatus()
//            if (receivedStatus.isDownload() || fileMessage.getLocalPath() != null) {
//                helper.setViewVisibility(R.id.llError, View.GONE).setViewVisibility(R.id.cpbLoading, View.GONE)
//                bivPic.showShadow(false)
//            } else {
//                if (!TextUtils.isEmpty(item.getExtra())) {
//                    cpbLoading.max = 100
//                    cpbLoading.progress = Integer.valueOf(item.getExtra())
//                } else {
//                    cpbLoading.max = 100
//                    cpbLoading.progress = 0
//                }
//                helper.setViewVisibility(R.id.llError, View.GONE).setViewVisibility(R.id.cpbLoading, View.VISIBLE)
//                bivPic.showShadow(true)
//            }
//        }
    }

    @JvmStatic
    fun setVideoMessageView(context: Context, helper: LQRViewHolderForRecyclerView, item: Message, position: Int) {
        val videoBivPic = helper.getView<BubbleImageView>(R.id.bivPic)
        val ivBtnPlay = helper.getView<ImageView>(R.id.iv_btn_play)
        videoBivPic.setImageResource(R.drawable.meizhi)
        ivBtnPlay.setImageResource(R.drawable.wechat_btn_play)
    }

}