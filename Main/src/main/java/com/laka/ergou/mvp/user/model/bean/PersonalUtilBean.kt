package com.laka.ergou.mvp.user.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.ergou.mvp.user.constant.UserConstant

/**
 * @Author:Rayman
 * @Date:2019/1/7
 * @Description:我的界面，小工具Bean。具体的跳转可以根据路由跳转
 */
class PersonalUtilBean(var utilIcon: Int,
                       var utilName: String,
                       var utilNavigation: String):MultiItemEntity{
    override fun getItemType(): Int {
        return UserConstant.SINGLE_UTIL
    }

}
