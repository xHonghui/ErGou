package com.laka.ergou.mvp.db

import android.content.ContentValues
import android.content.Context
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.ListUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.mvp.chat.constant.ChatConstant
import com.laka.ergou.mvp.chat.model.bean.ChatRobotInfo
import com.laka.ergou.mvp.chat.model.bean.ChatUserInfo
import com.laka.ergou.mvp.chat.model.bean.Message
import com.laka.ergou.mvp.chat.model.bean.event.ChatEvent
import com.laka.ergou.mvp.chat.utils.socket.CommunicateConverter
import com.laka.ergou.mvp.db.constant.DbConstant
import com.tencent.wcdb.Cursor
import com.tencent.wcdb.SQLException
import com.tencent.wcdb.database.SQLiteDatabase

/**
 * @Author:summer
 * @Date:2019/2/19
 * @Description:数据库引擎，负责数据库原始操作
 */
object ChatDbEngine {

    private lateinit var mDataBaseHelper: CustomDatabaseOpenHelper
    private lateinit var mDataBase: SQLiteDatabase

    //表名规则
    fun getTableName(userId: String): String {
        return "_${userId}_table"
    }

    var mTimes = 0

    /**
     * 使用前需要先初始化
     * */
    @JvmStatic
    fun init(context: Context) {
        try {
            mDataBaseHelper = CustomDatabaseOpenHelper(context, CustomDatabaseOpenHelper.DATABASE_NAME, null, CustomDatabaseOpenHelper.DATABASE_VERSION)
            mDataBase = mDataBaseHelper.readableDatabase
            mTimes = 0
        } catch (e: Exception) {
            e.printStackTrace()
            if (++mTimes > 3) {
                init(context)
            }
        }
    }

    fun createTable(sql: String) {
        exceSql(sql, DbConstant.CREATE_TABLE)
    }

    fun createTableForName(tableName: String) {
        if (!ChatDbEngine.isExitTableForName(tableName)) {
            exceSql(getCreateTableSql(tableName), DbConstant.CREATE_TABLE)
        } else if (CustomDatabaseOpenHelper.DATABASE_VERSION > CustomDatabaseOpenHelper.FIRST_DATABASE_VERSION
                && ChatDbEngine.isExitTableForName(tableName)) { //更新数据表
            for (i in CustomDatabaseOpenHelper.FIRST_DATABASE_VERSION until CustomDatabaseOpenHelper.DATABASE_VERSION) {
                when (i) {
                    CustomDatabaseOpenHelper.FIRST_DATABASE_VERSION -> { // 数据库 1版本，最新版本为2，增加了 server_msg_id 字段
                        updateToVersion2(tableName)
                    }
                }
            }
        }
    }

    /**数据版本从1 更新到2*/
    private fun updateToVersion2(tableName: String) {
        val updateSql = "alter table $tableName add column ${DbConstant.SERVER_MSG_ID} varchar(20)"
        exceSql(updateSql, DbConstant.UPDATE_TABLE)
    }

    private fun exceSql(sql: String, type: Int) {
        try {
            mDataBase.execSQL(sql)
            LogUtils.info("创建或者更新表成功！$sql")
        } catch (e: SQLException) {
            when (type) {
                DbConstant.UPDATE_TABLE -> {
                    EventBusManager.postEvent(ChatEvent(type = ChatConstant.UPDATE_TABLE_ERROR))
                }
                DbConstant.CREATE_TABLE -> {
                    EventBusManager.postEvent(ChatEvent(type = ChatConstant.CREATE_TABLE_ERROR))
                }
                else -> {

                }
            }
            e.printStackTrace()
        }
    }


    //判断指定表是否存在
    fun isExitTableForName(tableName: String): Boolean {
        var cursor: Cursor? = null
        var isExit = false
        cursor = mDataBase.rawQuery(getIsExitTableSql(), null)
        while (cursor.moveToNext()) {
            val name = cursor.getString(0)//遍历出表名
            if (name == tableName) {
                isExit = true
                break
            }
        }
        return isExit
    }

    fun isHasMoreData(tableName: String, offset: Int, pageSize: Int): Boolean {
        val cursor = mDataBase.rawQuery(getSelectSql(tableName, offset, pageSize), null)
        if (cursor.count > 0) {
            return true
        }
        return false
    }


    //====================================== 增删改查操作 ==========================================
    //增
    fun add(tableName: String, message: Message): Long {
        val values = beanToContentValues(message)
        return mDataBase.insert(tableName, null, values)
    }

    //批量插入
    fun addForBatch(tableName: String, list: ArrayList<Message>): Long {
        mDataBase.beginTransaction()
        val isSuccess = !list
                .map { add(tableName, it) }
                .contains(-1L)
        return if (isSuccess) {
            mDataBase.setTransactionSuccessful()
            mDataBase.endTransaction()
            1
        } else {
            mDataBase.endTransaction()
            -1
        }
    }

