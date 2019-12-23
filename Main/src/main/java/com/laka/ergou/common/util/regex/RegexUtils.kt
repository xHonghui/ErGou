package com.laka.ergou.common.util.regex

import android.text.TextUtils
import com.laka.androidlib.util.LogUtils
import java.util.regex.Pattern

/**
 * @Author:summer
 * @Date:2019/2/19
 * @Description:各种字符串匹配 utils
 */
object RegexUtils {

    /**
     * 根据正则表达式和源字符串获取目标字符串
     * @sourceStr:源字符串
     * @regex:正则表达式
     * @group:需要获取的组，默认0，根据具体的正则表达式来确定目标str 所在第几组
     * 例如：code=([a-zA-Z0-9]+)，我需要获取小括号内部的匹配数据，"code="不需要，则传入的组为 1
     * "code=" ：组一  "([a-zA-Z0-9]+)" :组二
     * */
    @JvmStatic
    fun findTergetStrForRegex(sourceStr: String, regex: String, group: Int = 0): String {
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(sourceStr)
        if (matcher.find()) {
            return matcher.group(group)
        }
        return ""
    }

    @JvmStatic
    fun findTergetStrForRegex(sourceStr: String, regex: String): ArrayList<String> {
        val list = ArrayList<String>()
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(sourceStr)
        if (matcher.find()) {
            list.add(matcher.group())
        }
        return list
    }

    /**
     * 从url中解析出所有参数
     * */
    @JvmStatic
    fun findParamsForUrl(url: String): HashMap<String, String> {
        if (TextUtils.isEmpty(url)) return HashMap()
        val index = url.indexOf("?")
        if (index < 0 || index >= url.length - 1) return HashMap()

        val params = HashMap<String, String>()
        val paramsStr = url.substring(index + 1)
        val array = paramsStr.split("&")
        array.forEach {
            val keyValue = it.split("=")
            if (keyValue.size > 1) {
                params[keyValue[0]] = keyValue[1]
            }
        }
        return params
    }

}