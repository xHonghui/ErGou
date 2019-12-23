package com.laka.ergou.mvp.message.model.respository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.message.constract.MessageConstract
import com.laka.ergou.mvp.message.model.bean.MessageResponse
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/3/8
 * @Description:
 */
class MessageModel : MessageConstract.IMessageModel {

    private lateinit var mView: MessageConstract.IMessageView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: MessageConstract.IMessageView) {
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

    override fun onLoadMessage(params: HashMap<String, String>, callBack: ResponseCallBack<MessageResponse>) {
        MyMessageRetrofixHelper.instance
                .onLoadMessage(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<MessageResponse, MessageConstract.IMessageView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}