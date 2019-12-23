package com.laka.ergou.mvp.login.presenter

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.mvp.login.contract.ITaoBaoLoginContract


/**
 * @Author:summer
 * @Date:2018/12/19
 * @Description:
 */
class TaoBaoAuthorPresenter : ITaoBaoLoginContract.ILoginPresenter {

    private lateinit var mView: ITaoBaoLoginContract.ILoginView

    override fun onViewCreate() {
    }

    override fun onViewDestroy() {
    }

    override fun onLifeCycleChange(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    override fun setView(view: ITaoBaoLoginContract.ILoginView) {
        mView = view
    }


    override fun onTaoBaoAuthor(context: Activity) {
        if (!AlibcLogin.getInstance().isLogin) {
            //进入淘宝授权前，清空本地粘贴板内容，防止粘贴板含有淘口令从而引起淘宝弹窗搜索弹窗
            ClipBoardManagerHelper.getInstance.clearClipBoardContentForHasTkl()
            AlibcLogin.getInstance().showLogin(object :AlibcLoginCallback{
                override fun onSuccess(p0: Int, p1: String?, p2: String?) {
                    LogUtils.error("获取手掏用户信息: " + AlibcLogin.getInstance().session)
                    mView?.onTaoBaoAuthorSuccess()
                }

                override fun onFailure(p0: Int, p1: String?) {
                    ToastHelper.showToast("授权失败 ")
                }
            })
        } else {
            mView?.onTaoBaoAuthorSuccess()
        }
    }

}