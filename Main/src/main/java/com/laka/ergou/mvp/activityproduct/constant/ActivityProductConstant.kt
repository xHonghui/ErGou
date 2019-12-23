package com.laka.ergou.mvp.activityproduct.constant

import com.laka.ergou.BuildConfig

/**
 * @Author:summer
 * @Date:2019/8/5
 * @Description:
 */
object ActivityProductConstant {
    const val ACTIVITY_ROOT_URL: String = BuildConfig.ERGOU_BASE_HOST
    const val API_ACTIVITY_PRODUCT: String = "activity/detail"
    const val KEY_BANNER_URL: String = "KEY_BANNER_URL"
    const val KEY_ACTIVITY_ID: String = "KEY_ACTIVITY_ID"
    const val KEY_TITLE: String = "KEY_TITLE"


    /**
     * type
     * */
    const val TYPE_USER_TAOBAO: String = "0"
    const val TYPE_USER_TMALL: String = "1"
}