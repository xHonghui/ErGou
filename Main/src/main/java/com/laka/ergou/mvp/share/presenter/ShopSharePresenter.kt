package com.laka.ergou.mvp.share.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.mvp.IBaseModel
import com.laka.ergou.mvp.share.contract.ShopShareConstract
import com.laka.ergou.mvp.share.model.repository.ShopShareModel

/**
 * @Author:summer
 * @Date:2019/5/30
 * @Description:
 */
class ShopSharePresenter : ShopShareConstract.IShopSharePresenter {

    private var mModel: ShopShareModel = ShopShareModel()
    private lateinit var mView: ShopShareConstract.IShopShareView

    override fun setView(view: ShopShareConstract.IShopShareView) {
        this.mView = view
        mModel.setView(view)
    }

    override fun onViewCreate() {

    }

    override fun onViewDestroy() {
        mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun onLoadQrCode(productId: String) {
//        mView.showLoading()
//        mModel.onLoadShopShare(productId)
//        mView.dismissLoading()
    }

}