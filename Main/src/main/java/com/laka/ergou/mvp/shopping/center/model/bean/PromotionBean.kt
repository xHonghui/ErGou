package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/5/13
 * @Description:活动专区
 */
data class PromotionBean(
        @SerializedName("title") var title: String = "",
        @SerializedName("img_path") var imgUrl: String = "",
        @SerializedName("scene_id") var sceneId: String = "",
        @SerializedName("scene_value") var sceneValue: String = "",
        @SerializedName("scene_extra") var sceneExtra: HashMap<String, String> = HashMap()
)