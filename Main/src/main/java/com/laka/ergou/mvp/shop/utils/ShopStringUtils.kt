package com.laka.ergou.mvp.shop.utils

import android.text.TextUtils
import java.util.regex.Pattern

/**
 * @Author:summer
 * @Date:2019/1/16
 * @Description:
 */
object ShopStringUtils {

    fun onMatchStr(str: String?): String {
        var group = "0"
        try {
            if (TextUtils.isEmpty(str)) {
                return group
            }
            var regix = "[减](\\d+)[元]"
            var pattern = Pattern.compile(regix)
            var matcher = pattern.matcher("$str")
            if (matcher.find()) {
                group = matcher.group()
            }
            regix = "(\\d+)"
            pattern = Pattern.compile(regix)
            matcher = pattern.matcher("$group")
            return if (matcher.find()) {
                group = matcher.group()
                group
            } else {
                "0"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return group
    }

}