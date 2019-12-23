package com.laka.ergou.mvp.shopping.center.contract

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.androidlib.mvp.IBaseListView
import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.shopping.center.model.bean.*
import io.reactivex.Observable

/**
 * @Author:Rayman
 * @Date:2018/12/15
 * @Description:商品模块数据列表Contract
 */
interface IShoppingListContract {

    interface IShoppingListModel : IBaseModel<IShoppingView> {

        /**
         * 加载商品列表数据
         * */
        fun getGoodsData(params: HashMap<String, String>, callBack: ResponseCallBack<ShoppingListResponse>)

    }

    interface IShoppingListPresenter : IBasePresenter<IShoppingView> {

        /**
         * 获取商品列表数据
         * */
        fun getGoodsData(cId:String,page:Int,order_field:String,order:String)

    }

    interface IShoppingView : IBaseLoadingView<ProductWithCoupon> {

        /**
         * 加载商品列表数据成功
         * */
        fun onLoadGoodsDataSuccess(data:BaseListBean<ProductWithCoupon>)

        /**
         * 加载商品列表数据失败
         * */
        fun onLoadGoodsDataFail(msg:String,page:Int)

    }

}
