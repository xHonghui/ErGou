package com.laka.ergou.mvp.user.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:Rayman
 * @Date:2019/1/18
 * @Description:机器人数据响应体
 */
class RobotDataResponse<T> {

    @SerializedName("has_robot")
    var haveRobot: Int = 0

    @SerializedName("robots")
    var robotData: RobotListResponse<T> = RobotListResponse()

    @SerializedName("robot")
    var defaultRobot: RobotInfo = RobotInfo()

}