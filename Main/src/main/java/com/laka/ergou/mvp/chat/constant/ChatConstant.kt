package com.laka.ergou.mvp.chat.constant

import android.support.annotation.IntDef
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.BuildConfig

/**
 * @Author:Rayman
 * @Date:2019/2/13
 * @Description:购小二模块常量类
 */
object ChatConstant {

    const val CHAT_BASE_HOST: String = BuildConfig.ERGOU_BASE_HOST
    // 消息列表一页的条数
    const val MESSAGE_PAGE_SIZE: Int = 20
    // 消息发送的状态
    const val STATUS_WAITING_MESSAGE = 0x1000 // 等待发送
    const val STATUS_SENDING_MESSAGE = 0x1001 // 发送中
    const val STATUS_FAILED_MESSAGE = 0x1002  // 发送失败
    const val STATUS_SUCCESS_MESSAGE = 0x1003 // 发送成功

    @IntDef(STATUS_SENDING_MESSAGE,
            STATUS_FAILED_MESSAGE,
            STATUS_SUCCESS_MESSAGE)
    annotation class CHAT_MESSAGE_SEND_STATUS

    /**
     * chatPresenter 内部 Handler 发送的 message 类型定义
     * */
    const val MESSAGE_HANDLER_DISTRIBUTE_ROBOT = 1
    const val MESSAGE_HANDLER_SUCCESS_CALLBACK = 2
    const val MESSAGE_HANDLER_FAIL_CALLBACK = 3
    const val MESSAGE_HANDLER_HISTORY_MESSAGE = 4
    const val MESSAGE_HANDLER_SEND_MESSAGE = 5
    const val MESSAGE_HANDLER_RESEND_MESSAGE = 6
    const val MESSAGE_HANDLER_LOOP = 7
    const val MESSAGE_HANDLER_SOCKET_CONNECT = 8
    const val MESSAGE_HANDLER_SOCKET_DISCONNECT = 9
    const val MESSAGE_HANDLER_INTERNET_CONNECT = 10


    /**
     * description:聊天信息类型（针对数据）
     * 分别对应文本，图片，视频，链接，语音，系统通知
     **/
    const val CHAT_MSG_TYPE_TEXT = 0x10011
    const val CHAT_MSG_TYPE_IMAGE = 0x10022
    const val CHAT_MSG_TYPE_VIDEO = 0x10033
    const val CHAT_MSG_TYPE_MIXTURE = 0x10044
    const val CHAT_MSG_TYPE_AUDIO = 0x10055
    const val CHAT_MSG_TYPE_NOTICE = 0x10066

    /**
     * 聊天模块 eventBus 事件
     * */
    const val CHAT_LIST_UPDATE_EVENT: Int = 0x1001
    const val DELETE_MESSAGE_FOR_POSITION_EVENT: Int = 0x1002
    const val CREATE_TABLE_ERROR: Int = 0x1003 //建表失败
    const val UPDATE_TABLE_ERROR: Int = 0x1004 //更新表结构失败
    /**
     * 聊天记录富文本点击的 eventBus 事件
     * */
    const val LINK_TEXT_CLICK_EVENT = 0x1003
    const val NUMBER_TEXT_CLICK_EVENT = 0x1004


    @IntDef(CHAT_MSG_TYPE_TEXT,
            CHAT_MSG_TYPE_IMAGE,
            CHAT_MSG_TYPE_VIDEO,
            CHAT_MSG_TYPE_MIXTURE,
            CHAT_MSG_TYPE_AUDIO,
            CHAT_MSG_TYPE_NOTICE)
    annotation class CHAT_MESSAGE_TYPE

    /**
     * description:聊天用户身份
     **/
    const val CHAT_IDENTIFY_ROBOT = 0x101
    const val CHAT_IDENTIFY_USER = 0x102

    @IntDef(CHAT_IDENTIFY_ROBOT, CHAT_IDENTIFY_USER)
    annotation class CHAT_IDENTIFY

    /**
     * description:Activity跳转常量
     **/
    const val CHAT_CONTACT_ID = "CHAT_CONTACT_ID"
    const val CHAT_CONTENT = "CHAT_CONTENT"
    /**
     * 聊天列表item动画时长
     * */
    const val CHAT_LIST_ITEM_ANIM_DURATION: Long = 100
    @JvmField
    val DEFAULT_IMAGEVIEW_WIDTH: Int = ScreenUtils.dp2px(200f)
    @JvmField
    val DEFAULT_IMAGEVIEW_HEIGHT: Int = ScreenUtils.dp2px(200f)


}