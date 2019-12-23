package com.laka.ergou.mvp.shopping.search.contract

import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.mvp.IBaseView
import com.laka.ergou.mvp.shopping.search.model.bean.SearchHistoryBean
import com.laka.ergou.mvp.shopping.center.model.bean.BaseProduct
import io.reactivex.Observable

/**
 * @Author:Rayman
 * @Date:2019/1/7
 * @Description:搜索主页面Contract类
 */
interface ISearchHomeContract {

    interface ISearchHomeModel {

        /**
         * description:获取搜索历史数据
         **/
        fun getSearchHistoryData(): Observable<List<SearchHistoryBean>>

        /**
         * description:获取搜索热门数据
         **/
        fun getSearchHotData(): Observable<List<String>>

        /**
         * description:保存搜索历史记录
         **/
        fun saveSearchHistoryData(keyWord: String)

        /**
         * description:删除选中的历史记录
         **/
        fun deleteSearchHistoryData(keyWord: String)

        /**
         * description:清除搜索历史记录
         **/
        fun clearSearchHistoryData()
    }

    interface ISearchHomePresenter : IBasePresenter<ISearchHomeView> {

        fun getSearchHistoryData()

        fun getSearchHotData()

        fun saveSearchHistoryData(keyWord: String)

        fun deleteSearchHistoryData(keyWord: String)

        fun clearSearchHistoryData()
    }

    interface ISearchHomeView : IBaseView<List<BaseProduct>> {

        /**
         * description:热门数据回调
         **/
        fun showHotData(hotData: List<String>)

        /**
         * description:历史数据回调
         **/
        fun showHistoryData(historyData: List<SearchHistoryBean>)
    }
}