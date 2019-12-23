package com.laka.ergou.mvp.main.view.activity

import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.ViewTreeObserver
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.laka.androidlib.base.activity.BaseFragmentActivity
import com.laka.androidlib.eventbus.Event
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.mvp.chat.constant.ChatEventConstant
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import com.laka.ergou.mvp.main.view.fragment.WebFragment
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserApiConstant
import kotlinx.android.synthetic.main.fragment_web.*
import org.json.JSONObject

/**
 * @Author:Rayman
 * @Date:2019/1/7
 * @Description:H5交互页面
 * 可以考虑封装到lib里面
 * （使用 X5WebActivity 替換）
 */
open class WebActivity : BaseFragmentActivity() {

    private lateinit var webFragment: WebFragment

    private var title = ""
    private var pagerUrl = ""
    private var isNativeWebView = true
    private var viewTreeObserver: ViewTreeObserver? = null
    private var webViewHeight = 0
    private var webViewWidth = 0
    private lateinit var mNativeWebView: WebView
    private lateinit var mX5WebView: WebView

    override fun initIntent() {
        intent?.extras?.let {
            title = it.getString(HomeConstant.WEB_TITLE, "")
            pagerUrl = it.getString(HomeConstant.WEB_URL, "")
            isNativeWebView = it.getBoolean(HomeConstant.WEB_KERNEL, true)
        }
    }

    override fun initData() {
    }

    override fun initEvent() {

    }

    override fun setFragment(): Fragment {
        LogUtils.info("执行setFragment的时间：${System.currentTimeMillis()}")
        webFragment = WebFragment.newInstance(pagerUrl, isNativeWebView, title)
        return webFragment
    }

    override fun setFragmentTag(): String {
        return WebFragment::class.java.simpleName
    }

    override fun isPlayAnimation(): Boolean {
        return false
    }

    override fun onEvent(event: Event?) {
        super.onEvent(event)
        when (event?.name) {
            HomeEventConstant.EVENT_WEB_VIEW_INIT_COMPLETED -> onFragmentViewInitCompleted()
        }
    }

    fun getWebLocalTitle(): String {
        return title
    }

    override fun onDestroy() {
        super.onDestroy()
        // 发送事件，解除--ChatActivity多次发送Copy到的文本限制
        EventBusManager.postEvent(ChatEventConstant.EVENT_BLOCK_COPY_FROM_WEB, false)
    }

    /**
     * description:存在当前类被继承的情况下，子类必须通过这个回调才能对WebView做相关的操作
     * 否则获取WebView的时候会空指针。
     * 这个回调为Fragment初始化完毕的回调。在这里可以对WebFragment的View进行操作。
     **/
    open fun onFragmentViewInitCompleted() {
        if (!TextUtils.isEmpty(title)) {
            webFragment.setTitle(title)
        }

        if (isNativeWebView) {
            mNativeWebView = webFragment.nativeWebView
            viewTreeObserver = mNativeWebView.viewTreeObserver
            viewTreeObserver?.addOnGlobalLayoutListener {
                webViewWidth = mNativeWebView.width
                webViewHeight = mNativeWebView.height
            }
            mNativeWebView.settings.setSupportZoom(false)
            mNativeWebView.settings.builtInZoomControls = false
            mNativeWebView.settings.displayZoomControls = false
            webFragment.title_bar_web?.showDivider(false)
            //H5 交互
            mNativeWebView.addJavascriptInterface(InvitationJsInterface(), "WebViewJavascriptBridge")
        } else {
            mX5WebView = webFragment.nativeWebView
            viewTreeObserver = mX5WebView.viewTreeObserver
            viewTreeObserver?.addOnGlobalLayoutListener {
                webViewWidth = mX5WebView.width
                webViewHeight = mX5WebView.height
            }
            mX5WebView.settings.setSupportZoom(false)
            mX5WebView.settings.builtInZoomControls = false
            mX5WebView.settings.displayZoomControls = false
            webFragment.title_bar_web?.showDivider(false)
            //H5 交互
            mX5WebView.addJavascriptInterface(InvitationJsInterface(), "WebViewJavascriptBridge")
        }
    }

    inner class InvitationJsInterface {
        @JavascriptInterface
        fun jsCallBack(jsonObjectStr: String?) {
            LogUtils.info("输出StrJson：$jsonObjectStr")
            val jsonObject = JSONObject(jsonObjectStr)
            val action = jsonObject?.getString(UserApiConstant.JS_ACTION)
            when (action) {
                UserApiConstant.JS_ACTION_SHARE -> {

                }
                UserApiConstant.JS_ACTION_POSTER -> {
                    UserModuleNavigator.startInvitationPlaybillActivity(this@WebActivity)
                }
                UserApiConstant.JS_ACTION_MINE_COMMISSION -> {
                    UserModuleNavigator.startMyCommissionActivity(this@WebActivity)
                }
            }
        }

        @JavascriptInterface
        fun loginToken(): String {
            LogUtils.info("token--------：")
            return UserUtils.getUserToken()
        }

        @JavascriptInterface
        fun loginUserID(): String {
            LogUtils.info("userId--------：")
            return UserUtils.getUserId().toString()
        }
    }

}