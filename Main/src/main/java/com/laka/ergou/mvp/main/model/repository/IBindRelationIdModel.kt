package com.laka.ergou.mvp.main.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.main.contract.IBindRelationIdContract
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.UrlResponse
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * @Author:summer
 * @Date:2019/8/14
 * @Description:
 */
class IBindRelationIdModel : IBindRelationIdContract.IBindRelationIdModel {

    private val mDisposableList = ArrayList<Disposable>()
    private lateinit var mView: IBindRelationIdContract.IBindRelationIdView

    override fun setView(v: IBindRelationIdContract.IBindRelationIdView) {
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

    override fun getUnionCodeUrl(params: HashMap<String, String>, callback: ResponseCallBack<UrlResponse>) {
        HomeRetrofitHelper.newProductInstance()
                .getUnionCodeUrl(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<UrlResponse, IBindRelationIdContract.IBindRelationIdView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun handleUnionCode(params: HashMap<String, String>, callback: ResponseCallBack<RelationResponse>) {
        HomeRetrofitHelper.newProductInstance()
                .handleUnionCode(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<RelationResponse, IBindRelationIdContract.IBindRelationIdView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}