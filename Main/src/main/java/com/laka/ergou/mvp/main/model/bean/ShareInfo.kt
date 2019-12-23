package com.laka.ergou.mvp.main.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:Rayman
 * @Date:2019/1/22
 * @Description:App分享文案数据
 */
data class ShareInfo(
        @SerializedName("title") var shareTitle: String = "",
        @SerializedName("sub_title") var shareContent: String = "",
        @SerializedName("url") var shareUrl: String = "",
        @SerializedName("detail") var shareDetail: String = "",
        @SerializedName("pic") var sharePic: String = ""
)
