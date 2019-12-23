package com.laka.ergou.mvp.chat.model.repository

import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.chat.contract.IChatContract
import com.laka.ergou.mvp.chat.model.bean.Message
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/2/14
 * @Description:购小二---聊天Model
 */
class ChatModel : IChatContract.IChatModel {

    private lateinit var mView: IChatContract.IChatView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: IChatContract.IChatView) {
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

    override fun onGetHistoryMessages(hashMap: HashMap<String, String>, callBack: ResponseCallBack<Message>) {
        ChatRetrofixHelper.instance.onGetHistoryMessages(hashMap)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<Message, IChatContract.IChatView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}