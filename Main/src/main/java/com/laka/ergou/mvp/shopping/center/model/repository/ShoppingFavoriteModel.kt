package com.laka.ergou.mvp.shopping.center.model.repository

import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.shopping.center.contract.IShoppingFavoriteContract
import com.laka.ergou.mvp.shopping.center.model.bean.ShoppingResponse
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:Rayman
 * @Date:2019/1/30
 * @Description:商品精选页面Model实现类
 */
class ShoppingFavoriteModel : IShoppingFavoriteContract.IShoppingFavoriteModel {

    private lateinit var mView: IShoppingFavoriteContract.IShoppingFavoriteView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: IShoppingFavoriteContract.IShoppingFavoriteView) {
        this.mView = v
    }

    override fun onViewDestory() {
        mDisposableList.forEach {
            if (!it.isDisposed){
                it.dispose()
            }
        }
        mDisposableList.clear()
    }

    /**
     * description:获取精选数据
     **/
    override fun getFavoriteProductList(params: HashMap<String, String>, callBack: ResponseCallBack<ShoppingResponse>) {
        ShoppingCustomRetrofitHelper.INSTANCE
                .getProductListByFavorite(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<ShoppingResponse, IShoppingFavoriteContract.IShoppingFavoriteView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}