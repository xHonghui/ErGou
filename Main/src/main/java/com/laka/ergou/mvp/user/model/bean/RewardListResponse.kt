package com.laka.ergou.mvp.user.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/6/26
 * @Description:
 */
data class RewardListResponse(
        @SerializedName("current_page")
        val currentPage: Int = 0,
        @SerializedName("first_page_url")
        val firstPageUrl: String = "",
        @SerializedName("from")
        val from: Int = 0,
        @SerializedName("last_page")
        val lastPage: Int = 0,
        @SerializedName("last_page_url")
        val lastPageUrl: String = "",
        @SerializedName("next_page_url")
        val nextPageUrl: String = "",
        @SerializedName("path")
        val path: String = "",
        @SerializedName("per_page")
        val perPage: String = "",
        @SerializedName("prev_page_url")
        val prevPageUrl: String = "",
        @SerializedName("to")
        val to: Int = 0,
        @SerializedName("total")
        val total: Int = 0,
        @SerializedName("data")
        val data: ArrayList<RewardListBean> = ArrayList()
)