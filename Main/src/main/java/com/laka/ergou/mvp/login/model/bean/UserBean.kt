package com.laka.ergou.mvp.login.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2018/12/19
 * @Description: 用户信息
 */
data class UserBean(
        var agent_level: String = "", //代理等级，10:超级代理, 20: 金牌合伙人, 30:品牌运营商
        var adzone: Adzone = Adzone(),
        var api_version: String = "",
        var avatar: String = "",
        var certificate_status: Int = 0,
        var certificated_time: Int = 0,
        var channel: String = "",
        var created_time: Int = 0,
        var gender: String = "",
        var id: Int = 0,
        var is_first: Int = 0,
        var name: String = "",
        var nickname: String = "",
        var os_version: String = "",
        var phone: String = "",
        var platform: String = "",
        var profit_level: Int = 0,
        var qq_id: String = "",
        var token: String = "",
        var ut_expire: String = "",
        @SerializedName("wechat_id", alternate = ["wxid"])
        var wechat_id: String = "",
        var weibo_id: String = "",
        var withdraw_phone: String = "",
        var withdraw_wechat_id: String = "",
        @SerializedName("alipay_username")
        var aliUserName: String = "",
        @SerializedName("alipay_realname")
        var aliUserRealName: String = "",
        var bound_wx: String = "",
        var updated_time: Long = -1,
        var tmp_token: String = "",
        var can_fill_agent: Boolean = false,
        var is_especial: Boolean = false,
        var agent_word: String = "",
        var robot: String = "",
        var jpush_id: String = "",
        var other_msg_read: String = "0", //其他消息，1表示未读, 0表示已读
        var comm_msg_read: String = "0", //补贴消息，1表示未读, 0表示已读
        var do_not_disturb_sleep: String = "",
        @SerializedName("open_gou_xiao_er")
        var openGouXiaoEr: Int = 0,
        @SerializedName("agent_code")
        var agentCode: String = "",
        @SerializedName("wx_nickname")
        var wxAccount: String = "",
        @SerializedName("wx_bound")
        var wxBind: Boolean = false,
        @SerializedName("wx_agent_code")
        var wxAgentCode: String = "",
        var relation_id: String = "",   //渠道ID，未授权为空字符串
        @SerializedName("rank")
        var rank: Rank = Rank(),  //战队等级信息
        @SerializedName("unread_msg_count")
        var unreadMsgCount: String = "") {

    var isOpenChat: Boolean
        set(value) {
            isOpenChat = value
        }
        get() {
            return openGouXiaoEr == 1
        }
}

data class Adzone(
        var adzone_id: String = "",
        var adzonepid: String = "",
        var created_time: Int = 0,
        var gcid: Int = 0,
        var memberid: Int = 0,
        var siteid: Int = 0,
        var user_id: Int = 0
)

data class Rank(
        @SerializedName("agent_level")
        var agentLevel: String = "",
        @SerializedName("agent_rate")
        var agentRate: String = "",
        @SerializedName("agent_word")
        var agentWord: String = "",
        @SerializedName("own_rate")
        var ownRate: String = "",
        @SerializedName("reward")
        var reward: String = "")