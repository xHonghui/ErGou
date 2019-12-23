package com.laka.ergou.mvp.user.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:Rayman
 * @Date:2019/1/18
 * @Description:我的模块---机器人信息
 */
data class RobotInfo(
        @SerializedName("id") var robotId: Int = 0,
        @SerializedName("wxaccount", alternate = ["robot_wxid"]) var robotWxId: String = "",
        @SerializedName("nickname") var robotName: String = "",
        @SerializedName("avatar", alternate = ["qrcode_data"]) var robotAvatar: String = "",
        @SerializedName("online") var robotStatus: Int = 0
) {
    fun isOnline(): Boolean {
        return robotStatus == 1
    }
}
