package com.laka.ergou.mvp.user.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.ergou.common.util.rx.RxSubscriber
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.contract.IUserBindAliAccountContract
import com.laka.ergou.mvp.user.model.bean.CommonData
import com.laka.ergou.mvp.user.model.responsitory.UserBindAliAccountModel
import io.reactivex.disposables.Disposable

/**
 * @Author:Rayman
 * @Date:2019/1/21
 * @Description:用户模块---绑定阿里账号Presenter实现层
 */
class UserBindAliAccountPresenter : IUserBindAliAccountContract.IUserBindAliAccountPresenter {

    private var mView: IUserBindAliAccountContract.IUserBindAliAccountView? = null
    private val mModel: IUserBindAliAccountContract.IUserBindAliAccountModel = UserBindAliAccountModel()
    private var mDisposableList: ArrayList<Disposable> = ArrayList()

    override fun setView(view: IUserBindAliAccountContract.IUserBindAliAccountView) {
        mView = view
    }

    override fun onViewCreate() {
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun onViewDestroy() {
        mDisposableList?.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposableList.clear()
    }

    override fun bindAliAccount(realName: String, aliAccount: String) {
        mView?.showLoading()
        mModel?.bindAliAccount(realName, aliAccount)
                .subscribe(object : RxSubscriber<CommonData, IUserBindAliAccountContract.IUserBindAliAccountView>(mView) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }

                    override fun onNext(t: CommonData) {
                        super.onNext(t)
                        mView?.bindAliAccountSuccess("绑定成功")
                        // 更新阿里账号
                        t?.let {
                            UserUtils.updateBindAliAccount(aliAccount, realName)
                        }
                    }
                })
    }
}