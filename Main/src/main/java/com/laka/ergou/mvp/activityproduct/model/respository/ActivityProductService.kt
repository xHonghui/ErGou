package com.laka.ergou.mvp.activityproduct.model.respository

import com.laka.ergou.mvp.activityproduct.constant.ActivityProductConstant
import com.laka.ergou.mvp.activityproduct.model.bean.ActivityProductResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:
 */
interface ActivityProductService {

    @GET(ActivityProductConstant.API_ACTIVITY_PRODUCT)
    fun onLoadActivityProductList(@QueryMap params: HashMap<String, String>): Observable<ActivityProductResponse>
}