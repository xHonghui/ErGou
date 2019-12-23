package com.laka.ergou.mvp.chat.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.content.ContentValues
import android.content.Context
import android.support.v4.util.ArrayMap
import android.text.TextUtils
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.ListUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.network.NetworkUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.common.threadpool.ThreadPoolHelp
import com.laka.ergou.common.util.ui.UIUtils
import com.laka.ergou.mvp.chat.constant.ChatApiConstant
import com.laka.ergou.mvp.chat.constant.ChatConstant
import com.laka.ergou.mvp.chat.contract.IChatContract
import com.laka.ergou.mvp.chat.model.bean.ChatRobotInfo
import com.laka.ergou.mvp.chat.model.bean.ChatUserInfo
import com.laka.ergou.mvp.chat.model.bean.Message
import com.laka.ergou.mvp.chat.model.repository.ChatModel
import com.laka.ergou.mvp.chat.utils.socket.SocketManager
import com.laka.ergou.mvp.chat.utils.socket.SocketManagerHelper
import com.laka.ergou.mvp.db.ImplChatDao
import com.laka.ergou.mvp.db.constant.DbConstant
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import java.util.*
import kotlin.collections.ArrayList

/**
 * @Author:summer
 * @Date:2019/2/14
 * @Description:购小二模块---聊天---Presenter层
 */
class ChatPresenter : IChatContract.IChatPresenter {

    private lateinit var mView: IChatContract.IChatView
    private lateinit var mContext: Context
    private var chatDao: ImplChatDao = ImplChatDao()
    private var mModel: IChatContract.IChatModel = ChatModel()
    private var mData = ArrayList<Message>()
    private val mUserId = UserUtils.getUserId().toString()

    /**
     * description:消息相关处理
     **/
    private var mMessage: Message = Message()
    private var contactId = ""
    private var messageCallBack: SocketManager.SocketCallBack
    private var chatUserInfo: ChatUserInfo
    private var lastHistoryMessageChatId = 0

    /**
     * description:系统消息
     **/
    private var systemMsgs = ArrayList<Message>()

    /**
     * description:请求与message映射表
     **/
    private var reqMsgIdReflectMap = ArrayMap<String, Message>()

    /**
     * description:异常数据处理(无机器人信息，需要保存当前发送列表)
     **/
    private var isLostContactInfo = false

    /**
     * 是否可执行发送消息操作
     * */
    private var mIsLoop = true
    private var mPeriodTime = 300L
    private var mDelayTime = 300L
    private var mTimer = Timer()

    private var mTimerTask = object : TimerTask() {
        override fun run() {
            LogUtils.info("issue-------：轮询器轮询isLoop:${isLoop()}")
            if (isLoop()) {
                continueSendMessageList()
            }
        }
    }

