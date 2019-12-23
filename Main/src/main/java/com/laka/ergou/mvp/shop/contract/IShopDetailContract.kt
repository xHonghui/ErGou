package com.laka.ergou.mvp.shop.contract

import android.content.Context
import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.mvp.shop.model.bean.*
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.UrlResponse

/**
 * @Author:summer
 * @Date:2018/12/20
 * @Description:
 */
interface IShopDetailContract {

    interface IShopDetailView : IBaseLoadingView<CustomProductDetail> {
        fun onLoadRecommendDataSuccess(list:ArrayList<ProductWithCoupon>)
        fun onLoadProductDetailSuccess(result: CustomProductDetail)
        fun onLoadProductDetailFail()
        fun onLoadListDataFail()
        fun onLoadHighVolumeCouponInfoSuccess(result: HighVolumeInfoResponse, type: Int)
        fun onLoadHighVolumeCouponInfoFail(msg: String, type: Int)
        fun onTaoBaoAuthorSuccess(type: Int)
        fun getUnionCodeUrlSuccess(url: String)
        fun handleUnionCodeSuccess()
        fun handleUnionCodeFail()
        fun onGetProductDetailH5ServiceUrlSuccess(resultMap: HashMap<String, String>)
        fun onGetProductDetailH5ServiceUrlFail(msg: String)
        fun onGetProductDetailForH5UrlSuccess(result: CustomProductDetail)
        fun onGetProductDetailForH5UrlFail(msg: String)
        fun onGetProcutDetailImageListSuccess(imageDetailList: ArrayList<ImageDetail>)
        fun onGetProcutDetailImageListFail(msg: String)
    }

    interface IShopDetailPresenter : IBasePresenter<IShopDetailView> {
        fun onLoadRecommendData(context: Context, itemId:String)
        fun onLoadProductDetail(productId: String)
        fun onLoadHighVolumeCouponInfo(productId: String, type: Int)
        fun onTaoBaoAuthor(type: Int)
        fun handleUnionCode(code: String, state: String)
        fun getUnionCodeUrl()
        fun onGetProductDetailH5ServiceUrl(productId: String)
        fun onGetProductDetailForH5Url(url: String)
        fun onGetProductDetailImageList(url: String)
        fun onGetProductDetailImageList2(url:String)
    }

    interface IShopDetailModel : IBaseModel<IShopDetailView> {
        fun onLoadProductDetail(params: HashMap<String, String>, callBack: ResponseCallBack<CustomProductDetail>)
        fun onLoadHighVolumeCouponInfo(params: HashMap<String, String>, callBack: ResponseCallBack<HighVolumeInfoResponse>)
        fun onLoadTPwdCreate(params: HashMap<String, String>, callBack: ResponseCallBack<TPwdCreateResponse>)
        fun getUnionCodeUrl(params: HashMap<String, String>, callback: ResponseCallBack<UrlResponse>)
        fun handleUnionCode(params: HashMap<String, String>, callback: ResponseCallBack<RelationResponse>)
        fun onLoadRecommendData(params: HashMap<String, String>, callBack: ResponseCallBack<ProductRecommendResponse>)
        fun onLoadTklUrl(params: HashMap<String, String>, callBack: ResponseCallBack<HashMap<String, String>>)
        fun onGetProductDetailH5ServiceUrl(params: HashMap<String, String>, callBack: ResponseCallBack<HashMap<String, String>>)
        fun onGetProductDetailForH5Url(url: String, callBack: ResponseCallBack<CustomProductDetail>)
        fun onGetProductDetailImageList(url: String, callBack: ResponseCallBack<ArrayList<ImageDetail>>)
        fun onGetProductDetailImageList2(url: String, callBack: ResponseCallBack<ArrayList<ImageDetail>>)
    }

}