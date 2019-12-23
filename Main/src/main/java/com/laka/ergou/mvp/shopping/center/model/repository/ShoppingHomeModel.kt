package com.laka.ergou.mvp.shopping.center.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.shopping.center.contract.IShoppingHomeContract
import com.laka.ergou.mvp.shopping.center.model.bean.AdvertBean
import com.laka.ergou.mvp.shopping.center.model.bean.HomePageResponse
import com.laka.ergou.mvp.shopping.center.model.bean.HomeUrlBean
import com.laka.ergou.mvp.shopping.center.model.bean.ShoppingFavoriteResponse
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:Rayman
 * @Date:2018/12/24
 * @Description:商品主页Model类
 */
class ShoppingHomeModel : IShoppingHomeContract.IShoppingHomeModel {


    private lateinit var mView: IShoppingHomeContract.IShoppingHomeView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: IShoppingHomeContract.IShoppingHomeView) {
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

    /*获取分类数据（new）*/
    override fun getShoppingParentType(callBack: ResponseCallBack<ShoppingFavoriteResponse>) {
        ShoppingCustomRetrofitHelper.INSTANCE
                .getFavoritesProductBannerData()
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<ShoppingFavoriteResponse, IShoppingHomeContract.IShoppingHomeView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    /*获取首页数据（banner、专题、分类、活动专区）*/
    override fun getHomePageData(params: HashMap<String, String>, callBack: ResponseCallBack<HomePageResponse>) {
        ShoppingCustomRetrofitHelper.INSTANCE
                .getHomePageData(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<HomePageResponse, IShoppingHomeContract.IShoppingHomeView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    //获取广告页信息
    override fun onGetAdvert(params: HashMap<String, String>, callBack: ResponseCallBack<List<AdvertBean>>) {
        ShoppingCustomRetrofitHelper.INSTANCE
                .getAdvert(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<List<AdvertBean>, IShoppingHomeContract.IShoppingHomeView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun onGetH5Url(callBack: ResponseCallBack<HomeUrlBean>) {

        ShoppingCustomRetrofitHelper.INSTANCE
                .getH5Url()
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<HomeUrlBean, IShoppingHomeContract.IShoppingHomeView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}