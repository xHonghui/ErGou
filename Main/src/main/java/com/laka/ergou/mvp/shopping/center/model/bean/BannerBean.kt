package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/5/13
 * @Description:banner
 */
data class BannerBean(
        @SerializedName("id") val id: String = "",
        @SerializedName("title") val title: String = "",
        @SerializedName("img_path") val imgUrl: String = "",
        @SerializedName("scene_id") val sceneId: String = "",
        @SerializedName("scene_value") val sceneValue: String = "",
        @SerializedName("up") val upColor: String = "",
        @SerializedName("down") val downColor: String = "",
        @SerializedName("create_time") val createTime: String = "",
        @SerializedName("scene_extra") val sceneExtraParams: HashMap<String, String> = HashMap(),  //扩展参数
        @SerializedName("big_img_url") val bigImageUrl: String = ""
)