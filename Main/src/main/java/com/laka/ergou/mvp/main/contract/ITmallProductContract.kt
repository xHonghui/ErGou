package com.laka.ergou.mvp.main.contract

import android.app.Activity
import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.mvp.shop.model.bean.CustomProductDetail
import com.laka.ergou.mvp.shop.model.bean.HighVolumeInfoResponse
import com.laka.ergou.mvp.shop.model.bean.TPwdCreateResponse
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.UrlResponse

/**
 * @Author:summer
 * @Date:2019/4/25
 * @Description:天猫详情
 */
interface ITmallProductContract {

    interface ITmallProductView : IBaseLoadingView<CustomProductDetail> {
        fun onLoadHighVolumeCouponInfoSuccess(result: HighVolumeInfoResponse,type: Int)
        fun onLoadHightVolumeCouponInfoFail(msg:String,type:Int)
        fun onLoadDetailDataSuccess(result: CustomProductDetail)
        fun onLoadDetailDataFail(errorMsg: String)
        fun onLoadTPwdCreateSuccess(result: TPwdCreateResponse)
        fun onTaoBaoAuthorSuccess(type: Int)
        fun getUnionCodeUrlSuccess(url: String)
        fun handleUnionCodeSuccess()
        fun handleUnionCodeFail()
    }

    interface ITmallProductPresenter : IBasePresenter<ITmallProductView> {
        fun onLoadHighVolumeCouponInfo(productId: String,type: Int)
        fun onLoadDetailData(id: String)
        fun onLoadTPwdCreate(productId: String, title: String, url: String, logo: String)
        fun onCreateShareWorwds(pwd: String, product: CustomProductDetail): String
        fun onTaoBaoAuthor(activity: Activity, type: Int)
        fun handleUnionCode(code: String, state: String)
        fun getUnionCodeUrl()
    }

    interface ITmallProductModel : IBaseModel<ITmallProductView> {
        fun onLoadHighVolumeCouponInfo(params: HashMap<String, String>, callBack: ResponseCallBack<HighVolumeInfoResponse>)
        fun onLoadDetailData(params: HashMap<String, String>, callBack: ResponseCallBack<CustomProductDetail>)
        fun onLoadTPwdCreate(params: HashMap<String, String>, callBack: ResponseCallBack<TPwdCreateResponse>)
        fun getUnionCodeUrl(params: HashMap<String, String>, callback: ResponseCallBack<UrlResponse>)
        fun handleUnionCode(params: HashMap<String, String>, callback: ResponseCallBack<RelationResponse>)
    }

}