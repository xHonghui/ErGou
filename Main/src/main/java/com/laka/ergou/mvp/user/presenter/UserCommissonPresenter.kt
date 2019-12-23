package com.laka.ergou.mvp.user.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.ergou.mvp.user.contract.IUserCommissionContract

/**
 * @Author:Rayman
 * @Date:2019/1/17
 * @Description:用户模块---我的补贴Presenter层
 */
class UserCommissonPresenter : IUserCommissionContract.IUserCommissionPresenter {

    private var mView: IUserCommissionContract.IUserCommissionView? = null

    override fun setView(view: IUserCommissionContract.IUserCommissionView) {
        this.mView = view
    }

    override fun onViewCreate() {
    }

    override fun onViewDestroy() {
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {
    }
}