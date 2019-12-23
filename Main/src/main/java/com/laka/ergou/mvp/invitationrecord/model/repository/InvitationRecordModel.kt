package com.laka.ergou.mvp.invitationrecord.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.invitationrecord.constract.InvitationRecordConstract
import com.laka.ergou.mvp.invitationrecord.model.bean.InvitationRecordResponse
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/3/13
 * @Description:
 */
class InvitationRecordModel : InvitationRecordConstract.IInvitationRecordModel {

    private lateinit var mView: InvitationRecordConstract.IInvitationRecordView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: InvitationRecordConstract.IInvitationRecordView) {
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

    override fun onLoadInvitationRecord(params: HashMap<String, String>, callBack: ResponseCallBack<InvitationRecordResponse>) {
        InvitationRecordRetrofixHelper.instance
                .onLoadInvitationRecoedData(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<InvitationRecordResponse, InvitationRecordConstract.IInvitationRecordView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })

    }
}