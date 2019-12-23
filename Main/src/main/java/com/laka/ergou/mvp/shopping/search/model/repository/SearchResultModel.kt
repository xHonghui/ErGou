package com.laka.ergou.mvp.shopping.search.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.main.contract.ISearchResultContract
import com.laka.ergou.mvp.shopping.center.model.bean.ShoppingResponse
import com.laka.ergou.mvp.shopping.center.model.repository.ShoppingCustomRetrofitHelper
import io.reactivex.disposables.Disposable
import java.util.ArrayList

/**
 * @Author:Rayman
 * @Date:2019/1/12
 * @Description:主页---搜索结果页Model
 */
class SearchResultModel : ISearchResultContract.ISearchResultModel {

    private lateinit var mView: ISearchResultContract.ISearchResultView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: ISearchResultContract.ISearchResultView) {
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

    override fun getSearchResult(params: HashMap<String, String>, callBack: ResponseCallBack<ShoppingResponse>) {
        ShoppingCustomRetrofitHelper.INSTANCE
                .getProductListByType(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<ShoppingResponse, ISearchResultContract.ISearchResultView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }
}