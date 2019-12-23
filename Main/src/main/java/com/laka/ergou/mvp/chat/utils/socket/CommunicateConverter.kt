package com.laka.ergou.mvp.chat.utils.socket

import android.text.TextUtils
import android.util.Xml
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.mvp.chat.constant.ChatConstant
import com.laka.ergou.mvp.chat.model.bean.*
import org.xmlpull.v1.XmlPullParser

/**
 * @Author:Rayman
 * @Date:2019/2/19
 * @Description:Socket通信消息转换器 .
 * 因为有的数据类型是本地使用的，为了和服务器端解耦，通过当前的类，建立本地数据与服务器端数据映射关系
 */
object CommunicateConverter {

    /**
     * description:本地、服务器端消息类型映射表
     **/
    var msgTypeReflectMap = HashMap<Int, ErgouIm.MessageType>()

    init {
        msgTypeReflectMap[ChatConstant.CHAT_MSG_TYPE_TEXT] = ErgouIm.MessageType.TEXT
        msgTypeReflectMap[ChatConstant.CHAT_MSG_TYPE_IMAGE] = ErgouIm.MessageType.IMAGE
        msgTypeReflectMap[ChatConstant.CHAT_MSG_TYPE_VIDEO] = ErgouIm.MessageType.VIDEO
        msgTypeReflectMap[ChatConstant.CHAT_MSG_TYPE_MIXTURE] = ErgouIm.MessageType.RICH
        msgTypeReflectMap[ChatConstant.CHAT_MSG_TYPE_AUDIO] = ErgouIm.MessageType.VOICE
    }

    /**
     * description:将本地消息数据类型转换成ProtoBuffer消息数据类型
     **/
    fun covertMessageType(@ChatConstant.CHAT_MESSAGE_TYPE messageType: Int): ErgouIm.MessageType {
        return msgTypeReflectMap[messageType]!!
    }

    /**
     * description:将ProtoBuffer消息数据类型转换成本地消息数据类型
     **/
    private fun covertMessageType(messageType: ErgouIm.MessageType): Int {
        //LogUtils.info("输出MessageType：${messageType.number}")
        msgTypeReflectMap.mapValues {
            if (it.value.number == messageType.number) {
                return it.key
            }
        }
        return ChatConstant.CHAT_MSG_TYPE_TEXT
    }

    /**
     * description:转换服务器数据到本地Message
     **/
    fun convertMessage(remoteMessage: ErgouIm.IM_Message, contactInfo: Any?, reqId: String = ""): Message {
        LogUtils.info("socket---------remoteMessage.content=${remoteMessage.content}")
        var messageType = ChatConstant.CHAT_MSG_TYPE_TEXT
        remoteMessage.msgType.let {
            messageType = covertMessageType(ErgouIm.MessageType.forNumber(remoteMessage.msgType))
        }
        var message = Message(
                createTime = remoteMessage.createTime.toLong(),
                fromId = remoteMessage.fromId,
                toId = remoteMessage.toId,
                msgId = remoteMessage.msgId.toInt(),
                msgType = messageType,
                content = remoteMessage.content
        )
        if (!TextUtils.isEmpty(reqId)) {
            message.requestId = reqId
        }
        contactInfo?.let {
            if (contactInfo is ChatRobotInfo) {
                message.messageCreatorType = ChatConstant.CHAT_IDENTIFY_ROBOT
                message.messageCreator = contactInfo
            } else if (contactInfo is ChatUserInfo) {
                message.messageCreatorType = ChatConstant.CHAT_IDENTIFY_USER
                message.messageCreator = contactInfo
            }
        }
        message.msgContent = convertMessageContentToBean(messageType, message.content)
        //LogUtils.info("输出后端Message：$remoteMessage,\n输出转换后的Message：$message")
        return message
    }