    init {
        //开启消息队列循环任务
        mTimer.schedule(mTimerTask, mDelayTime, mPeriodTime)

        chatUserInfo = ChatUserInfo(UserUtils.getUserId().toString(),
                UserUtils.getUserName(),
                UserUtils.getUserAvatar(),
                UserUtils.getUserGender(),
                0,
                0)

        //这里的回调在主线程中
        messageCallBack = object : SocketManager.SocketCallBack {

            override fun socketConnect() {
                // 假若Socket重连的时候，重新发送消息
                LogUtils.error("issue---------：鉴权响应后才调用的连接成功")
                setIsLoop(true)
            }

            /**
             * 断开Socket链接
             * */
            override fun socketDisconnect() {
                //收到Socket连接断开回调，则立即置当前发送消息为失败状态。等待下一次连接成功后重发。
                for ((index, key) in reqMsgIdReflectMap.keys.withIndex()) {
                    if (reqMsgIdReflectMap[key]?.sendStatus == ChatConstant.STATUS_SENDING_MESSAGE) {
                        reqMsgIdReflectMap[key]?.sendStatus = ChatConstant.STATUS_WAITING_MESSAGE
                        val content = ContentValues()
                        content.put(DbConstant.SEND_STATUS, ChatConstant.STATUS_WAITING_MESSAGE)
                        content.put(DbConstant.MSG_ID, reqMsgIdReflectMap[key]?.msgId)
                        chatDao.updateForRequestId(mUserId, reqMsgIdReflectMap[key]?.requestId!!, content)
                    }
                }
                setIsLoop(true)
                LogUtils.error("issue---------：断开socket链接")
            }

            override fun distributeRobot(robotInfo: ChatRobotInfo?) {
                if (robotInfo == null) {
                    // 获取不到任何机器人信息。
                    contactId = ""
                    isLostContactInfo = true
                } else {
                    // 获取到机器人信息，重发消息
                    contactId = robotInfo.robotId
                    isLostContactInfo = false
                }
                LogUtils.error("issue---------：ChatPresenter重新分配用机器人数据：${robotInfo?.robotId}")

                // 重新走Socket流程，获取到机器人数据的时候。重新发送消息
                setIsLoop(true)
                // 重新握手后，获取广播数据
                getSystemNotification()
            }

            override fun getUserInfo(userInfo: ChatUserInfo) {

            }

            override fun getOfflineMessageCount(offlineMessageCount: Int) {
                if (offlineMessageCount > 0) {
                    SocketManager.instance.getOfflineMessage(offlineMessageCount)
                }
            }

            override fun getOfflineMessage(offlineMessages: ArrayList<Message>) {
                // 获取到离线消息，添加到列表显示
                offlineMessages.forEach {
                    it.messageRead = true
                    val line = chatDao.add(UserUtils.getUserId().toString(), it)
                    if (line != -1L) {
                        mView.onMessageReceiverOrSend(it)
                        // 数据入库后，获取最后一条消息的ChatId，存储到lastHistoryMessageChatId中。
                        UIUtils.postTaskDelay({ mView.getRvMsg().moveToPosition(0) }, 50)
                    }
                }

                // 假若在当前页面收到离线的消息，重置HomeActivity未读消息数
                EventBusManager.postEvent(HomeEventConstant.EVENT_UPDATE_UNREAD_MESSAGE, 0)
            }

            override fun getUserTipsMessageSuccess(messages: ArrayList<Message>) {
                messages.forEach {
                    it.messageRead = true
                    val line = chatDao.add(UserUtils.getUserId().toString(), it)
                    if (line != -1L) {
                        mView.onMessageReceiverOrSend(it)
                        // 数据入库后，获取最后一条消息的ChatId，存储到lastHistoryMessageChatId中。（目前在发消息的逻辑中更新lastHistoryMessageChatId）
                        UIUtils.postTaskDelay({ mView.getRvMsg().moveToPosition(0) }, 50)
                    }
                }
                mView.onGetUserTipsMessageSuccess()
            }

            override fun sendMessageCallBack(message: Message) {
                // 当msgId == -1的情况下，信息发送失败。否则发送成功
                if (message.msgId == -1) {
                    // 记录当前的reqId
                    sendMessageFail(message)
                    LogUtils.info("issue-------发送信息失败回调：$message")
                } else {
                    // 成功，添加到映射表
                    sendMessageSuccess(message)
                    LogUtils.info("issue-------发送信息成功回调：$message")
                }
                setIsLoop(true)
            }

            override fun replyMessageCallBack(message: Message) {
                LogUtils.info("issue---------：收到响应的消息：$message")
                /*耗性能的查询统一开启子线程进行，防止聊天列表卡顿*/
                ThreadPoolHelp.Builder().builder().execute {
                    message.messageRead = true
                    val msgList = chatDao.getRowByMsgId(UserUtils.getUserId().toString(), message.msgId.toString())
                    if (msgList.isEmpty()) { //数据库中没有重复的消息
                        LogUtils.info("数据库中没有重复消息")
                        val line = chatDao.add(UserUtils.getUserId().toString(), message)
                        if (line != -1L) {
                            //延时更新UI
                            UIUtils.postTaskDelay({
                                mView.onMessageReceiverOrSend(message)
                                // 数据入库后，获取最后一条消息的ChatId，存储到lastHistoryMessageChatId中。
                                UIUtils.postTaskDelay({ mView.getRvMsg().moveToPosition(0) }, 50)
                            }, 400)
                        }
                    } else {
                        LogUtils.info("数据库中已经存在相同msgId的消息，重复，不处理")
                    }
                }
            }

            override fun systemNoticeListCallBack(systemNotices: ArrayList<Message>) {
                LogUtils.info("issue---------：获取到系统通知消息列表回调：$systemNotices")
                LogUtils.info("issue---------：获取到系统通知消息列表回调：长度：${systemNotices.size}")
                systemMsgs = systemNotices
                // 转换成String集合回调
                mView.getSystemMessageSuccess(convertNotification2Str(systemMsgs))
            }

            override fun systemNoticeCallBack(message: Message) {
                LogUtils.info("issue---------：获取系统通知消息回调：$message")
                // 添加到当前滚动的列表，并且插入到当前的滚动的位置的下一条
                val curPosition = mView.getNotificationCurPosition()
                if (ListUtils.isNotEmpty(systemMsgs) && curPosition in 0..systemMsgs.size) {
                    if (curPosition + 1 >= systemMsgs.size) {
                        systemMsgs.add(curPosition, message)
                    } else {
                        systemMsgs.add(curPosition + 1, message)
                    }
                }
                mView.updateSystemMessage(convertNotification2Str(systemMsgs))
            }

            override fun handleError(code: Int, msg: String?) {
                // 统一处理错误
                when (code) {
                    ChatApiConstant.IM_E_INVALID_USER_TOKEN,
                    ChatApiConstant.IM_E_USER_NOT_EXIST,
                    ChatApiConstant.IM_E_USER_NOT_VERIFY -> {
                        mView.onUserIllegalOrTokenInvalid()
                    }
                }
                //ToastHelper.showToast(msg)
                //更改发送中消息的状态
                updateMessageStatus()
                LogUtils.info("issue---------：errorMsg：$msg")
                setIsLoop(true)
            }

        }
        SocketManager.instance.addCallback(messageCallBack)
        LogUtils.info("issue----------------：ChatPresenter中设置socket的回调")
    }

