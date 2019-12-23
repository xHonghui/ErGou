package com.laka.ergou.mvp.main.view.adapter

import com.laka.ergou.mvp.commission.model.bean.CommissionBean
import com.laka.ergou.mvp.login.model.bean.UserBean
import com.laka.ergou.mvp.user.contract.IUserHomeConstract
import com.laka.ergou.mvp.user.model.bean.BannerAdvBean

/**
 * @Author:summer
 * @Date:2019/4/3
 * @Description:
 */
open class UserHomeViewAdapter: IUserHomeConstract.IUserHomeView {


    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun showData(data: UserBean) {

    }

    override fun showErrorMsg(msg: String?) {

    }

    override fun onLoadUserInfoSuccess(userBean: UserBean) {

    }

    override fun loadUserCommissionSuccess(commissionBean: CommissionBean) {

    }

    override fun getBannerDataSuccess(bannerData: ArrayList<BannerAdvBean>) {

    }

    override fun onTaoBaoAuthorSuccess() {

    }

    override fun getUnionCodeUrlSuccess(url: String) {

    }

    override fun handleUnionCodeSuccess() {

    }

    override fun authorInvalid() {

    }
}