package com.laka.ergou.mvp.user.model.responsitory

import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.rx.RxResponseComposer
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import com.laka.ergou.mvp.user.constant.UserApiConstant
import com.laka.ergou.mvp.user.contract.IUserBindOrderContract
import io.reactivex.Observable
import org.json.JSONObject
import java.util.*

/**
 * @Author:Rayman
 * @Date:2019/1/17
 * @Description:用户模块---绑定订单Model实现层
 */
class UserBindOrderModel : IUserBindOrderContract.IUserBindOrderModel {

    override fun bindOrder(orderId: String): Observable<JSONObject> {
        val params = HashMap<String, String?>()
        params[UserApiConstant.ORDER_ID] = orderId
        return UserRetrofitHelper.instance.bindOrder(params)
                .compose(RxResponseComposer.flatResponse())
    }
}