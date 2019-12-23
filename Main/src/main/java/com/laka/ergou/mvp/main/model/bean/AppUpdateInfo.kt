package com.laka.ergou.mvp.main.model.bean

import com.google.gson.annotations.SerializedName
import com.laka.androidlib.util.SystemUtils

/**
 * @Author:Rayman
 * @Date:2019/1/22
 * @Description:App升级数据Bean
 */
class AppUpdateInfo(
        @SerializedName("state") var state: Int = 0,//1：提醒更新  2：强制更新
        @SerializedName("must") var isForceUpdate: Int = 0,
        @SerializedName("url") var downLoadUrl: String = "",
        @SerializedName("version") var appVersion: Int = 0,
        @SerializedName("versionName") var appVersionName: String = "",
        @SerializedName("version_show") var appVersionShow: String = "",
        @SerializedName("description") var updateDescription: String = "",
        @SerializedName("edittime") var editTime: String = ""
) {
    fun isForceUpdate(): Boolean { //强制更新
        return state == 2 //1：提醒更新  2：强制更新
    }

    fun isNeedUpdate(): Boolean {
        //只要 state 有数据，则判断为需要更新
        return state == 1 || state == 2
    }
}
