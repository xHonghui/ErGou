package com.laka.ergou.mvp.commission.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.commission.constract.ICommissionDetailConstract
import com.laka.ergou.mvp.commission.model.bean.CommissionDetailBean
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/1/18
 * @Description:
 */
class CommissionDetailModel : ICommissionDetailConstract.ICommissionDetailModel {

    private lateinit var mView: ICommissionDetailConstract.ICommissionDetailView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: ICommissionDetailConstract.ICommissionDetailView) {
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

    override fun onLoadCommissionDetailData(params: HashMap<String, String>, callBack: ResponseCallBack<CommissionDetailBean>) {
        CommissionRetrofixHelper.instance.onLoadCommissionDetailData(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<CommissionDetailBean, ICommissionDetailConstract.ICommissionDetailView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}