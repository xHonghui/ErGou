package com.laka.ergou.mvp.login.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.login.contract.IBindInvitationCodeConstract
import com.laka.ergou.mvp.login.model.bean.AgentSimpleInfo
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/3/9
 * @Description:
 */
class BindInvitationCodeModel : IBindInvitationCodeConstract.IBindInvitationCodeModel {

    private lateinit var mView: IBindInvitationCodeConstract.IBindInvitationCodeView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: IBindInvitationCodeConstract.IBindInvitationCodeView) {
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

    override fun onLoadUserAgentInfo(params: HashMap<String, String>, callBack: ResponseCallBack<AgentSimpleInfo>) {
        CustomLoginRetrofixHelper.instance
                .onLoadAgentInfo(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<AgentSimpleInfo, IBindInvitationCodeConstract.IBindInvitationCodeView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun onBindSuperAgent(params: HashMap<String, String>, callBack: ResponseCallBack<JSONObject>) {
        CustomLoginRetrofixHelper.instance
                .onBindSuperAgent(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<JSONObject, IBindInvitationCodeConstract.IBindInvitationCodeView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}