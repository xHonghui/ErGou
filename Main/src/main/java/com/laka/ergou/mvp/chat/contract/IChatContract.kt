package com.laka.ergou.mvp.chat.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.mvp.chat.model.bean.Message
import com.lqr.recyclerview.LQRRecyclerView
import java.util.*
import kotlin.collections.ArrayList

/**
 * @Author:summer
 * @Date:2019/2/14
 * @Description:购小二模块---Contract类
 */
interface IChatContract {

    interface IChatModel : IBaseModel<IChatView> {
        fun onGetHistoryMessages(hashMap: HashMap<String, String>, callBack: ResponseCallBack<Message>)
    }

    interface IChatPresenter : IBasePresenter<IChatView> {
        fun sendMessage(message: String, contactId: String)
        fun resendMessage(requestId: String)
        fun onGetHistoryMessages(sessionId: Int, userId: Int, offset: Int)
        fun isHasMoreData(sessionId: Int, userId: Int, offset: Int): Boolean
        fun deleteAllMessageRecordForUser(sessionId: Int, userId: Int)
        fun onInternetChange(isLostInternet: Boolean)
        fun getSystemNotification()
        fun getUserTipsMessage()
    }

    interface IChatView : IBaseLoadingView<Message> {
        fun getSystemMessageSuccess(msgList: ArrayList<String>)
        fun getNotificationCurPosition(): Int
        fun updateSystemMessage(msgList: ArrayList<String>)
        fun getRvMsg(): LQRRecyclerView
        fun getDataList(): ArrayList<Message>
        fun setOrNotifyAdapter()
        fun onMessageLoadMoreFinish(data: ArrayList<Message>)
        fun onMessageFirstLoadFinish(data: ArrayList<Message>)
        fun onMessageReceiverOrSend(message: Message)
        fun onUserIllegalOrTokenInvalid()
        fun onGetUserTipsMessageSuccess()
    }
}