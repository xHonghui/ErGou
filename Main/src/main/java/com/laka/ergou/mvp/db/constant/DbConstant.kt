package com.laka.ergou.mvp.db.constant

/**
 * @Author:summer
 * @Date:2019/2/20
 * @Description:
 */
object DbConstant {

    // 聊天记录表结构的key
    const val CHAT_ID: String = "chat_id"
    const val REQUEST_ID: String = "request_id"
    const val CREATE_TIME: String = "create_time"
    const val FROM_ID: String = "from_id"
    const val TO_ID: String = "to_id"
    const val MSG_ID: String = "msg_id"
    const val MSG_TYPE: String = "msg_type"
    const val MSG_CONTENT: String = "content"
    const val FOLLOW: String = "follow"
    const val GANDER: String = "gander"
    const val AVATAR: String = "avatar"
    const val NICKNAME: String = "nickname"
    const val LEVEL: String = "level"
    const val IS_SEND: String = "is_send"
    const val SEND_STATUS: String = "send_status"
    const val READ_STATUS: String = "read_status"
    //用来标记服务端发来的消息id，作为服务器去重判断依据，服务器发来消息或者读取离线消息时，先判断最近数据库里有没有相同的serverMsgId 的消息
    //有的话就不要添加到表里，有时候链接断开，服务器并不知道我们客户端有没有收到相应的消息，就会重发，客户端就需要判断去重
    const val SERVER_MSG_ID:String = "server_msg_id"
    //表名
    const val CHAT_TABLE_NAME: String = "chat_table"
    //页大小
    const val PAGE_SIZE: Int = 20

    //通过sql语句执行数据库操作的类型
    const val CREATE_TABLE = 0x101
    const val UPDATE_TABLE = 0x102


}