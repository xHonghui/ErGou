package com.laka.ergou.mvp.login.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.login.contract.ILoginContract
import com.laka.ergou.mvp.login.model.bean.UserBean
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/3/9
 * @Description:(微信登录/用户注册)
 */
class LoginModel : ILoginContract.ILoginModel {

    private lateinit var mView: ILoginContract.ILoginView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: ILoginContract.ILoginView) {
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

    override fun onWechatLogin(params: HashMap<String, String>, callBack: ResponseCallBack<UserBean>) {
        CustomLoginRetrofixHelper.instance
                .onWechatLogin(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<UserBean, ILoginContract.ILoginView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun onUserRegister(params: HashMap<String, String>, callBack: ResponseCallBack<UserBean>) {
        CustomLoginRetrofixHelper.instance
                .onUserRegister(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<UserBean, ILoginContract.ILoginView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun onGetVerificationCode(params: HashMap<String, String>, callBack: ResponseCallBack<JSONObject>) {
        CustomLoginRetrofixHelper.instance
                .onGetVerificationCodeForLogin(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<JSONObject, ILoginContract.ILoginView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun onPhoneLogin(params: HashMap<String, String>, callBack: ResponseCallBack<UserBean>) {
        CustomLoginRetrofixHelper.instance
                .onPhoneLogin(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<UserBean, ILoginContract.ILoginView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}