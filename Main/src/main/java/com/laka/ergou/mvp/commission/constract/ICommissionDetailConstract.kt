package com.laka.ergou.mvp.commission.constract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.commission.model.bean.CommissionDetailBean
import com.laka.ergou.mvp.commission.model.bean.CommissionDetailData

/**
 * @Author:summer
 * @Date:2019/1/18
 * @Description:
 */
interface ICommissionDetailConstract {

    interface ICommissionDetailView : IBaseLoadingView<BaseListBean<CommissionDetailData>> {
        fun onLoadCommissionDetailDataSuccess(result: BaseListBean<CommissionDetailData>)
        fun onLoadError()
    }

    interface ICommissionDetailPresenter : IBasePresenter<ICommissionDetailView> {
        fun onLoadCommissionDetailData(page: Int)
    }

    interface ICommissionDetailModel : IBaseModel<ICommissionDetailView> {
        fun onLoadCommissionDetailData(params: HashMap<String, String>, callBack: ResponseCallBack<CommissionDetailBean>)
    }
}