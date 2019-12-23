package com.laka.ergou.mvp.activityproduct.model.respository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.activityproduct.constract.ActivityProductConstract
import com.laka.ergou.mvp.activityproduct.model.bean.ActivityProductResponse
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:
 */
class ActivityProductModel : ActivityProductConstract.IActivityProductModel {

    private lateinit var mView: ActivityProductConstract.IActivityProductView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: ActivityProductConstract.IActivityProductView) {
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

    override fun onLoadActivityProductList(params: HashMap<String, String>, callBack: ResponseCallBack<ActivityProductResponse>) {
        ActivityProductRetrofixHelper.instance
                .onLoadActivityProductList(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<ActivityProductResponse, ActivityProductConstract.IActivityProductView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}