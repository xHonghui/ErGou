package com.laka.ergou.mvp.login.contract

import android.app.Activity
import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.mvp.login.model.bean.UserInfoBean

/**
 * @Author:summer
 * @Date:2018/12/19
 * @Description:登录模块Contract
 */
interface ITaoBaoLoginContract {

    interface ILoginView: IBaseLoadingView<UserInfoBean>{
        fun onTaoBaoAuthorSuccess()
    }

    interface ILoginPresenter:IBasePresenter<ILoginView>{
        fun onTaoBaoAuthor(context: Activity)
    }
}