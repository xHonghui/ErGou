package com.laka.ergou.mvp.armsteam.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @Author:summer
 * @Date:2019/5/28
 * @Description:战队实体类
 */
data class MyArmsLevelsBean(
        @SerializedName("id")
        val mId: String = "",
        @SerializedName("avatar")
        val avatar: String = "",
        @SerializedName("nickname")
        val nickname: String = "",
        @SerializedName("created_time")
        val createdTime: String = "",
        @SerializedName("level")
        val level: String = "",   //战友等级， 10：超级队长， 20：金牌团长， 30：荣耀总司令
        @SerializedName("title")
        val mAlertMsg: String = "",  //战友等级提示语
        @SerializedName("comm_word")
        val commonWord: String = "",  //佣金提示语
        @SerializedName("earn")
        val commonValue: String = "",  //佣金
        @SerializedName("number")
        val personCount: Int = 0  //战友人数
)
