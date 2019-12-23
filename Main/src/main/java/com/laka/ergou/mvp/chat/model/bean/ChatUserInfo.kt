package com.laka.ergou.mvp.chat.model.bean

/**
 * @Author:Rayman
 * @Date:2019/2/18
 * @Description:聊天用户信息
 */
data class ChatUserInfo(
        var userId: String,                     //用户ID
        var userName: String? = "",              //用户昵称
        var userAvatar: String? = "",            //用户头像
        var userGender: String? = "",            //用户性别
        var userLevel: Int = 0,                 //用户身份
        var isFollow: Int = 0                   //是否关注
)