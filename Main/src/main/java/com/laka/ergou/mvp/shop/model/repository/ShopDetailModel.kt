package com.laka.ergou.mvp.shop.model.repository

import android.text.TextUtils
import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.constant.RequestCommonCode
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.ergou.common.util.regex.RegexUtils
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.shop.contract.IShopDetailContract
import com.laka.ergou.mvp.shop.model.bean.*
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.UrlResponse
import com.laka.ergou.mvp.user.model.responsitory.UserCustomRetrofitHelper
import io.reactivex.disposables.Disposable
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * @Author:summer
 * @Date:2018/12/20
 * @Description:
 */
class ShopDetailModel : IShopDetailContract.IShopDetailModel {

    private lateinit var mView: IShopDetailContract.IShopDetailView
    private val mDisposableList = ArrayList<Disposable>()
    private val mCallList = ArrayList<Call>()

    override fun setView(v: IShopDetailContract.IShopDetailView) {
        this.mView = v
    }

    override fun onViewDestory() {
        mDisposableList.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposableList.clear()
        mCallList.forEach { it.cancel() }
        mCallList.clear()
    }

    /**
     * 获取产品详情、详情图、高额优惠券信息等
     * */
    override fun onLoadProductDetail(params: HashMap<String, String>, callBack: ResponseCallBack<CustomProductDetail>) {
        ShopDetailCustomRetrofitHelper.instance
                .onLoadProductDetail(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<CustomProductDetail, IShopDetailContract.IShopDetailView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    /**
     * 加载相关推荐商品列表
     * */
    override fun onLoadRecommendData(params: HashMap<String, String>, callBack: ResponseCallBack<ProductRecommendResponse>) {
        ShopDetailCustomRetrofitHelper.instance
                .onLoadRecommendData(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<ProductRecommendResponse, IShopDetailContract.IShopDetailView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    /**
     * 获取淘口令
     * */
    override fun onLoadTPwdCreate(params: HashMap<String, String>, callBack: ResponseCallBack<TPwdCreateResponse>) {
        ShopDetailCustomRetrofitHelper.instance
                .onGetPwdCreate(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<TPwdCreateResponse, IShopDetailContract.IShopDetailView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    /**
     * 获取淘口令二维码url
     * */
    override fun onLoadTklUrl(params: HashMap<String, String>, callBack: ResponseCallBack<HashMap<String, String>>) {
        ShopDetailCustomRetrofitHelper.instance
                .onLoadTklQrCodeUrl(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<HashMap<String, String>, IShopDetailContract.IShopDetailView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })

        //下载二维码图片


    }

    /**
     * 获取高额优惠券
     * */
    override fun onLoadHighVolumeCouponInfo(params: HashMap<String, String>, callBack: ResponseCallBack<HighVolumeInfoResponse>) {
        ShopDetailCustomRetrofitHelper.instance
                .onLoadHighVolumeCouponInfo(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<HighVolumeInfoResponse, IShopDetailContract.IShopDetailView>(mView, callBack, false) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    /**
     * 获取绑定渠道id的 h5 url
     * */
    override fun getUnionCodeUrl(params: HashMap<String, String>, callback: ResponseCallBack<UrlResponse>) {
        ShopDetailCustomRetrofitHelper.instance
                .getUnionCodeUrl(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<UrlResponse, IShopDetailContract.IShopDetailView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    /**
     * 绑定淘宝渠道ID到我们自己的服务器
     * */
    override fun handleUnionCode(params: HashMap<String, String>, callback: ResponseCallBack<RelationResponse>) {
        UserCustomRetrofitHelper.instance
                .handleUnionCode(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<RelationResponse, IShopDetailContract.IShopDetailView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    /**
     * 获取产品详情服务器地址
     * */
    override fun onGetProductDetailH5ServiceUrl(params: HashMap<String, String>, callBack: ResponseCallBack<HashMap<String, String>>) {
        UserCustomRetrofitHelper.instance
                .getProductDetailService(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<HashMap<String, String>, IShopDetailContract.IShopDetailView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    /**
     * 获取产品详情图
     * */
    override fun onGetProductDetailImageList2(url: String, callBack: ResponseCallBack<ArrayList<ImageDetail>>) {
        val httpClient = OkHttpClient()
        val request = Request.Builder()
                .url(url)
                .build()
        val call = httpClient.newCall(request)
        mCallList.add(call)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                LogUtils.info("请求失败")
                ApplicationUtils.getMainHandler().post {
                    callBack.onFail(object : BaseException(RequestCommonCode.LK_ERROR_REDIS_CONNECT_FATLED, "请求失败") {})
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                val code = response?.code()
                val response = response?.body()?.string()
                if (code == 200) {
                    val imageList = parseProductDetailImageList2(response)
                    ApplicationUtils.getMainHandler().post {
                        callBack.onSuccess(imageList)
                    }
                } else {
                    ApplicationUtils.getMainHandler().post {
                        callBack.onFail(object : BaseException(RequestCommonCode.LK_ERROR_REDIS_CONNECT_FATLED, "请求失败") {})
                    }
                }
            }
        })
    }

    /**
     * 解析图片详情列表数据
     * */
    private fun parseProductDetailImageList2(response: String?): ArrayList<ImageDetail> {
        if (response == null || TextUtils.isEmpty(response)) return ArrayList()
        val imageList = ArrayList<ImageDetail>()
        response?.let {
            try {
                val jsonObject = getJSONObjectFromJson(response)
                if (jsonObject.has("data")) {
                    val dataJSONObject = jsonObject.getJSONObject("data")
                    if (dataJSONObject.has("wdescContent")) {
                        val wdescJSONObject = dataJSONObject.getJSONObject("wdescContent")
                        if (wdescJSONObject.has("pages")) {
                            val pagesJSONObject = wdescJSONObject.getJSONArray("pages")
                            for (i in 0 until pagesJSONObject.length()) {
                                //<img size=620x171>//img.alicdn.com/imgextra/i3/21891593/O1CN0125l7jn1NddQcXsrLj_!!21891593.jpg</img>
                                val imageStr = pagesJSONObject.get(i) as? String
                                LogUtils.info("imageDetail-------$imageStr")
                                if (imageStr != null && !TextUtils.isEmpty(imageStr)) {
                                    val imageDetail = ImageDetail()
                                    var sizeStr = RegexUtils.findTergetStrForRegex(imageStr, "size=([a-zA-Z0-9]+)", 1)
                                    LogUtils.info("imageDetail-----------sizeStr=$sizeStr")
                                    if (sizeStr != null && !TextUtils.isEmpty(sizeStr)) {
                                        //620x960
                                        val widthHeigtArray = sizeStr.split("x")
                                        if (widthHeigtArray.size == 2) {
                                            imageDetail.imageWidth = widthHeigtArray[0]
                                            imageDetail.imageHeight = widthHeigtArray[1]
                                            LogUtils.info("imageDetail----width=${imageDetail.imageWidth}------height=${imageDetail.imageHeight}")
                                        }
                                    }
                                    val startIndex = imageStr.indexOf(">")
                                    val endIndex = imageStr.lastIndexOf("<")
                                    if (startIndex >= 0 && startIndex < imageStr.length && startIndex < endIndex && endIndex < imageStr.length) {
                                        val url = imageStr.substring(startIndex + 1, endIndex)
                                        if (url != null && !TextUtils.isEmpty(url)) {
                                            imageDetail.imageUrl = if (url.startsWith("http")) {
                                                url
                                            } else {
                                                "http:$url"
                                            }
                                        }
                                    }
                                    LogUtils.info("imageDetail-----$i----$imageDetail")
                                    imageList.add(imageDetail)
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return imageList
    }

    /**
     * 获取商品详情图片（客户端通过h5 url 获取）
     * */
    override fun onGetProductDetailImageList(url: String, callBack: ResponseCallBack<ArrayList<ImageDetail>>) {
        val httpClient = OkHttpClient()
        val request = Request.Builder()
                .url(url)
                .build()
        val call = httpClient.newCall(request)
        mCallList.add(call)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                LogUtils.info("请求失败")
                ApplicationUtils.getMainHandler().post {
                    callBack.onFail(object : BaseException(RequestCommonCode.LK_ERROR_REDIS_CONNECT_FATLED, "请求失败") {})
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                val code = response?.code()
                val response = response?.body()?.string()
                if (code == 200) {
                    val imageList = parseProductDetailImageList(response)
                    ApplicationUtils.getMainHandler().post {
                        callBack.onSuccess(imageList)
                    }
                } else {
                    ApplicationUtils.getMainHandler().post {
                        callBack.onFail(object : BaseException(RequestCommonCode.LK_ERROR_REDIS_CONNECT_FATLED, "请求失败") {})
                    }
                }
            }
        })
    }

    /**
     * 获取产品详情（通过获取到的产品服务器地址来获取产品详情）
     * */
    override fun onGetProductDetailForH5Url(url: String, callBack: ResponseCallBack<CustomProductDetail>) {
        val httpClient = OkHttpClient()
        val request = Request.Builder()
                .url(url)
                .build()
        val call = httpClient.newCall(request)
        mCallList.add(call)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                LogUtils.info("请求失败")
                ApplicationUtils.getMainHandler().post {
                    callBack.onFail(object : BaseException(RequestCommonCode.LK_ERROR_REDIS_CONNECT_FATLED, "请求失败") {})
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                val code = response?.code()
                val response = response?.body()?.string()
                if (code == 200) {
                    val sellerBean = parseSellerDataFromJson(response)
                    val productDetailVideos = parseProductDetailVideoFromJson(response)
                    val shopImageDetailUrl = parseProductImageDetailUrlFromJson(response)
                    val freeShipping = parseProdcutDetailFreeShipping(response)
                    val productDetail = CustomProductDetail()
                    productDetail.seller = sellerBean
                    productDetail.productVideo = productDetailVideos
                    productDetail.productImageDetailUrl = shopImageDetailUrl
                    productDetail.freeShipping = freeShipping
                    ApplicationUtils.getMainHandler().post {
                        callBack.onSuccess(productDetail)
                    }
                } else {
                    if (code != null) {
                        ApplicationUtils.getMainHandler().post {
                            callBack.onFail(object : BaseException(code, "请求失败") {})
                        }
                    } else {
                        ApplicationUtils.getMainHandler().post {
                            callBack.onFail(object : BaseException(RequestCommonCode.LK_ERROR_REDIS_CONNECT_FATLED, "请求失败") {})
                        }
                    }
                }
            }
        })
    }

    /**
     * 商品是否包邮
     * // 1：包邮  2：不包邮
     * */
    private fun parseProdcutDetailFreeShipping(response: String?): Int {
        if (TextUtils.isEmpty(response)) return 2
        // 1：包邮  2：不包邮
        var freeShippingUrl = 2
        try {
            response?.let {
                val jsonObject = getJSONObjectFromJson(response)
                if (jsonObject.has("data")) {
                    val dataObject = jsonObject.getJSONObject("data")
                    if (dataObject.has("apiStack")) {
                        val apiStackArray = dataObject.getJSONArray("apiStack")
                        if (apiStackArray != null && apiStackArray.length() > 0) {
                            val apiStackObject = apiStackArray.getJSONObject(0)
                            if (apiStackObject.has("value")) {
                                var videoResStr = apiStackObject.getString("value")
                                if (!TextUtils.isEmpty(videoResStr)) {
                                    val videoJSONObject = JSONObject(videoResStr)
                                    val itemObject = videoJSONObject.getJSONObject("delivery")
                                    val targetStr = itemObject.getString("postage")
                                    if (targetStr.contains("包邮")
                                            || targetStr.contains("0.0")
                                            || targetStr.contains("快递: 0.0")
                                            || targetStr.contains("免运费")
                                            || targetStr.contains("快递包邮")) {
                                        freeShippingUrl = 1
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return freeShippingUrl
    }

    /**
     * 解析商品详情图片url
     * */
    private fun parseProductImageDetailUrlFromJson(response: String?): String {
        if (TextUtils.isEmpty(response)) return ""
        var productDetailImageUrl = ""
        try {
            response?.let {
                val jsonObject = getJSONObjectFromJson(response)
                if (jsonObject.has("data")) {
                    val dataObject = jsonObject.getJSONObject("data")
                    if (dataObject.has("item")) {
                        var itemJSONObject = dataObject.getJSONObject("item")
                        itemJSONObject?.let {
                            productDetailImageUrl = if (itemJSONObject.getString("moduleDescUrl").startsWith("http")) {
                                "${itemJSONObject.getString("moduleDescUrl")}"
                            } else {
                                "http:${itemJSONObject.getString("moduleDescUrl")}"
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return productDetailImageUrl
    }

    /**
     * 解析视频数据
     * */
    private fun parseProductDetailVideoFromJson(response: String?): ProductDetailVideos {
        if (TextUtils.isEmpty(response)) return ProductDetailVideos()
        val productVideo = ProductDetailVideos()
        try {
            response?.let {
                val jsonObject = getJSONObjectFromJson(response)
                if (jsonObject.has("data")) {
                    val dataObject = jsonObject.getJSONObject("data")
                    if (dataObject.has("apiStack")) {
                        val apiStackArray = dataObject.getJSONArray("apiStack")
                        if (apiStackArray != null && apiStackArray.length() > 0) {
                            val apiStackObject = apiStackArray.getJSONObject(0)
                            if (apiStackObject.has("value")) {
                                var videoResStr = apiStackObject.getString("value")
                                if (!TextUtils.isEmpty(videoResStr)) {
                                    val videoJSONObject = JSONObject(videoResStr)
                                    if (videoJSONObject.has("item")) {
                                        val itemObject = videoJSONObject.getJSONObject("item")
                                        if (itemObject.has("videos")) {
                                            val videoArray = itemObject.getJSONArray("videos")
                                            if (videoArray != null && videoArray.length() > 0) {
                                                val videoObject = videoArray.getJSONObject(0)
                                                productVideo.videoId = "${videoObject.getString("videoId")}"
                                                productVideo.videoUrl = if (videoObject.getString("url").startsWith("http")) {
                                                    "${videoObject.getString("url")}"
                                                } else {
                                                    "http:${videoObject.getString("url")}"
                                                }
                                                productVideo.videoThumbnailURL = "${videoObject.getString("videoThumbnailURL")}"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return productVideo
    }

    /**
     * 解析商家信息
     * */
    private fun parseSellerDataFromJson(response: String?): SellerBean {
        if (TextUtils.isEmpty(response)) return SellerBean()
        val sellerResponse = SellerBean()
        try {
            response?.let {
                val jsonObject = getJSONObjectFromJson(response)
                if (jsonObject.has("data")) {
                    val dataObject = jsonObject.getJSONObject("data")
                    if (dataObject.has("seller")) {
                        val sellerObject = dataObject.getJSONObject("seller")
                        //解析店铺名称和icon
                        sellerResponse.shopName = "${sellerObject.getString("shopName")}"
                        sellerResponse.shopIcon = if (sellerObject.getString("shopIcon").startsWith("http")) {
                            "${sellerObject.getString("shopIcon")}"
                        } else {
                            "http:${sellerObject.getString("shopIcon")}"
                        }
                        //解析店铺评论信息
                        val evaluatesArray = sellerObject.getJSONArray("evaluates")
                        val evaluatesList = ArrayList<Evaluate>()
                        for (i in 0 until evaluatesArray.length()) {
                            val targetEvaluate = Evaluate()
                            val evaluatesObject = evaluatesArray.getJSONObject(i)
                            try {
                                targetEvaluate.title = "${evaluatesObject.getString("title")}"
                                targetEvaluate.score = "${evaluatesObject.getString("score")}"
                                targetEvaluate.level = "${evaluatesObject.getString("level")}"
                                targetEvaluate.levelText = "${evaluatesObject.getString("levelText")}"
                                targetEvaluate.levelTextColor = "${evaluatesObject.getString("levelTextColor")}"
                                targetEvaluate.levelBackgroundColor = "${evaluatesObject.getString("levelBackgroundColor")}"
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            evaluatesList.add(targetEvaluate)
                        }
                        LogUtils.info("evaluatesList---------$evaluatesList")
                        sellerResponse.evaluates = evaluatesList
                        return sellerResponse
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return SellerBean()
    }

    /**
     * 解析商品详情image list
     * */
    private fun parseProductDetailImageList(response: String?): ArrayList<ImageDetail> {
        if (TextUtils.isEmpty(response)) return ArrayList()
        val imageDetailList = ArrayList<ImageDetail>()
        try {
            response?.let {
                val jsonObject = JSONObject(response)
                if (jsonObject.has("data")) {
                    val dataObject = jsonObject.getJSONObject("data")
                    if (dataObject.has("children")) {
                        val childrenArray = dataObject.getJSONArray("children")
                        if (childrenArray != null && childrenArray.length() > 0) {
                            for (i in 0 until childrenArray.length()) {
                                val childrenObject = childrenArray.getJSONObject(i)
                                if (childrenObject.has("params")) {
                                    val paramsJSONObject = childrenObject.getJSONObject("params")
                                    if (paramsJSONObject.has("picUrl") && paramsJSONObject.has("size")) {
                                        val imageDetail = ImageDetail()
                                        val picUrl = paramsJSONObject.getString("picUrl")
                                        val sizeJSONObject = paramsJSONObject.getJSONObject("size")
                                        val width = sizeJSONObject.getString("width")
                                        val height = sizeJSONObject.getString("height")
                                        imageDetail.imageHeight = height
                                        imageDetail.imageWidth = width
                                        imageDetail.imageUrl = if (picUrl.startsWith("http")) {
                                            "$picUrl"
                                        } else {
                                            "http:$picUrl"
                                        }
                                        imageDetailList.add(imageDetail)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return imageDetailList
    }

    /**
     * 获取JSONObject
     * */
    private fun getJSONObjectFromJson(response: String): JSONObject {
        if (TextUtils.isEmpty(response)) return JSONObject()
        try {
            response?.let {
                val startIndex = response.indexOf("{")
                val endIndex = response.lastIndexOf("}")
                if (startIndex != -1 && endIndex != -1) {
                    val jsonStr = response.substring(startIndex, endIndex + 1)
                    return JSONObject(jsonStr)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return JSONObject()
    }


}