package com.laka.ergou.mvp.login.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.login.contract.IWechatLoginContract
import com.laka.ergou.mvp.login.model.bean.UserBean
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/3/9
 * @Description:微信授权
 */
class WechatAuthorModel : IWechatLoginContract.IWechatAuthorModel {

    private lateinit var mView: IWechatLoginContract.IWechatAuthorView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: IWechatLoginContract.IWechatAuthorView) {
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

    override fun onWechatAuthor(params: HashMap<String, String>, callBack: ResponseCallBack<UserBean>) {
        CustomLoginRetrofixHelper.instance
                .onWechatAuthor(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<UserBean, IWechatLoginContract.IWechatAuthorView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    //手机一键登录
    override fun onLoginTokenVerify(params: HashMap<String, String>, callBack: ResponseCallBack<UserBean>) {
        CustomLoginRetrofixHelper.instance
                .onLoginTokenVerify(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<UserBean, IWechatLoginContract.IWechatAuthorView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

}