    fun setIsLoop(loop: Boolean) {
        this.mIsLoop = loop
    }

    fun isLoop(): Boolean {
        return mIsLoop
    }

    /**
     * 网络状态改变
     * */
    override fun onInternetChange(isLostInternet: Boolean) {
        if (!isLostInternet) { //有网络
            setIsLoop(true)
            if (!SocketManagerHelper.instance.isSocketConnected()) {
                //socket未连接
                LogUtils.info("issue-------：有网络，Socket未连接")
                EventBusManager.postEvent(UserEvent(UserConstant.SOCKET_CONNECT_EVENT))
            } else {
                LogUtils.info("issue-------：有网络，Socket已连接")
            }
        } else {
            //无网络，将发送中的消息置为 waiting 状态
            updateMessageStatus()
            LogUtils.info("issue-------：无网络状态!!!")
        }
    }

    /**
     * 将队列中的消息从发送中更改为等待中，这样才能进行下一次发送
     * */
    private fun updateMessageStatus() {
        for ((index, key) in reqMsgIdReflectMap.keys.withIndex()) {
            if (reqMsgIdReflectMap[key]?.sendStatus == ChatConstant.STATUS_SENDING_MESSAGE) {
                reqMsgIdReflectMap[key]?.sendStatus = ChatConstant.STATUS_WAITING_MESSAGE
                val content = ContentValues()
                content.put(DbConstant.SEND_STATUS, ChatConstant.STATUS_WAITING_MESSAGE)
                content.put(DbConstant.MSG_ID, reqMsgIdReflectMap[key]?.msgId)
                chatDao.updateForRequestId(mUserId, reqMsgIdReflectMap[key]?.requestId!!, content)
                LogUtils.info("issue-------：从状态sending更改为waiting，message_status：${reqMsgIdReflectMap[key]?.sendStatus}")
            }
        }
    }

    override fun setView(view: IChatContract.IChatView) {
        this.mView = view
        mModel.setView(mView)
    }

    fun setContext(context: Context) {
        this.mContext = context
    }

    override fun onViewCreate() {

    }

