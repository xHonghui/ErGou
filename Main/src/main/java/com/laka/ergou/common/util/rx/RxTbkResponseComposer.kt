package com.laka.ergou.common.util.rx

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.exception.ApiException
import com.laka.ergou.mvp.main.model.bean.TaoBaoResponse
import io.reactivex.*

/**
 * @Author:Rayman
 * @Date:2019/1/19
 * @Description:Rx淘宝客Response统一处理
 */
class RxTbkResponseComposer {
    companion object {

        /**
         * description:统一处理淘宝客响应体，返回正确的数据（正确的数据列表与错误的响应体）
         * 两个参数的说明：Response层数据和真实数据
         * 因为业务逻辑的需求，这边需要拿到的是TBKResponse的数据。
         * 当前的实现只是针对主页列表的处理
         **/
        fun <T> flatMap(): ObservableTransformer<TaoBaoResponse<T>, T> {
            return ObservableTransformer { upstream ->
                val composeObservable = upstream.compose(RxSchedulerComposer.normalSchedulersTransformer())
                        .flatMap {
                            val code = it?.response.code
                            val data = it?.response
                            val errorMsg = it?.response.errorMsg
                            if (it?.response?.code == 0) {
                                return@flatMap createData(data)
                            } else {
                                return@flatMap Observable.error<T>(ApiException(code, errorMsg))
                            }
                        }
                composeObservable as ObservableSource<T>
            }
        }

        /**
         * description:重组正常数据
         **/
        private fun <T> createData(data: T): ObservableSource<*> {
            return Observable.create(ObservableOnSubscribe<T> { e ->
                e.onNext(data)
                e.onComplete()
            })
        }
    }
}