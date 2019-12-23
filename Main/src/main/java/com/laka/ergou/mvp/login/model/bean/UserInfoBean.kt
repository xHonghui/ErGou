package com.laka.ergou.mvp.login.model.bean

import com.ali.auth.third.core.model.Session
import com.laka.ergou.mvp.login.constant.LoginConstant

/**
 * @Author:summer
 * @Date:2018/12/19
 * @Description: 用户登录信息，包含手机登录和淘宝授权登录
 */
data class UserInfoBean(var userBean: UserBean? = UserBean(), var session: Session? = Session()) {
    var loginType: Int? = -1
        get() {
            if (session != null) {
                return LoginConstant.TAOBAO_LOGIN_TYPE
            } else if (userBean != null) {
                return LoginConstant.PHONE_LOGIN_TYPE
            }
            return -1
        }

    fun getNickName():String?{
        return when(loginType){
            LoginConstant.TAOBAO_LOGIN_TYPE ->{
                session?.nick
            }
            LoginConstant.PHONE_LOGIN_TYPE ->{
                userBean?.nickname
            }
            else ->{
                ""
            }
        }
    }

    fun getAvatarUrl():String?{
        return when(loginType){
            LoginConstant.TAOBAO_LOGIN_TYPE ->{
                session?.avatarUrl
            }
            LoginConstant.PHONE_LOGIN_TYPE ->{
                userBean?.avatar
            }
            else ->{
                ""
            }
        }
    }

    fun getGender():String?{
        return when(loginType){
            LoginConstant.TAOBAO_LOGIN_TYPE ->{
                "未知"
            }
            LoginConstant.PHONE_LOGIN_TYPE ->{
                userBean?.gender
            }
            else ->{
                "未知"
            }
        }
    }
}
