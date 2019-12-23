package com.laka.ergou.mvp.user.presenter

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.SettingConstract

/**
 * @Author:summer
 * @Date:2019/1/12
 * @Description:
 */
class SettingPresenter : SettingConstract.ISettingPresenter {

    private lateinit var mView: SettingConstract.ISettingView

    override fun onViewCreate() {

    }

    override fun onViewDestroy() {

    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {

    }

    override fun setView(view: SettingConstract.ISettingView) {
        mView = view
    }

    override fun onLogout(activity: Activity) {
        mView.showLoading()
        SPHelper.clearByFileName(LoginConstant.USER_INFO_FILENAME)
        AlibcLogin.getInstance().logout(object :AlibcLoginCallback{
            override fun onSuccess(p0: Int, p1: String?, p2: String?) {
                ToastHelper.showCenterToast("退出登录")
                EventBusManager.postEvent(UserEvent(UserConstant.LOGOUT_EVENT))
                mView.onLogoutSuccess()
                mView.dismissLoading()
            }

            override fun onFailure(p0: Int, p1: String?) {
                ToastHelper.showCenterToast("退出登录失败，请稍后重试！")
                mView.dismissLoading()
            }
        })

    }
}