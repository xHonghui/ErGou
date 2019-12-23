package com.laka.ergou.mvp.teamaward.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.constant.RequestCommonCode
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.order.model.bean.OrderDataBean
import com.laka.ergou.mvp.teamaward.constant.TearmAwardConstant
import com.laka.ergou.mvp.teamaward.constract.ITearmAwardConstract
import com.laka.ergou.mvp.teamaward.model.bean.Order
import com.laka.ergou.mvp.teamaward.model.bean.TearmAwardBean
import com.laka.ergou.mvp.teamaward.model.respository.TearmAwardModel
import org.json.JSONObject

class TearmAwardPresenter : ITearmAwardConstract.ITearmAwardPresenter {

    override fun onGetComradeSubsidy(type: String, sourcetype: String, page: Int) {
        val params = HashMap<String, String>()
        params["type"] = type
        params["source_type"] = sourcetype
        params["page"] = "$page"
        params["pageSize"] = "${TearmAwardConstant.PAGE_SIZE}"
        mModel.onGetComradeSubsidy(params, object : ResponseCallBack<TearmAwardBean> {
            override fun onSuccess(t: TearmAwardBean) {
                mView.onGetComradeSubsidySuccess(object : BaseListBean<Order>() {
                    override fun getList(): MutableList<Order> {
                        return t.orders
                    }

                    override fun getPageTotalCount(): Int {
                        return if (t.total % TearmAwardConstant.PAGE_SIZE == 0) {
                            t.total / TearmAwardConstant.PAGE_SIZE
                        } else {
                            t.total / TearmAwardConstant.PAGE_SIZE + 1
                        }
                    }
                }, t.total)
            }

            override fun onFail(e: BaseException?) {
                if (e?.code == RequestCommonCode.LK_WRONG_USER_TOKEN
                        || e?.code == RequestCommonCode.LK_NOT_LOGIN) {
                    mView.onAuthorFail()
                }
                ToastHelper.showCenterToast(e?.errorMsg)
                mView.onLoadError(page)
            }
        })
    }

    private lateinit var mView: ITearmAwardConstract.ITearmAwardView

    private var mModel = TearmAwardModel()

    override fun setView(view: ITearmAwardConstract.ITearmAwardView) {
        this.mView = view
        mModel.setView(view)
    }

    override fun onViewCreate() {

    }

    override fun onViewDestroy() {
        mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }
}