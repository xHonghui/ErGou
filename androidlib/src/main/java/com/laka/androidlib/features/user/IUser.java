package com.laka.androidlib.features.user;

import com.google.gson.annotations.SerializedName;
import com.laka.androidlib.net.utils.parse.anno.Exclude;

/**
 * @Author:Rayman
 * @Date:2018/12/18
 * @Description:用户基类（仅存在用户ID与Token信息、登陆状态等数据）
 */

public class IUser {

    @SerializedName("user_id")
    protected String userId;
    @SerializedName("token")
    protected String token;

    @Exclude
    protected boolean isLogin = false;

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
