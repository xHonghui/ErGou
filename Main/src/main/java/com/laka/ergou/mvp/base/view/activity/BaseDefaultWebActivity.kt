package com.laka.ergou.mvp.base.view.activity

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.RelativeLayout
import android.widget.TextView
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.base.webConfig.*
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import kotlinx.android.synthetic.main.activity_x5_web.*

abstract class BaseDefaultWebActivity : BaseWebActivity(), IWebViewInitializer {
    /**
     * description:错误信息
     **/
    private var mErrorView: View? = null
    private var mTvErrorHint: TextView? = null
    var mWebUrl = ""
    var islandport = false
    var mWebTitle = ""
    protected var mWebContainer: RelativeLayout? = null
    //    protected var mProgressBar: ProgressBar? = null
    protected lateinit var chromeClient: WebChromeClient

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        mWebContainer = findViewById(R.id.mWebContainer)
//        mProgressBar = findViewById(R.id.pb_web)
    }

    override fun initIntent() {
        intent?.extras?.let {
            mWebUrl = it.getString(HomeConstant.WEB_URL, "")
            mWebTitle = it.getString(HomeConstant.WEB_TITLE, "")
        }
    }

    override fun initViews() {
        mErrorView = LayoutInflater.from(this).inflate(R.layout.layout_no_data, null)

        var layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        mErrorView?.layoutParams = layoutParams
        mWebContainer?.addView(mErrorView)
        mWebContainer?.isDrawingCacheEnabled = true
        mTvErrorHint = mErrorView?.findViewById(R.id.tv_no_data)
        mTvErrorHint?.text = "页面加载出错啦"
        mErrorView?.visibility = View.GONE
    }

    override fun initWebViewSettings(webView: WebView): WebView {
        return WebViewSettingsInitializer().createWebView(webView)
    }

    override fun initWebViewClient(): WebViewClient {
        return object : WebViewClientDefault(this) {

        }
    }

    open fun setWebTitle(title: String?) {
    }

    var myView: View? = null
    var myCallback: IX5WebChromeClient.CustomViewCallback? = null
    override fun initWebChromeClient(): WebChromeClient {
        chromeClient = object : WebChromeClientImpl() {
            override fun onShowCustomView(view: View?, callback: IX5WebChromeClient.CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                if (islandport) {
                    return
                }
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                mWebView?.visibility = View.GONE
                if (myView != null) {
                    callback?.onCustomViewHidden()
                    return
                }

                videoView.addView(view)
                myView = view
                myCallback = callback
                videoView.visibility = View.VISIBLE
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                if (myView == null) {
                    return
                } else {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    myView?.visibility = View.GONE
                    videoView.removeView(myView)
                    videoView.visibility = View.GONE
                    mWebView?.visibility = View.VISIBLE
                    myCallback?.onCustomViewHidden()
                    myView = null

                }
            }
        }
        return chromeClient
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            islandport = true
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            islandport = false
        }
    }

    final override fun initWebJSInterface(): JSInterfaceDefault {
        return JSInterfaceDefault(this, object : JSInterfaceCallBack {
            override fun jsSubCallBack(jsonObjectStr: String?) {
                jsCallBackInform(jsonObjectStr)
            }
        })
    }

    override fun setInitializer(): IWebViewInitializer {
        return this
    }

    open fun jsCallBackInform(jsonObjectStr: String?) {

    }

    override fun initEvent() {
        mErrorView?.setOnClickListener {
            loadUrl(mWebUrl)
            showErrorView(false)
        }
    }

    fun showErrorView(isShow: Boolean) {

        LogUtils.info("showErrorView---${mErrorView.toString()}----${mWebContainer.toString()}")

        if (mErrorView != null) {
            mErrorView?.visibility = if (isShow) View.VISIBLE else View.GONE
        }
        mWebView?.let {
            it.visibility = if (!isShow) View.VISIBLE else View.GONE
        }
    }

    override fun initWebViewBefore() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onWebViewGoback()
        }
        return true
    }

    fun onWebViewGoback() {
        //判断当前WebView是否仍然可回退，不可回退则直接退出当前activity
        if (islandport) run {
            if (chromeClient != null) {
                chromeClient.onHideCustomView()
            }
        } else {
            mWebView?.let {
                if (it.canGoBack()) {
                    it.goBack()
                } else {
                    onBackPressed()
                }
            }
        }


    }
}