package com.laka.ergou.mvp.activityproduct.model.bean

import com.google.gson.annotations.SerializedName
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon

/**
 * @Author:summer
 * @Date:2019/8/5
 * @Description:
 */
class ActivityProductDetailBean(
        @SerializedName("activity_id")
        val activityId: Int = -1,
        @SerializedName("activity_title")
        val activityTitle: String = "",
        @SerializedName("activity_type")
        val activityType: Int = 1, //活动类型:1产品专题,2活动专题
        @SerializedName("img_path")
        val imgPath: String = "",
        @SerializedName("activity_url")
        val activityUrl: String = "",
        @SerializedName("product_list")
        val dataList: ArrayList<ProductWithCoupon>)