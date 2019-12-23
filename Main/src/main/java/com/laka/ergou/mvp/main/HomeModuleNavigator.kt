package com.laka.ergou.mvp.main

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.view.activity.HomeActivity
import com.laka.ergou.mvp.main.view.activity.X5WebActivity
import com.laka.ergou.mvp.shopping.search.view.activity.SearchHomeActivity

/**
 * @Author:Rayman
 * @Date:2018/12/24
 * @Description:项目主程跳转类
 */
object HomeModuleNavigator {

    fun startMainActivity(activity: Activity) {
        BaseActivityNavigator.startActivity(activity, HomeActivity::class.java)
    }

    @JvmStatic
    fun startSearchActivity(context: Context, searchKey: String) {
        var bundle = Bundle()
        bundle.putString(HomeConstant.SEARCH_KEY_WORD, searchKey)
        BaseActivityNavigator.startActivity(context, SearchHomeActivity::class.java, bundle)
    }

    @JvmStatic
    fun startSearchActivityForResult(context: Activity, searchKey: String, requestCode: Int) {
        var bundle = Bundle()
        bundle.putString(HomeConstant.SEARCH_KEY_WORD, searchKey)
        BaseActivityNavigator.startActivityForResult(context, SearchHomeActivity::class.java, bundle, requestCode)
    }


    @JvmStatic
    fun startSearchActivity(activity: Activity) {
        BaseActivityNavigator.startActivity(activity, SearchHomeActivity::class.java)
    }

    @JvmStatic
    fun startSearchActivity(activity: Activity, searchKey: String) {
        var bundle = Bundle()
        bundle.putString(HomeConstant.SEARCH_KEY_WORD, searchKey)
        BaseActivityNavigator.startActivity(activity, SearchHomeActivity::class.java, bundle)
    }

    fun startWebActivity(activity: Context, title: String = "", url: String) {
        var bundle = Bundle()
        bundle.putString(HomeConstant.WEB_TITLE, title)
        bundle.putString(HomeConstant.WEB_URL, url)
        BaseActivityNavigator.startActivity(activity, X5WebActivity::class.java, bundle)
    }

    fun startWebActivityForResult(activity: Activity, title: String = "", url: String, requestCode: Int) {
        var bundle = Bundle()
        bundle.putString(HomeConstant.WEB_TITLE, title)
        bundle.putString(HomeConstant.WEB_URL, url)
        BaseActivityNavigator.startActivityForResult(activity, X5WebActivity::class.java, bundle, requestCode)
    }

}