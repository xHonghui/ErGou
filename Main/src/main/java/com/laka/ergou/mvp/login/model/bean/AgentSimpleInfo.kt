package com.laka.ergou.mvp.login.model.bean

/**
 * @Author:summer
 * @Date:2019/3/9
 * @Description:
 */
data class AgentSimpleInfo(
        val avatar: String = "",
        val id: Int = -1,
        val nickname: String = "",
        var agent_code:String = ""
)