package com.laka.ergou.mvp.order.constract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.order.model.bean.OrderDataBean
import com.laka.ergou.mvp.order.model.bean.OrderListBean

/**
 * @Author:summer
 * @Date:2019/1/19
 * @Description:
 */
interface IMyOrderConstract {

    interface IMyOrderView : IBaseLoadingView<OrderListBean> {
        fun onLoadMyOrderDataSuccess(result: BaseListBean<OrderDataBean>, total: Int)
        fun onLoadError(page:Int)
        fun onAuthorFail()
    }

    interface IMyOrderPresenter : IBasePresenter<IMyOrderView> {
        fun onLoadMyOrderData(type: Int, sourceType: Int,page:Int)
    }

    interface IMyOrderModel : IBaseModel<IMyOrderView> {
        fun onLoadMyOrderData(params: HashMap<String, String>, callBack: ResponseCallBack<OrderListBean>)
    }
}