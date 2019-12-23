package com.laka.ergou.mvp.teamaward.model.respository

import com.laka.ergou.mvp.commission.constant.CommissionConstant
import com.laka.ergou.mvp.order.constant.MyOrderConstant
import com.laka.ergou.mvp.order.model.bean.OrderListBean
import com.laka.ergou.mvp.teamaward.constant.TearmAwardConstant
import com.laka.ergou.mvp.teamaward.model.bean.TearmAwardBean
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap


interface TearmAwardService {

    @POST(TearmAwardConstant.COMRADE_SUBSIDY)
    fun onGetComradeSubsidy(@QueryMap params: HashMap<String, String>): Observable<TearmAwardBean>

}