package com.laka.ergou.mvp.chat.utils.socket

import android.text.TextUtils
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.ActivityManager
import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.common.threadpool.ThreadPoolHelp
import com.laka.ergou.mvp.chat.constant.ChatApiConstant
import com.laka.ergou.mvp.chat.constant.ChatConstant
import com.laka.ergou.mvp.chat.constant.ChatEventConstant
import com.laka.ergou.mvp.chat.model.bean.ChatRobotInfo
import com.laka.ergou.mvp.chat.model.bean.ChatUserInfo
import com.laka.ergou.mvp.chat.model.bean.Message
import com.laka.ergou.mvp.chat.view.activity.ChatActivity
import com.laka.ergou.mvp.db.ImplChatDao
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils

/**
 * @Author:Rayman
 * @Date:2019/2/21
 * @Description:封装SocketManger实现，使用外观模式 .
 * 屏蔽内部实现。
 * 仅暴露部分函数并封装里面的实现
 *
 */
class SocketManagerHelper {

    private var mContext = ApplicationUtils.getContext()

    /**
     * description:Socket通信的数据
     **/
    private var contactId = ""
    private var robotInfo: ChatRobotInfo? = null
    private var offlineMessageCount = 0
    private var offlineMessages = ArrayList<Message>()
    private var systemNoticeMessageCount = 0
    private var systemNoticeMessages = ArrayList<Message>()

    /**
     * description:数据库控制
     **/
    private var chatDao: ImplChatDao = ImplChatDao()

    /**
     * description:当前业务流程的数据配置
     **/
    private var chatUserInfo = ChatUserInfo(UserUtils.getUserId().toString(),
            UserUtils.getUserName(),
            UserUtils.getUserAvatar(),
            UserUtils.getUserGender(),
            0,
            0)
    private var mMessage: Message = Message()

    /**
     * description:未读消息配置
     **/
    private var unreadMessagesCount = 0

