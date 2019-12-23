package com.laka.ergou.mvp.advertbanner.model.respositroy

import com.laka.ergou.mvp.advertbanner.constant.AdvertBannerConstant
import com.laka.ergou.mvp.advertbanner.model.bean.AdvertBannerBean
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @Author:summer
 * @Date:2019/7/27
 * @Description:
 */
interface AdvertBannerService {

    @GET(AdvertBannerConstant.API_GET_ADVERT_BANNER_URL)
//    @FormUrlEncoded
//    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun onLoadAdvertBannerData(@QueryMap params: HashMap<String, String>): Observable<ArrayList<AdvertBannerBean>>
}