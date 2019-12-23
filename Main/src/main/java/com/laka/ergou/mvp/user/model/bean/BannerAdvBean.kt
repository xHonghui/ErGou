package com.laka.ergou.mvp.user.model.bean

import com.google.gson.annotations.SerializedName
import com.laka.ergou.mvp.user.constant.UserApiConstant
import org.json.JSONObject

/**
 * @Author:Rayman
 * @Date:2019/3/7
 * @Description:用户界面Banner数据---单指Banner在后端的响应体数据
 */
data class BannerAdvBean(
        var id: String = "",
        var title: String = "",
        @SerializedName(UserApiConstant.IMAGE_PATH)
        var images: String = "",
        @SerializedName(UserApiConstant.SCENE_ID)
        var bannerType: Int = 1,
        @SerializedName(UserApiConstant.SCENE_VALUE)
        var bannerParams: String,                           // 对应场景的值
        @SerializedName(UserApiConstant.SCENE_EXTRA)
        var bannerExtraParams: String                       // 对应场景额外参数
)