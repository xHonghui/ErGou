package com.laka.ergou.mvp.order.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.constant.RequestCommonCode
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.order.constant.MyOrderConstant
import com.laka.ergou.mvp.order.constract.IMyOrderConstract
import com.laka.ergou.mvp.order.model.respository.MyOrderModel
import com.laka.ergou.mvp.order.model.bean.OrderDataBean
import com.laka.ergou.mvp.order.model.bean.OrderListBean

/**
 * @Author:summer
 * @Date:2019/1/19
 * @Description:
 */
class MyOrderPresenter : IMyOrderConstract.IMyOrderPresenter {

    private lateinit var mView: IMyOrderConstract.IMyOrderView
    private var mModel: IMyOrderConstract.IMyOrderModel = MyOrderModel()

    override fun onViewCreate() {

    }

    override fun onViewDestroy() {
        mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun setView(view: IMyOrderConstract.IMyOrderView) {
        this.mView = view
        mModel.setView(mView)
    }

    override fun onLoadMyOrderData(type: Int, sourceType: Int, page: Int) {
        val params = HashMap<String, String>()
        params[MyOrderConstant.TYPE] = "$type"
        params[MyOrderConstant.PAGE_NUMER] = "$page"
        params[MyOrderConstant.SOURCE_TYPE] = "$sourceType"
        params[MyOrderConstant.PAGE_SIZE] = MyOrderConstant.SIZE
        mModel.onLoadMyOrderData(params, object : ResponseCallBack<OrderListBean> {
            override fun onSuccess(t: OrderListBean) {
                mView.onLoadMyOrderDataSuccess(object : BaseListBean<OrderDataBean>() {
                    override fun getList(): MutableList<OrderDataBean> {
                        return t.data
                    }

                    override fun getPageTotalCount(): Int {
                        return t.last_page
                    }
                }, t.total)
            }

            override fun onFail(e: BaseException?) {
                if (e?.code == RequestCommonCode.LK_WRONG_USER_TOKEN || e?.code == RequestCommonCode.LK_NOT_LOGIN) {
                    mView.onAuthorFail()
                }
                mView.onLoadError(page)
            }
        })
    }

}