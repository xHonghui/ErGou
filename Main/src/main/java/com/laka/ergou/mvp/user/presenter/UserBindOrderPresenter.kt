package com.laka.ergou.mvp.user.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.ergou.common.util.rx.RxSubscriber
import com.laka.ergou.mvp.user.contract.IUserBindOrderContract
import com.laka.ergou.mvp.user.model.responsitory.UserBindOrderModel
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import java.util.*

/**
 * @Author:Rayman
 * @Date:2019/1/17
 * @Description:用户模块---绑定订单Presenter实现层
 */
class UserBindOrderPresenter : IUserBindOrderContract.IUserBindOrderPresenter {

    private var mView: IUserBindOrderContract.IUserBindOrderView? = null
    private var mModel: IUserBindOrderContract.IUserBindOrderModel = UserBindOrderModel()
    private var mDisposableList: ArrayList<Disposable> = ArrayList()

    override fun setView(view: IUserBindOrderContract.IUserBindOrderView) {
        this.mView = view
    }

    override fun onViewCreate() {
    }

    override fun onViewDestroy() {
        mDisposableList?.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposableList.clear()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun bindOrder(orderId: String) {
        mView?.showLoading()
        mModel?.bindOrder(orderId)
                .subscribe(
                        object : RxSubscriber<JSONObject, IUserBindOrderContract.IUserBindOrderView>(mView) {
                            override fun onSubscribe(d: Disposable) {
                                super.onSubscribe(d)
                                mDisposableList.add(d)
                            }

                            override fun onNext(t: JSONObject) {
                                super.onNext(t)
                                mView?.bindOrderSuccess("订单绑定成功，请在【我的订单】页面查看！")
                            }
                        })
    }
}
