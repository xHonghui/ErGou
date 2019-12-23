package com.laka.ergou.mvp.user.model.responsitory

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.user.contract.IInvitationPlaybillConstract
import com.laka.ergou.mvp.user.contract.IRewardListConstract
import com.laka.ergou.mvp.user.model.bean.RewardListResponse
import io.reactivex.disposables.Disposable

/**
 * @Author:summer
 * @Date:2019/6/26
 * @Description:
 */
class RewardListModel : IRewardListConstract.IRewardListModel {

    private lateinit var mView: IRewardListConstract.IRewardListView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: IRewardListConstract.IRewardListView) {
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

    override fun onLoadRewardListData(params: HashMap<String, String>, callBack: ResponseCallBack<RewardListResponse>) {
        UserCustomRetrofitHelper.instance
                .onLoadRewardListData(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object :RxCustomSubscriber<RewardListResponse,IRewardListConstract.IRewardListView>(mView,callBack){
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}