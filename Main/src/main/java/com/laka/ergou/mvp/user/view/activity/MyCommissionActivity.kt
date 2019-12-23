package com.laka.ergou.mvp.user.view.activity

import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.R
import com.laka.ergou.mvp.user.contract.IUserCommissionContract
import com.laka.ergou.mvp.user.presenter.UserCommissonPresenter
import java.util.*

/**
 * @Author:Rayman
 * @Date:2019/1/17
 * @Description:我的补贴页面
 */
class MyCommissionActivity : BaseMvpActivity<Objects>(), IUserCommissionContract.IUserCommissionView {

    override fun setContentView(): Int {
        return R.layout.activity_my_commission
    }

    override fun initIntent() {
    }

    override fun initViews() {
    }

    override fun initData() {
    }

    override fun initEvent() {
    }

    override fun showData(data: Objects) {
    }

    override fun showErrorMsg(msg: String?) {
    }

    override fun createPresenter(): IBasePresenter<*> {
        return UserCommissonPresenter()
    }
}