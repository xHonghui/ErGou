package com.laka.ergou.mvp.circle.model.repository

import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.common.util.rx.RxCustomSubscriber
import com.laka.ergou.mvp.circle.constract.ICircleConstract
import com.laka.ergou.mvp.circle.model.bean.CircleArticleResponse
import com.laka.ergou.mvp.circle.model.bean.CircleCategoryResponse
import com.laka.ergou.mvp.circle.model.bean.CircleCommentResponse
import com.laka.ergou.mvp.commission.constract.ICommissionConstract
import com.laka.ergou.mvp.commission.model.bean.CommissionBean
import com.laka.ergou.mvp.commission.model.bean.CommissionNewBean
import com.laka.ergou.mvp.shopping.center.contract.IShoppingListContract
import com.laka.ergou.mvp.shopping.center.model.bean.ShoppingListResponse
import com.laka.ergou.mvp.shopping.center.model.repository.ShoppingCustomRetrofitHelper
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import java.util.ArrayList

/**
 * @Author:summer
 * @Date:2019/8/7
 * @Description:
 */
class CircleModel : ICircleConstract.IBaseCircleModel {
    override fun sendCircle(params: MutableMap<String, Any>, callBack: ResponseCallBack<Any>) {
        CircleRetrofixHelper.instance
                .sendCircle(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<Any, ICircleConstract.IBaseCircleView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun getArticleList(params: MutableMap<String, Any>, callBack: ResponseCallBack<CircleArticleResponse>) {
        CircleRetrofixHelper.instance
                .getArticleList(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<CircleArticleResponse, ICircleConstract.IBaseCircleView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun getCategoryList(params: MutableMap<String, Any>, callBack: ResponseCallBack<CircleCategoryResponse>) {
        CircleRetrofixHelper.instance
                .getCategoryList(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<CircleCategoryResponse, ICircleConstract.IBaseCircleView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    override fun getCircleComment(params: MutableMap<String, Any>, callBack: ResponseCallBack<CircleCommentResponse>) {
        CircleRetrofixHelper.instance
                .getCircleComment(params)
                .compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe(object : RxCustomSubscriber<CircleCommentResponse, ICircleConstract.IBaseCircleView>(mView, callBack) {
                    override fun onSubscribe(d: Disposable) {
                        super.onSubscribe(d)
                        mDisposableList.add(d)
                    }
                })
    }

    private lateinit var mView: ICircleConstract.IBaseCircleView
    private val mDisposableList = ArrayList<Disposable>()

    override fun setView(v: ICircleConstract.IBaseCircleView) {
        this.mView = v
    }

    override fun onViewDestory() {
        mDisposableList.forEach {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        mDisposableList.clear()
    }


}