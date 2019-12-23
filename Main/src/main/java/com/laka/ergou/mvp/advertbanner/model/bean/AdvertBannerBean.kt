package com.laka.ergou.mvp.advertbanner.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/7/29
 * @Description:
 */
class AdvertBannerBean(
        @SerializedName("id")
        val id: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("img_path")
        val imgPath: String,
        @SerializedName("scene_id")
        val sceneId: String,
        @SerializedName("scene_value")
        val sceneValue: String,
        @SerializedName("scene_extra")
        val sceneExtra: HashMap<String, String>? = HashMap(),
        @SerializedName("create_time")
        val createTime: String
)