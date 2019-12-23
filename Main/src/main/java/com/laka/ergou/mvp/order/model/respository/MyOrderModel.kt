package com.laka.ergou.mvp.order.model.respository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.order.constract.IMyOrderConstract
import com.laka.ergou.mvp.order.model.bean.OrderListBean
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/1/19
 * @Description:
 */
class MyOrderModel : IMyOrderConstract.IMyOrderModel {

    private lateinit var mView: IMyOrderConstract.IMyOrderView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: IMyOrderConstract.IMyOrderView) {
        this.mView = v
    }

    override fun onViewDestory() {
        mDisposableList.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposableList.clear()
    }

    override fun onLoadMyOrderData(params: HashMap<String, String>, callBack: ResponseCallBack<OrderListBean>) {
        MyOrderRetrofixHelper.instance
                .onLoadMyOrderData(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<OrderListBean, IMyOrderConstract.IMyOrderView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}