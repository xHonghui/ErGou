package com.laka.ergou.mvp.main.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.main.contract.ITmallProductContract
import com.laka.ergou.mvp.shop.model.bean.CustomProductDetail
import com.laka.ergou.mvp.shop.model.bean.HighVolumeInfoResponse
import com.laka.ergou.mvp.shop.model.bean.TPwdCreateResponse
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.UrlResponse
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/4/25
 * @Description:天貓详情页面，截取商品ID，查找优惠券
 */
class TmallProductModel : ITmallProductContract.ITmallProductModel {

    private lateinit var mView: ITmallProductContract.ITmallProductView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: ITmallProductContract.ITmallProductView) {
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

    /**
     * 加载商品详情
     * */
    override fun onLoadDetailData(params: HashMap<String, String>, callBack: ResponseCallBack<CustomProductDetail>) {
        HomeRetrofitHelper.newProductInstance()
                .onLoadProductDetail(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<CustomProductDetail, ITmallProductContract.ITmallProductView>(mView, callBack) {
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
        HomeRetrofitHelper.newProductInstance()
                .onGetPwdCreate(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<TPwdCreateResponse, ITmallProductContract.ITmallProductView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    /**
     * 获取绑定渠道ID的url
     * */
    override fun getUnionCodeUrl(params: HashMap<String, String>, callback: ResponseCallBack<UrlResponse>) {
        HomeRetrofitHelper.newProductInstance()
                .getUnionCodeUrl(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<UrlResponse, ITmallProductContract.ITmallProductView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    /**
     * 绑定渠道ID
     * */
    override fun handleUnionCode(params: HashMap<String, String>, callback: ResponseCallBack<RelationResponse>) {
        HomeRetrofitHelper.newProductInstance()
                .handleUnionCode(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<RelationResponse, ITmallProductContract.ITmallProductView>(mView, callback) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun onLoadHighVolumeCouponInfo(params: HashMap<String, String>, callBack: ResponseCallBack<HighVolumeInfoResponse>) {
        HomeRetrofitHelper.newProductInstance()
                .onLoadHighVolumeCouponInfo(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<HighVolumeInfoResponse, ITmallProductContract.ITmallProductView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}