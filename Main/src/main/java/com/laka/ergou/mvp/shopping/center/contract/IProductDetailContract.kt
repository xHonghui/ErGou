package com.laka.ergou.mvp.shopping.center.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.mvp.shopping.center.model.bean.ProductDetail
import io.reactivex.Observable

/**
 * @Author:Rayman
 * @Date:2018/12/20
 * @Description:商品详情Contract
 */
interface IProductDetailContract {

    interface IProductDetailModel {

        /**
         * description: 获取商品详情
         * @param productId 商品ID
         **/
        fun getProductDetail(productId: String): Observable<ProductDetail>
    }

    interface IProductDetailPresenter : IBasePresenter<IProductDetailView> {

        /**
         * description: 获取商品详情
         * @param productId 商品ID
         **/
        fun getProductDetail(productId: String)

    }

    interface IProductDetailView : IBaseLoadingView<ProductDetail>
}