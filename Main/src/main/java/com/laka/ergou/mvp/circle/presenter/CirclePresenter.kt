package com.laka.ergou.mvp.circle.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.common.dsl.params
import com.laka.ergou.common.ext.convertRefresh
import com.laka.ergou.mvp.circle.constant.CircleConstant
import com.laka.ergou.mvp.circle.constract.ICircleConstract
import com.laka.ergou.mvp.circle.model.bean.CircleArticleResponse
import com.laka.ergou.mvp.circle.model.bean.CircleCategoryResponse
import com.laka.ergou.mvp.circle.model.bean.CircleCommentResponse
import com.laka.ergou.mvp.circle.model.repository.CircleModel

/**
 * @Author:sming
 * @Date:2019/8/8
 * @Description:
 */
class CirclePresenter : ICircleConstract.IBaseCirclePresenter {
    override fun sendCircle(article_id: String, position: Int) {
        mModel.sendCircle(params {
            "article_id" to article_id
        }, object : ResponseCallBack<Any> {
            override fun onSuccess(t: Any) {
                mView.onSendCircle(0, "", position)
            }

            override fun onFail(e: BaseException) {
                mView.onSendCircle(e.code, e.errorMsg, position)
            }
        })
    }

    override fun getArticleList(category_id: String, page_no: Int) {
        mModel.getArticleList(params {
            "category_id" to category_id
            "page_no" to page_no
            "page_size" to CircleConstant.PAGE_SIZE
        }, object : ResponseCallBack<CircleArticleResponse> {
            override fun onSuccess(t: CircleArticleResponse) {
                mView?.articleListRespone(t.list.convertRefresh(page_no))
            }

            override fun onFail(e: BaseException) {
                mView?.onNetWorkFail(e.code, e.errorMsg)
            }
        })
    }

    override fun getCategoryList(pid: String) {

        mModel.getCategoryList(params {
            "pid" to pid
        }, object : ResponseCallBack<CircleCategoryResponse> {
            override fun onSuccess(t: CircleCategoryResponse) {
                mView.categoryListRespone(t.list)
            }

            override fun onFail(e: BaseException) {
                mView.onNetWorkFail(e.code, e.errorMsg)
            }
        })
    }

    override fun getCircleComment(itemId: String, couponId: String, position: Int) {
        mView.showLoading()
        mModel.getCircleComment(params {
            "item_id" to itemId
            "coupon_id" to couponId
        }, object : ResponseCallBack<CircleCommentResponse> {
            override fun onSuccess(t: CircleCommentResponse) {
                mView.dismissLoading()
                mView.getCircleCommentSuccess(t,position)
            }

            override fun onFail(e: BaseException?) {
                ToastHelper.showToast("${e?.errorMsg}")
            }
        })
    }

    private lateinit var mView: ICircleConstract.IBaseCircleView
    private val mModel: ICircleConstract.IBaseCircleModel = CircleModel()

    override fun setView(view: ICircleConstract.IBaseCircleView) {
        this.mView = view
        mModel.setView(mView)
    }

    override fun onViewCreate() {

    }

    override fun onViewDestroy() {
        mModel.onViewDestory()
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

}