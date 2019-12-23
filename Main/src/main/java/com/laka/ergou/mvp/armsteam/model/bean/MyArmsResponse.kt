package com.laka.ergou.mvp.armsteam.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/5/28
 * @Description:我的战队
 */
data class MyArmsResponse(
        @SerializedName("data")
        val data: ArrayList<MyArmsLevelsBean> = ArrayList(),
        @SerializedName("current_page")
        val currentPage: Int,
        @SerializedName("first_page_url")
        val firstPageUrl: String,
        @SerializedName("from")
        val from: String,
        @SerializedName("last_page")
        val pageCount: Int,
        @SerializedName("last_page_url")
        val lastPageUrl: String,
        @SerializedName("next_page_url")
        val nextPageUrl: String,
        @SerializedName("path")
        val path: String,
        @SerializedName("per_page")
        val perPage: String,
        @SerializedName("prev_page_url")
        val prevPageUrl: String,
        @SerializedName("to")
        val to: String,
        @SerializedName("total")
        val total: Int
)