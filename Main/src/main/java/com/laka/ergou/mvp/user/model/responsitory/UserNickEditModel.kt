package com.laka.ergou.mvp.user.model.responsitory

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.user.contract.IUserNickEditConstract
import com.laka.ergou.mvp.user.model.bean.CommonData
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/1/11
 * @Description:
 */
class UserNickEditModel : IUserNickEditConstract.IUserNickEditModel {

    private lateinit var mView: IUserNickEditConstract.IUserNickEditView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: IUserNickEditConstract.IUserNickEditView) {
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

    override fun onUserNickEdit(params: HashMap<String, String?>, callback: ResponseCallBack<CommonData>) {
        UserCustomRetrofitHelper.instance
                .onUpdateUserNickOrGenderData(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<CommonData, IUserNickEditConstract.IUserNickEditView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}