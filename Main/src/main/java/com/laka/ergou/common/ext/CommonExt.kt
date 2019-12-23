package com.laka.ergou.common.ext

import android.view.View
import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseView
import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.circle.constract.ICircleConstract
import com.laka.ergou.mvp.circle.model.bean.CircleCommentResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/*
    扩展点击事件，参数为方法
 */
fun View.onClick(method: () -> Unit): View {
    setOnClickListener { method() }
    return this
}


fun <T> Observable<T>.excute(add: ArrayList<Disposable>, callBack: ResponseCallBack<T>) {
    this.compose(RxSchedulerComposer.normalSchedulersTransformer())
            .subscribe(object : RxCustomSubscriber<T, ICircleConstract.IBaseCircleView>(null, callBack) {
                override fun onSubscribe(d: Disposable) {
                    super.onSubscribe(d)
                    add.add(d)
                }
            })
}


fun <T> MutableList<T>.convertRefresh(page: Int): BaseListBean<T> {
    var list = this
    return object : BaseListBean<T>() {
        override fun getList(): MutableList<T> {
            return list
        }

        override fun getPageTotalCount(): Int {
            return if (list.size > 0) {
                page + 1
            } else {
                page
            }
        }
    }
}