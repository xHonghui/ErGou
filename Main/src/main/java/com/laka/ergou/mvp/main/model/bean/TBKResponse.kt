package com.laka.ergou.mvp.main.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:Rayman
 * @Date:2018/12/25
 * @Description:淘宝客Response中间层数据（数据嵌套严重，格式也很乱。应该让后台统一返回格式的）
 */
class TBKResponse<T> {

    // 正式响应数据
    @SerializedName("request_id")
    var requestId: String = ""
    @SerializedName("results", alternate = ["result_list", "uatm_tbk_item", "map_data", "tbk_favorites"])
    var results: T? = null
    @SerializedName("total_results")
    var totalPage: Int = 0

    // 失败响应数据
    @SerializedName("code")
    var code = 0
    @SerializedName("msg")
    var msg = ""
    @SerializedName("sub_code")
    var subCode = ""
    @SerializedName("sub_msg")
    var errorMsg = ""
}
