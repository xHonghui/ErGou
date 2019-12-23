package com.laka.ergou.mvp.user.contract

import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBasePresenter
import java.util.*

/**
 * @Author:Rayman
 * @Date:2019/1/17
 * @Description:用户模块--我的补贴Contract
 */
interface IUserCommissionContract {

    interface IUserCommissionModel
    interface IUserCommissionPresenter : IBasePresenter<IUserCommissionView>
    interface IUserCommissionView : IBaseLoadingView<Objects>

}