package com.laka.ergou.mvp.shopping.center.model.repository

import com.laka.ergou.mvp.shopping.center.constant.ShoppingApiConstant
import com.laka.ergou.mvp.shopping.center.model.bean.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @Author:Rayman
 * @Date:2018/12/18
 * @Description:主页商品Service
 */
interface ShoppingService {

    /**
     * description:获取首页头部数据（banner、专题、分类、活动专区）
     * */
    @POST(ShoppingApiConstant.API_GET_HOME_PAGE_DATA)
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun getHomePageData(@FieldMap paramsMap: HashMap<String, String>): Observable<HomePageResponse>

    /**
     * description:获取商品列表---根据分类获取
     **/
    @GET(ShoppingApiConstant.API_GET_PRODUCT_LIST_BY_TYPE)
    fun getProductListByType(@QueryMap paramsMap: HashMap<String, String>): Observable<ShoppingResponse>

    /**
     * description:获取商品列表--主页精选Banner数据
     **/
    @GET(ShoppingApiConstant.API_GET_BANNER_LIST_BY_FAVORITES)
    fun getFavoritesProductBannerData(): Observable<ShoppingFavoriteResponse>

    /**
     * description:获取商品列表---主页精选数据
     **/
    @GET(ShoppingApiConstant.API_GET_PRODUCT_LIST_BY_FAVORITES)
    fun getProductListByFavorite(@QueryMap paramsMap: HashMap<String, String>): Observable<ShoppingResponse>


    /**
     * description:获取商品列表---主页精选数据
     **/
    @POST(ShoppingApiConstant.API_GET_PRODUCT_LIST_BY_SPECIAL_ID)
    fun getProductListByCategoryId(@QueryMap paramsMap: HashMap<String, String>): Observable<ProductListResponse>

    @GET(ShoppingApiConstant.API_GET_PRODUCT_LIST_BY_SELECT)
    fun getProductListBySelect(@QueryMap paramsMap: HashMap<String, String>): Observable<ShoppingListResponse>

    @POST(ShoppingApiConstant.API_GET_ADVERT)
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun getAdvert(@FieldMap paramsMap: HashMap<String, String>): Observable<List<AdvertBean>>

    @GET(ShoppingApiConstant.API_GET_H5_URL)
    fun getH5Url(): Observable<HomeUrlBean>
    /**
     * description:获取商品列表---根据KeyWord去获取（实际上和分类获取的类似）
     **/
//    @POST(ShoppingApiConstant.BASE_API_FUNCTION)
//    fun getProductListByKeyWord(@Query("q") keyWord: String,
//                                @Query("method") method: String = ShoppingApiConstant.API_GET_PRODUCT_LIST_BY_KEY_WORD,
//                                @Query("field") field: String = "num_iid,title,pict_url,click_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick,shop_title,zk_final_price_wap,event_start_time,event_end_time,tk_rate,status,type,coupon_info,coupon_click_url,coupon_share_url",
//                                @Query("format") format: String = "json",
//                                @Query("page_size") pageSize: Int = 20,
//                                @Query("page") page: Int): Observable<TaoBaoResponse<ShoppingResponse>>


}