package com.laka.ergou.mvp.user.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.ergou.mvp.user.constant.UserConstant

/**
 * @Author:Rayman
 * @Date:2019/3/7
 * @Description:用户界面水平工具Bean
 */
class PersonalMixtureBean(var utilsList: ArrayList<PersonalUtilBean>) : MultiItemEntity {

    override fun getItemType(): Int {
        return UserConstant.MIXTURE_UTIL
    }
}