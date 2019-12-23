package com.laka.ergou.mvp.shopping.center.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.text.TextUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.common.util.rx.RxSubscriber
import com.laka.ergou.mvp.shopping.center.constant.ShoppingApiConstant
import com.laka.ergou.mvp.shopping.center.contract.IShoppingListContract
import com.laka.ergou.mvp.shopping.center.model.bean.*
import com.laka.ergou.mvp.shopping.center.model.repository.ShoppingListModel
import io.reactivex.disposables.Disposable

/**
 * @Author:Rayman
 * @Date:2018/12/20
 * @Description:商品列表P层
 */
class ShoppingListPresenter : IShoppingListContract.IShoppingListPresenter {

    private var mView: IShoppingListContract.IShoppingView? = null
    private var mListModel: IShoppingListContract.IShoppingListModel = ShoppingListModel()
    private var mDisposableList: ArrayList<Disposable> = ArrayList()

    override fun setView(view: IShoppingListContract.IShoppingView) {
        mView = view
        mListModel.setView(view)
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
        mListModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    /**
     * 获取商品列表
     * */
    override fun getGoodsData(cId: String, page: Int, order_field: String, order: String) {
        val params = HashMap<String, String>()
        params[ShoppingApiConstant.PARAM_SPECIAL_ID] = cId
        params[ShoppingApiConstant.PARAM_PAGE] = "$page"
        params[ShoppingApiConstant.PARAM_PAGE_SIZE] = ShoppingApiConstant.PARAM_DEFAULT_PAGE_SIZE.toString()
        params[ShoppingApiConstant.PARAM_ORDER_FIELD] = order_field
        params[ShoppingApiConstant.PARAM_ORDER_SORT] = order
        mListModel.getGoodsData(params, object : ResponseCallBack<ShoppingListResponse> {
            override fun onSuccess(t: ShoppingListResponse) {
                val list = t.list
                LogUtils.info("shoppingListFragment---------onLoadGoodsDataSuccess")
                mView?.onLoadGoodsDataSuccess(object : BaseListBean<ProductWithCoupon>() {
                    override fun getList(): MutableList<ProductWithCoupon> {
                        return list
                    }

                    override fun getPageTotalCount(): Int {
                        return if (list.size > 0) {
                            page + 1
                        } else {
                            page
                        }
                    }
                })
            }

            override fun onFail(e: BaseException?) {
                mView?.onLoadGoodsDataFail("${e?.errorMsg}", page)
                LogUtils.info("shoppingListFragment---------加载失败2")
            }
        })
    }
}