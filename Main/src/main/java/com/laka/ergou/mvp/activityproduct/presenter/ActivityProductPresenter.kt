package com.laka.ergou.mvp.activityproduct.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.activityproduct.constract.ActivityProductConstract
import com.laka.ergou.mvp.activityproduct.model.bean.ActivityProductResponse
import com.laka.ergou.mvp.activityproduct.model.respository.ActivityProductModel
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon

/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:
 */
class ActivityProductPresenter : ActivityProductConstract.IActivityProductPresenter {

    private lateinit var mView: ActivityProductConstract.IActivityProductView
    private var mModel: ActivityProductConstract.IActivityProductModel = ActivityProductModel()

    override fun setView(view: ActivityProductConstract.IActivityProductView) {
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

    override fun onLoadActivityProductList(page: Int, activityId: String) {
        val params = HashMap<String, String>()
        params["activity_id"] = activityId
        mModel.onLoadActivityProductList(params, object : ResponseCallBack<ActivityProductResponse> {
            override fun onSuccess(t: ActivityProductResponse) {
                mView.onLoadActivityProductListSuccess(object : BaseListBean<ProductWithCoupon>() {
                    override fun getList(): MutableList<ProductWithCoupon> {
                        return if (t?.detail == null) {
                            ArrayList()
                        } else {
                            t?.detail?.dataList
                        }
                    }

                    override fun getPageTotalCount(): Int {
                        return 1 //只有一页
                    }
                }, "${t?.detail?.imgPath}", page)
            }

            override fun onFail(e: BaseException?) {
                mView.onLoadFail("${e?.errorMsg}", page)
            }
        })
    }

}
