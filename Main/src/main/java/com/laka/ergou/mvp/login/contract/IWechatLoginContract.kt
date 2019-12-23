package com.laka.ergou.mvp.login.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBaseModel
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.ergou.mvp.login.model.bean.User
import com.laka.ergou.mvp.login.model.bean.UserBean

/**
 * @Author:summer
 * @Date:2019/3/9
 * @Description:微信授权
 */
interface IWechatLoginContract {

    interface IWechatAuthorView : IBaseLoadingView<UserBean> {
        fun onWechatAuthorSuccess(result: UserBean)
        fun onLoginTokenVerifySuccess(result:UserBean)
    }

    interface IWechatAutorPresenter : IBasePresenter<IWechatAuthorView> {
        fun onWechatAuthor(code: String, hasToken: Boolean)
        fun onLoginTokenVerify(loginToken: String)
    }

    interface IWechatAuthorModel : IBaseModel<IWechatAuthorView> {
        fun onWechatAuthor(params: HashMap<String, String>, callBack: ResponseCallBack<UserBean>)
        fun onLoginTokenVerify(params: HashMap<String, String>, callBack: ResponseCallBack<UserBean>)
    }


}