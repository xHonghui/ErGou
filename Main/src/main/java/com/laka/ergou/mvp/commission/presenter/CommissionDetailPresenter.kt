package com.laka.ergou.mvp.commission.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.commission.constant.CommissionConstant
import com.laka.ergou.mvp.commission.constract.ICommissionDetailConstract
import com.laka.ergou.mvp.commission.model.bean.CommissionDetailBean
import com.laka.ergou.mvp.commission.model.bean.CommissionDetailData
import com.laka.ergou.mvp.commission.model.repository.CommissionDetailModel

/**
 * @Author:summer
 * @Date:2019/1/18
 * @Description:
 */
class CommissionDetailPresenter : ICommissionDetailConstract.ICommissionDetailPresenter {

    private lateinit var mView: ICommissionDetailConstract.ICommissionDetailView
    private var mModel: ICommissionDetailConstract.ICommissionDetailModel = CommissionDetailModel()

    override fun setView(view: ICommissionDetailConstract.ICommissionDetailView) {
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

    override fun onLoadCommissionDetailData(page: Int) {
        val params = HashMap<String, String>()
        params[CommissionConstant.PAGE] = page.toString()
        params[CommissionConstant.PAGE_SIZE_KEY] = CommissionConstant.PAGE_SIZE
        mModel.onLoadCommissionDetailData(params, object : ResponseCallBack<CommissionDetailBean> {
            override fun onSuccess(t: CommissionDetailBean) {
                mView.onLoadCommissionDetailDataSuccess(object : BaseListBean<CommissionDetailData>() {
                    override fun getList(): MutableList<CommissionDetailData> {
                        return t.data
                    }

                    override fun getPageTotalCount(): Int {
                        return t.last_page
                    }
                })
            }

            override fun onFail(e: BaseException?) {
                mView.onLoadError()
            }
        })
    }

}