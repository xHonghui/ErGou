package com.laka.ergou.mvp.shopping.center.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/5/13
 * @Description:首页banner、专题、分类、活动分区
 */
data class HomePageResponse(
        @SerializedName("topic") val topicList: ArrayList<TopicBean>,
        @SerializedName("category") val categoryList: ArrayList<CategoryBean>,
        @SerializedName("banner") val bannerList: ArrayList<BannerBean>,
        @SerializedName("promotion") val promotionList: ArrayList<PromotionBean>,
        @SerializedName("popup") val popup:HomePopupBean?
)