package com.laka.ergou.mvp.circle.constant

import com.laka.ergou.BuildConfig

/**
 * @Author:sming
 * @Date:2019/8/8
 * @Description:
 */
interface CircleConstant {

    companion object {
        const val CIRCLE_BASE_HOSTL: String = BuildConfig.ERGOU_BASE_HOST
        const val API_GET_CATEGORY_LIST = "circle/category-list"
        const val API_GET_ARTICLE_LIST = "circle/article-list"
        const val API_GET_CIRCLE_COMMENT = "product/tkl"
        const val API_SEND_CIRCLE = "circle/send"
        const val CATEGORY_ID = "category_id"
        const val CATEGORY_POSITION = "category_position"
        const val CATEGORY_POSITION2 = "category_position2"
        const val PAGE_SIZE = "20"
        const val JS_ACTION_ONE_KEY_SEND = "wx_one_key_send"
        const val JS_ACTION_SET_TITLE = "wx_set_title"
        const val TITLE = "title"
        const val TYPE_LOGIN = "type_login"
        const val TKL_MATCHER = "{淘口令}"

        //发圈页面进入
        const val BUTLER_REQUEST_CODE_FOR_CIRCLE: Int = 0x0815
        //个人中心页面进入
        const val BUTLER_REQUEST_CODE_FOR_USERCENTER: Int = 0x0816
        //个人中心进入的requestCode key
        const val BUTLER_RESULT_CODE_FOR_USERCENTER: Int = 0x0817
    }

}