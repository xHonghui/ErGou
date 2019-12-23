package com.laka.ergou.mvp.circle.constract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.mvp.circle.model.bean.*
import com.laka.ergou.mvp.commission.model.bean.CommissionNewBean

/**
 * @Author:sming
 * @Date:2019/8/9
 * @Description:
 */
interface ICircleConstract {

    interface IBaseCircleView : IBaseLoadingView<CommissionNewBean> {
        fun categoryListRespone(list: List<CircleCategory>) {}
        fun articleListRespone(list: BaseListBean<CircleArticle>) {}
        fun onNetWorkFail(erroeCode: Int, error: String)
        fun onSendCircle(erroeCode: Int, error: String, position: Int) {}
        fun getCircleCommentSuccess(response: CircleCommentResponse, position: Int){}
    }

    interface IBaseCirclePresenter : IBasePresenter<IBaseCircleView> {
        fun getCategoryList(pid: String)
        fun getArticleList(category_id: String, page_no: Int)
        fun sendCircle(article_id: String, position: Int)
        fun getCircleComment(itemId: String, couponId: String, position: Int)
    }

    interface IBaseCircleModel : IBaseModel<IBaseCircleView> {
        fun getCategoryList(params: MutableMap<String, Any>, callBack: ResponseCallBack<CircleCategoryResponse>)
        fun getArticleList(params: MutableMap<String, Any>, callBack: ResponseCallBack<CircleArticleResponse>)
        fun getCircleComment(params: MutableMap<String, Any>,callBack: ResponseCallBack<CircleCommentResponse>)
        fun sendCircle(params: MutableMap<String, Any>, callBack: ResponseCallBack<Any>)
    }

}