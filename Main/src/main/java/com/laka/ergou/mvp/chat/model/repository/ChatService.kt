package com.laka.ergou.mvp.chat.model.repository

import com.laka.ergou.mvp.chat.model.bean.Message
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * @Author:summer
 * @Date:2019/2/14
 * @Description:
 */
interface ChatService {
    @GET()
    fun onGetHistoryMessages(@QueryMap params: HashMap<String, String>): Observable<Message>
}