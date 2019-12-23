package com.laka.ergou.mvp.login.constant

import com.laka.ergou.BuildConfig

/**
 * @Author:summer
 * @Date:2018/12/19
 * @Description:
 */
interface LoginConstant {
    companion object {

        // 接口
        // 手机号注册
        const val PHONE_LOGIN_URL = "user/phone-login"
        // 获取验证码
        const val GET_VERIFICATION_CODE = "user/get-phone-code"
        //微信授权
        const val WECHAT_AUTHOR_URL = "user/wx-authorize"
        //微信登录
        const val WECHAT_LOGIN_URL = "user/login-bind-phone"
        //用户注册
        const val USER_REGISTER_URL = "user/phone-login"
        //绑定上级
        const val BIND_SUPERAGENT_URL = "user/bind-agent-code"
        //获取上级代理基本信息
        const val AGENT_INFO_URL = "user/agent-info"
        //一键登录token认证
        const val API_LOGIN_TOKEN_VERIFY = "jpush/login-token-verify"

        // 用户基本信息存储 key
        val NICK_TAOBAO = "nick_taobao"
        val HEAD_PORTRAIT_TAOBAO = "head_portrait_taobao"
        val OPENID_TAOBAO = "openid_taobao"
        val OPENSID_TAOBOA = "opensid_taoboa"


        const val PHONE: String = "phone"
        const val CODE: String = "code"
        const val OS_VERSION: String = "os_version"
        const val API_VERSION: String = "api_version"
        const val CHANNEL: String = "channel"
        const val GENDER: String = "gender"
        const val AVATAR: String = "avatar"
        const val PLATFORM: String = "platform"
        const val LOGIN_BASE_HOST: String = BuildConfig.ERGOU_BASE_HOST
        const val TAOBAO_SESSION_BEAN: String = "TAOBAO_SESSION_BEAN"
        const val USER_LOGIN_INFO: String = "user_info"
        const val LOGIN_RESULT_CODE: Int = 1001
        const val LOGIN_REQUEST_CODE: Int = 1002
        const val PHONE_LOGIN_TYPE: Int = 1003
        const val TAOBAO_LOGIN_TYPE: Int = 1004
        const val USER_INFO_FILENAME: String = "user_info_filename"
        const val VERIFICATION_CODE_TYPE: String = "type"
        const val ANDROID_PLATFROM: String = "Android"
        const val JPUSH_REGISTER_ID: String = "jpush_id"
        const val JPUSH_REGISTER_CHANNEL: String = "channel"
        const val KEY_LOGIN_TOKEN: String = "login_token"
        const val WECHAT_AUTHOR_CODE: String = "code"
        const val TOKEN: String = "token"
        const val TMP_TOKEN: String = "tmp_token"
        const val AGENT_CODE: String = "agent_code"
        const val INVITATION_CODE: String = "agent_code"
        const val TYPE: String = "type"
        const val RESULT_SCAN_QR: String = "result_scan_qr"
        const val TIMES_CANCEL_ONE_CLICK_LOGIN_PAGE:String = "times_cancel_one_click_login_page"
        const val TIME_FIRST_INTO_ONE_CLICK_LOGIN_PAGE:String = "time_first_into_one_click_login_page"


        /**登录类型（微信、手机号、普通注册）*/
        const val LOGIN_TYPE: String = "login_type"
        // 登录类型
        const val LOGIN_TYPE_REGISTER: Int = 0x1101 //注册
        const val LOGIN_TYPE_WECAHT: Int = 0x1102  //微信登录
        const val LOGIN_TYPE_PHONE: Int = 0x1103  //普通手机登录
        const val LOGIN_TYPE_ONE_CLICK_LOGIN: Int = 0x1104 //手机一键登录
        //微信登录错误码
        const val CODE_WECHAT_ALREADY_BIND: Int = 208
        //验证码类型
        const val VERIFICATION_TYPE_LOGIN: Int = 1 //普通手机登录
        const val VERIFICATION_TYPE_BIND: Int = 2 //绑定
        const val VERIFICATION_TYPE_WECHAT_LOGIN: Int = 3 //微信登录
        const val VERIFICATION_TYPE_REGISTER: Int = 4 //用户注册

        const val LOGIN_TYPE_PHONE_LOGIN: Int = 1 //普通手机登录
        const val LOGIN_TYPE_BIND: Int = 2 //绑定
        const val LOGIN_TYPE_WECHAT_LOGIN: Int = 3 //微信登录
        const val LOGIN_TYPE_REGIS: Int = 4 //用户注册
        const val EVENT_LOGIN_WX: String = "event_login_wx"//微信登录event
        //startActivityForResult 的请求 code
        const val REQUEST_SCAN_QR_CODE: Int = 0x1103
        const val RESULT_SCAN_QR_CODE: Int = 0x1104
        const val REQUEST_CHOICE_PHOTO_CODE: Int = 0x1105
        //匹配邀请码的正则表达式
        const val MATCHER_INVITATION_CODE_REGEX: String = "code=([a-zA-Z0-9]{6})"

    }
}
