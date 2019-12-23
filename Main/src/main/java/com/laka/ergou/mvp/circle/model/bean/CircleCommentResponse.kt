package com.laka.ergou.mvp.circle.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/8/14
 * @Description:
 */
data class CircleCommentResponse(
        @SerializedName("tkl")
        val tkl: String,
        @SerializedName("tkl_url")
        val tklUrl: String
)