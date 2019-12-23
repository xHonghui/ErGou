package com.laka.ergou.mvp.user.contract

import android.app.Activity
import android.content.Context
import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.mvp.IBaseView
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.mvp.commission.model.bean.CommissionBean
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.user.model.bean.BannerAdvBean
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.UrlResponse
import io.reactivex.Observable

/**
 * @Author:summer
 * @Date:2019/1/14
 * @Description:
 */
interface IUserHomeConstract {

    interface IUserHomeView : IBaseLoadingView<UserBean> {
        fun onLoadUserInfoSuccess(userBean: UserBean)
        fun loadUserCommissionSuccess(commissionBean: CommissionBean)
        fun getBannerDataSuccess(bannerData: ArrayList<BannerAdvBean>)
        fun onTaoBaoAuthorSuccess()
        fun getUnionCodeUrlSuccess(url: String)
        fun handleUnionCodeSuccess()
        fun authorInvalid()
    }

    interface IUserHomePresenter : IBasePresenter<IUserHomeView> {
        fun onLoadUserInfo(context: Context)
        fun getUserCommissionData()
        fun onTaoBaoAuthor(activity: Activity)
        fun getBannerAdv()
        fun getUnionCodeUrl()
        fun handleUnionCode(code: String, state: String)
    }

    interface IUserHomeModel : IBaseModel<IUserHomeView> {
        fun onLoadUserInfo(params: HashMap<String, String?>, callBack: ResponseCallBack<UserBean>)

        fun getUserCommissionData(): Observable<CommissionBean>

        fun getBannerAdv(params: HashMap<String, String>): Observable<ArrayList<BannerAdvBean>>

        fun getUnionCodeUrl(params: HashMap<String, String>, callback: ResponseCallBack<UrlResponse>)

        fun handleUnionCode(params: HashMap<String, String>, callback: ResponseCallBack<RelationResponse>)
    }

}