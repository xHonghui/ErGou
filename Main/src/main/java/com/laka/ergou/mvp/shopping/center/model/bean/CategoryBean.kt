package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/5/13
 * @Description:分类信息（首页tab分类列表）
 */
data class CategoryBean(
        @SerializedName("id") val categoryId: String = "",
        @SerializedName("title") val title: String = ""
)