package com.laka.ergou.mvp.armsteam.model.repository

import com.laka.ergou.mvp.armsteam.constant.MyArmsLevelsConstant
import com.laka.ergou.mvp.armsteam.model.bean.MyArmsResponse
import com.laka.ergou.mvp.armsteam.model.bean.MyLowerLevelsListBean
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @Author:summer
 * @Date:2019/1/21
 * @Description:
 */
interface MyArmsLevelsService {

    @GET(MyArmsLevelsConstant.MY_LOWER_LEVELS_URL)
    fun onLoadMyLowerLevelsData(@QueryMap params: HashMap<String, String>): Observable<MyLowerLevelsListBean>

    @POST(MyArmsLevelsConstant.MY_ARMS_LEVELS_URL)
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun onLoadMyArmsData(@FieldMap params: HashMap<String, String>): Observable<MyArmsResponse>

}