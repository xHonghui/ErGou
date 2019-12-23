package com.laka.ergou.mvp.base.webConfig

import android.app.Activity
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.os.Handler
import com.laka.ergou.mvp.advert.dialog.ErGouDialog
import com.laka.ergou.mvp.base.view.activity.BaseDefaultWebActivity
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

open class WebViewClientDefault(var baseActivity: BaseDefaultWebActivity) : WebViewClient() {


    private var mIPageLoadListener: IPageLoadListener? = null
    lateinit var dialog: ErGouDialog
    private var mLoadingViewShowTime: Long = 0
    //loading view 显示出来的最小时间，由于打开一个页面，onPageStarted()方法有时会出现走多次的情况，，onPageFinish()也会相应走多次
    //如果只是单纯在 onPageStarted() 显示 loadingView 在 onPageFinish() 隐藏 ，就会引起闪动的效果，体验是很不好的，所以设置 mTimeSpace
    //当loadingView 显示的时间少于指定值时，onPageFinish 方法中就不隐藏，等 handler 定时任务来隐藏。
    private var mTimeSpace: Long = 800
    //当前webView 的状态，1：默认状态  2：加载状态
    val WEBVIEW_STATUS_NORMAL: Int = 1  // 默认状态
    val WEBVIEW_STATUS_LOADING: Int = 2  // 加载状态
    private var mStatus = WEBVIEW_STATUS_NORMAL
    private var mHandler = Handler()
    private var mRunnable = Runnable {
        if (dialog.isShowing
                && mStatus == WEBVIEW_STATUS_NORMAL) {
            if (::dialog.isInitialized) {
                //get the Context object that was used to great the dialog
                var context = (dialog.context as ContextWrapper).baseContext
                //if the Context used here was an activity AND it hasn't been finished or destroyed
                //then dismiss it
                try {
                    if (context is Activity) {
                        if (!context.isFinishing && !context.isDestroyed) {
                            dialog.dismiss()
                        }
                    } else {
                        dialog.dismiss()
                    }
                } catch (e: Exception) {

                }
            }
        }
    }

    init {
        dialog = ErGouDialog(baseActivity)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        if (::dialog.isInitialized && !dialog.isShowing) {
            if (!baseActivity.isFinishing) {
                dialog.show()
            }
            mStatus = WEBVIEW_STATUS_LOADING
            mLoadingViewShowTime = System.currentTimeMillis()
            mHandler.removeCallbacksAndMessages(null)
            mHandler.postDelayed(mRunnable, mTimeSpace)
        }
        mIPageLoadListener?.let {
            it.onLoadStart(view, url)
        }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        mStatus = WEBVIEW_STATUS_NORMAL
        mStatus = WEBVIEW_STATUS_NORMAL
        if (System.currentTimeMillis() - mLoadingViewShowTime > mTimeSpace) {
            if (::dialog.isInitialized && dialog.isShowing
                    && !baseActivity.isFinishing) {
                dialog.dismiss()
            }
        }
        baseActivity.setWebTitle(view?.title)
        mIPageLoadListener?.let {
            it.onLoadEnd(view, url)
        }
    }

    override fun onReceivedError(p0: WebView?, p1: Int, p2: String?, p3: String?) {
        super.onReceivedError(p0, p1, p2, p3)
        baseActivity.showErrorView(true)
    }

    override fun shouldOverrideUrlLoading(webView: WebView?, p1: String?): Boolean {
        webView?.loadUrl(p1)
        return true
    }

    fun setIPageLoadListener(mIPageLoadListener: IPageLoadListener) {
        this.mIPageLoadListener = mIPageLoadListener
    }
}