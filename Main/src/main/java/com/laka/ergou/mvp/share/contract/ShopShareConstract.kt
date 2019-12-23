package com.laka.ergou.mvp.share.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter

/**
 * @Author:summer
 * @Date:2019/5/30
 * @Description:
 */
interface ShopShareConstract {

    interface IShopShareView : IBaseLoadingView<String> {
        fun onLoadQrCodeSuccess(path: String)
    }

    interface IShopSharePresenter : IBasePresenter<IShopShareView> {
        fun onLoadQrCode(productId: String)
    }

    interface IShopShareModel : IBaseModel<IShopShareView> {
        fun onLoadShopShare(productId: String)
    }
}