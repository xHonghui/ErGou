package com.laka.ergou.mvp.db

import android.content.ContentValues
import com.laka.ergou.mvp.chat.model.bean.Message


/**
 * @Author:summer
 * @Date:2019/2/19
 * @Description:数据库CRUD操作的顶级接口
 */
interface IBaseChatDao {
    fun initTable(userId: String): Boolean
    fun isExitTable(userId: String): Boolean
    fun add(userId: String, message: Message): Long
    fun addForBatch(userId: String, list: ArrayList<Message>): Long
    fun removeForRequestId(userId: String, requestId: String): Int
    fun updateForRequestId(userId: String, requestId: String, values: ContentValues): Int
    fun updateForMessageId(userId: String, msgId: String, values: ContentValues): Int
    fun getRowForPage(userId: String, offset: Int, pageSize: Int): ArrayList<Message>
    fun getRowAll(userId: String): ArrayList<Message>
    fun getRowByMsgId(userId: String,msgId:String):ArrayList<Message>
    fun hasMoreData(userId: String, offset: Int, pageSize: Int): Boolean
    fun createTableForName(userId: String)
    fun deleteAllMessageRecordForUser(userId: String)
    fun getLastData(userId: String): Message
    fun getUnreadMessage(userId: String): ArrayList<Message>
    fun markMessageAsRead(userId: String): Boolean
}