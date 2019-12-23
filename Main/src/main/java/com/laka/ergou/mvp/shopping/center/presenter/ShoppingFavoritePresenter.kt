package com.laka.ergou.mvp.shopping.center.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.shopping.center.constant.ShoppingApiConstant
import com.laka.ergou.mvp.shopping.center.contract.IShoppingFavoriteContract
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.shopping.center.model.bean.ShoppingResponse
import com.laka.ergou.mvp.shopping.center.model.repository.ShoppingFavoriteModel
import io.reactivex.disposables.Disposable

/**
 * @Author:Rayman
 * @Date:2019/1/30
 * @Description:商品精选页面Presenter实现类
 */
class ShoppingFavoritePresenter : IShoppingFavoriteContract.IShoppingFavoritePresenter {

    private var mView: IShoppingFavoriteContract.IShoppingFavoriteView? = null
    private var mListModel: IShoppingFavoriteContract.IShoppingFavoriteModel = ShoppingFavoriteModel()
    private var mDisposableList: ArrayList<Disposable> = ArrayList()

    override fun setView(view: IShoppingFavoriteContract.IShoppingFavoriteView) {
        mView = view
        mListModel.setView(view)
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
        mListModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun getFavoriteProductList(favoriteId: String, page: Int) {
        val params = HashMap<String, String>()
        params.put("favorites_id", favoriteId)
        params.put("field", ShoppingApiConstant.SEARCH_FIELD_KEY)
        params.put("format", "json")
        params.put("page_size", ShoppingApiConstant.PARAM_DEFAULT_PAGE_SIZE.toString())
        params.put("page_no", page.toString())
        mListModel?.getFavoriteProductList(params, object : ResponseCallBack<ShoppingResponse> {
            override fun onSuccess(t: ShoppingResponse) {
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
                ToastHelper.showCenterToast(e?.errorMsg)
            }
        })
    }

}