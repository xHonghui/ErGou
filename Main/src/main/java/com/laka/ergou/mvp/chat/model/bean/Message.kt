package com.laka.ergou.mvp.chat.model.bean

import com.laka.ergou.mvp.chat.constant.ChatConstant

/**
 * @Author:summer
 * @Date:2019/2/14
 * @Description:消息数据类，每条消息的数据结构都是这样，区别在于不同的消息类型，content 内容不同，
 * 并且我们拿到这个 content 数据后，解析的方式也不同。
 */
data class Message(var chatId: Int = -1,                //消息id（数据库中主键）
                   var requestId: String = "",          //消息请求ID，每条消息都会随机生成一个
                   var createTime: Long = 0,            //消息创建时间
                   var fromId: String = "",             //消息来源者Id
                   var toId: String = "",               //消息接收者id
                   var msgId: Int = 0,                  //消息id
                   @ChatConstant.CHAT_MESSAGE_TYPE
                   var msgType: Int = ChatConstant.CHAT_MSG_TYPE_TEXT,          //消息类型
                   @ChatConstant.CHAT_MESSAGE_SEND_STATUS
                   var sendStatus: Int = ChatConstant.STATUS_SUCCESS_MESSAGE,   //消息发送状态
                   var content: String = "",            //消息内容, 不同消息类型，消息内容格式不同，内含消息类型。假若是其他类型，可能需要转换
                   var msgContent: Any? = null,         //具体消息内容：假若是Text类型，那么这里就是一个String，否则就不同Type解析Content的语句，然后再设置
                   var messageRead: Boolean = false     //消息阅读状态，是否已读
) {
    @ChatConstant.CHAT_IDENTIFY
    var messageCreatorType: Int = 0         //消息创建者类型
    var messageCreator: Any? = null         //消息创建者（暂时有Robot和User，根据上面的CreatorType判断强转）
    var marginBottom: Int = 0               //设置item的外边距
}
