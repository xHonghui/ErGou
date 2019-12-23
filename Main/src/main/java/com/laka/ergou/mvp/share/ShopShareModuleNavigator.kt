package com.laka.ergou.mvp.share

import android.content.Context
import android.os.Bundle
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.share.constant.ShareConstant
import com.laka.ergou.mvp.share.model.bean.ShareResponse
import com.laka.ergou.mvp.share.view.activity.SeeBigImageActivity
import com.laka.ergou.mvp.share.view.activity.ShopShareActivity

/**
 * @Author:summer
 * @Date:2019/5/27
 * @Description:
 */
object ShopShareModuleNavigator {

    fun startShopShareActivity(context: Context, shareResponse: ShareResponse) {
        val bundle = Bundle()
        bundle.putSerializable(ShareConstant.SHARE_DATA_FOR_WECHAT, shareResponse)
        BaseActivityNavigator.startActivity(context, ShopShareActivity::class.java, bundle)
    }

    fun startWechatMomentCourseWebActivity(context: Context, url: String, title: String) {
        val target = RouterNavigator.bannerRouterReflectMap[5] //H5链接
        val params = HashMap<String, String>()
        params[HomeConstant.WEB_TITLE] = title
        params[HomeConstant.WEB_URL] = url
        RouterNavigator.handleAppInternalNavigator(context, "$target", params)
    }

    fun startSeeBigImageActivity(context: Context, list: ArrayList<String>,position:Int) {
        val bundle = Bundle()
        bundle.putSerializable(ShareConstant.KEY_SEE_BIGIMAGE_LIST, list)
        bundle.putInt(ShareConstant.KEY_SEE_BIGIMAGE_POSITION,position)
        BaseActivityNavigator.startActivity(context, SeeBigImageActivity::class.java, bundle)
    }

}