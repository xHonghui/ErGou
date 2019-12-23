package com.laka.ergou.common.dsl


/**
 * Created by aa on 2019-01-19.
 * @auto sming
 */
class ParamsMap {
    private val map: MutableMap<String, Any> = mutableMapOf()

    infix fun String.to(v: Any) {
        map[this] = v
    }

    fun getParams(): MutableMap<String, Any> {
        return map
    }

}

fun params(init: ParamsMap.() -> Unit): MutableMap<String, Any> {
    val paramsUtils = ParamsMap()
    paramsUtils.init()
    return paramsUtils.getParams()
}

