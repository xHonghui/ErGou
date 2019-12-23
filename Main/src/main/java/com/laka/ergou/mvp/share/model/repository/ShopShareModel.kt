package com.laka.ergou.mvp.share.model.repository

import com.laka.ergou.BuildConfig
import com.laka.ergou.mvp.share.constant.ShareConstant
import com.laka.ergou.mvp.share.contract.ShopShareConstract
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * @Author:summer
 * @Date:2019/5/30
 * @Description:
 */
class ShopShareModel:ShopShareConstract.IShopShareModel {

    private lateinit var mView:ShopShareConstract.IShopShareView

    override fun setView(v: ShopShareConstract.IShopShareView) {
        this.mView = v
    }

    override fun onViewDestory() {

    }

    override fun onLoadShopShare(productId: String) {
//        val okHttpClient = OkHttpClient.Builder()
//        val body = FormBody.Builder()
//        val request = Request.Builder().post()
//
//
//        val call = okHttpClient.build().newCall(request)
//        call.enqueue(callback)
//        return call
    }
}