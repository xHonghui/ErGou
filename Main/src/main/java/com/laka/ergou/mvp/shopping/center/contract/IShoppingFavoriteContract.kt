package com.laka.ergou.mvp.shopping.center.contract

import com.laka.androidlib.mvp.IBaseListView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.shopping.center.model.bean.ShoppingResponse
import io.reactivex.Observable

/**
 * @Author:Rayman
 * @Date:2019/1/30
 * @Description:商品精选页面Contract
 */
interface IShoppingFavoriteContract {

    interface IShoppingFavoriteModel:IBaseModel<IShoppingFavoriteView> {

        /**
         * description:根据banner精选ID获取商品列表
         * favoriteId: String, page: Int
         **/
        fun getFavoriteProductList(params:HashMap<String,String>,callBack: ResponseCallBack<ShoppingResponse>)

    }

    interface IShoppingFavoritePresenter : IBasePresenter<IShoppingFavoriteView> {

        fun getFavoriteProductList(favoriteId: String, page: Int)

    }

    interface IShoppingFavoriteView : IBaseListView<ProductWithCoupon>
}