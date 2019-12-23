package com.laka.ergou.mvp.main.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.common.util.rx.RxSubscriber
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.contract.ISearchResultContract
import com.laka.ergou.mvp.shopping.center.constant.ShoppingApiConstant
import com.laka.ergou.mvp.shopping.search.model.repository.SearchResultModel
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.shopping.center.model.bean.ShoppingResponse
import io.reactivex.disposables.Disposable

/**
 * @Author:Rayman
 * @Date:2019/1/12
 * @Description:主页---搜索结果页Presenter
 */
class SearchResultPresenter : ISearchResultContract.ISearchResultPresenter {

    private var mView: ISearchResultContract.ISearchResultView? = null
    private var mModel: ISearchResultContract.ISearchResultModel = SearchResultModel()
    private var mDisposableList: ArrayList<Disposable> = ArrayList()

    override fun setView(view: ISearchResultContract.ISearchResultView) {
        this.mView = view
        mModel.setView(view)
    }

    override fun onViewCreate() {

    }

    override fun onViewDestroy() {
        // View层destroy释放disposable
        mDisposableList.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposableList.clear()
        mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun getSearchResult(keyWord: String, sortType: String, isAsc: Boolean, isCoupon: Boolean, page: Int) {
        val defaultPageSize = ShoppingApiConstant.PARAM_DEFAULT_PAGE_SIZE
        var params = HashMap<String, String>()
        params[ShoppingApiConstant.PARAM_END_PRICE] = "10000"
        params[ShoppingApiConstant.PARAM_SENSITIVE_FUZZY] = "true"
        params[ShoppingApiConstant.PARAM_FORMAT] = "json"
        params[ShoppingApiConstant.PARAM_HAS_COUPON] = if (isCoupon) "true" else "false"
        params[ShoppingApiConstant.PARAM_PAGE] = page.toString()
        params[ShoppingApiConstant.PARAM_PAGE_SIZE] = "$defaultPageSize"
        params[ShoppingApiConstant.PARAM_Q] = keyWord
        // 只有销量和价格排序的时候，才需要添加这个sort字段
        when (sortType) {
            HomeConstant.SEARCH_SORT_COUNT -> {
                params[ShoppingApiConstant.PARAM_SORT] = if (isAsc) ShoppingApiConstant.PARAM_SORT_COUNT_ASC else ShoppingApiConstant.PARAM_SORT_COUNT_DESC
            }
            HomeConstant.SEARCH_SORT_PRICE -> {
                params[ShoppingApiConstant.PARAM_SORT] = if (isAsc) ShoppingApiConstant.PARAM_SORT_PRICE_ASC else ShoppingApiConstant.PARAM_SORT_PRICE_DESC
            }
        }
        params[ShoppingApiConstant.PARAM_METHOD] = ShoppingApiConstant.API_GET_PRODUCT_LIST_BY_TYPE
        mModel.getSearchResult(params, object : ResponseCallBack<ShoppingResponse> {
            override fun onSuccess(t: ShoppingResponse) {
                LogUtils.info("search_result:$t")
                mView?.onGetListDataSuccess(object : BaseListBean<ProductWithCoupon>() {
                    override fun getList(): List<ProductWithCoupon> {
                        return t.baseProductList
                    }

                    override fun getPageTotalCount(): Int {
                        val temp = t.total_result % ShoppingApiConstant.PARAM_DEFAULT_PAGE_SIZE
                        return if (temp != 0) {
                            (t.total_result / ShoppingApiConstant.PARAM_DEFAULT_PAGE_SIZE) + 1
                        } else {
                            t.total_result / ShoppingApiConstant.PARAM_DEFAULT_PAGE_SIZE
                        }
                    }
                })
            }

            override fun onFail(e: BaseException?) {
                LogUtils.info("search_result_error:${e?.errorMsg}")
                ToastHelper.showCenterToast(e?.errorMsg)
                mView?.onLoadListDataFail("${e?.errorMsg}", page)
            }
        })
    }
}