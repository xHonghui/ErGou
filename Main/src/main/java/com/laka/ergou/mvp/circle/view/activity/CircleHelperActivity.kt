package com.laka.ergou.mvp.circle.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.StatusBarUtil
import com.laka.ergou.R
import com.laka.ergou.common.util.regex.RegexUtils
import com.laka.ergou.mvp.base.view.activity.BaseDefaultWebActivity
import com.laka.ergou.mvp.base.webConfig.WebViewClientDefault
import com.laka.ergou.mvp.circle.constant.CircleConstant
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.view.activity.X5WebActivity
import com.laka.ergou.mvp.user.constant.UserApiConstant
import com.laka.ergou.mvp.user.constant.UserConstant
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.activity_advert_detail_x5_web.*
import org.json.JSONObject
import java.net.URLDecoder

class CircleHelperActivity : X5WebActivity() {

    private var mType: Int = 0

    override fun initIntent() {
        super.initIntent()
        mType = intent.getIntExtra(CircleConstant.TYPE_LOGIN, 0)
    }

    override fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, com.laka.androidlib.R.color.white), 0)
            StatusBarUtil.setLightModeNotFullScreen(this, true)
        } else {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, color), 0)
        }
    }

    override fun initTitle() {
        title_bar_web.setTitle(mWebTitle)
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setRightIcon(R.drawable.icon_reload)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)
                .setOnRightClickListener { mWebView?.reload() }
                .setOnLeftClickListener { finish() }
    }

    override fun initUrl() {
        when (mType) {
            0 -> {
                loadUrl("$mWebUrl")
            }
            1, 2 -> {
                loadUrl("$mWebUrl/index?type=$mType")
            }
        }
    }

    override fun initWebViewClient(): WebViewClient {

        return object : WebViewClientDefault(this) {
            override fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
                LogUtils.info("pay_url----------$url")
                if (url!!.contains(HomeApiConstant.URL_WX_PAY)) {
                    val params = matchRefererParams(url)
                    LogUtils.info("pay_url---------params=$params")
                    val extraHeaders = HashMap<String, String>()
                    extraHeaders["Referer"] = params
                    webView?.loadUrl(url, extraHeaders)
                } else {
                    doSchemeJump(webView!!, url)
                }
                return true
            }
        }
    }

    private fun matchRefererParams(url: String): String {
        val params = url.split("&")
        for (item in params) {
            if (item.contains("referer")) {
                return item.replace("referer=", "")
            }
        }
        return ""
    }


    private fun doSchemeJump(webView: WebView, url: String) {
        try {
            if (!TextUtils.isEmpty(url)) {
                val uri = Uri.parse(url)
                val scheme = uri.scheme
                if (scheme == "http" || scheme == "https") {
                    loadUrl(webView, url, uri)
                } else {
                    openBrowser(url)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openBrowser(url: String) {
        try {
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadUrl(webView: WebView, url: String, uri: Uri) {
        val bundle = parseExtras(uri)
        if (bundle != null) {
            if (bundle.containsKey("scheme")) {
                val scheme = bundle.getString("scheme")
                //兼容支付宝H5支付
                if (scheme != null && scheme.startsWith("alipays")) {
                    val schemeUrl = URLDecoder.decode(scheme)
                    try {
                        open(schemeUrl)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return
                }
            }
        }
        webView?.loadUrl(url)
    }

    private fun parseExtras(uri: Uri): Bundle {
        var extras: Bundle? = null
        val queryParameterNames = uri.queryParameterNames
        for (key in queryParameterNames) {
            val value = uri.getQueryParameter(key)
            if (extras == null) {
                extras = Bundle()
            }
            extras.putString(key, value)
        }
        return extras!!
    }

    private fun open(schemeUrl: String) {
        val intent = Intent.parseUri(schemeUrl, Intent.URI_INTENT_SCHEME)
        intent.addCategory("android.intent.category.BROWSABLE")
        intent.component = null
        startActivity(intent)
    }


    override fun jsCallBackInform(jsonObjectStr: String?) {
        LogUtils.info("输出StrJson：$jsonObjectStr")
        val jsonObject = JSONObject(jsonObjectStr)
        val action = jsonObject?.getString(UserApiConstant.JS_ACTION)
        when (action) {
            CircleConstant.JS_ACTION_ONE_KEY_SEND -> {
                setResult(CircleConstant.BUTLER_RESULT_CODE_FOR_USERCENTER, Intent())
                finish()
            }
            CircleConstant.JS_ACTION_SET_TITLE -> {
                if (jsonObject.has(UserApiConstant.JS_PARAMETERS)) {
                    val parameters = jsonObject?.getString(UserApiConstant.JS_PARAMETERS)
                    var jsonParameters = JSONObject(parameters)
                    var title = jsonParameters.getString(CircleConstant.TITLE)
                    if (title.isNotBlank()) {
                        runOnUiThread {
                            setWebTitle(title)
                        }
                    }
                }
            }
        }
    }


    override fun setWebTitle(title: String?) {
        super.setWebTitle(title)
        mWebTitle = "$title"
        title_bar_web.setTitle(title)
    }
}