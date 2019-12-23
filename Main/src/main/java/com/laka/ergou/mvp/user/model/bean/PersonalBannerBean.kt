package com.laka.ergou.mvp.user.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.ergou.mvp.advertbanner.model.bean.AdvertBannerBean
import com.laka.ergou.mvp.user.constant.UserConstant


/**
 * @Author:Rayman
 * @Date:2019/3/7
 * @Description:用户界面Banner数据
 */
class PersonalBannerBean(var bannerList: ArrayList<AdvertBannerBean>) : MultiItemEntity {

    override fun getItemType(): Int {
        return UserConstant.BANNER_UTIL
    }

}