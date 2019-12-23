package com.laka.ergou.mvp.shop.utils

import android.app.Activity
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alibaba.baichuan.android.trade.AlibcTrade
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback
import com.alibaba.baichuan.android.trade.model.AlibcShowParams
import com.alibaba.baichuan.android.trade.model.OpenType
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.mvp.user.UserUtils.UserUtils

/**
 * @Author:summer
 * @Date:2019/1/16
 * @Description:
 */
object AliPageUtils {

    fun openAliPage(activity: Activity, url: String, taokeParams: AlibcTaokeParams) {
        val showParams = AlibcShowParams(OpenType.Native)
        showParams.isShowTitleBar = false
        showParams.backUrl = "tbopen://m.laka.com"
        AlibcTrade.openByUrl(activity, "", url, WebView(activity), WebViewClient(), WebChromeClient(), showParams, taokeParams, HashMap<String, String>(), object : AlibcTradeCallback {
            override fun onFailure(p0: Int, p1: String?) {

            }

            override fun onTradeSuccess(p0: AlibcTradeResult?) {

            }
        })
    }

    /**
     * 通过阿里百川SDK打开H5页面，传入相应的 webView
     * */
    fun openAliPageForH5(activity: Activity, webView: WebView, webViewClient: WebViewClient, webChromeClient: WebChromeClient, url: String) {
        val tbkParams = AlibcTaokeParams("", "", "")
        tbkParams.setPid("${UserUtils.getUserInfoBean()?.userBean?.adzone?.adzonepid}")
        tbkParams.setAdzoneid("${UserUtils.getUserInfoBean()?.userBean?.adzone?.adzone_id}")
        val showParams = AlibcShowParams()
        showParams.isShowTitleBar = false
        showParams.openType = OpenType.Auto
        showParams.backUrl = "tbopen://m.laka.com"
        //第二个参数是套件名称
        AlibcTrade.openByUrl(activity, "", url, webView, webViewClient, webChromeClient, showParams, tbkParams, HashMap<String, String>(), object : AlibcTradeCallback {
            override fun onFailure(p0: Int, p1: String?) {
                ToastHelper.showCenterToast("打开失败---$p0---$p1")
            }

            override fun onTradeSuccess(p0: AlibcTradeResult?) {
                ToastHelper.showCenterToast("打开成功---")
            }
        })
    }

    /**
     * 通过阿里百川SDK打开H5页面，不传入 webView
     * */
    fun openAliPageForAuto(activity: Activity, url: String, taokeParams: AlibcTaokeParams) {
        val showParams = AlibcShowParams()
        showParams.isShowTitleBar = false
        showParams.openType = OpenType.Auto
        showParams.backUrl = "tbopen://m.laka.com"
        AlibcTrade.openByUrl(activity, "", url, WebView(activity), WebViewClient(), WebChromeClient(), showParams, taokeParams, HashMap<String, String>(), object : AlibcTradeCallback {
            override fun onFailure(p0: Int, p1: String?) {
                ToastHelper.showCenterToast("打开失败：$p1")
            }

            override fun onTradeSuccess(p0: AlibcTradeResult?) {
                ToastHelper.showCenterToast("打开成功：$p0")
            }
        })

    }

}