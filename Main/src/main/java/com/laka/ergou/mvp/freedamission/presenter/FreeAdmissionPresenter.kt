package com.laka.ergou.mvp.freedamission.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.freedamission.constract.FreeAdmissionConstract
import com.laka.ergou.mvp.freedamission.model.bean.FreeAdmissionResponse
import com.laka.ergou.mvp.freedamission.model.respository.FreeAdmissionModel
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon

/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:
 */
class FreeAdmissionPresenter : FreeAdmissionConstract.IFreeAdmissionPresenter {

    private lateinit var mView: FreeAdmissionConstract.IFreeAdmissionView
    private var mModel: FreeAdmissionConstract.IFreeAdmissionModel = FreeAdmissionModel()

    override fun setView(view: FreeAdmissionConstract.IFreeAdmissionView) {
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

    override fun onLoadFreeAdmissionProductList(page: Int) {
        val params = HashMap<String, String>()
        mModel.onLoadFreeAdmissionProductList(params, object : ResponseCallBack<FreeAdmissionResponse> {
            override fun onSuccess(t: FreeAdmissionResponse) {
                t.dataList.forEach { it.isFirst = t.isFirst }
                mView.onLoadFreeAdmissionProductListSuccess(object : BaseListBean<ProductWithCoupon>() {
                    override fun getList(): MutableList<ProductWithCoupon> {
                        return t.dataList
                    }

                    override fun getPageTotalCount(): Int {
                        return 1 //0元购只有一页
                    }
                }, "${t.imgPath}", page)
            }

            override fun onFail(e: BaseException?) {
                mView.onLoadFail("${e?.errorMsg}", page)
            }
        })
    }

}
