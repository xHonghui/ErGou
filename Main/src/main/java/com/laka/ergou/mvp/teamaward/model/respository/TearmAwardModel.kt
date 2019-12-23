package com.laka.ergou.mvp.teamaward.model.respository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.teamaward.constract.ITearmAwardConstract
import com.laka.ergou.mvp.teamaward.model.bean.TearmAwardBean
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import java.util.*

class TearmAwardModel :ITearmAwardConstract.ITaearmAwardModel{
    override fun onGetComradeSubsidy(params: HashMap<String, String>, callBack: ResponseCallBack<TearmAwardBean>) {
        TearmAwardRetrofixHelper.instance.onGetComradeSubsidy(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<TearmAwardBean, ITearmAwardConstract.ITearmAwardView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    private lateinit var mView: ITearmAwardConstract.ITearmAwardView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: ITearmAwardConstract.ITearmAwardView) {
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

}