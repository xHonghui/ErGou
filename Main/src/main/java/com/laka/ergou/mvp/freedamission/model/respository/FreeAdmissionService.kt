package com.laka.ergou.mvp.freedamission.model.respository

import com.laka.ergou.mvp.freedamission.constant.FreeAdmissionContant
import com.laka.ergou.mvp.freedamission.model.bean.FreeAdmissionResponse
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import io.reactivex.Observable
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:
 */
interface FreeAdmissionService {

    @POST(FreeAdmissionContant.API_LOAD_FREE_ADMISSION_URL)
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun onLoadFreeAdmissionProductList(@FieldMap params:HashMap<String,String>):Observable<FreeAdmissionResponse>
}