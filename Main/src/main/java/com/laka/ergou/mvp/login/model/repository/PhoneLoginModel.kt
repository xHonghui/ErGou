package com.laka.ergou.mvp.login.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.login.contract.IPhoneLoginContract
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.login.model.bean.VerificationCodeDataBean
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/1/7
 * @Description: 手机登录
 */
class PhoneLoginModel : IPhoneLoginContract.ILoginModel {

//    override fun onLogin(paramesMap: HashMap<String, String>): Observable<UserBean> {
//        return PhoneLoginRetrofitHelper.instance.onPhoneLogin(paramesMap).compose(RxSchedulerComposer.normalSchedulersTransformer())
//    }
private val mDisposableList = ArrayList<Disposable>()

    private lateinit var mView: IPhoneLoginContract.ILoginView

    override fun setView(v: IPhoneLoginContract.ILoginView) {
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

    override fun onLogin(params: HashMap<String, String>, callBack: ResponseCallBack<UserBean>) {
        CustomLoginRetrofixHelper.instance
                .onPhoneLogin(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<UserBean, IPhoneLoginContract.ILoginView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun onGetVerificationCode(parames: HashMap<String, String>): Observable<VerificationCodeDataBean> {
        return PhoneLoginRetrofitHelper.instance.onGetVerificationCode(parames).compose(RxSchedulerComposer.normalSchedulersTransformer())
    }
}