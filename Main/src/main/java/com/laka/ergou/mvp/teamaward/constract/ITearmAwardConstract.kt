package com.laka.ergou.mvp.teamaward.constract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.order.model.bean.OrderDataBean
import com.laka.ergou.mvp.teamaward.model.bean.Order
import com.laka.ergou.mvp.teamaward.model.bean.TearmAwardBean
import org.json.JSONObject

interface ITearmAwardConstract {
    interface ITearmAwardView : IBaseLoadingView<TearmAwardBean> {
//        fun onGetComradeSubsidy(type: TearmAwardBean)

        fun onGetComradeSubsidySuccess(result: BaseListBean<Order>, total: Int)
        fun onLoadError(page: Int)
        fun onAuthorFail()
    }

    interface ITearmAwardPresenter : IBasePresenter<ITearmAwardView> {
        fun onGetComradeSubsidy(type: String, sourcetype: String, page: Int)
    }

    interface ITaearmAwardModel : IBaseModel<ITearmAwardView> {
        fun onGetComradeSubsidy(params: HashMap<String, String>, callBack: ResponseCallBack<TearmAwardBean>)
    }
}