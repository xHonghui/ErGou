package com.laka.ergou.mvp.shopping.search.model.repository

import android.text.TextUtils
import com.laka.androidlib.data_structure.LimitQueue
import com.laka.androidlib.net.utils.parse.ParseUtil
import com.laka.androidlib.util.ListUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.rx.RxResponseComposer
import com.laka.androidlib.util.rx.RxSpLocalComposer
import com.laka.ergou.R
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import com.laka.ergou.mvp.shopping.center.model.bean.ProductParentType
import com.laka.ergou.mvp.shopping.center.model.bean.ShoppingFavoriteResponse
import com.laka.ergou.mvp.shopping.center.model.repository.ShoppingRetrofitHelper
import com.laka.ergou.mvp.shopping.search.constant.ShoppingSearchConstant
import com.laka.ergou.mvp.shopping.search.contract.ISearchHomeContract
import com.laka.ergou.mvp.shopping.search.model.bean.SearchHistoryBean
import com.laka.ergou.mvp.shopping.search.model.bean.SearchHotResponse
import io.reactivex.Observable

/**
 * @Author:Rayman
 * @Date:2019/1/11
 * @Description:搜索主页Model
 */
class SearchHomeModel : ISearchHomeContract.ISearchHomeModel {
    private var historyLimitQueue = LimitQueue<String>(5)

    override fun saveSearchHistoryData(keyWord: String) {
        if (!TextUtils.isEmpty(keyWord)) {
            historyLimitQueue.offerFirst(keyWord, false)
            val jsonStr = ParseUtil.toJson(historyLimitQueue)
            //LogUtils.info("添加数据到SP:${jsonStr}\n原列表数据：${historyLimitQueue.linkedList}")
            SPHelper.putString(HomeConstant.SEARCH_HISTORY, jsonStr)
        }
    }

    override fun deleteSearchHistoryData(keyWord: String) {
        if (!TextUtils.isEmpty(keyWord)) {
            historyLimitQueue.remove(keyWord)
            val jsonStr = ParseUtil.toJson(historyLimitQueue)
            SPHelper.putString(HomeConstant.SEARCH_HISTORY, jsonStr)
        }
    }

    override fun clearSearchHistoryData() {
        historyLimitQueue.clearAll()
        SPHelper.putString(HomeConstant.SEARCH_HISTORY, "")
    }

    override fun getSearchHistoryData(): Observable<List<SearchHistoryBean>> {
        return Observable.create {
            val historyLocalData = SPHelper.getString(HomeConstant.SEARCH_HISTORY, "")
            val historyData = ArrayList<SearchHistoryBean>()
            if (!TextUtils.isEmpty(historyLocalData)) {
                historyLimitQueue = ParseUtil.parseJson<LimitQueue<String>>(historyLocalData, LimitQueue::class.java)
                //LogUtils.info("输出SP缓存数据:${historyLocalData}，\n输出列表数据：${historyLimitQueue}" +
                //        "\n输出LinkedList数据：${historyLimitQueue.linkedList}")
                if (historyLimitQueue != null && ListUtils.isNotEmpty(historyLimitQueue.linkedList)) {
                    for (datum in historyLimitQueue.linkedList) {
                        historyData.add(SearchHistoryBean(datum as String))
                    }
                }
            }
            it.onNext(historyData)
            it.onComplete()
        }
    }

    override fun getSearchHotData(): Observable<List<String>> {
        // 处理Sp数据，假若Sp有数据则先返回Sp的数据。再请求网络更新Sp数据
        val localResponse = SPHelper.getObject(ShoppingSearchConstant.SP_SHOPPING_SEARCH_FILE_NAME,
                ShoppingSearchConstant.SP_KEY_SHOPPING_SEARCH_HOT_DATA, SearchHotResponse::class.java)

        // 处理本地数据
        val localTitleList = if (localResponse == null) {
            ArrayList()
        } else {
            localResponse?.keyWords
        }
        var resultObservable: Observable<List<String>>

        // 本地数据为空，请求数据
        if (ListUtils.isEmpty(localTitleList)) {
            resultObservable = SearchRetrofitHelper.INSTANCE.getSearchHotWord()
                    .compose(RxResponseComposer.flatResponse())
                    .compose(RxSpLocalComposer.compose(ShoppingSearchConstant.SP_SHOPPING_SEARCH_FILE_NAME,
                            ShoppingSearchConstant.SP_KEY_SHOPPING_SEARCH_HOT_DATA))
                    .flatMap({ response ->
                        // 处理数据并返回
                        val titleList = response?.keyWords
                        return@flatMap Observable.create<List<String>> {
                            it.onNext(titleList)
                            it.onComplete()
                        }
                    })
        } else {
            // 本地有数据的情况下，返回本地数据。
            resultObservable = Observable.create<List<String>> {
                it.onNext(localTitleList)
                it.onComplete()
            }

            // 更新本地Sp数据，并返回
            SearchRetrofitHelper.INSTANCE.getSearchHotWord()
                    .compose(RxResponseComposer.flatResponse())
                    .compose(RxSpLocalComposer.compose(ShoppingSearchConstant.SP_SHOPPING_SEARCH_FILE_NAME,
                            ShoppingSearchConstant.SP_KEY_SHOPPING_SEARCH_HOT_DATA))
                    .subscribe({ }, { })
        }
        return resultObservable
    }
}