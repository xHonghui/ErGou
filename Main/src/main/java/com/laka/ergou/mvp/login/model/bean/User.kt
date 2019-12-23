package com.laka.ergou.mvp.login.model.bean

import com.google.gson.annotations.SerializedName
import com.laka.androidlib.features.user.IUser

/**
 * @Author:Rayman
 * @Date:2018/12/20
 * @Description:用户测试类
 */
class User : IUser() {

    @SerializedName("user_name")
    var userAge: String = ""
    @SerializedName("user_phone")
    var userPhone: String = ""

}