package com.laka.ergou.mvp.user.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.ergou.mvp.user.constant.UserConstant

/**
 * @Author:Rayman
 * @Date:2019/3/12
 * @Description:我的页面防丢单item
 */
class PersonalHintBean : MultiItemEntity {
    override fun getItemType(): Int {
        return UserConstant.HINT_UTIL
    }
}