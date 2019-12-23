package com.laka.ergou.mvp.shopping

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import com.laka.ergou.mvp.shopping.center.view.activity.FavoriteProductListActivity
import com.laka.ergou.mvp.shopping.center.view.activity.NavigatorActivity
import com.laka.ergou.mvp.shopping.center.view.activity.ProductDetailActivity
import com.laka.ergou.mvp.shopping.center.view.activity.ShoppingSpecialActivity
import com.laka.ergou.mvp.user.UserModuleNavigator

/**
 * @Author:Rayman
 * @Date:2018/12/19
 * @Description:HOME模块页面跳转类
 */
object ShoppingModuleNavigator {

    fun startShoppingProductSpecialActivity(activity: Context, specialTitle: String, specialId: String, bigImageUrl: String) {
        val bundle = Bundle()
        bundle.putString(ShoppingCenterConstant.SPECIAL_TITLE, specialTitle)
        bundle.putString(ShoppingCenterConstant.SPECIAL_ID, specialId)
        bundle.putString(ShoppingCenterConstant.SPECIAL_BIG_IMAGE, bigImageUrl)
        BaseActivityNavigator.startActivity(activity, ShoppingSpecialActivity::class.java, bundle)
    }

    fun startShoppingProductSpecialActivityForResult(activity: Activity, specialTitle: String, specialId: String, bigImageUrl: String, requestCode: Int) {
        val bundle = Bundle()
        bundle.putString(ShoppingCenterConstant.SPECIAL_TITLE, specialTitle)
        bundle.putString(ShoppingCenterConstant.SPECIAL_ID, specialId)
        bundle.putString(ShoppingCenterConstant.SPECIAL_BIG_IMAGE, bigImageUrl)
        BaseActivityNavigator.startActivityForResult(activity, ShoppingSpecialActivity::class.java, bundle, requestCode)
    }

    fun startMainNavigatorActivity(activity: Context) {
        BaseActivityNavigator.startActivity(activity, NavigatorActivity::class.java)
    }

    fun startProductFavoriteActivity(activity: Context, favoriteTitle: String, favoriteId: String) {
        val bundle = Bundle()
        bundle.putString(ShoppingCenterConstant.FAVORITE_TITLE, favoriteTitle)
        bundle.putString(ShoppingCenterConstant.FAVORITE_ID, favoriteId)
        BaseActivityNavigator.startActivity(activity, FavoriteProductListActivity::class.java, bundle)
    }

    fun startProductDetailActivity(activity: Context) {
        BaseActivityNavigator.startActivity(activity, ProductDetailActivity::class.java)
    }

    fun startLoginActivity(activity: Context) {
        LoginModuleNavigator.startLoginActivity(activity)
    }

    fun startMessageActivity(activity: Context) {
        UserModuleNavigator.startMessageCenterActivity(activity)
    }

    fun startVideoCourseActivity(activity: Context, title: String, url: String) {
        val params = HashMap<String, String>()
        params[HomeConstant.WEB_TITLE] = title
        params[HomeConstant.WEB_URL] = url
        RouterNavigator.handleAppInternalNavigator(activity, HomeNavigatorConstant.NAV_WEB, params)
    }

}