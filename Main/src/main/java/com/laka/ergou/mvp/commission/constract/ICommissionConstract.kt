package com.laka.ergou.mvp.commission.constract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.commission.model.bean.CommissionBean
import com.laka.ergou.mvp.commission.model.bean.CommissionDIYBean
import com.laka.ergou.mvp.commission.model.bean.CommissionNewBean
import com.laka.ergou.mvp.order.model.bean.OrderDataBean
import org.json.JSONObject

/**
 * @Author:summer
 * @Date:2019/1/18
 * @Description:
 */
interface ICommissionConstract {

    interface IBaseCommissonView : IBaseLoadingView<CommissionNewBean> {
        fun onLoadMyCommissonDataSuccess(result: CommissionBean) {}
        fun onGetMyCommissonDataSuccess(result: CommissionNewBean) {}
        fun onGetMyCommissonDataSuccess(result: CommissionNewBean, list: BaseListBean<CommissionDIYBean>) {}
        fun onWithdrawalSuccess() {}
        fun onLoadError(){}
    }

    interface IBaseCommissonPresenter : IBasePresenter<IBaseCommissonView> {
        fun onLoadMyCommissonData()
        fun onLoadWithdrawal()
        fun onGetMyCommissonData(type: String, page: String)
    }

    interface IBaseCommissonModel : IBaseModel<IBaseCommissonView> {
        fun onLoadMyCommissonData(params: HashMap<String, String>, callBack: ResponseCallBack<CommissionBean>)
        fun onLoadWithdrawal(params: HashMap<String, String>, callBack: ResponseCallBack<JSONObject>)
        fun onGetMyCommissonData(params: HashMap<String, String>, callBack: ResponseCallBack<CommissionNewBean>)
    }

}