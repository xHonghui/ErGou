package com.laka.ergou.common.util.manager

import android.app.ActivityManager
import android.content.Context
import com.laka.androidlib.util.LogUtils

/**
 * @Author:summer
 * @Date:2019/6/17
 * @Description:
 */
object ActivityManagerUtils {

    fun isActivityTop(cls: Class<*>, context: Context): Boolean {
        try {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            manager?.let {
                val name = manager.getRunningTasks(1)[0].topActivity.className
                LogUtils.info("class_name----------$name===${cls.name}")
                return name == cls.name
            }
            return false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

}