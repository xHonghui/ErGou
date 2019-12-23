package com.laka.ergou.mvp.advertbanner.model.respositroy

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.main.contract.IAdvertBannerConstract
import com.laka.ergou.mvp.advertbanner.model.bean.AdvertBannerBean
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * @Author:summer
 * @Date:2019/7/27
 * @Description:
 */
class AdvertBannerModel : IAdvertBannerConstract.IAdvertBannerModel {

    private val mDisposableList = ArrayList<Disposable>()
    private lateinit var mView: IAdvertBannerConstract.IAdvertBannerView

    override fun setView(v: IAdvertBannerConstract.IAdvertBannerView) {
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

    override fun onLoadAdvertBannerData(params: HashMap<String, String>, callBack: ResponseCallBack<ArrayList<AdvertBannerBean>>) {
        AdvertBannerRetrofitHelper.instance
                .onLoadAdvertBannerData(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<ArrayList<AdvertBannerBean>, IAdvertBannerConstract.IAdvertBannerView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}