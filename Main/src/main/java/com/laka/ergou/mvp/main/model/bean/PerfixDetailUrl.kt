package com.laka.ergou.mvp.main.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/4/25
 * @Description:天猫超市--商品详情页面域名匹配以及产品 ID 获取
 */
class PerfixDetailUrl(
        @SerializedName("key")
        var key: String = "",
        @SerializedName("url")
        var host: String = "")