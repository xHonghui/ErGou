package com.laka.ergou.mvp.shop.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/7/27
 * @Description:标签
 */
data class ProductLabelsBean(
        @SerializedName("name")
        var name: String = "",
        @SerializedName("word_color")
        var wordColor: String = "",
        @SerializedName("bg_color")
        var bgColor: String = "",
        @SerializedName("image")
        var image: String = ""
)