package com.laka.ergou.mvp.db

import android.content.Context
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.tencent.wcdb.database.SQLiteDatabase
import com.tencent.wcdb.database.SQLiteOpenHelper

/**
 * @Author:summer
 * @Date:2019/2/19
 * @Description:
 */
class CustomDatabaseOpenHelper : SQLiteOpenHelper {

    companion object {
        const val DATABASE_NAME = "ergou.db"
        const val DATABASE_VERSION = 1
        const val FIRST_DATABASE_VERSION = 1
    }

    constructor(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : super(context, name, factory, version)

    /**
     * 创建数据库
     * */
    override fun onCreate(db: SQLiteDatabase?) {
        LogUtils.info("创建数据库！")
        //ToastHelper.showCenterToast("创建数据库！")
    }

    /**
     * 更新数据库
     * */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        LogUtils.info("更新数据库！")
    }
}