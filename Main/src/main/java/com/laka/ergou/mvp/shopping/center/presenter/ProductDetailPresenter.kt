package com.laka.ergou.mvp.shopping.center.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.rx.BaseRxSubscriber
import com.laka.ergou.common.util.rx.RxSubscriber
import com.laka.ergou.mvp.shopping.center.contract.IProductDetailContract
import com.laka.ergou.mvp.shopping.center.model.bean.ProductDetail
import com.laka.ergou.mvp.shopping.center.model.repository.ProductDetailModel

/**
 * @Author:Rayman
 * @Date:2018/12/20
 * @Description:商品详情Presenter
 */
class ProductDetailPresenter : IProductDetailContract.IProductDetailPresenter {

    lateinit var mView: IProductDetailContract.IProductDetailView
    var mModel: IProductDetailContract.IProductDetailModel = ProductDetailModel()

    override fun onViewCreate() {
    }

    override fun onViewDestroy() {
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun setView(view: IProductDetailContract.IProductDetailView) {
        this.mView = view
    }

    override fun getProductDetail(productId: String) {
        mModel?.getProductDetail(productId)
                .subscribe(object : RxSubscriber<ProductDetail, IProductDetailContract.IProductDetailView>(mView) {
                    override fun onNext(t: ProductDetail) {
                        super.onNext(t)
                        mView?.showData(t)
                    }
                })
    }
}