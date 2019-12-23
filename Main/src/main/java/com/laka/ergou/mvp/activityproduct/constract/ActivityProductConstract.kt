package com.laka.ergou.mvp.activityproduct.constract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.activityproduct.model.bean.ActivityProductResponse
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon

/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:
 */
interface ActivityProductConstract {

    interface IActivityProductView : IBaseLoadingView<BaseListBean<ProductWithCoupon>> {
        fun onLoadActivityProductListSuccess(list: BaseListBean<ProductWithCoupon>, imgPath: String, page: Int)
        fun onLoadFail(msg: String, page: Int)
    }

    interface IActivityProductPresenter : IBasePresenter<IActivityProductView> {
        fun onLoadActivityProductList(page: Int, activityId: String)
    }

    interface IActivityProductModel : IBaseModel<IActivityProductView> {
        fun onLoadActivityProductList(params: HashMap<String, String>, callBack: ResponseCallBack<ActivityProductResponse>)
    }
}