package com.laka.ergou.mvp.shop.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @Author:summer
 * @Date:2019/1/24
 * @Description:
 */
data class SmallImages(@SerializedName("string")
                       val imageList: ArrayList<String> = ArrayList(),
                       var uiType: Int = -1
) : Serializable, MultiItemEntity {
    override fun getItemType(): Int {
        return uiType
    }
}