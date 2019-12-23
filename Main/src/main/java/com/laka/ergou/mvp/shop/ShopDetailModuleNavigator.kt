package com.laka.ergou.mvp.shop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.laka.androidlib.util.BaseActivityNavigator
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shop.view.activity.ReceiveCouponActivity
import com.laka.ergou.mvp.shop.view.activity.ShopDetailActivity
import com.laka.ergou.mvp.shop.view.activity.TaoBaoAuthorFailActivity
import com.laka.ergou.mvp.shop.view.activity.TaoBaoAuthorSuccessActivity
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon

/**
 * @Author:summer
 * @Date:2019/1/8
 * @Description:
 */
object ShopDetailModuleNavigator {

    private val mShopDetailActivityList: ArrayList<ShopDetailActivity> = ArrayList()
    private var mShopDetailActivityStackLength: Int = 5

    init {
        mShopDetailActivityStackLength = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            5
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            8
        } else {
            5
        }
    }

    @JvmStatic
    fun addElementForActivityStack(activity: ShopDetailActivity) {
        if (mShopDetailActivityList.size >= mShopDetailActivityStackLength
                && mShopDetailActivityStackLength > 0) {
            val firstActivity = mShopDetailActivityList[0]
            if (firstActivity != null && !firstActivity.isFinishing && !firstActivity.isDestroyed) {
                firstActivity.finish(true)
            }
        }
        mShopDetailActivityList.add(activity)
    }

    @JvmStatic
    fun removeElementForActivityStack() {
        if (mShopDetailActivityList.isNotEmpty()) {
            mShopDetailActivityList.removeAt(mShopDetailActivityList.size - 1)
        }
    }

    @JvmStatic
    fun removeElementForActivityStack(index: Int) {
        if (index >= 0 && index < mShopDetailActivityList.size) {
            mShopDetailActivityList.removeAt(index)
        }
    }

    @JvmStatic
    fun setShopDetailActivityListSize(size: Int) {
        if (size > 0) {
            this.mShopDetailActivityStackLength = size
        }
    }

    @JvmStatic
    fun startShopDetailActivity(context: Context, productId: String) {
        val bundle = Bundle()
        bundle.putString(ShopDetailConstant.PRODUCT_ID, productId)
        bundle.putInt(ShopDetailConstant.ENTRANCE, ShopDetailConstant.PRODUCT_ITEM_CLICK_SKELETON)
        BaseActivityNavigator.startActivity(context, ShopDetailActivity::class.java, bundle)
    }

    @JvmStatic
    fun startShopDetailActivityForResult(context: Activity, productId: String, requestCode: Int) {
        val bundle = Bundle()
        bundle.putString(ShopDetailConstant.PRODUCT_ID, productId)
        bundle.putInt(ShopDetailConstant.ENTRANCE, ShopDetailConstant.PRODUCT_ITEM_CLICK_SKELETON)
        BaseActivityNavigator.startActivityForResult(context, ShopDetailActivity::class.java, bundle, requestCode)
    }

    fun startTaoBaoAuthorSuccessActivity(context: Context) {
        BaseActivityNavigator.startActivity(context, TaoBaoAuthorSuccessActivity::class.java)
    }

    fun startTaoBaoAuthorFailActivity(context: Context) {
        BaseActivityNavigator.startActivity(context, TaoBaoAuthorFailActivity::class.java)
    }

    fun startReceiveCouponActivity(context: Context, url: String, pid: String, adzoneId: String) {
        val bundle = Bundle()
        bundle.putString(HomeConstant.WEB_URL, url)
        bundle.putString(HomeConstant.USER_PID, pid)
        bundle.putString(HomeConstant.USER_ADZONE_ID, adzoneId)
        BaseActivityNavigator.startActivity(context, ReceiveCouponActivity::class.java,bundle)
    }

}