package com.laka.ergou.mvp.shopping.center.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.shopping.center.constant.ShoppingApiConstant
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import com.laka.ergou.mvp.shopping.center.contract.IShoppingSpecialContract
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.shopping.center.model.bean.ProductListResponse
import com.laka.ergou.mvp.shopping.center.model.repository.ShoppingSpecialModel

/**
 * @Author:summer
 * @Date:2019/4/29
 * @Description:
 */
class ShoppingSpecialPresenter : IShoppingSpecialContract.IShoppingSpecialPresenter {

    private lateinit var mView: IShoppingSpecialContract.IShoppingSpecialView
    private var mModel: IShoppingSpecialContract.IShoppingSpecialModel = ShoppingSpecialModel()

    override fun setView(view: IShoppingSpecialContract.IShoppingSpecialView) {
        this.mView = view
        mModel.setView(view)
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun onViewCreate() {

    }

    override fun onViewDestroy() {
        mModel.onViewDestory()
    }

    override fun onLoadProductSpecialData(sId: String, page: Int) {
        val params = HashMap<String, String>()
        params[ShoppingApiConstant.PARAM_SPECIAL_ID] = sId
        params[ShoppingApiConstant.PARAM_PAGE] = "$page"
        params[ShoppingApiConstant.PARAM_PAGE_SIZE] = ShoppingApiConstant.PARAM_DEFAULT_PAGE_SIZE.toString()
        mModel.onLoadProductSpecialData(params, object : ResponseCallBack<ProductListResponse> {
            override fun onSuccess(t: ProductListResponse) {
                val list = t.results
                mView.onLoadProductDataSuccess(object : BaseListBean<ProductWithCoupon>() {
                    override fun getList(): MutableList<ProductWithCoupon> {
                        return list
                    }

                    override fun getPageTotalCount(): Int {
                        return if (t.isMore == 1) {
                            page + 1
                        } else {
                            page
                        }
                    }
                }, "${t.imgPath}", page)
            }

            override fun onFail(e: BaseException?) {
                mView.onLoadFail("${e?.errorMsg}", page)
            }
        })
    }
}