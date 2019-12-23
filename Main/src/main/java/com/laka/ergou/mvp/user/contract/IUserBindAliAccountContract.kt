package com.laka.ergou.mvp.user.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.mvp.user.model.bean.CommonData
import io.reactivex.Observable

/**
 * @Author:Rayman
 * @Date:2019/1/21
 * @Description:用户模块---绑定阿里账号Contract
 */
interface IUserBindAliAccountContract {

    interface IUserBindAliAccountModel {

        /**
         * description:绑定阿里账号
         **/
        fun bindAliAccount(realName: String, aliAccount: String): Observable<CommonData>
    }

    interface IUserBindAliAccountPresenter : IBasePresenter<IUserBindAliAccountView> {

        fun bindAliAccount(realName: String, aliAccount: String)
    }

    interface IUserBindAliAccountView : IBaseLoadingView<CommonData> {

        fun bindAliAccountSuccess(msg: String)
    }

}