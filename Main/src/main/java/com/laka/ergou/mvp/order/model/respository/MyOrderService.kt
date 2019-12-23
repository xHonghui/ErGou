package com.laka.ergou.mvp.order.model.respository

import com.laka.ergou.mvp.order.constant.MyOrderConstant
import com.laka.ergou.mvp.order.model.bean.OrderListBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * @Author:summer
 * @Date:2019/1/21
 * @Description:
 */
interface MyOrderService {

    @GET(MyOrderConstant.MY_ORDER_URL)
    fun onLoadMyOrderData(@QueryMap params: HashMap<String, String>): Observable<OrderListBean>
}