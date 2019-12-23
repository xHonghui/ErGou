package com.laka.ergou.mvp.armsteam.constant

import com.laka.ergou.BuildConfig
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:常量管理类
 */
object MyArmsLevelsConstant {
    private const val MY_LEVELS_FIRST_PAGE: String = "一级战友\n（0）"
    private const val MY_LEVELS_SECOND_PAGE: String = "二级战友\n（0）"
    private const val MY_LEVELS_THIRD_PAGE: String = "军团战友\n（0）"
    /**
     * 战友等级
     * */
    const val MY_ARMS_LEVELS_FIRST: String = "10"
    const val MY_ARMS_LEVELS_SECOND: String = "20"
    const val MY_ARMS_LEVELS_THIRD: String = "30"

    /*我的战友（微信战友、app战友）*/
    val MY_LOWER_LEVELS_TITLE: ArrayList<String> = arrayListOf(MY_LEVELS_FIRST_PAGE, MY_LEVELS_SECOND_PAGE, MY_LEVELS_THIRD_PAGE)
    const val MY_LOWER_LEVELS_URL: String = "user/sub-agent"  //我的战友
    const val MY_ARMS_LEVELS_URL: String = "user/war-team"  //我的战队
    const val MY_LOWER_LEVELS_BASE_HOSTL: String = BuildConfig.ERGOU_BASE_HOST
    const val MY_LOWER_LEVELS_PAGE_TYPE: String = "MY_LOWER_LEVELS_PAGE_TYPE"
    const val MY_COMRADE_ARMS_LEVELS_TYPE: String = "MY_COMRADE_ARMS_LEVELS_TYPE"
    const val TYPE_APP: Int = 1
    const val TYPE_WECHAT: Int = 2
    const val TYPE_LOWER_PAGE: String = "type"
    const val PAGE_SIZE_KEY: String = "pageSize"
    const val PAGE_SIZE: String = "20"
    const val TOKEN: String = "token"
    const val PAGE_KEY: String = "page"
    const val SOURCE_TYPE: String = "SOURCE_TYPE"
    const val PAGE_TYPE: String = "PAGE_TYPE"
    const val MY_LOWER_PAGE_FIRST: Int = 1
    const val MY_LOWER_PAGE_SECOND: Int = 2
    const val MY_LOWER_PAGE_THIRD: Int = 3
    const val MY_COMRADE_ARMS_LOWER_ID: String = "MY_COMRADE_ARMS_LOWER_ID"
    const val ARMS_LEVELS_TYPE: String = "class_type"
    const val KEY_MY_ARMS_ID: String = "KEY_MY_ARMS_ID"
    const val KEY_MY_ARMS_PAGE_TYPE: String = "KEY_MY_ARMS_PAGE_TYPE"
    const val KEY_MY_ARMS_CLASS_TYPE: String = "KEY_MY_ARMS_CLASS_TYPE"
    const val KEY_SUB_USER_ID: String = "sub_user_id"
}