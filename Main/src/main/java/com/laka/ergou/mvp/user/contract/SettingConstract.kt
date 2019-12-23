package com.laka.ergou.mvp.user.contract

import android.app.Activity
import android.content.Context
import com.laka.androidlib.mvp.IBaseLoadingView
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.mvp.IBaseView

/**
 * @Author:summer
 * @Date:2019/1/12
 * @Description: 设置
 */
interface SettingConstract {

    interface ISettingView : IBaseLoadingView<String>{
        fun onLogoutSuccess()
    }

    interface ISettingPresenter : IBasePresenter<ISettingView>{
        fun onLogout(activity: Activity)
    }
}