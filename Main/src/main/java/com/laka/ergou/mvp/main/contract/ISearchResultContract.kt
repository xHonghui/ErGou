package com.laka.ergou.mvp.main.contract

import com.laka.androidlib.mvp.IBaseListView
import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.shopping.center.model.bean.BaseProduct
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.shopping.center.model.bean.ShoppingResponse
import io.reactivex.Observable

/**
 * @Author:Rayman
 * @Date:2019/1/12
 * @Description:主页---搜索页面结果Contract
 */
interface ISearchResultContract {

    interface ISearchResultModel:IBaseModel<ISearchResultView> {

        /**
         * description:根据KeyWord，综合，销量升降序，价格升降序去获取列表
         **/
        fun getSearchResult(params:HashMap<String,String>,callBack: ResponseCallBack<ShoppingResponse>)

    }

    interface ISearchResultPresenter : IBasePresenter<ISearchResultView> {

        fun getSearchResult(keyWord: String,
                            @HomeConstant.SEARCH_SORT_TYPE sortType: String,
                            isAsc: Boolean,
                            isCoupon: Boolean,
                            page: Int)

    }

    interface ISearchResultView : IBaseListView<ProductWithCoupon>{
        /**
         * 列表数据加载失败回调
         * */
        fun onLoadListDataFail(msg: String, page: Int)
    }
}