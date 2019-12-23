package com.laka.ergou.mvp.user.view.adapter

import android.text.TextUtils
import com.chad.library.adapter.base.BaseViewHolder
import com.laka.androidlib.base.adapter.MultipleAdapterItem
import com.laka.ergou.R
import com.laka.ergou.common.widget.banner.BannerGlideImageLoader
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.main.util.RouterParamsConverter
import com.laka.ergou.mvp.user.model.bean.PersonalBannerBean
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import org.json.JSONObject

/**
 * @Author:Rayman
 * @Date:2019/3/7
 * @Description:用户个人资料，Banner面板
 */
class PersonalBannerItem : MultipleAdapterItem<PersonalBannerBean> {

    override fun convert(helper: BaseViewHolder?, item: PersonalBannerBean?) {
        val bannerView = helper?.getView<Banner>(R.id.banner_personal)
        item?.let {
            val images = ArrayList<String>()
            for (productFavorite in item.bannerList) {
                images.add(productFavorite.imgPath)
            }
            bannerView?.let {
                it.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                        .setIndicatorGravity(BannerConfig.CENTER)
                        .setBannerAnimation(Transformer.Default)
                        .setDelayTime(5000)
                        .setImageLoader(BannerGlideImageLoader(true))
                        .setImages(images)
                        .isAutoPlay(true)
                        .start()

                it.setOnBannerListener { position ->
                    // 具体的跳转页面需要先解析出跳转到App哪个页面。假若解析出来的target不存在本地映射表，不做跳转
                    val bannerBean = item.bannerList[position]
                    var target = ""
                    val bannerType = bannerBean.sceneId.toInt()
                    if (RouterNavigator.bannerRouterReflectMap.containsKey(bannerType)) {
                        target = RouterNavigator.bannerRouterReflectMap[bannerType].toString()
                    }
                    var paramsMap = bannerBean.sceneExtra ?: HashMap()
                    paramsMap[HomeConstant.TITLE] = bannerBean.title
                    paramsMap[HomeNavigatorConstant.ROUTER_VALUE] = bannerBean.sceneValue
                    if (!paramsMap.containsKey(HomeConstant.TOPIC_BIG_IMAGE_URL)) {
                        paramsMap[HomeConstant.TOPIC_BIG_IMAGE_URL] = bannerBean.imgPath
                    }
                    // 具体跳转交给路由处理
                    RouterNavigator.handleAppInternalNavigator(helper.convertView.context, target, paramsMap)
                }
            }
        }
    }
}