    override fun onViewDestroy() {
        SocketManager.instance.removeCallback(messageCallBack)
        mTimer.cancel()
        mTimerTask.cancel()
        mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    /**
     * 加载历史记录
     * */
    override fun onGetHistoryMessages(sessionId: Int, userId: Int, offset: Int) {
        ThreadPoolHelp.Builder().builder().execute {
            val localMessageList = getLocalHistoryMessage(userId, offset)

            // 标记当前消息为已读状态
            chatDao.markMessageAsRead(UserUtils.getUserId().toString())
            EventBusManager.postEvent(HomeEventConstant.EVENT_UPDATE_UNREAD_MESSAGE, 0)
            if (offset == 0) {
                mView.onMessageFirstLoadFinish(localMessageList)
            } else {
                mView.onMessageLoadMoreFinish(localMessageList)
            }
            if (ListUtils.isNotEmpty(localMessageList)) {
                val lastIndex = localMessageList.size - 1
                lastHistoryMessageChatId = localMessageList[lastIndex].chatId
            }
        }
    }

    private fun getLocalHistoryMessage(userId: Int, offset: Int): ArrayList<Message> {
        val messageList = chatDao.getRowForPage(userId.toString(), offset, ChatConstant.MESSAGE_PAGE_SIZE)
        // 遍历列表数据，查询出失败、发送中的消息数据。添加到reflectMap中
        messageList.reversed().forEach {
            it.messageRead = true
            if (it.sendStatus != ChatConstant.STATUS_SUCCESS_MESSAGE) {
                // 重发的消息数据，存储到映射表中
                if (it.sendStatus == ChatConstant.STATUS_SENDING_MESSAGE) {
                    //如果数据库中获取的消息为SENDING 状态，则更改为 waiting 状态再存放到待发送队列中
                    //不需要改变数据库中的状态，应为 sending 和 waiting 两种状态显示的UI是一致的
                    it.sendStatus = ChatConstant.STATUS_WAITING_MESSAGE
                }
                reqMsgIdReflectMap[it.chatId.toString()] = it
            }
            LogUtils.info("输出本地数据库待发送数据：$it")
        }
        return messageList
    }

    /**
     * 是否有更多数据
     * */
    override fun isHasMoreData(sessionId: Int, userId: Int, offset: Int): Boolean {
        return chatDao.hasMoreData(mUserId, offset, ChatConstant.MESSAGE_PAGE_SIZE)
    }

    /**
     * description:根据Message的reqId去更新数据
     **/
    private fun sendMessageFail(message: Message) {
        UIUtils.postTaskDelay({
            val localMessage = reqMsgIdReflectMap[message.requestId]
            localMessage?.sendStatus = ChatConstant.STATUS_FAILED_MESSAGE
            localMessage?.msgId = message.msgId
            val content = ContentValues()
            content.put(DbConstant.SEND_STATUS, ChatConstant.STATUS_FAILED_MESSAGE)
            content.put(DbConstant.MSG_ID, message.msgId)
            val line = chatDao.updateForRequestId(mUserId, message.requestId, content)
            if (line > 0) {
                // 发送失败，继续发送下一条
                mView.setOrNotifyAdapter()
            } else {
                //todo 发送失败，并且修改数据库错误，待处理
            }
        }, 400)
    }

    /**
     * description:根据Message的reqId去更新数据
     **/
    private fun sendMessageSuccess(message: Message) {
        UIUtils.postTaskDelay({
            // 根据reqId获取到映射message
            val localMessage = reqMsgIdReflectMap[message.requestId]
            localMessage?.sendStatus = ChatConstant.STATUS_SUCCESS_MESSAGE
            localMessage?.msgId = message.msgId
            //LogUtils.info("输出LocalMessage的Id:$localMessage,\n对象是否相等：${localMessage == mMessage}")
            val content = ContentValues()
            content.put(DbConstant.SEND_STATUS, ChatConstant.STATUS_SUCCESS_MESSAGE)
            content.put(DbConstant.MSG_ID, message.msgId)
            val line = chatDao.updateForRequestId(mUserId, message.requestId, content)
            if (line > 0) {
                mView.setOrNotifyAdapter()
                // 发送成功的时候，移除映射表的message
                if (reqMsgIdReflectMap.contains(message.requestId)) {
                    reqMsgIdReflectMap.remove(message.requestId)
                }
            } else {
                //todo 发送成功，但是修改数据库失败，导致重复发送的bug有可能存在这里，之前的逻辑无论插库成功还是失败，都会走继续发送的逻辑
            }
        }, 400)
    }

    /**
     * 用户首次进入，发送获取新用户提示请求，
     * */
    override fun getUserTipsMessage() {
        LogUtils.info("socket-----------发送用户提示请求")
        SocketManager.instance.sendUserTipsMsgRequest()
    }

    /**
     * description:发送消息
     **/
    override fun sendMessage(message: String, contactId: String) {
        // 判断Token是否有效，无效则退出聊天
        if (TextUtils.isEmpty(UserUtils.getUserToken())) {
            ToastHelper.showToast("用户有效信息过期，请重新登录~")
            UserModuleNavigator.startLoginActivity(mContext)
            return
        }

//        todo 无论contactId是否为空，在发送消息阶段都不必去获取，具体由消息队列的 continueSendMessage() 来完成
//        假若传递的contactId是空的，尝试获取静态内存里面的ContractId(当前为Robot)
//        if (TextUtils.isEmpty(contactId)) {
//            this.contactId = SocketManagerHelper.instance.getContactRobotId()
//        } else {
//            this.contactId = contactId
//        }
//
//        // 假若确实contactId为空了，重新获取contactInfo。直到机器人数据有的情况下才发送。
//        if (TextUtils.isEmpty(this.contactId)) {
//            SocketManagerHelper.instance.refreshContactInfo()
//            isLostContactInfo = true
//        }

        mMessage = Message()
        val createTime = System.currentTimeMillis() / 1000L
        lastHistoryMessageChatId = chatDao.getLastData(UserUtils.getUserId().toString()).chatId
        LogUtils.info("lastHistoryMessageChatId ：$lastHistoryMessageChatId")
        mMessage.requestId = if (lastHistoryMessageChatId == -1) "1" else "${lastHistoryMessageChatId + 1}"
        mMessage.messageCreatorType = ChatConstant.CHAT_IDENTIFY_USER
        mMessage.messageCreator = chatUserInfo
        mMessage.msgType = ChatConstant.CHAT_MSG_TYPE_TEXT
        mMessage.createTime = createTime
        mMessage.sendStatus = ChatConstant.STATUS_WAITING_MESSAGE
        mMessage.toId = this.contactId
        mMessage.fromId = UserUtils.getUserId().toString()
        mMessage.content = message
        mMessage.messageRead = true

        val line = chatDao.add(UserUtils.getUserId().toString(), mMessage)
        //socket发送消息
        if (line != -1L) {
            mData.add(mMessage)
            reqMsgIdReflectMap[mMessage.requestId] = mMessage

            // 假若无网络状态的时候，不调用Socket发送流程。只是添加到Map里面
            // 假若无网络发送数据，那么会出现Socket通信上面的问题。
            // 假若Socket断开的情况下，也不往Socket的InputStream传输数据了。

            //todo 问题所在，连续快速点击发消息，会把消息添加到 reqMsgIdReflectMap 集合中，当某一条消息发送成功后，在消息成功的回调中会
            //todo 调用 continueSendMessage() 方法继续发送集合中的消息，而此时有一些消息正在发送并且存在集合 reqMsgIdReflectMap 中
            //todo 这时从集合中取出的消息就有可能是正在发送中的消息，这样消息就重复了。
            //todo 解决方案：创建一个消息轮询器，发送消息或者重发消息，都只是将消息添加到消息队列中而已，然后换醒轮训器进行消息轮询发送，
            //todo 前一条消息没发送成功前，不会进行下一条消息的发送，
            //if (!NetWorkHelper.isLostInternet()) {
            //LogUtils.info("发送消息：$mMessage")
            //SocketManager.instance.sendDirectMessage(mMessage)
            //}

            LogUtils.info("send message：$mMessage")
            mView.onMessageReceiverOrSend(mMessage)
//            mHandler.sendEmptyMessage(ChatConstant.MESSAGE_HANDLER_SEND_MESSAGE)
        } else {
            ToastHelper.showCenterToast("发送失败")
        }
    }

    /**
     * description:根据RequestId重新发送消息。但是重新发送消息的情况下，需要等后台回调了消息才发送第二条
     **/
    override fun resendMessage(requestId: String) {
        // 从映射表查出重发数据，调用Socket重发
        val resendMessage = reqMsgIdReflectMap[requestId]

        // 判断Token是否有效，无效则退出聊天
        if (TextUtils.isEmpty(UserUtils.getUserToken())) {
            ToastHelper.showToast("用户有效信息过期，请重新登录~")
            UserModuleNavigator.startLoginActivity(mContext)
            return
        }

        resendMessage?.let {

            //            todo 无论contactId是否为空，在发送消息阶段都不必去获取，具体由消息队列的 continueSendMessage() 来完成
//           假若Message的contactId是空的，尝试获取静态内存里面的ContractId(当前为Robot)
//            if (TextUtils.isEmpty(it.toId)) {
//                this.contactId = SocketManagerHelper.instance.getContactRobotId()
//                it.toId = contactId
//            }
//
//            // 假若确实contactId为空了，重新获取contactInfo。直到机器人数据有的情况下才发送。
//            if (TextUtils.isEmpty(it.toId)) {
//                SocketManagerHelper.instance.refreshContactInfo()
//                isLostContactInfo = true
//            }


            it?.sendStatus = ChatConstant.STATUS_WAITING_MESSAGE
            val content = ContentValues()
            content.put(DbConstant.SEND_STATUS, ChatConstant.STATUS_WAITING_MESSAGE)
            val line = chatDao.updateForRequestId(mUserId, it.requestId, content)
            if (line > 0) {
                mView.setOrNotifyAdapter()
            }
        }
    }

    /**
     * 删除所有聊天记录
     * */
    override fun deleteAllMessageRecordForUser(sessionId: Int, userId: Int) {
        chatDao.deleteAllMessageRecordForUser("$userId")
        //清空消息队列
        reqMsgIdReflectMap.clear()
    }

    override fun getSystemNotification() {
        SocketManager.instance.getSystemMessage()
    }

    /**
     * description:继续发送映射表的数据 .
     * 因为发送的消息和发送失败的消息都会进入ReflectMap里面，但是当前Socket的模型是：
     * 发送多条消息数据的时候，一定需要等待上一条消息数据的回调才能继续发送.
     * 所以：每次发送Map中的第一条数据，而且这条数据必须是sending的
     **/
    private fun continueSendMessageList() {
        if (isLoop()) {
            //无网络
            if (!NetworkUtils.isNetworkAvailable()) return
            //Socket是否已连接
            if (!SocketManagerHelper.instance.isSocketConnected()) {
                EventBusManager.postEvent(UserEvent(UserConstant.SOCKET_CONNECT_EVENT))
                return
            }
            //判断Socket状态是否AesKey验证成功
            LogUtils.info("issue--------------AesKey：${SocketManagerHelper.instance.isSocketAesKey()}")
            if (!SocketManagerHelper.instance.isSocketAesKey()) {
                SocketManagerHelper.instance.sendAesKey()
                return
            }
            //如果机器人ID为空，则从本地获取
            if (TextUtils.isEmpty(this.contactId)) {
                this.contactId = SocketManagerHelper.instance.getContactRobotId()
            }
            //机器人ID仍为空，则发送Socket请求获取
            LogUtils.info("issue---------contactId：$contactId")
            if (TextUtils.isEmpty(this.contactId)) {
                SocketManagerHelper.instance.refreshContactInfo()//重新获取机器人
                isLostContactInfo = true
                setIsLoop(false)
                return
            }
            LogUtils.info("issue--------------：消息队列大小：${reqMsgIdReflectMap.size}")



            setIsLoop(false)
            var sendingIndex = -1
            if (reqMsgIdReflectMap.isNotEmpty()) {
                for ((index, key) in reqMsgIdReflectMap.keys.withIndex()) {
                    if (reqMsgIdReflectMap[key]?.sendStatus == ChatConstant.STATUS_WAITING_MESSAGE) {
                        sendingIndex = index
                        break
                    }
                    LogUtils.info("issue-------：输出映射表数据---key:$key,value：${reqMsgIdReflectMap[key]}")
                }
                if (sendingIndex != -1) {
                    //todo 发送消息
                    LogUtils.info("轮询器发送消息")
                    val sendMsg = reqMsgIdReflectMap.valueAt(sendingIndex)
                    sendMsg?.sendStatus = ChatConstant.STATUS_SENDING_MESSAGE
                    sendMsg?.toId = this.contactId
                    val content = ContentValues()
                    content.put(DbConstant.SEND_STATUS, ChatConstant.STATUS_SENDING_MESSAGE)
                    val line = chatDao.updateForRequestId(mUserId, sendMsg.requestId, content)
                    if (line > 0) {
                        SocketManager.instance.sendDirectMessage(sendMsg)
                        LogUtils.info("issue-------：发送消息$sendMsg")
                    }
                } else {
                    setIsLoop(true)
                }
            } else {
                setIsLoop(true)
            }
        }
    }

    /**
     * * description:将通知转换成String集合
     **/
    private fun convertNotification2Str(systemMsgs: ArrayList<Message>): ArrayList<String> {
        val systemMsgList = ArrayList<String>()
        if (ListUtils.isNotEmpty(systemMsgs)) {
            systemMsgs.forEach {
                var message = it.msgContent.toString()
                systemMsgList.add(message)
            }
        }
        return systemMsgList
    }
}