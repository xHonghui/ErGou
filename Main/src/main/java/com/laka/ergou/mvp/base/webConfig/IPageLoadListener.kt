package com.laka.ergou.mvp.base.webConfig

import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
interface IPageLoadListener {

    fun onLoadStart(view: WebView?, url: String?)

    fun onLoadEnd(view: WebView?, url: String?)
}