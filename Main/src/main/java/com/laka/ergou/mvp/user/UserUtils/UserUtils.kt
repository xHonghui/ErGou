package com.laka.ergou.mvp.user.UserUtils

import android.text.TextUtils
import com.laka.androidlib.util.SPHelper
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.bean.Rank
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author:summer
 * @Date:2019/1/14
 * @Description:用户工具类
 */
object UserUtils {

    private var userInfoBean: UserInfoBean? = null

    fun isLogin(): Boolean {
        if (userInfoBean == null) {
            return false
        } else if (userInfoBean?.session == null && userInfoBean?.userBean == null) { // 未登录
            return false
        } else if (userInfoBean?.userBean != null && TextUtils.isEmpty(userInfoBean?.userBean?.token)) {
            return false
        } else if (userInfoBean?.userBean != null && TextUtils.isEmpty("${userInfoBean?.userBean?.id}")) {
            return false
        }
        return true
    }

    fun getUploadObjectKey(): String {
        val sdf = SimpleDateFormat("yyyy/MMdd")
        return "avatar/${sdf.format(Date())}_${userInfoBean?.userBean?.id}_${System.currentTimeMillis()}.png"
    }

    fun getUserId(): Int {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            userInfoBean?.userBean?.id!!
        } else {
            0
        }
    }

    fun getUserToken(): String {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            userInfoBean?.userBean?.token!!
        } else {
            ""
        }
    }

    fun getUserPhone(): String {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            userInfoBean?.userBean?.phone!!
        } else {
            ""
        }
    }

    fun getUserName(): String {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            userInfoBean?.getNickName()!!
        } else {
            ""
        }
    }

    fun getUserAvatar(): String {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            userInfoBean?.getAvatarUrl()!!
        } else {
            ""
        }
    }

    fun getUserGender(): String {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            userInfoBean?.getGender()!!
        } else {
            ""
        }
    }

    fun getWeChatId(): String? {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            userInfoBean?.userBean?.wechat_id!!
        } else {
            ""
        }
    }

    fun getWeChatAccount(): String? {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            userInfoBean?.userBean?.wxAccount!!
        } else {
            ""
        }
    }

    fun isWeChatBind(): Boolean {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            userInfoBean?.userBean?.wxBind!!
        } else {
            false
        }
    }

    fun getUserAliAccount(): String {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            userInfoBean?.userBean?.aliUserName!!
        } else {
            ""
        }
    }

    fun getUserAliRealName(): String {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            userInfoBean?.userBean?.aliUserRealName!!
        } else {
            ""
        }
    }

    fun getUserAgentCode(): String {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            userInfoBean?.userBean?.agentCode!!
        } else {
            ""
        }
    }

    fun getWxAgentCode(): String? {
        return if (userInfoBean != null && userInfoBean?.userBean != null && userInfoBean?.userBean?.wxAgentCode != null) {
            userInfoBean?.userBean?.wxAgentCode!!
        } else {
            ""
        }
    }

    fun isOpenChat(): Boolean {
        return if (userInfoBean == null || userInfoBean?.userBean == null) {
            false
        } else {
            userInfoBean?.userBean?.isOpenChat!!
        }
    }

    fun getTmpToken(): String {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            userInfoBean?.userBean?.tmp_token!!
        } else {
            ""
        }
    }

    fun getAgentCode(): String {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            userInfoBean?.userBean?.agentCode!!
        } else {
            ""
        }
    }

    fun getUnReadMsgCount(): String {
        return if (userInfoBean != null && userInfoBean?.userBean != null && userInfoBean?.userBean?.unreadMsgCount != null) {
            userInfoBean?.userBean?.unreadMsgCount!!
        } else {
            "0"
        }
    }

    fun getRelationId(): String {
        return if (userInfoBean != null && userInfoBean?.userBean != null && userInfoBean?.userBean?.relation_id != null) {
            userInfoBean?.userBean?.relation_id!!
        } else {
            ""
        }
    }

    fun getAgentLevel(): String {
        return if (userInfoBean != null && userInfoBean?.userBean != null && userInfoBean?.userBean?.agent_level != null) {
            userInfoBean?.userBean?.agent_level!!
        } else {
            ""
        }
    }

    fun getUserInfoBean(): UserInfoBean {
        return if (userInfoBean != null) {
            userInfoBean!!
        } else {
            UserInfoBean()
        }
    }

    fun getAgentInfo(): Rank {
        return if (userInfoBean != null && userInfoBean?.userBean != null && userInfoBean?.userBean?.rank != null) {
            userInfoBean?.userBean?.rank!!
        } else {
            Rank()
        }
    }

    /**是否有未读消息*/
    fun hasUnReadMsgCount(): Boolean {
        return if (userInfoBean != null && userInfoBean?.userBean != null) {
            when {
                userInfoBean?.userBean?.other_msg_read != "0" -> // "0" 表示已读
                    true
                userInfoBean?.userBean?.comm_msg_read != "0" -> // "0" 表示已读
                    true
                else -> false
            }
        } else {
            false
        }
    }

    fun updateUserName(userName: String?) {
        userInfoBean?.userBean?.nickname = if (TextUtils.isEmpty(userName)) {
            ""
        } else {
            userName!!
        }
        SPHelper.saveObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, userInfoBean)
    }

    fun updateUserGender(gender: String?) {
        userInfoBean?.userBean?.gender = if (TextUtils.isEmpty(gender)) {
            ""
        } else {
            gender!!
        }
        SPHelper.saveObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, userInfoBean)
    }

    fun updateUserAvatar(avatar: String?) {
        userInfoBean?.userBean?.avatar = if (TextUtils.isEmpty(avatar)) {
            ""
        } else {
            avatar!!
        }
        SPHelper.saveObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, userInfoBean)
    }

    fun updateUserPhone(phone: String) {
        userInfoBean?.userBean?.phone = phone
        SPHelper.saveObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, userInfoBean)
    }

    fun updateBindAliAccount(account: String, realName: String) {
        userInfoBean?.userBean?.aliUserName = account
        userInfoBean?.userBean?.aliUserRealName = realName
        SPHelper.saveObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, userInfoBean)
    }

    fun updateWeChatInfo(wxId: String) {
        userInfoBean?.userBean?.wxAccount = wxId
        SPHelper.saveObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, userInfoBean)
    }

    fun updateWeChatBind(isBind: Boolean) {
        userInfoBean?.userBean?.wxBind = isBind
        SPHelper.saveObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, userInfoBean)
    }

    fun updateOpenChat(isOpen: Boolean) {
        userInfoBean?.userBean?.openGouXiaoEr = if (isOpen) 1 else 0
        SPHelper.saveObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, userInfoBean)
    }

    fun updateUnReadMsgCount(count: String) {
        userInfoBean?.userBean?.unreadMsgCount = count
        SPHelper.saveObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, userInfoBean)
    }

    fun updateRelationId(id: String) {
        userInfoBean?.userBean?.relation_id = id
        SPHelper.saveObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, userInfoBean)
    }


    fun clearOtherUnReadMsg() {
        userInfoBean?.userBean?.other_msg_read = "0"
        SPHelper.saveObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, userInfoBean)
    }

    fun clearCommissionUnReadMsg() {
        userInfoBean?.userBean?.comm_msg_read = "0"
        SPHelper.saveObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, userInfoBean)
    }

    fun clearUserInfo() {
        // 退出登陆的时候，清除SP数据
        SPHelper.clearByFileName(LoginConstant.USER_INFO_FILENAME)
        userInfoBean = null
    }

    fun updateUserInfo(userInfoBean: UserInfoBean) {
        SPHelper.saveObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, userInfoBean)
        this.userInfoBean = userInfoBean
    }

    /**
     * description:更新本地信息
     **/
    fun updateLocalUserInfo() {
        this.userInfoBean = SPHelper.getObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, UserInfoBean::class.java)
    }

}