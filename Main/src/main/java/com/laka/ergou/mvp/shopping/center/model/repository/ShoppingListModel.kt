package com.laka.ergou.mvp.shopping.center.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.shopping.center.contract.IShoppingListContract
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.shopping.center.model.bean.ShoppingListResponse
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * @Author:Rayman
 * @Date:2018/12/18
 * @Description:首页商品列表model
 */
class ShoppingListModel : IShoppingListContract.IShoppingListModel {

    private lateinit var mView: IShoppingListContract.IShoppingView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: IShoppingListContract.IShoppingView) {
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
     * 获取商品列表数据
     * */
    override fun getGoodsData(params: HashMap<String, String>, callBack: ResponseCallBack<ShoppingListResponse>) {
        ShoppingCustomRetrofitHelper.INSTANCE
                .getProductListBySelect(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<ShoppingListResponse, IShoppingListContract.IShoppingView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}
