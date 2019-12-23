package com.laka.ergou.mvp.shopping.search.model.repository

import com.laka.androidlib.net.response.BaseResponse
import com.laka.ergou.mvp.shopping.search.constant.ShoppingSearchApiConstant
import com.laka.ergou.mvp.shopping.search.model.bean.SearchHotResponse
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * @Author:Rayman
 * @Date:2019/1/30
 * @Description:商品搜索页Service
 */
interface ShoppingSearchService {

    @GET(ShoppingSearchApiConstant.API_SEARCH_HOT_WORD)
    fun getSearchHotWord(): Observable<BaseResponse<SearchHotResponse>>

}