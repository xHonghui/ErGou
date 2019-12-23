package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName
import com.laka.ergou.mvp.main.constant.HomeConstant

data class HomeUrlBean(
        val promotion: Promotion
)


data class Promotion(
        val expire: Int,
        val img_url: String,
        val show: Int,
        val url: String,
        @SerializedName(HomeConstant.TITLE)
        val title: String, //备用字段，标题
        @SerializedName("scene_extra")
        val extraParams: HashMap<String, String>
)