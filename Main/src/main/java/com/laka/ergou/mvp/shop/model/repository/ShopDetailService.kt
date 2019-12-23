package com.laka.ergou.mvp.shop.model.repository

import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shop.model.bean.*
import com.laka.ergou.mvp.user.constant.UserApiConstant
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.UrlResponse

import io.reactivex.Observable
import retrofit2.http.*

/**
 * @Author:summer
 * @Date:2018/12/20
 * @Description:
 */
interface ShopDetailService {

    /**
     * 商品详情页--猜您喜欢（自己服务器的接口）
     * */
    @GET(ShopDetailConstant.API_PRODUCT_RECOMMEND)
    fun onLoadRecommendData(@QueryMap params: HashMap<String, String>): Observable<ProductRecommendResponse>

    /**
     * 获取淘口令
     * */
    @GET(ShopDetailConstant.API_TBK_CREATE_TPWD)
    fun onGetPwdCreate(@QueryMap params: HashMap<String, String>): Observable<TPwdCreateResponse>

    /**
     * 获取高额优惠券信息
     * */
    @GET(ShopDetailConstant.API_TBK_PRIVILEGE_GET)
    fun onLoadHighVolumeCouponInfo(@QueryMap params: HashMap<String, String>): Observable<HighVolumeInfoResponse>

    /**
     * 获取产品详情（banner、产品详情、产品详情图片等）
     * */
    @GET(ShopDetailConstant.CUSTOM_PRODUCT_URL)
    fun onLoadProductDetail(@QueryMap params: HashMap<String, String>): Observable<CustomProductDetail>

    /**
     * 获取产品详情图片
     * */
    @GET(ShopDetailConstant.TAOBAO_DETAIL_IMAGE)
    fun onLoadDetailImage(@QueryMap params: HashMap<String, String>): Observable<ArrayList<ImageDetail>>

    /**
     * description：获取联盟授权codeUrl
     * */
    @POST(UserApiConstant.API_GET_UNION_CODE_URL)
    fun getUnionCodeUrl(@QueryMap params: HashMap<String, String>): Observable<UrlResponse>

    @POST(UserApiConstant.API_HANDLE_UNION_CODE_URL)
    fun handleUnionCode(@QueryMap params: HashMap<String, String>): Observable<RelationResponse>

    /**
     * description：淘口令二维码url
     * */
    @POST(ShopDetailConstant.API_TBK_QRCODE_URL)
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun onLoadTklQrCodeUrl(@FieldMap params: HashMap<String, String>): Observable<HashMap<String, String>>

}