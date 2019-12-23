package com.laka.ergou.mvp.user.model.responsitory

import com.laka.androidlib.util.rx.RxResponseComposer
import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.commission.model.bean.CommissionBean
import com.laka.ergou.mvp.commission.model.repository.CommissionRetrofixHelper
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.user.contract.IUserHomeConstract
import com.laka.ergou.mvp.user.model.bean.BannerAdvBean
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.UrlResponse
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 * @Author:summer
 * @Date:2019/1/14
 * @Description:
 */
class UserHomeModel : IUserHomeConstract.IUserHomeModel {

    private lateinit var mView: IUserHomeConstract.IUserHomeView
    private val mDisposableList = java.util.ArrayList<Disposable>()

    override fun setView(v: IUserHomeConstract.IUserHomeView) {
        this.mView = v
    }

    override fun onViewDestory() {
        mDisposableList.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposableList.clear()
    }

    override fun onLoadUserInfo(params: HashMap<String, String?>, callBack: ResponseCallBack<UserBean>) {
        UserCustomRetrofitHelper.instance
                .onLoadUserInfo(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<UserBean, IUserHomeConstract.IUserHomeView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun getUserCommissionData(): Observable<CommissionBean> {
        val params = HashMap<String, String>()
        return CommissionRetrofixHelper.instance.onLoadMyCommissonData(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
    }

    override fun getBannerAdv(params: HashMap<String, String>): Observable<ArrayList<BannerAdvBean>> {
        return UserRetrofitHelper.instance
                .getUserBannerAdv(params)
                .compose(RxResponseComposer.flatResponse())
    }


    override fun getUnionCodeUrl(params: HashMap<String, String>, callback: ResponseCallBack<UrlResponse>) {
        UserCustomRetrofitHelper.instance
                .getUnionCodeUrl(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<UrlResponse, IUserHomeConstract.IUserHomeView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun handleUnionCode(params: HashMap<String, String>, callback: ResponseCallBack<RelationResponse>) {
        UserCustomRetrofitHelper.instance
                .handleUnionCode(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<RelationResponse, IUserHomeConstract.IUserHomeView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}