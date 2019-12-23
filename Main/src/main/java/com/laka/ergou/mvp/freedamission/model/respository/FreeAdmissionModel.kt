package com.laka.ergou.mvp.freedamission.model.respository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.freedamission.constract.FreeAdmissionConstract
import com.laka.ergou.mvp.freedamission.model.bean.FreeAdmissionResponse
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:
 */
class FreeAdmissionModel : FreeAdmissionConstract.IFreeAdmissionModel {

    private lateinit var mView: FreeAdmissionConstract.IFreeAdmissionView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: FreeAdmissionConstract.IFreeAdmissionView) {
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

    override fun onLoadFreeAdmissionProductList(params: HashMap<String, String>, callBack: ResponseCallBack<FreeAdmissionResponse>) {
        FreeAdmissionRetrofixHelper.instance
                .onLoadFreeAdmissionProductList(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<FreeAdmissionResponse, FreeAdmissionConstract.IFreeAdmissionView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}