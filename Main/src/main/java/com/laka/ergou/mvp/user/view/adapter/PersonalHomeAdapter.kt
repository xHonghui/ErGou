package com.laka.ergou.mvp.user.view.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.ergou.R
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.model.bean.PersonalBannerBean
import com.laka.ergou.mvp.user.model.bean.PersonalHintBean
import com.laka.ergou.mvp.user.model.bean.PersonalMixtureBean
import com.laka.ergou.mvp.user.model.bean.PersonalUtilBean

/**
 * @Author:Rayman
 * @Date:2019/1/7
 * @Description:用户页面小工具Adapter
 */
class PersonalHomeAdapter(list: MutableList<MultiItemEntity>) : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(list) {

    /**
     * description:具体处理类
     **/
    private val bannerUtilItem = PersonalBannerItem()
    private val mixtureUtilItem = PersonalMixtureItem()
    private val singleUtilItem = PersonalSingleItem()
    private val hintUtilItem = PersonalHintItem()


    init {
        addItemType(UserConstant.BANNER_UTIL, R.layout.item_personal_util_banner)
        addItemType(UserConstant.MIXTURE_UTIL, R.layout.item_personal_mixture)
        addItemType(UserConstant.SINGLE_UTIL, R.layout.item_personal_util_horizontal)
        addItemType(UserConstant.HINT_UTIL, R.layout.item_personal_util_hint)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        when (helper?.itemViewType) {
            UserConstant.BANNER_UTIL -> bannerUtilItem.convert(helper, item as PersonalBannerBean)
            UserConstant.MIXTURE_UTIL -> mixtureUtilItem.convert(helper, item as PersonalMixtureBean)
            UserConstant.SINGLE_UTIL -> singleUtilItem.convert(helper, item as PersonalUtilBean)
            UserConstant.HINT_UTIL -> hintUtilItem.convert(helper, item as PersonalHintBean)
        }
    }
}