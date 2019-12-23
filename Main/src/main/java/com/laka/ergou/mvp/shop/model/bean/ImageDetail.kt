package com.laka.ergou.mvp.shop.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @Author:summer
 * @Date:2019/1/15
 * @Description: 图片详情
 */

data class ImageDetail(var imageUrl: String = "",
                       var imageWidth: String = "",
                       var imageHeight: String = "",
                       var uiType: Int = -1) : MultiItemEntity {
    override fun getItemType(): Int {
        return uiType
    }
}