package com.laka.ergou.mvp.shopping.center.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.shopping.center.contract.IShoppingSpecialContract
import com.laka.ergou.mvp.shopping.center.model.bean.ProductListResponse
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/4/29
 * @Description:
 */
class ShoppingSpecialModel : IShoppingSpecialContract.IShoppingSpecialModel {

    private lateinit var mView: IShoppingSpecialContract.IShoppingSpecialView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: IShoppingSpecialContract.IShoppingSpecialView) {
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

    override fun onLoadProductSpecialData(params: HashMap<String, String>, callBack: ResponseCallBack<ProductListResponse>) {
        ShoppingCustomRetrofitHelper.INSTANCE
                .getProductListByCategoryId(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<ProductListResponse, IShoppingSpecialContract.IShoppingSpecialView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}