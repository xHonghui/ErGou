package com.laka.ergou.mvp.user.constant

import android.support.annotation.IntDef
import com.laka.ergou.BuildConfig

/**
 * @Author:Rayman
 * @Date:2019/1/7
 * @Description:用户模块常量类
 */
object UserConstant {

    /**
     * description:用户模块小工具功能
     **/
    const val USER_MESSAGE = 0x440
    const val SETTING_UTIL = 0x441
    const val ORDER_UTIL = 0x442
    const val COMMISSION_UTIL = 0x443
    const val SUBORDINATE_UTIL = 0x444
    const val INVITATION_UTIL = 0x445
    const val BIND_ORDER_UTIL = 0x446
    const val ROBOT_UTIL = 0x447

    @IntDef(USER_MESSAGE,
            SETTING_UTIL,
            ORDER_UTIL,
            COMMISSION_UTIL,
            SUBORDINATE_UTIL,
            INVITATION_UTIL,
            BIND_ORDER_UTIL,
            ROBOT_UTIL)
    annotation class PersonalUtil

    /**
     * description:用户主页工具类类型---对应Banner、水平工具面板、垂直工具面板
     **/
    const val BANNER_UTIL = 0x448
    const val MIXTURE_UTIL = 0x449
    const val SINGLE_UTIL = 0x450
    const val HINT_UTIL = 0x451

    /**
     * description:阿里电商组件部分常量设置
     **/
    // 订单状态类型--对应全部-未支付-未发货-未收货-未评论
    const val MY_ORDER_STATUS_ALL = 0
    const val MY_ORDER_STATUS_NOT_PAY = 1
    const val MY_ORDER_STATUS_NOT_DELIVER = 2
    const val MY_ORDER_STATUS_NOT_RECEIVER = 3
    const val MY_ORDER_STATUS_NOT_COMMENT = 4

    @IntDef(MY_ORDER_STATUS_ALL, MY_ORDER_STATUS_NOT_PAY, MY_ORDER_STATUS_NOT_DELIVER, MY_ORDER_STATUS_NOT_RECEIVER, MY_ORDER_STATUS_NOT_COMMENT)
    annotation class MY_ORDER_STATUS

    const val NICKNAME_EDIT_LENGTH: Int = 100 // 修改昵称的长度限制
    const val ERGOU_IMG: String = "ergou_img"

    // 请求清理缓存权限（读写外部存储设备）
    const val CLEAR_CACHE_REQUEST_CODE: Int = 1001
    // 用户模块事件类型
    const val LOGIN_EVENT: Int = 1001
    const val LOGOUT_EVENT: Int = 1002
    const val EDIT_USER_INFO: Int = 1003
    const val TAOBAO_AUTHOR_SUCCESS_EVENT: Int = 1004 //淘宝授权成功
    const val TAOBAO_UNAUTHOR_EVENT: Int = 1008
    const val TAOBAO_AUTHOR_TO_PAGE_EVENT: Int = 1005
    const val SHOW_ALIBC_MY_ORDER_EVENT: Int = 1006 // 我的订单
    const val SHOW_ALIBC_MY_SHOP_CART_EVENT: Int = 1007 // 我的购物车
    const val READ_COMMISSION_MSG_EVENT: Int = 1009
    const val READ_OTHER_MSG_EVENT: Int = 1010
    const val TAOBAO_AUTHOR_EVENT: Int = 1011 //淘宝授权
    const val SOCKET_CONNECT_EVENT: Int = 1012 //发送socket重连事件，进行Socket重连
    const val APP_INSTALL_APK: Int = 1013 //app应用内安装（更新）

    /**
     * description:用户模块跳转关键字常量
     **/
    const val CHANG_PHONE_NUMBER = "CHANG_PHONE_NUMBER"
    const val ROBOT_ID = "ROBOT_ID"
    const val ROBOT_NAME = "ROBOT_NAME"
    const val MESSAGE_TYPE = "MESSAGE_TYPE"

    // request key
    const val NICKNAME: String = "nickname"
    const val GENDER: String = "gender"
    const val AVATAR: String = "avatar"
    const val OPEN_GOU_XIAO_ER: String = "open_gou_xiao_er"
    const val TOKEN: String = "token"
    const val CODE: String = "code"
    const val POST_ID: String = "poster_id"
    const val REWARD_SOOURCE_TYPE: String = "reward_soource_type"
    const val SOURCE_TYPE: String = "source_type"
    const val PAGE_NUMBER: String = "page"
    const val PAGE_SIZE: String = "pageSize"
    const val PAGE_SIZE_VALUE: Int = 20

    const val PHONE_AGENT_CODE = "PHONE_AGENT_CODE"
    const val WE_CHAT_AGENT_CODE = "WE_CHAT_AGENT_CODE"
    //startActivity 的 requestCode
    const val BIND_UNION_REQUEST_CODE: Int = 0x1001
    const val BIND_UNION_RESULT_CODE: Int = 0X1002
    //url
    const val UNION_CODE_URL: String = "UNION_CODE_URL"
    const val UNION_CODE: String = "code"
    const val UNION_STATE: String = "state"
    //高宽比例
    const val RADIO_PLAYBILL_WIDTH_HEIGHT: String = "1.608"
    //奖励类型
    const val TYPE_REWARD_APP: Int = 1  //app奖励
    const val TYPE_REWARD_WECHAT: Int = 2  //微信奖励
    //奖励订单状态
    const val COMMISSION_ENABLE: String = "3"  //可提现
    const val COMMISSION_SETTLED: String = "12"  //待结算
    //event
    const val EVENT_BIND_RELEATION_ID_SUCCESS = "EVENT_BIND_RELEATION_ID_SUCCESS"
}