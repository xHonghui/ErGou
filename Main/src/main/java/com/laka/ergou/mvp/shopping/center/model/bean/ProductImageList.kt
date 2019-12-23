package com.laka.ergou.mvp.shopping.center.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @Author:Rayman
 * @Date:2018/12/26
 * @Description:商品图片列表
 */
class ProductImageList : MultiItemEntity, Serializable {

    @SerializedName("string")
    var productPicList: ArrayList<String> = ArrayList()

    var uiType: Int = 0

    override fun getItemType(): Int {
        return uiType
    }
}