    companion object {
        val instance: SocketManagerHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SocketManagerHelper()
        }
    }

    init {
        SocketManager.instance.addCallback(object : SocketManager.SocketCallBack {

            override fun socketConnect() {

            }

            override fun socketDisconnect() {
                // Socket断开了连接
                LogUtils.info("Socket手动关闭---断开连接")
            }

            override fun getUserTipsMessageSuccess(messages: ArrayList<Message>) {

            }

            override fun getOfflineMessageCount(offlineMessageCount: Int) {
                LogUtils.info("获取离线消息回调：$offlineMessageCount")
                // 设置未读消息数
                SocketManager.instance.distributeRobot()
                if (offlineMessageCount > 0) {
                    SocketManager.instance.getOfflineMessage(offlineMessageCount)
                }

                this@SocketManagerHelper.offlineMessageCount = offlineMessageCount
                // 获取本地未读消息数
                unreadMessagesCount = offlineMessageCount + chatDao.getUnreadMessage(UserUtils.getUserId().toString()).size

                // 发送事件更新主页未读消息数
                EventBusManager.postEvent(HomeEventConstant.EVENT_UPDATE_UNREAD_MESSAGE, unreadMessagesCount)
            }

            override fun distributeRobot(robotInfo: ChatRobotInfo?) {
                if (robotInfo == null) {
                    // 获取不到机器人
                } else {
                    // 存储当前交流用户的ID（当前为机器人ID）
                    this@SocketManagerHelper.robotInfo = robotInfo
                    EventBusManager.postEvent(ChatEventConstant.EVENT_GET_ROBOT_ID)
                }
                LogUtils.error("获取机器人信息回调：$robotInfo")
            }

            override fun getUserInfo(userInfo: ChatUserInfo) {

            }

            override fun getOfflineMessage(offlineMessages: ArrayList<Message>) {
                LogUtils.info("获取到离线消息：$offlineMessages,\n长度：${offlineMessages.size}")
                this@SocketManagerHelper.offlineMessages = offlineMessages

                // 收到离线消息，入库。
                UserUtils.getUserId().let {
                    chatDao.addForBatch(it.toString(), offlineMessages)
                }
            }

            override fun sendMessageCallBack(message: Message) {

            }

            override fun replyMessageCallBack(message: Message) {
                // ********************--注意--******************
                // 存在一种情况就是，当我的ChatActivity页面关闭的时候，我们释放了对应的CallBack回调。
                // 假若在退出ChatActivity回到主页的时候（Socket还没断链），服务端发送了新的消息。
                // 那么这边就需要将这些数据入库
                // 还需要发送事件，更新主页的未读消息数
                if (!ActivityManager.getInstance().isContainActivity(ChatActivity::class.java)) {
                    UserUtils.getUserId().let {
                        message.messageRead = false
                        chatDao.add(UserUtils.getUserId().toString(), message)
                        unreadMessagesCount++
                        EventBusManager.postEvent(HomeEventConstant.EVENT_UPDATE_UNREAD_MESSAGE, unreadMessagesCount)
                    }
                }
            }

            override fun systemNoticeListCallBack(systemNotices: ArrayList<Message>) {
                // 保存List到缓存
                LogUtils.info("获取到系统通知消息：$systemNotices,\n长度：${systemNotices.size}")
                systemNoticeMessageCount = systemNoticeMessages.size
                systemNoticeMessages = systemNotices
            }

            override fun systemNoticeCallBack(message: Message) {

            }

            override fun handleError(code: Int, msg: String?) {
                // 统一处理错误
                when (code) {
                    ChatApiConstant.IM_E_INVALID_USER_TOKEN,
                    ChatApiConstant.IM_E_USER_NOT_EXIST,
                    ChatApiConstant.IM_E_USER_NOT_VERIFY -> {
                        LogUtils.info("SocketManagerHelper:--1--  token:" + UserUtils.getUserToken())
                        UserModuleNavigator.startLoginActivity(ApplicationUtils.getApplication().applicationContext)
                    }
                }
            }
        })
    }

    fun initSocket() {
        LogUtils.error("issue--------------：初始化Socket")
        ThreadPoolHelp.Builder().builder().execute {
            SocketManager.instance.startSocketConnect()
        }
    }

    fun stopSocketConnect() {
        ThreadPoolHelp.Builder().builder().execute {
            LogUtils.info("issue----------：退出登录，关闭Socket")
            SocketManager.instance.stopSocketConnect()
        }
    }

    fun getContactRobotId(): String {
        contactId = if (robotInfo != null && robotInfo?.robotId != null) {
            robotInfo?.robotId!!
        } else {
            ""
        }
        LogUtils.error("获取机器人ID：$contactId,\n机器人信息：$robotInfo")
        return contactId
    }

    /**
     * description:更新联系人信息
     **/
    fun refreshContactInfo() {
        // 重新获取Robot的信息，并回调
        if (SocketManager.instance.isAesHandShake) {
            SocketManager.instance.distributeRobot()
            LogUtils.info("重新获取机器人")
        } else {
            sendAesKey()
        }
    }

    fun getOfflineMessageCount(): Int {
        return offlineMessageCount
    }

    fun getOfflineMessages(): ArrayList<Message> {
        return offlineMessages
    }

    fun getSystemNoticeMessageCount(): Int {
        return systemNoticeMessageCount
    }

    fun getSystemNoticeMessages(): ArrayList<Message> {
        return systemNoticeMessages
    }

    @Deprecated(message = "")
    // 弃用---不想集成任何业务逻辑
    private fun sendMessage(message: String): Message? {
        if (TextUtils.isEmpty(UserUtils.getUserToken())) {
            ToastHelper.showToast("用户有效信息过期，请重新登录~")
            UserModuleNavigator.startLoginActivity(mContext)
            return null
        }

        if (TextUtils.isEmpty(contactId)) {
            refreshContactInfo()
            return null
        }

        mMessage = Message()
        val createTime = System.currentTimeMillis() / 1000L
        mMessage.messageCreatorType = ChatConstant.CHAT_IDENTIFY_USER
        mMessage.messageCreator = chatUserInfo
        mMessage.requestId = createTime.toString()
        mMessage.msgType = ChatConstant.CHAT_MSG_TYPE_TEXT
        mMessage.createTime = createTime
        mMessage.sendStatus = ChatConstant.STATUS_SENDING_MESSAGE
        mMessage.toId = this.contactId
        mMessage.fromId = UserUtils.getUserId().toString()
        mMessage.content = message

        SocketManager.instance.sendDirectMessage(mMessage)
        return mMessage
    }

    fun resetUnreadMessagesCount() {
        unreadMessagesCount = 0
    }

    // 测试数据
    private fun testLocalData() {

        chatUserInfo = ChatUserInfo(UserUtils.getUserId().toString(),
                UserUtils.getUserName(),
                UserUtils.getUserAvatar(),
                UserUtils.getUserGender(),
                0,
                0)

        // todo 测试插入本地测试数据
        val list = ArrayList<Message>()
        for (i in 0 until 12) {
            val message = Message()
            message.msgType = ChatConstant.CHAT_MSG_TYPE_TEXT
            message.createTime = System.currentTimeMillis()
            message.content = "--------聊天历史记录--------" + i
            message.messageCreatorType = ChatConstant.CHAT_IDENTIFY_USER
            message.messageCreator = chatUserInfo
            list.add(message)
        }
        chatDao.addForBatch(UserUtils.getUserId().toString(), list)
    }

    /**
     * 发送AesKey验证，重新握手
     * */
    fun sendAesKey() {
        // 重新执行Aes握手
        SocketManager.instance.sendAesKey()
        LogUtils.info("重新握手")
    }

    /**
     * 判断Socket是否已经链接或者链接中
     * */
    fun isSocketConnected(): Boolean {
        return SocketManager.instance.isSocketConnected()
    }

    /**
     * 判断Socket是否已经AesKey验证成功
     * */
    fun isSocketAesKey(): Boolean {
        return SocketManager.instance.isSocketAesKey()
    }

}