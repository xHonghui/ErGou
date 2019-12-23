package com.laka.ergou.mvp.circle.model.repository

import com.laka.ergou.mvp.circle.constant.CircleConstant
import com.laka.ergou.mvp.circle.model.bean.CircleArticleResponse
import com.laka.ergou.mvp.circle.model.bean.CircleCategoryResponse
import com.laka.ergou.mvp.circle.model.bean.CircleCommentResponse
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @Author:sming
 * @Date:2019/8/8
 * @Description:
 */
interface CircleService {

    /**
     * description:获取 首页分类列表
     **/
    @GET(CircleConstant.API_GET_CATEGORY_LIST)
    fun getCategoryList(@QueryMap paramsMap: MutableMap<String, Any>): Observable<CircleCategoryResponse>

    @GET(CircleConstant.API_GET_ARTICLE_LIST)
    fun getArticleList(@QueryMap paramsMap: MutableMap<String, Any>): Observable<CircleArticleResponse>

    @POST(CircleConstant.API_SEND_CIRCLE)
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun sendCircle(@FieldMap paramsMap: MutableMap<String, Any>): Observable<Any>

    @POST(CircleConstant.API_GET_CIRCLE_COMMENT)
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun getCircleComment(@FieldMap paramsMap: MutableMap<String, Any>): Observable<CircleCommentResponse>
}