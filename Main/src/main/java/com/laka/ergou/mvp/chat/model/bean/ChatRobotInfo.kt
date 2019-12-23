package com.laka.ergou.mvp.chat.model.bean

/**
 * @Author:Rayman
 * @Date:2019/2/18
 * @Description:聊天机器人信息
 */
data class ChatRobotInfo(var robotId: String,                     //机器人ID
                         var robotName: String?  = "",             //机器人昵称
                         var robotAvatar: String? = "",            //机器人头像
                         var robotGender: String? = ""             //机器人性别
)