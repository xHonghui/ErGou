package com.laka.ergou.mvp.shop.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @Author:summer
 * @Date:2018/12/25
 * @Description:不同类型的title实体
 */
data class TitleTypeBean(var type: Int, var title: String, var open: Int = 0) : MultiItemEntity {
    override fun getItemType(): Int {
        return type
    }
}