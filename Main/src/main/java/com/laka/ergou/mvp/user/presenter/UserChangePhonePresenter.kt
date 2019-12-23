package com.laka.ergou.mvp.user.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.ergou.common.util.rx.RxSubscriber
import com.laka.ergou.mvp.login.model.bean.VerificationCodeData
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.contract.IUserChangePhoneContract
import com.laka.ergou.mvp.user.model.responsitory.UserChangePhoneModel
import io.reactivex.disposables.Disposable
import org.json.JSONObject

/**
 * @Author:Rayman
 * @Date:2019/1/19
 * @Description:用户模块---更改手机号Presenter层
 */
class UserChangePhonePresenter : IUserChangePhoneContract.IUserChangePhonePresenter {

    private var mView: IUserChangePhoneContract.IUserChangePhoneView? = null
    private val mModel: IUserChangePhoneContract.IUserChangePhoneModel = UserChangePhoneModel()
    private var mDisposableList: ArrayList<Disposable> = ArrayList()

    override fun setView(view: IUserChangePhoneContract.IUserChangePhoneView) {
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

    override fun getVerifyCode(phone: String) {
        mView?.showLoading()
        mModel?.getVerifyCode(phone)
                .subscribe(object : RxSubscriber<VerificationCodeData, IUserChangePhoneContract.IUserChangePhoneView>(mView) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }

                    override fun onNext(t: VerificationCodeData) {
                        super.onNext(t)
                        mView?.onGetVerificationCodeSuccess(t)
                    }
                })
    }

    override fun changePhoneCommit(phone: String, code: String) {
        mView?.showLoading()
        mModel?.changePhoneCommit(phone, code)
                .subscribe(object : RxSubscriber<JSONObject, IUserChangePhoneContract.IUserChangePhoneView>(mView) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }

                    override fun onNext(t: JSONObject) {
                        super.onNext(t)
                        mView?.onChangePhoneSuccess()
                        // 更新用户信息
                        UserUtils.updateUserPhone(phone)
                    }
                })
    }
}