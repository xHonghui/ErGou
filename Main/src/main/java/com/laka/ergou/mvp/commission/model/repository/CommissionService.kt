package com.laka.ergou.mvp.commission.model.repository

import com.laka.ergou.mvp.commission.constant.CommissionConstant
import com.laka.ergou.mvp.commission.model.bean.CommissionBean
import com.laka.ergou.mvp.commission.model.bean.CommissionDetailBean
import com.laka.ergou.mvp.commission.model.bean.CommissionNewBean
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

/**
 * @Author:summer
 * @Date:2019/1/18
 * @Description:
 */
interface CommissionService {

    @GET(CommissionConstant.MY_COMMISSION_URL)
    fun onLoadMyCommissonData(@QueryMap params: HashMap<String, String>): Observable<CommissionBean>


    @POST(CommissionConstant.MY_NEW_COMMISSION_URL)
    fun onGetMyCommissonData(@QueryMap params: HashMap<String, String>): Observable<CommissionNewBean>

    @GET(CommissionConstant.MY_COMMISSION_DETAIL_URL)
    fun onLoadCommissionDetailData(@QueryMap params: HashMap<String, String>): Observable<CommissionDetailBean>

    @POST(CommissionConstant.MY_COMMISSION_WITHDRAW_URL)
    fun onLoadWithdrawal(@QueryMap params: HashMap<String, String>): Observable<JSONObject>

}