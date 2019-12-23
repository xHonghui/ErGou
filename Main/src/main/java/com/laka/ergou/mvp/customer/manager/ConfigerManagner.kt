package com.laka.ergou.mvp.customer.manager

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.laka.androidlib.util.ApplicationUtils


/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:
 */
open class ConfigerManagner {

    private var context: Context = ApplicationUtils.getApplication()

    companion object {
        private var mConfigManger: ConfigerManagner = ConfigerManagner()
        val instance: ConfigerManagner by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            mConfigManger
        }
    }

    private fun getMySharedPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun setString(name: String, value: String): Boolean {
        return getMySharedPreferences().edit().putString(name, value).commit()
    }

    fun getString(name: String): String {
        return getMySharedPreferences().getString(name, "")
    }

}