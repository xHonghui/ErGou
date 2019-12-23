package com.laka.ergou.common.dsl

import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException

/**
 * Created by aa on 2019-01-25.
 * @auto sming
 */
class RequestWrapper<T> {

    private val paramsContext = ParamsContext()
    lateinit var callBack: ResponseCallBack<T>
    var onSuccess: ((t: T) -> Unit)? = null
    var onFail: ((e: BaseException) -> Unit)? = null

    infix fun onSuccess(onSuccess: ((T) -> Unit)?) {
        this.onSuccess = onSuccess
    }

    infix fun onFail(onFail: ((e: BaseException) -> Unit)?) {
        this.onFail = onFail
    }

    fun callBack(init: ResponseCallBack<T>.() -> Unit) {
        callBack.init()
    }

    fun params(init: ParamsContext.() -> Unit) {

        paramsContext.init()
    }

    internal fun getParams() = paramsContext.getMap()
}

// 定义一个ParamsContext类，用于参数的封装。请求的参数是key-value的形式。
class ParamsContext {

    private val map: MutableMap<String, Any> = mutableMapOf()

    infix fun String.to(v: Any) {
        map[this] = v
    }

    fun getMap(): MutableMap<String, Any> {
        return map
    }
}

fun <T> request(init: RequestWrapper<T>.() -> Unit): RequestWrapper<T> {

    val wrap = RequestWrapper<T>()

    wrap.init()

    execute(wrap)
    return wrap
}

private fun <T> execute(wrap: RequestWrapper<T>) {
    wrap.callBack = object : ResponseCallBack<T> {
        override fun onFail(e: BaseException) {
            wrap.onFail?.let {
                it(e)
            }
        }

        override fun onSuccess(t: T) {
            wrap.onSuccess?.let {
                it(t)
            }
        }
    }
}