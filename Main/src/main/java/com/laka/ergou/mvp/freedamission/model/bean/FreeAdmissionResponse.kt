package com.laka.ergou.mvp.freedamission.model.bean

import com.google.gson.annotations.SerializedName
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon

/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:
 */
class FreeAdmissionResponse(
        @SerializedName("is_first")
        val isFirst: Int = 1,
        @SerializedName("img_path")
        val imgPath: String = "",
        @SerializedName("results")
        val dataList: ArrayList<ProductWithCoupon>)