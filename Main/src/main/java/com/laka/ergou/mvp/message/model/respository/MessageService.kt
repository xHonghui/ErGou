package com.laka.ergou.mvp.message.model.respository

import com.laka.ergou.mvp.message.constant.MessageConstant
import com.laka.ergou.mvp.message.model.bean.MessageResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * @Author:summer
 * @Date:2019/3/8
 * @Description:
 */
interface MessageService {

    @GET(MessageConstant.MESSAGE_LIST_URL)
    fun onLoadMessage(@QueryMap params: HashMap<String, String>): Observable<MessageResponse>

}