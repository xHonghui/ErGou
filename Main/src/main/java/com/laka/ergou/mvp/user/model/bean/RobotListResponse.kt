package com.laka.ergou.mvp.user.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:Rayman
 * @Date:2019/1/19
 * @Description:机器人数据列表响应体，嵌套在RobotDataResponse中
 */
class RobotListResponse<T> {

    @SerializedName("current_page")
    var currPage = 0

    @SerializedName("data")
    var robotList: ArrayList<T> = ArrayList()

    @SerializedName("total")
    var totalPage = 1
}