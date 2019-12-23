package com.laka.ergou.mvp.commission.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.rx.callback.ResponseCallBack
import com.laka.androidlib.util.rx.exception.BaseException
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.ergou.R
import com.laka.ergou.mvp.commission.constant.CommissionConstant
import com.laka.ergou.mvp.commission.constract.ICommissionConstract
import com.laka.ergou.mvp.commission.model.bean.CommissionBean
import com.laka.ergou.mvp.commission.model.bean.CommissionDIYBean
import com.laka.ergou.mvp.commission.model.bean.CommissionNewBean
import com.laka.ergou.mvp.commission.model.repository.CommissionModel
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.bean.UserInfoBean
import org.json.JSONObject

/**
 * @Author:summer
 * @Date:2019/1/18
 * @Description:
 */
class CommissionPresenter : ICommissionConstract.IBaseCommissonPresenter {


    private lateinit var mView: ICommissionConstract.IBaseCommissonView
    private val mModel: ICommissionConstract.IBaseCommissonModel = CommissionModel()

    override fun setView(view: ICommissionConstract.IBaseCommissonView) {
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

    override fun onLoadMyCommissonData() {
        val userInfoBean = SPHelper.getObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, UserInfoBean::class.java)
        val params = HashMap<String, String>()
        mModel.onLoadMyCommissonData(params, object : ResponseCallBack<CommissionBean> {
            override fun onSuccess(t: CommissionBean) {
                mView.onLoadMyCommissonDataSuccess(t)
            }

            override fun onFail(e: BaseException?) {
                ToastHelper.showCenterToast("${e?.errorMsg}")
            }
        })
    }

    override fun onGetMyCommissonData(type: String, page: String) {
        val params = HashMap<String, String>()
        params["type"] = type
        params["page"] = page
        params["pageSize"] = CommissionConstant.PAGE_SIZE
        mModel.onGetMyCommissonData(params, object : ResponseCallBack<CommissionNewBean> {
            override fun onSuccess(t: CommissionNewBean) {
                val list: MutableList<CommissionDIYBean> = mutableListOf()
                with(t) {
                    subsidy.taobao?.let {
                        list.add(CommissionDIYBean(CommissionConstant.TAOBAO, it.count, it.frozen, it.balance))
                    }
                    subsidy.agent?.let {
                        list.add(CommissionDIYBean(CommissionConstant.AGENT, it.count, it.frozen, it.balance))
                    }
                    subsidy.promotion?.let {
                        list.add(CommissionDIYBean(CommissionConstant.PROMOTION, it.count, it.frozen, it.balance))
                    }
                    subsidy.other?.let {
                        list.add(CommissionDIYBean(CommissionConstant.OTHER, it.count, it.frozen, it.balance))
                    }

                }
                mView.onGetMyCommissonDataSuccess(t,object : BaseListBean<CommissionDIYBean>() {
                    override fun getList(): MutableList<CommissionDIYBean> {
                        return list
                    }
                })
            }

            override fun onFail(e: BaseException?) {
                mView.onLoadError()
            }
        })
    }

    override fun onLoadWithdrawal() {
        val userInfoBean = SPHelper.getObject(LoginConstant.USER_INFO_FILENAME, LoginConstant.USER_LOGIN_INFO, UserInfoBean::class.java)
        val params = HashMap<String, String>()
        mModel.onLoadWithdrawal(params, object : ResponseCallBack<JSONObject> {
            override fun onSuccess(t: JSONObject) {
                ToastHelper.showCenterToast(R.string.apply_success)
                mView.onWithdrawalSuccess()
            }

            override fun onFail(e: BaseException?) {
                ToastHelper.showCenterToast("${e?.errorMsg}")
            }
        })
    }
}