    //删
    fun removeForMessageId(tableName: String, msgId: String): Int {
        return mDataBase.delete(tableName, "${DbConstant.MSG_ID}=?", arrayOf(msgId))
    }

    fun removeForRequestId(tableName: String, requestId: String): Int {
        return mDataBase.delete(tableName, "${DbConstant.REQUEST_ID}=?", arrayOf(requestId))
    }

    fun removeAllMessageRecordForTable(tableName: String) {
        mDataBase.delete(tableName, null, null)
    }

    //改
    fun updateForMsgId(tableName: String, msgId: String, values: ContentValues): Int {
        return mDataBase.update(tableName, values, "${DbConstant.MSG_ID}=?", arrayOf(msgId))
    }

    fun updateForRequestId(tableName: String, requestId: String, values: ContentValues): Int {
        return mDataBase.update(tableName, values, "${DbConstant.REQUEST_ID}=?", arrayOf(requestId))
    }

    //分页查询
    fun getRowForPage(tableName: String, offset: Int, pageSize: Int): ArrayList<Message> {
        val cursor = mDataBase.rawQuery(getSelectSql(tableName, offset, pageSize), null)
        return cursorToList(cursor)
    }

    //查询所有
    fun getRowAll(tableName: String): ArrayList<Message> {
        val cursor = mDataBase.query(tableName, null, null, null, null, null, null)
        return cursorToList(cursor)
    }

    // 返回最后一条数据
    fun getLastData(tableName: String): Message {
        val cursor = mDataBase.rawQuery(querySingleDataSql(tableName, true), null)
        val resultList = cursorToList(cursor)
        return if (ListUtils.isNotEmpty(resultList)) {
            resultList[0]
        } else {
            Message()
        }
    }

    /**更具msgId查询数据*/
    fun getRowByMsgId(tableName: String, msgId: String): ArrayList<Message> {
        val sql = "select * from $tableName where ${DbConstant.MSG_ID}=$msgId"
        val cursor = mDataBase.rawQuery(sql, null)
        return cursorToList(cursor)
    }

    fun getUnreadMessage(tableName: String): ArrayList<Message> {
        val cursor = mDataBase.rawQuery(queryUnreadDataSql(tableName), null)
        return cursorToList(cursor)
    }

    fun markMessageAsRead(tableName: String): Boolean {
        val cursor = mDataBase.query(tableName, null, null, null, null, null, null)
        val messageList = cursorToList(cursor)
        messageList.forEach {
            val content = ContentValues()
            content.put(DbConstant.READ_STATUS, true)
            updateForMsgId(tableName, it.msgId.toString(), content)
        }
        return false
    }


    private fun cursorToList(cursor: Cursor): ArrayList<Message> {
        val dataList = ArrayList<Message>()
        while (cursor.moveToNext()) {
            val chatId = cursor.getInt(cursor.getColumnIndex(DbConstant.CHAT_ID))
            val requestId = cursor.getString(cursor.getColumnIndex(DbConstant.REQUEST_ID))
            val createTime = cursor.getLong(cursor.getColumnIndex(DbConstant.CREATE_TIME))
            val fromId = cursor.getString(cursor.getColumnIndex(DbConstant.FROM_ID))
            val toId = cursor.getString(cursor.getColumnIndex(DbConstant.TO_ID))
            val messageId = cursor.getString(cursor.getColumnIndex(DbConstant.MSG_ID))
            val messageType = cursor.getInt(cursor.getColumnIndex(DbConstant.MSG_TYPE))
            val messageContent = cursor.getString(cursor.getColumnIndex(DbConstant.MSG_CONTENT))

            val follow = cursor.getInt(cursor.getColumnIndex(DbConstant.FOLLOW))
            val gendar = cursor.getString(cursor.getColumnIndex(DbConstant.GANDER))
            val avatar = cursor.getString(cursor.getColumnIndex(DbConstant.AVATAR))
            val nickname = cursor.getString(cursor.getColumnIndex(DbConstant.NICKNAME))
            val level = cursor.getInt(cursor.getColumnIndex(DbConstant.LEVEL))
            val isSend = cursor.getInt(cursor.getColumnIndex(DbConstant.IS_SEND))
            val sendStatus = cursor.getInt(cursor.getColumnIndex(DbConstant.SEND_STATUS))
            val readStatus = cursor.getInt(cursor.getColumnIndex(DbConstant.READ_STATUS))

            // 部分数据获取可能为null，添加判断。转为空字符串或者0


            // 获取数据库数据，根据用户状态判断数据类型
            val mMessage = Message(chatId, requestId, createTime, fromId, toId,
                    messageId.toInt(), messageType)
            mMessage.content = messageContent
            mMessage.sendStatus = sendStatus
            mMessage.messageRead = readStatus == 1

            // MsgContent需要转换成对应的Bean对象
            mMessage.msgContent =
                    CommunicateConverter.convertMessageContentToBean(messageType, messageContent)

            // 根据用户类型，设置Message的Creator
            if (isSend == 1) {
                mMessage.messageCreatorType = ChatConstant.CHAT_IDENTIFY_USER
                val chatUserInfo = ChatUserInfo(fromId, nickname, avatar, gendar, level, follow)
                mMessage.messageCreator = chatUserInfo
            } else {
                mMessage.messageCreatorType = ChatConstant.CHAT_IDENTIFY_ROBOT
                val chatRobotInfo = ChatRobotInfo(fromId, nickname, avatar, gendar)
                mMessage.messageCreator = chatRobotInfo
            }

            dataList.add(mMessage)

            //LogUtils.error("输出历史消息记录：$mMessage")
        }
        return dataList
    }

