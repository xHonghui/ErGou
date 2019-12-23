package com.laka.ergou.mvp.user.model.responsitory

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.user.contract.IInvitationPlaybillConstract
import io.reactivex.disposables.Disposable

/**
 * @Author:summer
 * @Date:2019/6/4
 * @Description:
 */
class InvitationPlaybillModel : IInvitationPlaybillConstract.IInvitationPlaybillModel {

    private lateinit var mView: IInvitationPlaybillConstract.IInvitationPlaybillView
    private val mDisposableList = java.util.ArrayList<Disposable>()

    override fun setView(v: IInvitationPlaybillConstract.IInvitationPlaybillView) {
        this.mView = v
    }

    override fun onViewDestory() {
        mDisposableList.forEach {
            if (!it.isDisposed){
                it.dispose()
            }
        }
        mDisposableList.clear()
    }

    override fun onLoadSharePosterData(params: HashMap<String, String>, callBack: ResponseCallBack<ArrayList<String>>) {
        UserCustomRetrofitHelper.instance
                .onLoadSharePosterData(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<ArrayList<String>, IInvitationPlaybillConstract.IInvitationPlaybillView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}