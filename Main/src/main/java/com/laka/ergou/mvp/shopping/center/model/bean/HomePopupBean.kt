package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @Author:summer
 * @Date:2019/8/1
 * @Description:首页活动弹窗实体
 */
data class HomePopupBean(
        @SerializedName("class_id")
        val classId: String = "",
        @SerializedName("id")
        val id: String = "",
        @SerializedName("img_path")
        val imgPath: String = "",
        @SerializedName("scene_extra")
        val sceneExtraParams: HashMap<String, String> = HashMap(),
        @SerializedName("scene_id")
        val sceneId: String = "",
        @SerializedName("scene_value")
        val sceneValue: String = "",
        @SerializedName("title")
        val title: String = "",
        var localImgPath: String = "", //本地图片路径
        var preShopTimestamp: Long = 0  //上一次显示的时间，一天只能显示一次
) : Serializable