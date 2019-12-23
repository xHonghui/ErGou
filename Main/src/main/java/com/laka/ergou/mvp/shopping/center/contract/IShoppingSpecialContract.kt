package com.laka.ergou.mvp.shopping.center.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.shopping.center.model.bean.ProductListResponse

/**
 * @Author:summer
 * @Date:2019/4/29
 * @Description:商品专题
 */
interface IShoppingSpecialContract {

    interface IShoppingSpecialView : IBaseLoadingView<ArrayList<ProductWithCoupon>> {
        fun onLoadProductDataSuccess(list: BaseListBean<ProductWithCoupon>, imgPath: String, page: Int)
        fun onLoadFail(msg: String, page: Int)
    }

    interface IShoppingSpecialPresenter : IBasePresenter<IShoppingSpecialView> {
        fun onLoadProductSpecialData(sId: String, page: Int)
    }

    interface IShoppingSpecialModel : IBaseModel<IShoppingSpecialView> {
        fun onLoadProductSpecialData(params: HashMap<String, String>, callBack: ResponseCallBack<ProductListResponse>)
    }

}