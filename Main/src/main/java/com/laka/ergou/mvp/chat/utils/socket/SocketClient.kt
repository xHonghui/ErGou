package com.laka.ergou.mvp.chat.utils.socket

import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.ResourceUtils
import com.laka.ergou.BuildConfig
import com.laka.ergou.R
import com.laka.ergou.mvp.chat.constant.ChatApiConstant
import java.io.BufferedInputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.channels.NotYetConnectedException
import java.util.concurrent.PriorityBlockingQueue

/**
 * @Author:Rayman
 * @Date:2019/2/13
 * @Description:重新实现Socket连接 .
 * 添加回调，读写控制，Socket状态上传
 */
open class SocketClient : Runnable {

    /**
     * description:当前Socket连接状态
     **/
    enum class ConnectState {
        /**
         * 未连接
         */
        DISCONNECT,
        /**
         * 连接中
         */
        CONNECTING,
        /**
         * 已经连接了
         */
        CONNECTED,
        /**
         * AesKey认证成功
         * */
        AESKEY_AUTHENTICATION,
    }

    private var mSocket: Socket? = null
    private var mConnectState = ConnectState.DISCONNECT
    private var socketIpAddress = BuildConfig.ERGOU_SOCKET_HOST
    private var socketPort = BuildConfig.ERGOU_SOCKET_POST
    private var onWork = false
    private var mListener: SocketListener? = null

    // 发送的消息队列
    private var messageQueue = PriorityBlockingQueue<ByteBuffer>()

    // input数据流
    private var mBufInputStream: BufferedInputStream? = null

    // Socket写入线程
    private var mWriteThread: WriteThread? = null

    /**/
    fun isSocketAesKey(): Boolean {
        return mConnectState == ConnectState.AESKEY_AUTHENTICATION
    }

    /*是否链接*/
    fun isSocketConnected(): Boolean {
        return if (mConnectState == ConnectState.AESKEY_AUTHENTICATION
                || mConnectState == ConnectState.CONNECTED
                || mConnectState == ConnectState.CONNECTING) {
            true
        } else if (mConnectState == ConnectState.DISCONNECT) {
            false
        } else {
            false
        }
    }

    /*设置Socket的链接状态*/
    fun setConnected(state: ConnectState) {
        mConnectState = state
    }

    override fun run() {
        while (onWork) {
            when (mConnectState) {
                ConnectState.AESKEY_AUTHENTICATION,
                ConnectState.CONNECTED -> {
                    LogUtils.info("issue-------------：Socket线程（读取数据）")
                    readSocket()
                }
                ConnectState.DISCONNECT -> {
                    LogUtils.info("issue-------------：Socket线程（发起重连）")
                    connectSocket()
                }
                else -> {

                }
            }
        }
    }

    /**
     * description:连接Socket
     **/
    private fun connectSocket() {
        // 假若当前连接状态为connecting，直接return不处理，防止多次SocketClose和创建引起的内存问题。
        if (mConnectState == ConnectState.CONNECTING) {
            return
        }

        try {
            mSocket = Socket()
            mSocket?.let {
                LogUtils.info("开始socket连接")
                it.soTimeout = ChatApiConstant.READ_TIMEOUT
                it.setSoLinger(true, 1)
                it.connect(InetSocketAddress(socketIpAddress, socketPort), ChatApiConstant.CONNECT_TIMEOUT)
            }
            mConnectState = ConnectState.CONNECTING

            // More info:
            // http://groups.google.com/group/android-developers/browse_thread/thread/45a8b53e9bf60d82
            // http://stackoverflow.com/questions/2879455/android-2-2-and-bad-address-family-on-socket-connect
            System.setProperty("java.net.preferIPv4Stack", "true")
            System.setProperty("java.net.preferIPv6Addresses", "false")

            mSocket?.let {
                if (it.isConnected) {
                    mConnectState = ConnectState.CONNECTED
                    mBufInputStream = BufferedInputStream(it.getInputStream())

                    // 创建Socket写入数据线程，开始轮询向Socket写入数据
                    if (mWriteThread == null || mWriteThread?.isInterrupted!!) {
                        mWriteThread = WriteThread()
                        mWriteThread?.start()
                        LogUtils.error("issue-----------：ChatModule---Socket连接成功---开启写入线程")

                        //读取编程和写入线程必须同时开
                        // Socket连接之后回调，发送AesKey验证包到服务器
                        mListener?.didConnect()
                    }
                    LogUtils.error("issue-----------：ChatModule---Socket连接成功---")
                } else {
                    // 重连Socket
                    LogUtils.error("issue-----------：ChatModule---Socket连接失败---")
                    reconnectSocket("服务器连接失败")
                }
            }
        } catch (e: Exception) {
            LogUtils.info("issue------------：Socket连接出现异常：${e.message}")
            LogUtils.error("ChatModule---Socket连接出错,3秒后重试\n${e.message}")
            reconnectSocket("连接失败")
        } finally {
            // 上传Socket连接状态报告
            uploadSocketConnection(10)
        }
    }

    /**
     * description:关闭Socket连接
     **/
    @Synchronized
    private fun closeSocket() {
        LogUtils.info("issue--------------：关闭Socket")
        mSocket?.let {
            try {
                if (it.isConnected) {
                    // 关闭流
                    it.shutdownOutput()
                    it.shutdownInput()
                    // 关闭缓存流
                    mBufInputStream?.close()
                    mBufInputStream = null

                    // 关闭Socket
                    it.close()
                    mSocket = null
                    LogUtils.error("ChatModule---Socket断开连接")
                }
            } catch (e: Exception) {
                LogUtils.error("ChatModule---Socket断链失败\n${e.message}")
            } finally {
                mConnectState = ConnectState.DISCONNECT
                mSocket = null
            }
        }
    }

