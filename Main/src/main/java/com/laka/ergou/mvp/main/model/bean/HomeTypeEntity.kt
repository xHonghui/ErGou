package com.laka.ergou.mvp.main.model.bean

import com.flyco.tablayout.listener.CustomTabEntity

/**
 * @Author:Rayman
 * @Date:2018/12/24
 * @Description:主页分类实体类
 */
data class HomeTypeEntity(
        var title: String,
        var normalIconRes: Int,
        var selectedIconRes: Int
) : CustomTabEntity {
    override fun getTabUnselectedIcon(): Int {
        return normalIconRes
    }

    override fun getTabSelectedIcon(): Int {
        return selectedIconRes
    }

    override fun getTabTitle(): String {
        return title
    }
}