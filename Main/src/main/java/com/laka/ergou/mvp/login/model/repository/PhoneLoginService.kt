package com.laka.ergou.mvp.login.model.repository

import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.bean.*
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.*

/**
 * @Author:summer
 * @Date:2019/1/7
 * @Description:
 */
interface PhoneLoginService {

    // 手机登录
    @POST(LoginConstant.PHONE_LOGIN_URL)
    fun onPhoneLogin(@QueryMap paramsMap: HashMap<String, String>): Observable<UserBean>

    // 获取手机验证码
    @POST(LoginConstant.GET_VERIFICATION_CODE)
    fun onGetVerificationCode(@QueryMap params: HashMap<String, String>): Observable<VerificationCodeDataBean>

    @POST(LoginConstant.WECHAT_AUTHOR_URL)
    fun onWechatAuthor(@QueryMap params: HashMap<String, String>): Observable<UserBean>

    @POST(LoginConstant.WECHAT_LOGIN_URL)
    fun onWechatLogin(@QueryMap params: HashMap<String, String>): Observable<UserBean>

    @POST(LoginConstant.USER_REGISTER_URL)
    fun onUserRegister(@QueryMap params: HashMap<String, String>): Observable<UserBean>

    @POST(LoginConstant.BIND_SUPERAGENT_URL)
    fun onBindSuperAgent(@QueryMap params: HashMap<String, String>): Observable<JSONObject>


    @GET(LoginConstant.AGENT_INFO_URL)
    fun onLoadAgentInfo(@QueryMap params: HashMap<String, String>): Observable<AgentSimpleInfo>

    // 获取手机验证码
    @POST(LoginConstant.GET_VERIFICATION_CODE)
    fun onGetVerificationCodeForLogin(@QueryMap params: HashMap<String, String>): Observable<JSONObject>

    //手机一键登录token认证
    @POST(LoginConstant.API_LOGIN_TOKEN_VERIFY)
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun onLoginTokenVerify(@FieldMap params: HashMap<String, String>):Observable<UserBean>



}