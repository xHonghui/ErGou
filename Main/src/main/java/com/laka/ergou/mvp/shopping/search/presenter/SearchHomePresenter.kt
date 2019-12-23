package com.laka.ergou.mvp.shopping.search.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.ergou.common.util.rx.RxSubscriber
import com.laka.ergou.mvp.shopping.search.contract.ISearchHomeContract
import com.laka.ergou.mvp.shopping.search.model.repository.SearchHomeModel
import com.laka.ergou.mvp.shopping.search.model.bean.SearchHistoryBean
import io.reactivex.disposables.Disposable

/**
 * @Author:Rayman
 * @Date:2019/1/11
 * @Description:搜索主页P层
 */
class SearchHomePresenter : ISearchHomeContract.ISearchHomePresenter {

    private var mView: ISearchHomeContract.ISearchHomeView? = null
    private var mModel: ISearchHomeContract.ISearchHomeModel = SearchHomeModel()
    private var mDisposableList: ArrayList<Disposable> = ArrayList()

    override fun setView(homeView: ISearchHomeContract.ISearchHomeView) {
        this.mView = homeView
    }

    override fun onViewCreate() {

    }

    override fun onViewDestroy() {
        mDisposableList?.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposableList.clear()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun saveSearchHistoryData(keyWord: String) {
        mModel?.saveSearchHistoryData(keyWord)
    }

    override fun deleteSearchHistoryData(keyWord: String) {
        mModel?.deleteSearchHistoryData(keyWord)
    }

    override fun clearSearchHistoryData() {
        mModel?.clearSearchHistoryData()
    }

    override fun getSearchHistoryData() {
        mModel?.getSearchHistoryData()
                .subscribe(object : RxSubscriber<List<SearchHistoryBean>, ISearchHomeContract.ISearchHomeView>(mView) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }

                    override fun onNext(t: List<SearchHistoryBean>) {
                        super.onNext(t)
                        mView?.showHistoryData(t)
                    }
                })
    }

    override fun getSearchHotData() {
        mModel?.getSearchHotData()
                .subscribe(object : RxSubscriber<List<String>, ISearchHomeContract.ISearchHomeView>(mView) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }

                    override fun onNext(t: List<String>) {
                        super.onNext(t)
                        mView?.showHotData(t)
                    }
                })
    }

}