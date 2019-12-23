package com.laka.ergou.mvp.shop.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/4/15
 * @Description:商品详情视频
 */
class ProductDetailVideos(
        @SerializedName("videoId")
        var videoId: String = "",
        @SerializedName("url")
        var videoUrl: String = "",
        @SerializedName("videoThumbnailURL")
        var videoThumbnailURL: String = "",
        @SerializedName("spatialVideoDimension")
        var spatialVideoDimension: String = ""
)