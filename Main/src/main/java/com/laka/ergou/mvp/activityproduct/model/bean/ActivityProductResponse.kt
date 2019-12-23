package com.laka.ergou.mvp.activityproduct.model.bean

import com.google.gson.annotations.SerializedName
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon

/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:
 */
class ActivityProductResponse(
        @SerializedName("detail")
        val detail: ActivityProductDetailBean)