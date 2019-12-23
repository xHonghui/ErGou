package com.laka.ergou.mvp.main.model.repository

import com.laka.androidlib.net.response.BaseResponse
import com.laka.androidlib.net.response.ResponseDownload
import com.laka.androidlib.util.retrofit.download.DownloadResponseBody
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.model.bean.AddLetteryTimeResponse
import com.laka.ergou.mvp.main.model.bean.AppUpdateInfo
import com.laka.ergou.mvp.main.model.bean.AppUrlsInfo
import com.laka.ergou.mvp.main.model.bean.ShareInfo
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shop.model.bean.CustomProductDetail
import com.laka.ergou.mvp.shop.model.bean.HighVolumeInfoResponse
import com.laka.ergou.mvp.shop.model.bean.TPwdCreateResponse
import com.laka.ergou.mvp.user.constant.UserApiConstant
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.UrlResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.http.*

/**
 * @Author:Rayman
 * @Date:2019/1/22
 * @Description:主页APIService
 */
interface HomeService {

    @GET(HomeApiConstant.API_UPDATE)
    fun getAppUpdateInfo(@QueryMap hashMap: HashMap<String, String?>): Observable<BaseResponse<AppUpdateInfo>>

    @GET(HomeApiConstant.API_WX_SHARE_OFFICIAL)
    fun getShareOfficial(@QueryMap hashMap: HashMap<String, String?>): Observable<BaseResponse<ShareInfo>>

    @POST(HomeApiConstant.API_WX_SHARE_PROMOTION)
    fun getSharePromotion(@QueryMap hashMap: HashMap<String, String?>): Observable<BaseResponse<ShareInfo>>

    @POST(HomeApiConstant.API_ADD_LOTTERY_TIME)
    fun addLotteryTime(@QueryMap hashMap: HashMap<String, String?>): Observable<BaseResponse<AddLetteryTimeResponse>>

    @GET(HomeApiConstant.API_GET_USER_BANNER_ADV)
    fun getAppUrls():Observable<BaseResponse<AppUrlsInfo>>

    @GET
    @Streaming
    fun downloadApk(@Url path: String): Observable<ResponseBody>

    /**
     * 产品详情（banner、详情、详情图、）
     * */
    @GET(ShopDetailConstant.CUSTOM_PRODUCT_URL)
    fun onLoadProductDetail(@QueryMap params: HashMap<String, String>): Observable<CustomProductDetail>

    /**
     * 获取高额优惠券信息
     * */
    @GET(ShopDetailConstant.API_TBK_PRIVILEGE_GET)
    fun onLoadHighVolumeCouponInfo(@QueryMap params: HashMap<String, String>): Observable<HighVolumeInfoResponse>

    /**
     * 获取淘口令
     * */
    @GET(ShopDetailConstant.API_TBK_CREATE_TPWD)
    fun onGetPwdCreate(@QueryMap params: HashMap<String, String>): Observable<TPwdCreateResponse>

    /**
     * description：获取联盟授权codeUrl
     * */
    @POST(UserApiConstant.API_GET_UNION_CODE_URL)
    fun getUnionCodeUrl(@QueryMap params: HashMap<String, String>): Observable<UrlResponse>

    /**
     * description：绑定渠道ID
     * */
    @POST(UserApiConstant.API_HANDLE_UNION_CODE_URL)
    fun handleUnionCode(@QueryMap params: HashMap<String, String>):Observable<RelationResponse>

}