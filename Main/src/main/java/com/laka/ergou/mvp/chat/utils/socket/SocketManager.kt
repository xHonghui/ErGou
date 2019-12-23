package com.laka.ergou.mvp.chat.utils.socket

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import com.google.protobuf.ByteString
import com.laka.androidlib.util.*
import com.laka.ergou.mvp.chat.constant.ChatApiConstant
import com.laka.ergou.mvp.chat.model.bean.*
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import java.io.BufferedInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * @Author:Rayman
 * @Date:2019/2/14
 * @Description:Socket通信工具类 .
 * 针对当前项目的协议，通过Socket与服务器端进行数据交互
 */
class SocketManager {

    // 心跳包间隔时间
    private val HEART_BEAT_INTERVAL = 30000L

    // 数据体长度字段压缩标识值
    private val IM_PACKET_COMPRESSED = 0x80000000

    // IM ProtoBuf 数据压缩阈值
    private val IM_COMPRESS_THRESHOLD_SIZE = 512

    // AesKey
    private lateinit var mAesKey: ByteArray

    // Socket连接
    private var mSocketClient: SocketClient = SocketClient()
    private var socketThread: Thread? = null

    // Socket心跳包Timer
    private var mHeartBeatTimer: Timer? = null

    // 回调
    var mCallBacks = ArrayList<SocketCallBack>()

    // 发送信息的相关参数
    private lateinit var mMessage: Message

    // 错误与Message映射表
    private var errorCodeMsgReflectMap = HashMap<Int, String>()

    // 保存机器人数据。需要回调
    private var mContactInfo: Any? = null

    // 通过Handler回调到主线程
    private var mHandler: Handler = Handler(Looper.getMainLooper(), Handler.Callback {
        // 处理回调
        false
    })
    // 是否Aes握手成功
    var isAesHandShake = false

    // 是否手动关闭
    var isManualCloseSocket = false

    // Socket重连，每五秒重连一次。
    var isCountDownStart = false

    var countDownTimer = object : CountDownTimer(5 * 1000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            isCountDownStart = true
        }

