package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:Rayman
 * @Date:2018/12/21
 * @Description:
 */
data class ProductParentType(
        @SerializedName("p_id") var pid: String,
        @SerializedName("p_name") var parentName: String,
        @SerializedName("favorite") var isFavorite: Boolean = false)
