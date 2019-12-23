package com.laka.ergou.mvp.armsteam.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.armsteam.contract.MyArmsLevelsContract
import com.laka.ergou.mvp.armsteam.model.bean.MyArmsResponse
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/3/8
 * @Description:我的战队
 */
class MyArmsLevelsModel : MyArmsLevelsContract.IMyLowerLevelsModel {
    private lateinit var mView: MyArmsLevelsContract.IMyLowerLevelsView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: MyArmsLevelsContract.IMyLowerLevelsView) {
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

    //===================================== 上级接口实现 ===========================================
    override fun onLoadMyArmsLevelsData(params: HashMap<String, String>, callBack: ResponseCallBack<MyArmsResponse>) {
        MyArmsLevelsRetrofixHelper.instance
                .onLoadMyArmsData(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<MyArmsResponse, MyArmsLevelsContract.IMyLowerLevelsView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}