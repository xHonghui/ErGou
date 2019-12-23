package com.laka.ergou.mvp.armsteam.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.armsteam.contract.MyComradeArmsLowerConstract
import com.laka.ergou.mvp.armsteam.model.bean.MyArmsResponse
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/5/24
 * @Description:
 */
class MyComradeArmsLowerModel : MyComradeArmsLowerConstract.IMyComradeArmsLowerModel {

    private lateinit var mView: MyComradeArmsLowerConstract.IMyComradeArmsLowerView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: MyComradeArmsLowerConstract.IMyComradeArmsLowerView) {
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

    override fun onLoadComradeArmsLowerData(params: HashMap<String, String>, callback: ResponseCallBack<MyArmsResponse>) {
        MyArmsLevelsRetrofixHelper.instance
                .onLoadMyArmsData(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<MyArmsResponse, MyComradeArmsLowerConstract.IMyComradeArmsLowerView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}