    /**
     * description:转换服务器多条信息到本地Message
     **/
    fun convertMessages(remoteMessages: List<ErgouIm.IM_Message>, contactInfo: Any?): ArrayList<Message> {
        var messageList = ArrayList<Message>()
        for (remoteMessage in remoteMessages) {
            messageList.add(convertMessage(remoteMessage, contactInfo))
        }
        return messageList
    }

    /**
     * description:根据消息的类型和内容，转换成对应的msgContent对象
     **/
    fun convertMessageContentToBean(messageType: Int, content: String): Any {
        LogUtils.info("messageContent---------$content--------messageType=$messageType")
        when (messageType) {
            ChatConstant.CHAT_MSG_TYPE_TEXT,
            ChatConstant.CHAT_MSG_TYPE_NOTICE -> {
                // 假若是Text、系统通知类型的话，直接传content就ok了。
                return content
            }
            ChatConstant.CHAT_MSG_TYPE_IMAGE -> { //图片
                return convertContentToImageMessage(content)
            }
            ChatConstant.CHAT_MSG_TYPE_VIDEO -> {

            }
            ChatConstant.CHAT_MSG_TYPE_MIXTURE -> {  //链接
                return convertContentToLinkMessage(content)
            }
            ChatConstant.CHAT_MSG_TYPE_AUDIO -> {

            }
        }
        return ""
    }

    private fun convertContentToImageMessage(content: String): ImageMessage {
        var imageMessage = ImageMessage()
        try {
            var xmlParser = Xml.newPullParser()
            xmlParser.setInput(content.byteInputStream(Charsets.UTF_8), "utf-8")
            var type = xmlParser.eventType
            while (type != XmlPullParser.END_DOCUMENT) {
                when (type) {
                    XmlPullParser.START_TAG -> {
                        when {
                            "img" == xmlParser.name -> {
                                imageMessage.thumbUrl = xmlParser.getAttributeValue("", "thumburl")
                                imageMessage.thumbLength = xmlParser.getAttributeValue("", "thumblength").toLong()
                                imageMessage.thumbHeight = xmlParser.getAttributeValue("", "thumbheight").toInt()
                                imageMessage.thumbWidth = xmlParser.getAttributeValue("", "thumbwidth").toInt()
                                imageMessage.bigUrl = xmlParser.getAttributeValue("", "bigurl")
                                imageMessage.length = xmlParser.getAttributeValue("", "length").toLong()
                                imageMessage.md5 = xmlParser.getAttributeValue("", "md5")
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {

                    }
                }
                type = xmlParser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        LogUtils.info("socket---------message=$imageMessage")
        return imageMessage
    }

    /**
     * description:将后端返回的Content转换成LinkMessage
     **/
    private fun convertContentToLinkMessage(content: String): LinkMessage {
        LogUtils.info("linkContent---------$content")
        var linkMessage = LinkMessage()
        try {
            // Link模式下的数据是用XML格式包裹的，拆分XML。使用pull解析
            var xmlParser = Xml.newPullParser()
            xmlParser.setInput(content.byteInputStream(Charsets.UTF_8), "utf-8")
            var type = xmlParser.eventType
            while (type != XmlPullParser.END_DOCUMENT) {
                when (type) {
                    XmlPullParser.START_TAG -> {
                        when {
                            "title" == xmlParser.name -> {
                                linkMessage.title = "${xmlParser.nextText()}"
                            }
                            "des" == xmlParser.name -> {
                                linkMessage.description = "${xmlParser.nextText()}"
                            }
                            "url" == xmlParser.name -> {
                                linkMessage.linkUrl = "${xmlParser.nextText()}"
                            }
                            "thumburl" == xmlParser.name -> {
                                linkMessage.thumbUrl = "${xmlParser.nextText()}"
                            }
                            "type" == xmlParser.name -> {
                                val value = xmlParser.nextText()
                                if (!TextUtils.isEmpty(value)) {
                                    linkMessage.type = value.toInt()
                                }
                            }
                            "id" == xmlParser.name -> {
                                linkMessage.id = "${xmlParser.nextText()}"
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {

                    }
                }
                type = xmlParser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return linkMessage
    }
}