package com.laka.ergou.mvp.freedamission.constract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.freedamission.model.bean.FreeAdmissionResponse
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon

/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:
 */
interface FreeAdmissionConstract {

    interface IFreeAdmissionView : IBaseLoadingView<BaseListBean<ProductWithCoupon>> {
        fun onLoadFreeAdmissionProductListSuccess(list: BaseListBean<ProductWithCoupon>, imgPath: String, page: Int)
        fun onLoadFail(msg: String, page: Int)
    }

    interface IFreeAdmissionPresenter : IBasePresenter<IFreeAdmissionView> {
        fun onLoadFreeAdmissionProductList(page: Int)
    }

    interface IFreeAdmissionModel : IBaseModel<IFreeAdmissionView> {
        fun onLoadFreeAdmissionProductList(params: HashMap<String, String>, callBack: ResponseCallBack<FreeAdmissionResponse>)
    }
}