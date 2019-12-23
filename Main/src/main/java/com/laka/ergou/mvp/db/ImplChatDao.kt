package com.laka.ergou.mvp.db

import android.content.ContentValues
import android.text.TextUtils
import com.laka.ergou.mvp.chat.model.bean.Message


/**
 * @Author:summer
 * @Date:2019/2/19
 * @Description:dao层，负责组装数据，然后通过 ChatDbEngine 进行数据库操作，P层与之交互，使用的都是userId，该层再按照一定的转换规则转换为相应的表名再进行数据库操作
 * 注意，不要进行多次userId->表名 转换，否则会出现表名不对应的bug
 */
class ImplChatDao : IBaseChatDao {

    /**
     * 初始化表结构，不同的会话对应不同的表，所以初始化会话时，也要初始化表
     * */
    override fun initTable(userId: String): Boolean {
        if (TextUtils.isEmpty(userId)) return false
        createTableForName(userId)
        return true
    }

    //增
    override fun add(userId: String, message: Message): Long {
        if (!initTable(userId)) return -1
        return ChatDbEngine.add(ChatDbEngine.getTableName(userId), message)
    }

    override fun addForBatch(userId: String, list: ArrayList<Message>): Long {
        if (!initTable(userId)) return -1
        return ChatDbEngine.addForBatch(ChatDbEngine.getTableName(userId), list)
    }

    //删
    override fun removeForRequestId(userId: String, requestId: String): Int {
        if (!initTable(userId)) return -1
        return ChatDbEngine.removeForRequestId(ChatDbEngine.getTableName(userId), requestId)
    }

    //改
    override fun updateForRequestId(userId: String, requestId: String, values: ContentValues): Int {
        if (!initTable(userId)) return -1
        //如果插入失败，则循环插入三次，三次都失败，则认为操作失败
        for (i in 0 until 3) {
            val line = ChatDbEngine.updateForRequestId(ChatDbEngine.getTableName(userId), requestId, values)
            if (line > 0) {
                return line
            }
        }
        return -1
    }

    override fun updateForMessageId(userId: String, msgId: String, values: ContentValues): Int {
        if (!initTable(userId)) return -1
        return ChatDbEngine.updateForMsgId(ChatDbEngine.getTableName(userId), msgId, values)
    }

    //查
    override fun getRowForPage(userId: String, offset: Int, pageSize: Int): ArrayList<Message> {
        if (!initTable(userId)) return ArrayList()
        return ChatDbEngine.getRowForPage(ChatDbEngine.getTableName(userId), offset, pageSize)
    }

    override fun getRowAll(userId: String): ArrayList<Message> {
        if (!initTable(userId)) return ArrayList()
        return ChatDbEngine.getRowAll(ChatDbEngine.getTableName(userId))
    }

    override fun getRowByMsgId(userId: String, msgId: String): ArrayList<Message> {
        if (!initTable(userId)) return ArrayList()
        return ChatDbEngine.getRowByMsgId(ChatDbEngine.getTableName(userId), msgId)
    }

    override fun hasMoreData(userId: String, offset: Int, pageSize: Int): Boolean {
        if (!initTable(userId)) return false
        return ChatDbEngine.isHasMoreData(ChatDbEngine.getTableName(userId), offset, pageSize)
    }

    //其他操作
    override fun createTableForName(userId: String) {
        ChatDbEngine.createTableForName(ChatDbEngine.getTableName(userId))
    }

    override fun isExitTable(userId: String): Boolean {
        return ChatDbEngine.isExitTableForName(ChatDbEngine.getTableName(userId))
    }

    //删除聊天记录
    override fun deleteAllMessageRecordForUser(userId: String) {
        if (!initTable(userId)) return
        ChatDbEngine.removeAllMessageRecordForTable(ChatDbEngine.getTableName(userId))
    }

    override fun getLastData(userId: String): Message {
        return if (initTable(userId)) {
            ChatDbEngine.getLastData(ChatDbEngine.getTableName(userId))
        } else {
            Message()
        }
    }

    override fun getUnreadMessage(userId: String): ArrayList<Message> {
        return if (initTable(userId)) {
            ChatDbEngine.getUnreadMessage(ChatDbEngine.getTableName(userId))
        } else {
            ArrayList()
        }
    }

    override fun markMessageAsRead(userId: String): Boolean {
        return if (initTable(userId)) {
            ChatDbEngine.markMessageAsRead(ChatDbEngine.getTableName(userId))
        } else {
            false
        }
    }
}