        override fun onFinish() {
            isCountDownStart = false
            reconnect()
        }
    }

    // 定时器定时检查连续收到无效数据包(解析数据后，得到非定义响应命令)持续时间大于90秒时，当前连接需断开重连。
    private var mTimerUntreatedError = Timer()

    private var mTimerTaskUntreatedError = object : TimerTask() {
        override fun run() {
            LogUtils.info("issue----------：连续90秒未收到一条成功或者已定义的错误从而导致关闭Socket")
            stopSocketConnect()
            startSocketConnect()
        }
    }

    /**
     * 终止定时检查“未定义错误”的任务线程
     * */
    private fun cancelUntreatedError() {
        mTimerUntreatedError.cancel()
        mTimerTaskUntreatedError.cancel()
    }

    companion object {
        val instance: SocketManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SocketManager()
        }
    }

    private constructor() {
        initErrorCodeMsgReflect()
        //注意：SocketClient的回调是在子线程回调的
        mSocketClient.setConnectListener(object : SocketClient.SocketListener {
            override fun didConnect() {
                //TODO 发送AesKey权鉴
                LogUtils.info("issue----------------：didConnect()回调方法，这里发送AesKey权鉴")
                sendAesKey()
            }

            override fun didDisconnect() {
                // Socket断开了连接
                mCallBacks.forEach {
                    it.socketDisconnect()
                }

                // 假若不是手动断开连接,而且网络是正常的。那么说明就是服务器端断开了连接
                if (!isManualCloseSocket && !isCountDownStart) {
                    countDownTimer.start()
                    LogUtils.info("Socket断开连接了")
                }
            }

            override fun didReadData(inputStream: BufferedInputStream) {
                // 处理服务器数据
                try {
                    handleSocketResponse(inputStream)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    LogUtils.error("issue--------：Socket读取数据报错---${exception.message}")
                }
            }

            override fun didErrorOccur(code: Int, message: String) {
                // Socket连接出现错误
                mCallBacks.forEach {
                    it.handleError(code, message)
                }
            }
        })
        socketThread = Thread(mSocketClient)
    }

    /**
     * description:初始化错误码和错误信息映射表
     **/
    private fun initErrorCodeMsgReflect() {
        errorCodeMsgReflectMap[ChatApiConstant.IM_RSP_AES_KEY_FAILED] = "权鉴认证失败"
        errorCodeMsgReflectMap[ChatApiConstant.IM_RSP_SERIALIZE_FAILED] = "权鉴认证失败"
        errorCodeMsgReflectMap[ChatApiConstant.IM_E_RSA_PRIVATE_DECRYPT_FAILED] = "RSA解密失败"
        errorCodeMsgReflectMap[ChatApiConstant.IM_E_PROTOCAL_SERIALIZE_FAILED] = "ProtoBuffer反序列化失败"
        errorCodeMsgReflectMap[ChatApiConstant.IM_E_INVALID_USER_TOKEN] = "用户Token过期"
        errorCodeMsgReflectMap[ChatApiConstant.IM_E_USER_NOT_EXIST] = "用户不存在"
        errorCodeMsgReflectMap[ChatApiConstant.IM_E_EMPTY_MESSAGE] = "发送空消息"
        errorCodeMsgReflectMap[ChatApiConstant.IM_E_USER_FORBIDDEN] = "用户被禁言"
        errorCodeMsgReflectMap[ChatApiConstant.IM_E_INVALAD_REQUEST_DATA] = "请求数据无效"
        errorCodeMsgReflectMap[ChatApiConstant.IM_E_TRY_AGAIN_LATER] = "请重新试试"
        errorCodeMsgReflectMap[ChatApiConstant.IM_E_USER_NOT_VERIFY] = "用户未认证"
    }

    /**
     * description:打开Socket连接
     **/
    @Synchronized
    fun startSocketConnect() {
        isManualCloseSocket = false
        LogUtils.info("issue-----------------：startSocketConnect")
        try {
            mSocketClient.startWork()
            if (socketThread == null) {
                socketThread = Thread(mSocketClient)
            }
            if (socketThread?.state == Thread.State.NEW) {
                socketThread?.start()
            }
        } catch (e: IllegalThreadStateException) {
            e.printStackTrace()
            LogUtils.info("issue----------：startSocketConnect 方法发生异常关闭Socket")
            stopSocketConnect()
        } finally {

        }
    }

    /**
     * description:断开Socket连接
     **/
    @Synchronized
    fun stopSocketConnect() {
        isManualCloseSocket = true
        mSocketClient.disConnectSocket()
        socketThread?.let {
            socketThread = null
        }
        stopHeartbeatTimer()
        LogUtils.info("停止Socket线程")
    }

    /**
     * description:发送AESKey规则：
     * 128Bytes加密数据 = (4Bytes ProtoBuf数据长度 + ProtoBuf 数据 + 填0)
     * 发送136Bytes数据 = 4Bytes数据长度 + 4Bytes命令 + 128Bytes使用RSA公钥加密数据
     **/
    fun sendAesKey() {
        //发送 AesKey 前，清空消息队列，因为没发送 AesKey 验证前，服务器是不会接收任何客户端的消息的
        mSocketClient.clearQueueMessage()

        // 创建一个128bytes数组传递数据，默认初始化为0
        var aesKeyByteArray = ByteArray(128)
        for ((index, byte) in aesKeyByteArray.withIndex()) {
            aesKeyByteArray[index] = 0
        }

        // 创建128随机AesKey
        mAesKey = AesKeyHelper.createRandomAesKey(128)

        // 获取系统数字版本号
        var versionCode = SystemUtils.getVersionCode()

        // 用户Token
        var userToken = UserUtils.getUserToken()

        if (TextUtils.isEmpty(userToken)) {
            // 假若用户Token为空，处理并返回
            handleSocketError(ChatApiConstant.IM_E_INVALID_USER_TOKEN)
            return
        }

        // 构建Aes请求数据
        var aesKeyProtoBufRequest = ErgouIm.Aes_Key_Request.newBuilder()
                .setPlantform(ErgouIm.PlantformType.ANDROID)
                .setVersion(versionCode)
                .setAesKey(ByteString.copyFrom(mAesKey))
                .setToken(userToken).build()

        //获取AesKeyRequest--ProtoBufferData长度
        var aesKeyProtoBufByteArray = aesKeyProtoBufRequest.toByteArray()
        var protoBufLength = aesKeyProtoBufByteArray.size

        // 先存储4bytes protoBuf数据长度，然后存入protoBuf数据
        // 128Bytes加密数据 = (4Bytes ProtoBuf数据长度 + ProtoBuf 数据 + 填0)
        System.arraycopy(ByteUtils.int2Bytes(protoBufLength), 0, aesKeyByteArray, 0, 4)
        System.arraycopy(aesKeyProtoBufByteArray, 0, aesKeyByteArray, 4, protoBufLength)

        // RSA加密AesKey
        var encryptAesKeyByteArray = EncryptUtils.rsaEncryptData(aesKeyByteArray, EncryptUtils.getPublicKey(ChatApiConstant.SOCKET_RSA_PUBLIC_KEY))

        // 依次发送数据长度、操作符、加密的AesKey数据
        // 长度为136，4位数据长度 + 4位操作符长度 + 128位AesKey数据长度
        sendSocketDataCommand(ChatApiConstant.IM_REQ_AES_KEY, encryptAesKeyByteArray)
    }

    /**
     * description:发送心跳包请求数据
     **/
    fun sendKeepAliveRequest() {
        //LogUtils.info("Socket---发送心跳包")
        sendEncryptPBDataCommand(ChatApiConstant.IM_REQ_KEEP_ALIVE)
    }

    /**
     * description:进入购小二页面，获取到购小二信息后发送请求，新用户第一次进入“购小二”时，
     * 发送获取新用户提示请求，IM服务端会返回图片消息格式。
     * */
    fun sendUserTipsMsgRequest() {
        ErgouIm.UserTips_Message_Request.newBuilder()
                .setSeqId(System.currentTimeMillis())
                .setTipsId(1) //目前写死
                .build()
        sendEncryptPBDataCommand(ChatApiConstant.IM_REQ_GET_USERTIPS_MSG)
    }

    /**
     * description:分配机器人
     **/
    fun distributeRobot() {
        var distributeRobotReq = ErgouIm.Assign_Target_Request.newBuilder()
                .setSeqId(System.currentTimeMillis())
                .setType(ErgouIm.Assign_Target_Request.TargetType.ROBOT)
                .build()
        sendEncryptPBDataCommand(ChatApiConstant.IM_REQ_ASSIGN_TARGET, protoBufferData = distributeRobotReq.toByteArray())
    }

    /**
     * description:创建用户消息并发送
     * @param message 发送的信息
     * @param reqId 唯一的请求ID
     **/
    fun sendDirectMessage(message: Message) {
        this.mMessage = message
        var sendMessageReq = ErgouIm.Send_Message_Request.newBuilder()
                .setSeqId(mMessage.requestId.toLong())
                .setToId(mMessage.toId)
                .setContent(message.content)
                .setMsgType(CommunicateConverter.covertMessageType(message.msgType))
                .build()

        LogUtils.info("发送message数据：$message,\n输出ProtoBuffer Message：$sendMessageReq")
        sendEncryptPBDataCommand(ChatApiConstant.IM_REQ_SEND_MESSAGE, protoBufferData = sendMessageReq.toByteArray())
    }

    /**
     * description:获取用户信息(暂时没用，因为当前是需要和机器人聊天。而不是User to User)
     **/
    fun getUserInfo() {
        sendEncryptPBDataCommand(ChatApiConstant.IM_REQ_GET_USER_INFO)
    }

    /**
     * description:获取离线消息数据
     **/
    fun getOfflineMessage(offlineMsgCount: Int) {
        var offlineMsgReq = ErgouIm.Offline_Message_Request.newBuilder()
                .setSeqId(System.currentTimeMillis())
                .setCount(offlineMsgCount)
                .build()
        sendEncryptPBDataCommand(ChatApiConstant.IM_REQ_GET_OFFLINE_MESSAGE, protoBufferData = offlineMsgReq.toByteArray())
    }

    /**
     * description:获取系统消息列表
     **/
    fun getSystemMessage() {
        var systemMsgReq = ErgouIm.Broadcast_Message_Request.newBuilder()
                .setSeqId(System.currentTimeMillis())
                .setType(ErgouIm.BroadcastType.TOP)
                .setCount(50)
                .build()
        LogUtils.error("获取系统消息：$systemMsgReq")
        sendEncryptPBDataCommand(ChatApiConstant.IM_REQ_GET_BROADCAST_MSG, protoBufferData = systemMsgReq.toByteArray())
    }

    /**
     * description:推送离线消息
     **/
    fun pushOfflineMessage() {
        sendEncryptPBDataCommand(ChatApiConstant.IM_REQ_PUSH_OFFLINE_MESSAGE)
    }

    /**
     * description:启动心跳包计时器
     **/
    private fun startHeartbeatTimer() {
        var timerTask = object : TimerTask() {
            override fun run() {
                sendKeepAliveRequest()
            }
        }
        mHeartBeatTimer = Timer(true)
        mHeartBeatTimer?.schedule(timerTask, HEART_BEAT_INTERVAL, HEART_BEAT_INTERVAL)
    }

    private fun stopHeartbeatTimer() {
        mHeartBeatTimer?.let {
            it.cancel()
            mHeartBeatTimer = null
        }
    }

    /**
     * description:根据收发协议，使用AESKey加密ProtoBuffer数据并发送 .
     * 4Bytes 数据长度 | 4Bytes 命令 | AESKey加密后的 【4Bytes ProtoBuf数据长度 | 请求ProtoBuf 数据】
     * 统一处理ProtoBuffer数据，添加到ByteBuffer并发送socket数据
     * 与sendCommand的区别在于：当前是对整个发送数据进行AES加密。
     **/
    private fun sendEncryptPBDataCommand(type: Int, isEncrypt: Boolean = true, protoBufferData: ByteArray = ByteArray(0)) {
        // 拼接protoBuf数据
        val protoBufferDataSize = protoBufferData.size
        var sourceData = ByteArray(4 + protoBufferDataSize)

        // 需要根据PB数据的长度，判断是否进行压缩
        val isNeedCompress = protoBufferDataSize > IM_COMPRESS_THRESHOLD_SIZE
        LogUtils.error("输出发送pb数据长度：$protoBufferDataSize,\n是否需要压缩：$isNeedCompress")
        if (isNeedCompress) {
            // 假若需要压缩的话，通过ZLib工具类压缩，然后数据的长度需要重新比对
            val compressData = ZlibUtils.compress(protoBufferData)
            // 重置的长度，需要用压缩的长度与标志位进行或运算。
            val compressDataSize = IM_PACKET_COMPRESSED.or(compressData.size.toLong())
            System.arraycopy(ByteUtils.long2UnsignedInt(compressDataSize), 0, sourceData, 0, 4)
            System.arraycopy(compressData, 0, sourceData, 4, compressData.size)
        } else {
            System.arraycopy(ByteUtils.int2Bytes(protoBufferDataSize), 0, sourceData, 0, 4)
            System.arraycopy(protoBufferData, 0, sourceData, 4, protoBufferDataSize)
        }
        var encryptData = ByteArray(0)

        // 假若AesKey初始化失败，默认发送sourceData
        if (::mAesKey.isInitialized) {
            if (isEncrypt) {
                encryptData = EncryptUtils.aesEncrypt(sourceData, mAesKey)
            }
        } else {
            encryptData = sourceData
        }

        sendSocketDataCommand(type, if (isEncrypt) {
            encryptData
        } else sourceData)
    }

    /**
     * description:根据协议发送数据，发送整段数据到服务器端。
     * 4Bytes 数据长度 | 4Bytes 命令 | 待发送数据
     **/
    private fun sendSocketDataCommand(type: Int, data: ByteArray = ByteArray(0)) {
        var dataSize = data.size
        dataSize += 8
        // 拼接请求的协议数据(分配ByteBuffer的时候，需要加上ByteOrder.LITTLE_ENDIAN--设置成小端)
        var byteBuffer = ByteBuffer.allocate(dataSize).order(ByteOrder.LITTLE_ENDIAN)
        byteBuffer.putInt(dataSize)
        byteBuffer.putInt(type)
        if (data.isNotEmpty()) {
            byteBuffer.put(data)
        }
        sendSocketData(byteBuffer)
    }

    /**
     * description:处理Socket响应
     **/
    private fun handleSocketResponse(buffInputStream: BufferedInputStream) {

        LogUtils.info("issue-------------：读取Socket输入流")
        var length = ByteUtils.readInt(buffInputStream)
        val command = ByteUtils.readInt(buffInputStream)
        // 心跳包的回调只有8个bytes数据
        if (command == ChatApiConstant.IM_RSP_KEEP_ALIVE) {
            //LogUtils.info("Socket----收到服务器端心跳包回应")
        }
        LogUtils.info("issue----------：length=$length，，command=$command")
        // 按照定义的规则，数据中前4位为数据长度，4-8位为操作命令，剩下的就是加密的protoBuffer数据
        // 加密的ProtoBuffer数据，需要用AES解密，解密后前4位为数据长度，4-8位为ResponseCode，剩下的就是ProtoBuffer的数据了
        if (length <= 8) {
            return
        }
        length -= 8
        LogUtils.error("issue-------------：Socket服务器回应 length = $length,\n" +
                "Socket服务器回应 : 0x${Integer.toHexString(command)}")
        when (command) {
            ChatApiConstant.IM_RSP_AES_KEY -> {
                // 鉴权响应
                LogUtils.info("issue------------：鉴权响应成功")
                handleAesKeyResponse(buffInputStream, length)
            }
            ChatApiConstant.IM_RSP_AES_KEY_FAILED -> {
                handleSocketError(ChatApiConstant.IM_RSP_AES_KEY_FAILED)
            }
            ChatApiConstant.IM_RSP_ASSIGN_TARGET -> {
                handleDistributeRobotResponse(buffInputStream, length)
            }
            ChatApiConstant.IM_RSP_GET_USER_INFO -> {
                handleUserInfoResponse(buffInputStream, length)
            }
            ChatApiConstant.IM_RSP_GET_BROADCAST_MSG -> {
                handleSystemNoticeMsgListResponse(buffInputStream, length)
            }
            ChatApiConstant.IM_REQ_BROADCAST_MSG -> {
                handleSystemNoticeMsgResponse(buffInputStream, length)
            }
            ChatApiConstant.IM_REQ_UNICAST_SAY -> {
                handleReplyMsgResponse(buffInputStream, length)
            }
            ChatApiConstant.IM_RSP_SEND_MESSAGE -> {
                handleDirectMsgResponse(buffInputStream, length)
            }
            ChatApiConstant.IM_RSP_GET_OFFLINE_MESSAGE -> {
                handleOfflineMsgResponse(buffInputStream, length)
            }
            ChatApiConstant.IM_RSP_PUSH_OFFLINE_MESSAGE -> {
                handleOfflineMsgPushResponse(buffInputStream, length)
            }
            ChatApiConstant.IM_RSP_GET_USERTIPS_MSG -> {
                handleUserTipsResponse(buffInputStream, length)
            }
            else -> {
                LogUtils.info("Socket连接，权鉴失败")
            }
        }
    }

    /**
     * description:进入购小二页面，获取到购小二信息后发送请求，
     * 新用户第一次进入“购小二”时，发送获取新用户提示请求，IM服务端会返回图片消息格式。
     * */
    private fun handleUserTipsResponse(buffInputStream: BufferedInputStream, length: Int) {
        LogUtils.info("socket-------------：获取到图片响应-----length=$length")
        var userTipsMessageResponse: ErgouIm.UserTips_Message_Response
        var socketResponse = handleCommonResponse(buffInputStream, length)
        LogUtils.info("socket-------------：获取到图片响应-----socketResponse.code=${socketResponse.code}")
        if (socketResponse.code == ChatApiConstant.SOCKET_RESPONSE_OK) {
            if (socketResponse.protoBufData.isNotEmpty()) {
                userTipsMessageResponse = ErgouIm.UserTips_Message_Response.parseFrom(socketResponse.protoBufData)
                userTipsMessageResponse?.let {
                    mHandler.post {
                        mCallBacks.forEach { callback ->
                            callback.getUserTipsMessageSuccess(CommunicateConverter.convertMessages(it.messageList, mContactInfo))
                        }
                    }
                }
            }
        } else {
            LogUtils.error("Socket------发送获取新用户提示请求失败")
        }
    }


    /**
     * TODO description:处理AesKey响应，此处才算是正在的连接Socket成功
     **/
    private fun handleAesKeyResponse(buffInputStream: BufferedInputStream, length: Int) {
        LogUtils.info("issue-------------：鉴权响应")
        var aesKeyResponse: ErgouIm.Aes_Key_Response? = null
        var socketResponse = handleCommonResponse(buffInputStream, length)
        var offlineMessageCount: Int

        // 权鉴通过的情况下，开始发送心跳包
        if (socketResponse.code == ChatApiConstant.SOCKET_RESPONSE_OK) {
            if (socketResponse.protoBufData.isNotEmpty()) {
                aesKeyResponse = ErgouIm.Aes_Key_Response.parseFrom(socketResponse.protoBufData)
                offlineMessageCount = aesKeyResponse.omNum
            } else {
                offlineMessageCount = 0
            }
            mHandler.post {
                mCallBacks.forEach {
                    it.getOfflineMessageCount(offlineMessageCount)
                }
            }
            startHeartbeatTimer()
            isAesHandShake = true

            //TODO 连接成功
            LogUtils.info("issue------：连接成功")
            mCallBacks.forEach {
                it.socketConnect()
            }
            //链接成功
            mSocketClient.setConnected(SocketClient.ConnectState.AESKEY_AUTHENTICATION)
        } else {
            LogUtils.error("issue------：转换错误")
            handleSocketError(ChatApiConstant.IM_RSP_SERIALIZE_FAILED)
            isAesHandShake = false
        }
    }

    /**
     * description:处理分配机器人响应
     **/
    private fun handleDistributeRobotResponse(buffInputStream: BufferedInputStream, length: Int) {
        var distributeRobotResponse: ErgouIm.Assign_Target_Response
        var socketResponse = handleCommonResponse(buffInputStream, length)
        if (socketResponse.code == ChatApiConstant.SOCKET_RESPONSE_OK) {
            if (socketResponse.protoBufData.isNotEmpty()) {
                distributeRobotResponse = ErgouIm.Assign_Target_Response.parseFrom(socketResponse.protoBufData)
                distributeRobotResponse?.let {
                    var robotInfo = ChatRobotInfo(it.userId, it.nickname, it.avatar, it.gender)
                    mHandler.post {
                        mCallBacks.forEach {
                            mContactInfo = robotInfo
                            it.distributeRobot(robotInfo)
                        }
                    }
                }
            }
        } else {
            mHandler.post {
                mCallBacks.forEach {
                    it.distributeRobot(null)
                }
            }
        }
    }

    /**
     * description:处理用户信息响应
     **/
    private fun handleUserInfoResponse(buffInputStream: BufferedInputStream, length: Int) {
        var userInfoResponse: ErgouIm.User_Info_Response
        var socketResponse = handleCommonResponse(buffInputStream, length)
        if (socketResponse.code == ChatApiConstant.SOCKET_RESPONSE_OK) {
            if (socketResponse.protoBufData.isNotEmpty()) {
                userInfoResponse = ErgouIm.User_Info_Response.parseFrom(socketResponse.protoBufData)
                userInfoResponse?.let {
                    var userInfo = ChatUserInfo(it.userId, it.nickname, it.avatar, it.gender)
                    mHandler.post {
                        mCallBacks.forEach {
                            mContactInfo = userInfo
                            it.getUserInfo(userInfo)
                        }
                    }
                }
            }
        } else {
            LogUtils.error("获取用户信息失败")
        }
    }

    /**
     * description:处理私信数据响应
     **/
    private fun handleDirectMsgResponse(buffInputStream: BufferedInputStream, length: Int) {
        var directMsgResponse: ErgouIm.Send_Message_Response?
        var socketResponse = handleCommonResponse(buffInputStream, length)

        if (socketResponse.code == ChatApiConstant.SOCKET_RESPONSE_OK) {
            directMsgResponse = ErgouIm.Send_Message_Response.parseFrom(socketResponse.protoBufData)
            directMsgResponse?.let {
                if (::mMessage.isInitialized) {
                    mMessage.requestId = it.seqId.toString()
                    mMessage.createTime = it.createTime.toLong()
                    mMessage.msgId = it.msgId.toInt()
                    mHandler.post {
                        mCallBacks.forEach {
                            it.sendMessageCallBack(mMessage)
                        }
                    }
                }
            }
        } else {
            // 回调发送失败
            LogUtils.error("Socket---发送信息失败")
            mMessage.msgId = -1
            mHandler.post {
                mCallBacks.forEach {
                    it.sendMessageCallBack(mMessage)
                }
            }
        }
    }

    /**
     * description:处理系统消息列表响应
     **/
    private fun handleSystemNoticeMsgListResponse(buffInputStream: BufferedInputStream, length: Int) {
        var systemMsgListResponse: ErgouIm.Broadcast_Message_Response? = null
        var socketResponse = handleCommonResponse(buffInputStream, length)

        if (socketResponse.code == ChatApiConstant.SOCKET_RESPONSE_OK) {
            systemMsgListResponse = ErgouIm.Broadcast_Message_Response.parseFrom(socketResponse.protoBufData)
            systemMsgListResponse?.let {
                mHandler.post {
                    mCallBacks.forEach { callBack ->
                        callBack.systemNoticeListCallBack(
                                CommunicateConverter.convertMessages(it.messageList, mContactInfo)
                        )
                    }
                }
            }
        }
    }

    /**
     * description:处理系统消息响应
     * 因为系统消息可能从服务器端新增，然后通过InputStream传递回来的。
     * 所以是不带Code类型
     **/
    private fun handleSystemNoticeMsgResponse(buffInputStream: BufferedInputStream, length: Int) {
        var systemMsgResponse: ErgouIm.Broadcast_Message_Notify? = null
        var socketResponse = handleCommonResponse(buffInputStream, length, false)

        systemMsgResponse = ErgouIm.Broadcast_Message_Notify.parseFrom(socketResponse.protoBufData)
        systemMsgResponse?.let {
            mHandler.post {
                mCallBacks.forEach { callBack ->
                    callBack.systemNoticeCallBack(CommunicateConverter.convertMessage(it.message, mContactInfo))
                }
            }
        }
    }

    /**
     * description:处理单播数据响应(回复的消息)
     * 因为单播的是走REQ的指令返回，所以是不带Code的。
     **/
    private fun handleReplyMsgResponse(buffInputStream: BufferedInputStream, length: Int) {
        var replyMsgResponse: ErgouIm.IM_Message? = null
        var socketResponse = handleCommonResponse(buffInputStream, length, false)

        if (socketResponse.protoBufData.isNotEmpty()) {
            replyMsgResponse = ErgouIm.IM_Message.parseFrom(socketResponse.protoBufData)
            replyMsgResponse?.let {
                // 获取到回复数据，处理并回调
                // 通过转换器转换成本地Message，然后回调
                mHandler.post {
                    mCallBacks.forEach { callback ->
                        // 需要拼装机器人数据，然后返回
                        // 返回的数据，需要添加上reqId（当前时间点），然后回调出去
                        callback.replyMessageCallBack(CommunicateConverter.convertMessage(it, mContactInfo,
                                it.createTime.toString()))
                    }
                }
            }
        }
    }

    /**
     * description:处理离线数据推送响应
     **/
    private fun handleOfflineMsgPushResponse(buffInputStream: BufferedInputStream, length: Int) {
        var offlineMsgResponse: ErgouIm.Offline_Message_Response? = null
        var socketResponse = handleCommonResponse(buffInputStream, length)

        if (socketResponse.code == ChatApiConstant.SOCKET_RESPONSE_OK) {
            offlineMsgResponse = ErgouIm.Offline_Message_Response.parseFrom(socketResponse.protoBufData)
            offlineMsgResponse?.let {
                // 回调离线数据---转换服务器端数据到本地数据列表
                mHandler.post {
                    mCallBacks.forEach { callback ->
                        callback.getOfflineMessage(
                                CommunicateConverter.convertMessages(it.messageList, mContactInfo))
                    }
                }
            }
        }
    }

    /**
     * description:处理离线数据列表响应
     **/
    private fun handleOfflineMsgResponse(buffInputStream: BufferedInputStream, length: Int) {
        var offlineMsgResponse: ErgouIm.Offline_Message_Response? = null
        var socketResponse = handleCommonResponse(buffInputStream, length)

        if (socketResponse.code == ChatApiConstant.SOCKET_RESPONSE_OK) {
            offlineMsgResponse = ErgouIm.Offline_Message_Response.parseFrom(socketResponse.protoBufData)
            offlineMsgResponse?.let {
                mHandler.post {
                    mCallBacks.forEach { callback ->
                        callback.getOfflineMessage(
                                CommunicateConverter.convertMessages(it.messageList, mContactInfo))
                    }
                }
            }
        }
    }

    /**
     * description:根据协议，统一处理Response
     * 4Bytes 数据长度 | 4Bytes 命令 | 【使用AES KEY加密的 4Bytes状态码 + 4Bytes ProtoBuf length + ProtoBuf 数据】
     * 处理括号内中的数据，然后返回正确的ProtoBuffer数据。
     * PS:返回的指令是：RSP的就会带有状态码，REQ的就没有状态码
     * 假若数据为空的情况下(指令为RSP,即便code是正确的)，后面的8-N位是protoBuf数据就会不返回的。
     * @param buffInputStream Socket输入流
     * @param length 数据长度
     * @param isWithResponseCode 返回的数据是否带有code，带code就需要解析code。
     * @return 回调code和data
     **/
    private fun handleCommonResponse(buffInputStream: BufferedInputStream, length: Int, isWithResponseCode: Boolean = true): SocketResponse {
        var socketResponse = SocketResponse()
        if (length < 0) {
            socketResponse.code = ChatApiConstant.SOCKET_RESPONSE_FAIL
            return socketResponse
        }
        LogUtils.info("chat byteArray length -------- :$length")
        if (::mAesKey.isInitialized) {
            // 加密的AES数据
            var encryptData = ByteArray(length)
            ByteUtils.readBytes(buffInputStream, encryptData)
            var decryptData = EncryptUtils.aesDecrypt(encryptData, mAesKey)

            // 假若解密不出任何数据，返回错误码
            if (decryptData == null) {
                socketResponse.code = ChatApiConstant.SOCKET_RESPONSE_FAIL
                return socketResponse
            }

            //  需要Code处理的情况下
            var dataLength: Int
            var isCompress: Boolean
            if (isWithResponseCode) {
                // 前四位为Code，4-8位为length
                var code = ByteUtils.bytes2Int(decryptData.copyOfRange(0, 4))

                // 获取到dataLength
                dataLength = ByteUtils.bytes2Int(decryptData.copyOfRange(4, 8))

                // 根据DataLength与标志位做与运算，得出服务端传递的数据是否为压缩的数据
                isCompress = IM_PACKET_COMPRESSED.and(dataLength.toLong()) == IM_PACKET_COMPRESSED
                dataLength = dataLength.and(IM_PACKET_COMPRESSED.inv().toInt())

                LogUtils.error("输出接收的数据长度：$dataLength，" +
                        "\n输出与运算结果：${Integer.toHexString(dataLength.and(IM_PACKET_COMPRESSED.toInt()))}" +
                        ",\n是否压缩：$isCompress")

                if (code == ChatApiConstant.SOCKET_RESPONSE_OK) {
                    //数据处理成功，则停止“未定义错误”的处理线程
                    cancelUntreatedError()

                    // 需要判断Size是否正确，还需要判断解析出来的dataLength长度是否正常
                    if (decryptData.size > 8 && dataLength > 0) {
                        if (isCompress) {
                            // 解压数据
                            val deCompressData = ZlibUtils.decompress(decryptData.copyOfRange(8, 8 + dataLength))
                            socketResponse.protoBufData = deCompressData
                            LogUtils.info("解压数据：$deCompressData")
                        } else {
                            socketResponse.protoBufData = decryptData.copyOfRange(8, 8 + dataLength)
                            LogUtils.info("原始数据：${decryptData.copyOfRange(8, 8 + dataLength)}")
                        }
                    }
                } else {
                    LogUtils.info("服务端响应code:$code")
                    handleSocketError(code)
                }
                socketResponse.code = code
            } else {
                dataLength = ByteUtils.bytes2Int(decryptData.copyOfRange(0, 4))

                // 根据DataLength与标志位做与运算，得出服务端传递的数据是否为压缩的数据
                isCompress = IM_PACKET_COMPRESSED.and(dataLength.toLong()) == IM_PACKET_COMPRESSED
                dataLength = dataLength.and(IM_PACKET_COMPRESSED.inv().toInt())

                LogUtils.error("输出接收的数据长度：$dataLength，" +
                        "\n输出与运算结果：${Integer.toHexString(dataLength.and(IM_PACKET_COMPRESSED.toInt()))}" +
                        ",\n是否压缩：$isCompress")

                if (decryptData.size > 4 && dataLength > 0) {
                    if (isCompress) {
                        val deCompressData = ZlibUtils.decompress(decryptData.copyOfRange(4, (4 + dataLength.toLong()).toInt()))
                        socketResponse.protoBufData = deCompressData
                    } else {
                        socketResponse.protoBufData = decryptData.copyOfRange(4, 4 + dataLength)
                    }
                }
            }
            socketResponse.dataLength = dataLength
            LogUtils.info("输出响应体的code：${socketResponse.code}" +
                    ",\n输出响应体数据长度：${socketResponse.dataLength}" +
                    ",\n输出响应体数据：${ByteUtils.byteArrToHexStr(socketResponse.protoBufData)}")
            return socketResponse
        }
        return socketResponse
    }

    /**
     * description:处理Socket数据错误问题
     **/
    private fun handleSocketError(error: Int) {
        LogUtils.error("Socket Error---$error")
        if (error == ChatApiConstant.IM_RSP_AES_KEY_FAILED
                || error == ChatApiConstant.IM_RSP_SERIALIZE_FAILED) {
            //AesKey响应错误，直接断开Socket再重连
            LogUtils.info("issue----------：AesKey发送失败关闭Socket")
            stopSocketConnect()
            startSocketConnect()
            return
        }
        when (error) {
            ChatApiConstant.IM_E_RSA_PRIVATE_DECRYPT_FAILED,
            ChatApiConstant.IM_E_PROTOCAL_SERIALIZE_FAILED,
            ChatApiConstant.IM_E_INVALID_USER_TOKEN,
            ChatApiConstant.IM_E_USER_NOT_EXIST,
            ChatApiConstant.IM_E_EMPTY_MESSAGE,
            ChatApiConstant.IM_E_USER_FORBIDDEN,
            ChatApiConstant.IM_E_INVALAD_REQUEST_DATA,
            ChatApiConstant.IM_E_TRY_AGAIN_LATER,
            ChatApiConstant.IM_E_USER_NOT_VERIFY -> {
                mHandler.post {
                    mCallBacks.forEach {
                        it.handleError(error, errorCodeMsgReflectMap[error])
                        LogUtils.info("issue---------：权鉴认证失败")
                    }
                }
                //出现已定义状态码，则停止“未定义错误”处理的任务线程
                cancelUntreatedError()
            }
            else -> { //未定义的错误码
                //启动未定义错误处理任务线程，如果超过90秒，仍然没有出现“成功”或者“定义好的错误码”，
                //则断开重连
                mTimerUntreatedError.schedule(mTimerTaskUntreatedError, 90 * 1000)
            }
        }
    }

    /**
     * description:发送socket数据
     **/
    private fun sendSocketData(byteBuffer: ByteBuffer) {
        mSocketClient.write(byteBuffer)
    }

    /**
     * description:重连Socket
     **/
    @Synchronized
    fun reconnect() {
        LogUtils.info("issue-----------：计时器重连Socket")
        isManualCloseSocket = false
        mSocketClient.disConnectSocketWithoutCallback()
        socketThread?.let {
            socketThread = null
        }
        stopHeartbeatTimer()
        startSocketConnect()
    }

    fun addCallback(callBack: SocketCallBack) {
        mCallBacks.add(callBack)
    }

    fun removeCallback(callBack: SocketCallBack) {
        mCallBacks.remove(callBack)
    }

    /**
     * description:Socket相关回调
     **/
    interface SocketCallBack {

        /**
         * description:Socket连接
         **/
        fun socketConnect()

        /**
         * description:Socket断开连接
         **/
        fun socketDisconnect()

        /**
         * description:首次进入购小二，获取提示消息成功
         * */
        fun getUserTipsMessageSuccess(messages: ArrayList<Message>)

        /**
         * description:获取用户离线消息数量(从AesKey请求中返回)
         **/
        fun getOfflineMessageCount(offlineMessageCount: Int)

        /**
         * description:分配机器人
         **/
        fun distributeRobot(robotInfo: ChatRobotInfo?)

        /**
         * description:获取对方用户信息回调（当前版本暂无）
         **/
        fun getUserInfo(userInfo: ChatUserInfo)

        /**
         * description:获取用户离线消息
         **/
        fun getOfflineMessage(offlineMessages: ArrayList<Message>)

        /**
         * description:系统通知数据回调
         * 返回一组通知消息
         **/
        fun systemNoticeListCallBack(systemNotices: ArrayList<Message>)

        /**
         * description:系统通知数据回调
         * 返回一条通知消息
         **/
        fun systemNoticeCallBack(message: Message)

        /**
         * description:发送文本回调，返回Message并携带MessageId .
         * 假若获取到messageId==-1，说明message发送失败
         * @param message 返回的Message
         **/
        fun sendMessageCallBack(message: Message)

        /**
         * description:收到信息回调，信息对应三种类型：文本、连接、图片、视频
         **/
        fun replyMessageCallBack(message: Message)

        /**
         * description:读取Socket数据异常
         **/
        fun handleError(code: Int, msg: String?)

    }

    /**
     * 获取Socket是否已经AesKey验证成功
     * */
    fun isSocketAesKey(): Boolean {
        return mSocketClient.isSocketAesKey()
    }

    fun isSocketConnected(): Boolean {
        return mSocketClient.isSocketConnected()
    }


}