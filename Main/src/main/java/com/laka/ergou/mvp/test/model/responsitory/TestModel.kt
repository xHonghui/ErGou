package com.laka.ergou.mvp.test.model.responsitory

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.shop.model.bean.ImageDetail
import com.laka.ergou.mvp.test.constract.ITestContract
import io.reactivex.disposables.Disposable

/**
 * @Author:summer
 * @Date:2018/12/20
 * @Description:
 */
class TestModel : ITestContract.ITestModel {

    lateinit var mView: ITestContract.ITestView
    private val mDisposableList = java.util.ArrayList<Disposable>()

    override fun setView(v: ITestContract.ITestView) {
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

    override fun onLoadShopDetailImage(params: HashMap<String, String>, callBack: ResponseCallBack<ArrayList<ImageDetail>>) {
        TestRetrofitHelper
                .instance
                .onLoadShopDetailImage(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<ArrayList<ImageDetail>, ITestContract.ITestView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}