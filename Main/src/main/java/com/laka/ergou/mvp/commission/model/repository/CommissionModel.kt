package com.laka.ergou.mvp.commission.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.commission.constract.ICommissionConstract
import com.laka.ergou.mvp.commission.model.bean.CommissionBean
import com.laka.ergou.mvp.commission.model.bean.CommissionNewBean
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/1/18
 * @Description:
 */
class CommissionModel : ICommissionConstract.IBaseCommissonModel {


    private lateinit var mView: ICommissionConstract.IBaseCommissonView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: ICommissionConstract.IBaseCommissonView) {
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

    override fun onLoadMyCommissonData(params: HashMap<String, String>, callBack: ResponseCallBack<CommissionBean>) {
        CommissionRetrofixHelper.instance.onLoadMyCommissonData(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<CommissionBean, ICommissionConstract.IBaseCommissonView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun onLoadWithdrawal(params: HashMap<String, String>, callBack: ResponseCallBack<JSONObject>) {
        CommissionRetrofixHelper.instance.onLoadWithdrawal(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<JSONObject, ICommissionConstract.IBaseCommissonView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
    override fun onGetMyCommissonData(params: HashMap<String, String>, callBack: ResponseCallBack<CommissionNewBean>) {
        CommissionRetrofixHelper.instance.onGetMyCommissonData(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<CommissionNewBean, ICommissionConstract.IBaseCommissonView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}