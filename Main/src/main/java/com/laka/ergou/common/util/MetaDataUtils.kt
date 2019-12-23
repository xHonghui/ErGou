package com.laka.ergou.common.util

import android.app.Activity
import android.content.pm.PackageManager
import android.content.pm.ApplicationInfo
import android.util.Log
import com.laka.androidlib.util.ApplicationUtils


/**
 * @Author:summer
 * @Date:2019/3/5
 * @Description:
 */
object MetaDataUtils {

    const val DEFAULT_VALUE: String = "lake"

    /**
     * application 元素下配置的 mateData 数据获取
     * */
    @JvmStatic
    fun getMateDataForApplicationInfo(key: String): String {
        var value = DEFAULT_VALUE
        try {
            val appInfo = ApplicationUtils.getApplication().packageManager.getApplicationInfo(ApplicationUtils.getApplication().packageName, PackageManager.GET_META_DATA)
            value = appInfo.metaData.getString(key)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return value
    }

    /**
     * activity 元素配置下的metaData 数据获取
     * */
    fun getMateDataForActivityInfo(activity: Activity, key: String): String {
        var value = DEFAULT_VALUE
        try {
            val appInfo = ApplicationUtils.getApplication().packageManager.getActivityInfo(activity.componentName, PackageManager.GET_META_DATA)
            value = appInfo.metaData.getString(key)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return value
    }

}