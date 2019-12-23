package com.laka.ergou.mvp.shop.model.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.ergou.mvp.advertbanner.model.bean.AdvertBannerBean
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant

/**
 * @Author:summer
 * @Date:2019/7/30
 * @Description:
 */
class AdvertBannerListBean(var data: ArrayList<AdvertBannerBean> = ArrayList()):MultiItemEntity{
    override fun getItemType(): Int {
        return ShopDetailConstant.SHOP_DETAIL_ADVERT_BANNER
    }
}