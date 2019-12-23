package com.laka.ergou.mvp.shopping.center.model.repository

import com.laka.androidlib.net.utils.parse.GsonUtil
import com.laka.ergou.mvp.shopping.center.contract.IProductDetailContract
import com.laka.ergou.mvp.shopping.center.model.bean.ProductDetail
import io.reactivex.Observable

/**
 * @Author:Rayman
 * @Date:2018/12/20
 * @Description:
 */
class ProductDetailModel : IProductDetailContract.IProductDetailModel {

    var testJson = "{\n" +
            "\"product_info\":{\n" +
            "\"product_id\":1,\n" +
            "\"product_name\":\"测试商品\",\n" +
            "\"product_content\":\"测试商品描述\",\n" +
            "\"product_price\":\"12.00\",\n" +
            "\"product_price_unit\":\"元/件\"\n" +
            "},\n" +
            "\"user_info\":{\n" +
            "\"user_id\":120,\n" +
            "\"token\":\"1231sakdjalsdj9zxceasxvjklasdioaskjd\",\n" +
            "\"user_name\":\"Rayman\",\n" +
            "\"user_phone\":\"18820785502\"\n" +
            "},\n" +
            "\"test_int\":\"\",\n" +
            "\"test_float\":\"123哈哈\",\n" +
            "\"test_long\":null,\n" +
            "\"test_double\":true\n" +
            "}"
    var gsonUtil: GsonUtil = GsonUtil()

    override fun getProductDetail(productId: String): Observable<ProductDetail> {
        return Observable.create {


            //            var productDetail = ProductDetail(
//                    BaseProduct(), User()
//            )

            var jsonData = gsonUtil.parseJson(testJson, ProductDetail::class.java)
            it.onNext(jsonData)
            it.onComplete()
        }
    }
}