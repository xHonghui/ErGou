package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

/**
 * @Author:summer
 * @Date:2019/5/13
 * @Description:专题
 */
data class TopicBean(
        @SerializedName("title") var title: String = "",
        @SerializedName("img_path") var imageUrl: String = "",
        @SerializedName("scene_id") var sceneId: String = "",
        @SerializedName("scene_value") var sceneValue: String = "",
        @SerializedName("scene_extra") var sceneExtra: HashMap<String, String>
)