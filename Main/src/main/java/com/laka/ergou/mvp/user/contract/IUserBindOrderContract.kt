package com.laka.ergou.mvp.user.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.net.response.BaseResponse
import io.reactivex.Observable
import org.json.JSONObject
import java.util.*

/**
 * @Author:Rayman
 * @Date:2019/1/17
 * @Description:用户模块---绑定订单Contract
 */
interface IUserBindOrderContract {

    interface IUserBindOrderModel {

        /**
         * description:绑定订单
         **/
        fun bindOrder(orderId: String): Observable<JSONObject>
    }

    interface IUserBindOrderPresenter : IBasePresenter<IUserBindOrderView> {

        fun bindOrder(orderId: String)

    }

    interface IUserBindOrderView : IBaseLoadingView<Objects> {
        fun bindOrderSuccess(msg: String)
    }
}