package com.laka.ergou.mvp.main.contract

import android.app.Activity
import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.mvp.user.model.bean.RelationResponse
import com.laka.ergou.mvp.user.model.bean.UrlResponse

/**
 * @Author:summer
 * @Date:2019/8/14
 * @Description:
 */
interface IBindRelationIdContract {

    interface IBindRelationIdView : IBaseLoadingView<String> {
        fun onTaoBaoAuthorSuccess()
        fun getUnionCodeUrlSuccess(url: String)
        fun handleUnionCodeSuccess()
        fun handleUnionCodeFail()
    }

    interface IBindRelationIdPresenter : IBasePresenter<IBindRelationIdView> {
        fun onTaoBaoAuthor(activity: Activity)
        fun getUnionCodeUrl()
        fun handleUnionCode(code: String, state: String)
    }

    interface IBindRelationIdModel : IBaseModel<IBindRelationIdView> {
        fun getUnionCodeUrl(params: HashMap<String, String>, callback: ResponseCallBack<UrlResponse>)
        fun handleUnionCode(params: HashMap<String, String>, callback: ResponseCallBack<RelationResponse>)
    }
}