    /**
     * description:Socket重连，不包括断开的情况
     **/
    private fun reconnectSocket(errorMsg: String) {
        LogUtils.info("issue--------------：reconnectSocket----mConnectState==$mConnectState")
        if (mConnectState == ConnectState.DISCONNECT) {
            return
        }

        var errorHint = StringBuffer(errorMsg)
        errorHint.append(ResourceUtils.getStringWithArgs(R.string.chat_retry_hint,
                ChatApiConstant.RECONNECT_TIME / 1000))

        closeSocketWriteChannel()
        mListener?.didErrorOccur(ChatApiConstant.ERROR_RECONNECT, errorHint.toString())
        mConnectState = ConnectState.DISCONNECT
    }

    /**
     * description:关闭SocketInput流与断开Socket连接
     **/
    private fun closeSocketWriteChannel() {
        mBufInputStream?.let {
            try {
                it.close()
                mBufInputStream = null
                LogUtils.error("ChatModule---关闭InputStream")
            } catch (e: IOException) {
                LogUtils.error("ChatModule---关闭InputStream失败：${e.message}")
            }
        }
        closeSocket()
    }

    /**
     * description:读取Socket数据
     **/
    private fun readSocket() {
        mSocket?.let {
            mBufInputStream?.let {
                mListener?.didReadData(it)
            }
        }
    }

    /**
     * description:向socket写入数据，使用Synchronized防止写入资源冲突
     **/
    @Synchronized
    private fun writeSocket(buffer: ByteBuffer) {
        // 从buffer的0开始
        buffer.flip()
        mSocket?.let {
            LogUtils.info("issue-----------------：写入Socket数据：$buffer")
            it.getOutputStream().write(buffer.array())
            LogUtils.info("issue-----------------：Socket 写线程写入数据")
        }
    }

    /**
     * description:开启Socket业务工作
     **/
    fun startWork() {
        onWork = true

        // 假若Socket被断开的情况下，Socket部分成员变量会置为null。
        // 所以再次启动的时候，需要判断是否重新创建Socket的对象
        if (mSocket == null || mSocket?.isClosed!!) {
            connectSocket()
        }
    }

    /**
     * description:暂停socket，停止写入数据
     **/
    fun stopWork() {
        onWork = false
    }

    /**
     * description:完全断开Socket连接。
     * 原本断开的操作是统一通过onWork去控制的，但是发现
     **/
    fun disConnectSocket() {
        onWork = false
        mConnectState = ConnectState.DISCONNECT
        mWriteThread?.let {
            mWriteThread = null
        }
        // 停止运行，断开socket连接并回调
        closeSocket()
        mListener?.didDisconnect()
        LogUtils.info("issue------------：正常关闭socket")
    }

    fun disConnectSocketWithoutCallback() {
        onWork = false
        mConnectState = ConnectState.DISCONNECT
        mWriteThread?.let {
            mWriteThread = null
        }
        //关闭Socket
        closeSocket()
    }

    /**
     * description:添加数据到消息队列
     **/
    fun write(buffer: ByteBuffer) {
        messageQueue.add(buffer)
        //LogUtils.error("输出消息队列：${messageQueue.size}")
    }

    fun setConnectListener(listener: SocketListener) {
        this.mListener = listener
    }

    /**
     * description:上传Socket连接状态到友盟
     **/
    private fun uploadSocketConnection(usedTime: Long) {
        if (BuildConfig.DEBUG) {
            return
        }
        LogUtils.error("ChatModule---reportSocketConnect usedTime : $usedTime")
        if (usedTime <= 500) {
            return
        }
//        val eventId = StringBuilder(AnalyticsReport.DEFAULT_VIEW_ID)
//                .append(AnalyticsReport.SEPARATOR)
//                .append("15451")
//                .append(AnalyticsReport.SEPARATOR)
//                .append(AnalyticsReport.NETWORK_ENVIRONMENT_MONITORING).toString()
//        AnalyticsReport.onEventValue(LiveApplication.getInstance(), eventId, null, usedTime.toInt())
    }

    /**
     * description:Socket写入数据线程
     **/
    inner class WriteThread : Thread() {
        override fun run() {
            super.run()
            while (onWork) {
                try {
                    LogUtils.info("issue-------------：循环写入消息 ${messageQueue.size}")
                    var buffer = messageQueue.take()
                    writeSocket(buffer)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    LogUtils.error("ChatModule----WriteError---${e.message}")
                } catch (e: IOException) {
                    e.printStackTrace()
                    // 假若出现Broken pipe通道错误，就意味着Socket断开了。
                    LogUtils.error("ChatModule----WriteError---${e.message}")
                    LogUtils.info("issue------------：写数据失败，关闭socket")
                    //mListener?.didDisconnect()
                    disConnectSocket() //断开连接
                } catch (e: NotYetConnectedException) {
                    e.printStackTrace()
                    LogUtils.error("ChatModule----WriteError---${e.message}")
                }
            }

            // 停止工作的时候，清除消息队列
            LogUtils.info("issue-------------：清空消息队列")
            clearQueueMessage()
        }
    }

    /**
     * 清空消息
     * */
    fun clearQueueMessage() {
        messageQueue.clear()
    }

    /**
     * description:Socket回调
     **/
    interface SocketListener {

        /**
         * Socket连接成功
         */
        fun didConnect()

        /**
         * 断开连接
         */
        fun didDisconnect()

        /**
         * 读取到数据,数据需要拷贝一份再做处理
         *
         * @param inputStream
         */
        @Throws(IOException::class)
        fun didReadData(inputStream: BufferedInputStream)

        /**
         * 发生错误
         *
         * @param code
         * @param message
         */
        fun didErrorOccur(code: Int, message: String)
    }
}