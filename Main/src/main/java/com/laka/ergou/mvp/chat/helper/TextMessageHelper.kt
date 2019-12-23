package com.laka.ergou.mvp.chat.helper

import android.content.Context
import android.text.style.ImageSpan
import android.view.View
import android.widget.TextView
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.mvp.chat.constant.ChatConstant
import com.laka.ergou.mvp.chat.model.bean.Message
import com.laka.ergou.mvp.chat.model.bean.event.ChatEvent
import com.laka.ergou.mvp.chat.view.widget.PopChatOperation
import com.laka.ergou.mvp.db.ImplChatDao
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.lqr.adapter.LQRViewHolderForRecyclerView
import com.lqr.emoji.MoonUtils

/**
 * @Author:summer
 * @Date:2019/2/19
 * @Description:
 */
object TextMessageHelper {

    @JvmStatic
    fun setTextStatus(helper: LQRViewHolderForRecyclerView, item: Message, position: Int) {
        val sentStatus = item.sendStatus
        when (sentStatus) {
            ChatConstant.STATUS_WAITING_MESSAGE,
            ChatConstant.STATUS_SENDING_MESSAGE -> // 发送中
                helper.setViewVisibility(R.id.pbSending, View.VISIBLE).setViewVisibility(R.id.llError, View.GONE)
            ChatConstant.STATUS_FAILED_MESSAGE -> // 发送失败
                helper.setViewVisibility(R.id.pbSending, View.GONE).setViewVisibility(R.id.llError, View.VISIBLE)
            ChatConstant.STATUS_SUCCESS_MESSAGE -> // 发送成功
                helper.setViewVisibility(R.id.pbSending, View.GONE).setViewVisibility(R.id.llError, View.GONE)
        }
    }

    @JvmStatic
    fun setTextMessageView(context: Context, helper: LQRViewHolderForRecyclerView, item: Message, position: Int) {
        MoonUtils.identifyFaceExpression(context, helper.getView(R.id.tvText), item.content, ImageSpan.ALIGN_BOTTOM, { type, content ->
            EventBusManager.postEvent(ChatEvent(type, content))
        })
    }

    @JvmStatic
    fun showTextMessageOperationBar(context: Context, helper: LQRViewHolderForRecyclerView, item: Message, position: Int) {
        val tvMsg = helper.getView<TextView>(R.id.tvText)
        tvMsg.setOnLongClickListener {
            PopChatOperation().init(context)
                    .setLeftClickListener {
                        ClipBoardManagerHelper.getInstance.writeToClipBoardContent(tvMsg.text.toString())
                        ToastHelper.showCenterToast("复制成功")
                    }
                    .setRightClickListener { onDeleteMsg(item, position) }.showAtDropTop(helper.getView(R.id.tvText))
            true
        }
    }

    private fun onDeleteMsg(item: Message, position: Int) {
        val dao = ImplChatDao()
        val removeForRequestId = dao.removeForRequestId("${UserUtils.getUserId()}", item.requestId)
        if (removeForRequestId >= 0) {
            EventBusManager.postEvent(ChatEvent(ChatConstant.DELETE_MESSAGE_FOR_POSITION_EVENT, position))
        }
    }

}