    private fun beanToContentValues(message: Message): ContentValues {
        val values = ContentValues()
        values.put(DbConstant.REQUEST_ID, message.requestId)
        values.put(DbConstant.CREATE_TIME, message.createTime)
        values.put(DbConstant.FROM_ID, message.fromId)
        values.put(DbConstant.TO_ID, message.toId)
        values.put(DbConstant.MSG_ID, message.msgId)
        values.put(DbConstant.MSG_TYPE, message.msgType)
        values.put(DbConstant.MSG_CONTENT, message.content)

        when (message.messageCreatorType) {
            ChatConstant.CHAT_IDENTIFY_ROBOT -> {
                val robotInfo = message.messageCreator
                if (robotInfo is ChatRobotInfo) {
                    values.put(DbConstant.FOLLOW, -1)
                    values.put(DbConstant.GANDER, robotInfo.robotGender)
                    values.put(DbConstant.AVATAR, robotInfo.robotAvatar)
                    values.put(DbConstant.NICKNAME, robotInfo.robotName)
                    values.put(DbConstant.LEVEL, -1)
                }
            }
            ChatConstant.CHAT_IDENTIFY_USER -> {
                val userInfo = message.messageCreator
                if (userInfo is ChatUserInfo) {
                    values.put(DbConstant.FOLLOW, userInfo.isFollow)
                    values.put(DbConstant.GANDER, userInfo.userGender)
                    values.put(DbConstant.AVATAR, userInfo.userAvatar)
                    values.put(DbConstant.NICKNAME, userInfo.userName)
                    values.put(DbConstant.LEVEL, userInfo.userLevel)
                }
            }
        }

        values.put(DbConstant.IS_SEND, message.messageCreatorType == ChatConstant.CHAT_IDENTIFY_USER)
        values.put(DbConstant.SEND_STATUS, message.sendStatus)
        values.put(DbConstant.READ_STATUS, message.messageRead)
        return values
    }

    //================================ 拼接SQL 语句 ============================================
    //建表
    private fun getCreateTableSql(tableName: String): String {
        var sqlStr = "create table $tableName(${DbConstant.CHAT_ID} integer primary key autoincrement," +
                "${DbConstant.REQUEST_ID} varchar(32)," +
                "${DbConstant.CREATE_TIME} bigint," +
                "${DbConstant.FROM_ID} varchar(20)," +
                "${DbConstant.TO_ID} varchar(20)," +
                "${DbConstant.MSG_ID} varchar(20)," +
                "${DbConstant.MSG_TYPE} int," +
                "${DbConstant.MSG_CONTENT} text," +
                "${DbConstant.FOLLOW} int," +
                "${DbConstant.GANDER} varchar(5)," +
                "${DbConstant.AVATAR} text," +
                "${DbConstant.NICKNAME} varchar(20)," +
                "${DbConstant.LEVEL} int," +
                "${DbConstant.IS_SEND} int," +
                "${DbConstant.SEND_STATUS} int," +
                "${DbConstant.READ_STATUS} int," +
                "${DbConstant.SERVER_MSG_ID} varchar(20)" +
                ")"
        LogUtils.info("拼接好的sql语句：" + sqlStr)
        return sqlStr
    }

    //查询表名称sql
    private fun getIsExitTableSql(): String {
        return "select name from sqlite_master where type='table'"
    }

    //查询语句
    private fun getSelectSql(tableName: String, offset: Int, pageSize: Int): String {
        LogUtils.info("select * from $tableName order by ${DbConstant.CHAT_ID} desc limit $offset,$pageSize")
        return "select * from $tableName order by ${DbConstant.CHAT_ID} desc limit $offset,$pageSize"
    }

    /**
     * description:查询一条数据
     * @param tableName 表名
     * @param isLast 是否最后一条
     **/
    private fun querySingleDataSql(tableName: String, isLast: Boolean): String {
        return "select * from $tableName order by ${DbConstant.CHAT_ID} ${if (isLast) "desc" else "asc"} limit 1"
    }

    private fun queryUnreadDataSql(tableName: String): String {
        return "select * from $tableName where ${DbConstant.READ_STATUS} = 0"
    }
}