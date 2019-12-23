package com.laka.ergou.mvp.user.model.responsitory

import com.laka.androidlib.net.response.BaseResponse
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.login.model.bean.VerificationCodeData
import com.laka.ergou.mvp.user.constant.UserApiConstant
import com.laka.ergou.mvp.user.model.bean.*
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.*
import kotlin.collections.HashMap

/**
 * @Author:summer
 * @Date:2019/1/12
 * @Description:
 */
interface UserService {

    /**
     * 修改用户信息
     * */
    @POST(UserApiConstant.API_EDIT_USER_INFO)
    fun onUpdateUserInfoData(@QueryMap params: HashMap<String, String?>): Observable<BaseResponse<CommonData>>

    /**
     * 修改用户信息
     * retrofix 表单提交会对参数进行默认编码方式的编码，这几导致上传的字符串带有特殊字符或者中文时，可能会出现乱码，
     * 至于怎么解决，目前还没有找到确却的方案，不过使用 query 提交的话，就不会有这种问题，所以暂时先这样解决
     * */
    @POST(UserApiConstant.API_EDIT_USER_INFO)
    fun onUpdateUserAvatarData(@Query("avatar") path: String): Observable<CommonData>

    /**
     * 修改用户信息（更改用户昵称）
     * */
    @POST(UserApiConstant.API_EDIT_USER_INFO)
//    @FormUrlEncoded
//    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun onUpdateUserNickOrGenderData(@QueryMap params: HashMap<String, String?>): Observable<CommonData>

    /**
     * 获取sts token
     * */
    @GET(UserApiConstant.API_GET_STS_TOKEN)
    fun onGetStsToken(@QueryMap params: HashMap<String, String?>): Observable<StsTokenBean>

    /**
     * 更新用户信息到本地
     * */
    @GET(UserApiConstant.API_GET_USER_INFO)
    fun onLoadUserInfo(@QueryMap params: HashMap<String, String?>): Observable<UserBean>

    /**
     * description:获取用户机器人列表
     **/
    @GET(UserApiConstant.API_USER_GET_ROBOT_LIST)
    fun getUserRobotList(@QueryMap params: HashMap<String, String?>): Observable<BaseResponse<RobotDataResponse<RobotInfo>>>

    /**
     * description:更换手机号
     **/
    @POST(UserApiConstant.API_CHANGE_USER_PHONE)
    fun changeUserPhone(@QueryMap params: HashMap<String, String?>): Observable<BaseResponse<JSONObject>>

    /**
     * description:更换手机号---获取验证码
     **/
    @POST(LoginConstant.GET_VERIFICATION_CODE)
    fun onGetVerificationCode(@QueryMap params: HashMap<String, String?>): Observable<BaseResponse<VerificationCodeData>>

    /**
     * description:绑定淘宝账号
     **/
    @POST(UserApiConstant.API_BIND_TAO_BAO_ACCOUNT)
    fun bindTaoBaoAccount(@QueryMap params: HashMap<String, String?>): Observable<BaseResponse<JSONObject>>

    /**
     * description:解绑淘宝账号
     **/
    @POST(UserApiConstant.API_UNBIND_TAO_BAO_ACCOUNT)
    fun unbindTaoBaoAccount(@QueryMap params: HashMap<String, String?>): Observable<BaseResponse<JSONObject>>

    /**
     * description:绑定订单号
     **/
    @POST(UserApiConstant.USER_BIND_ORDER)
    fun bindOrder(@QueryMap params: HashMap<String, String?>): Observable<BaseResponse<JSONObject>>

    /**
     * description:绑定微信账号
     **/
    @POST(UserApiConstant.API_BIND_WE_CHAT_ACCOUNT)
    fun bindWxAccount(@QueryMap params: HashMap<String, String?>): Observable<BaseResponse<UserBean>>

    /**
     * description:解除微信绑定
     **/
    @POST(UserApiConstant.API_UNBIND_WE_CHAT_ACCOUNT)
    fun unbindWxAccount(@QueryMap params: HashMap<String, String?>): Observable<BaseResponse<JSONObject>>

    /**
     * description:获取用户Banner
     **/
    @GET(UserApiConstant.API_GET_USER_BANNER_ADV)
    fun getUserBannerAdv(@QueryMap params: HashMap<String, String>): Observable<BaseResponse<ArrayList<BannerAdvBean>>>

    /**
     * description：获取联盟授权codeUrl
     * */
    @POST(UserApiConstant.API_GET_UNION_CODE_URL)
    fun getUnionCodeUrl(@QueryMap params: HashMap<String, String>): Observable<UrlResponse>

    @POST(UserApiConstant.API_HANDLE_UNION_CODE_URL)
    fun handleUnionCode(@QueryMap params: HashMap<String, String>): Observable<RelationResponse>

    @POST(UserApiConstant.API_GET_SHARE_POSTER_URL)
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun onLoadSharePosterData(@FieldMap params: HashMap<String, String>): Observable<ArrayList<String>>

    @GET(UserApiConstant.API_GET_PRODUCT_DETAIL_SERVICE_URL)
    fun getProductDetailService(@QueryMap params: HashMap<String, String>): Observable<HashMap<String, String>>

    @POST(UserApiConstant.API_GET_OTHER_REWARD)
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun onLoadRewardListData(@FieldMap params: HashMap<String, String>): Observable<RewardListResponse>

}