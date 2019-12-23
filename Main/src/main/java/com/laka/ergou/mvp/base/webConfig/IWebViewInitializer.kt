package com.laka.ergou.mvp.base.webConfig

import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient


interface IWebViewInitializer {
    fun initWebViewBefore(){}
    //初始化webview的基础配置
    fun initWebViewSettings(webView: WebView): WebView

    //针对浏览器本身行为的控制，如前进后退的回调
    fun initWebViewClient(): WebViewClient

    //针对页面的控制,如js交互
    fun initWebChromeClient(): WebChromeClient


    fun initWebJSInterface(): JSInterfaceDefault
}