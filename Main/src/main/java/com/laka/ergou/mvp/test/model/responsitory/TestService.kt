package com.laka.ergou.mvp.test.model.responsitory

import com.laka.ergou.mvp.shop.constant.ShopDetailConstant
import com.laka.ergou.mvp.shop.model.bean.CouponListBean
import com.laka.ergou.mvp.shop.model.bean.ProductRecommendResponse
import com.laka.ergou.mvp.shop.model.bean.ImageDetail
import com.laka.ergou.mvp.shop.model.bean.TPwdCreateResponse
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

/**
 * @Author:summer
 * @Date:2018/12/20
 * @Description:
 */
interface TestService {

//    @GET(ShopDetailConstant.TAOBAOKE_SHOP_URL)
//    fun onLoadShopDetailData(@QueryMap parameter: HashMap<String, String>): Observable<ShopDetailBean>

    /**
     * 商品详情
     * */
    @POST(ShopDetailConstant.TAOBAOKE_SHOP_URL)
    fun onLoadShopDetailData(@QueryMap params: HashMap<String, String>): Observable<ProductWithCoupon>


    /**
     * 商品详情页--猜您喜欢
     * */
    @POST(ShopDetailConstant.TAOBAOKE_SHOP_URL)
    fun onLoadGuessLike(@QueryMap params: HashMap<String, String>): Observable<ProductRecommendResponse>


    /**
     * 商品详情推荐列表
     * */
    @POST(ShopDetailConstant.TAOBAOKE_SHOP_URL)
    fun onLoadCouponList(@QueryMap params: HashMap<String, String>): Observable<CouponListBean>

    /**
     * 产品详情图片
     * */
    @GET(ShopDetailConstant.TAOBAO_DETAIL_IMAGE)
    fun onLoadShopDetailImage(@QueryMap params: HashMap<String, String>): Observable<ArrayList<ImageDetail>>

    /**
     * 获取淘口令
     * */
    @POST(ShopDetailConstant.TAOBAOKE_SHOP_URL)
    fun onGetPwdCreate(@QueryMap params: HashMap<String, String>): Observable<TPwdCreateResponse>
}