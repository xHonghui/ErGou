package com.laka.ergou.mvp.main.util

import android.app.Activity
import com.alibaba.baichuan.android.trade.AlibcTrade
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback
import com.alibaba.baichuan.android.trade.model.AlibcShowParams
import com.alibaba.baichuan.android.trade.model.OpenType
import com.alibaba.baichuan.android.trade.page.AlibcMyCartsPage
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.common.util.ClipBoardManagerHelper
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.user.constant.UserConstant

/**
 * @Author:Rayman
 * @Date:2019/1/9
 * @Description:对接阿里百川SDK跳转
 */
object AlibcNavigatorUtil {

    /**
     * description:跳转到我的购物车页面
     **/
    fun showAlibcMyShopCart(activity: Activity) {

    }


    /**
     * 淘宝授权
     * */
    private fun taoBaoAuthor(activity: Activity, type: Int) {
        if (!AlibcLogin.getInstance().isLogin) {
            //进入淘宝授权前，清空本地粘贴板内容，防止粘贴板含有淘口令从而引起淘宝弹窗搜索弹窗
            ClipBoardManagerHelper.getInstance.clearClipBoardContentForHasTkl()
            AlibcLogin.getInstance().showLogin(object : AlibcLoginCallback {
                override fun onSuccess(p0: Int, p1: String?, p2: String?) {
                    ToastHelper.showCenterToast("授权成功")
                    EventBusManager.postEvent(UserEvent(type))
                }

                override fun onFailure(p0: Int, p1: String?) {
                    ToastHelper.showCenterToast("授权失败")
                }
            })
        }
    }


}