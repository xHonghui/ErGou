package com.laka.ergou.mvp.main.view.fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.laka.androidlib.base.fragment.BaseX5WebFragment
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.main.constant.HomeConstant.WEB_KERNEL
import com.laka.ergou.mvp.main.constant.HomeConstant.WEB_TITLE
import com.laka.ergou.mvp.main.constant.HomeConstant.WEB_URL
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import kotlinx.android.synthetic.main.fragment_web.*

/**
 * @Author:Rayman
 * @Date:2019/1/7
 * @Description:项目通用展示H5页面的Fragment。提供API切换内核
 */
open class WebFragment : BaseX5WebFragment(), View.OnClickListener {

    /**
     * description:UI配置
     **/
    protected var isNativeKernel = false
    protected var webUrl = ""
    protected var webTitle = ""
    var mWebContainer: RelativeLayout? = null

    /**
     * description:错误信息
     **/
    private var mErrorView: View? = null
    private var mTvErrorHint: TextView? = null

    /**
     * description:回调
     **/
    private lateinit var errorClick: View.OnClickListener

    companion object {
        fun newInstance(url: String, isNativeKernel: Boolean, title: String = ""): WebFragment {
            val instance = WebFragment()
            var bundle = Bundle()
            bundle.putString(WEB_URL, url)
            bundle.putBoolean(WEB_KERNEL, isNativeKernel)
            bundle.putString(WEB_TITLE, title)
            instance.arguments = bundle
            return instance
        }
    }

    override fun setContentView(): Int {
        return R.layout.fragment_web
    }

    override fun initArgumentsData(arguments: Bundle?) {
        arguments?.let {
            webUrl = it.getString(WEB_URL, "")
            webTitle = it.getString(WEB_TITLE, "")
            isNativeKernel = it.getBoolean(WEB_KERNEL, true)!!
        }
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        mActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mWebContainer = fl_web_container
        rootView?.let {

            // 配置TitleBar
            title_bar_web.setLeftIcon(R.drawable.ic_arrow_back_white)
                    .setBackGroundColor(R.color.color_main)
                    .setTitleTextColor(R.color.white)
                    .setTitleTextSize(16)
                    .setOnLeftClickListener {
                        onWebViewGoback()
                    }

            if (isNativeKernel) {
                web_stub.parent?.let {
                    web_stub.inflate()
                    mNativeWebView = rootView.findViewById(R.id.web_content)
                }
            } else {
                web_x5_stub.parent?.let {
                    web_x5_stub.inflate()
                    mX5WebView = rootView.findViewById(R.id.web_x5_content)
                }
            }
        }
        mErrorView = LayoutInflater.from(mActivity).inflate(R.layout.layout_no_data, null)
        mWebContainer?.addView(mErrorView)
        mWebContainer?.isDrawingCacheEnabled = true
        mTvErrorHint = mErrorView?.findViewById(R.id.tv_no_data)
        mTvErrorHint?.text = "页面加载出错啦"
        mErrorView?.visibility = View.GONE
        switchWebViewKernel(isNativeKernel)
        setTitle(webTitle)
    }

    override fun initData() {

    }

    override fun initEvent() {
        errorClick = View.OnClickListener {
            // 重新刷新页面
            if (isNativeKernel) {
                mNativeWebView.loadUrl(webUrl)
            } else {
                mX5WebView.loadUrl(webUrl)
            }
            showErrorView(false)
        }
        mErrorView?.setOnClickListener(errorClick)
    }

    private fun onWebViewGoback() {
        //判断当前WebView是否仍然可回退，不可回退则直接退出当前activity
        if (isNativeKernel) {
            if (mNativeWebView.canGoBack()) {
                mNativeWebView.goBack()
            } else {
                onBackPressed()
            }
        } else {
            if (mX5WebView.canGoBack()) {
                mX5WebView.goBack()
            } else {
                onBackPressed()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    override fun getWebViewEventCallback(): WebViewEventCallBack {
        return object : WebViewEventCallBack {
            override fun onWebViewInitCompleted() {
                LogUtils.info("WebView初始化完毕时间：${System.currentTimeMillis()}")
                EventBusManager.postEvent(HomeEventConstant.EVENT_WEB_VIEW_INIT_COMPLETED)
                // 假若传递的URL为""的情况下，默认不做加载
                if (!TextUtils.isEmpty(webUrl)) {
                    LogUtils.info("调用加载Web时间：${System.currentTimeMillis()}")
                    loadUrl(webUrl)
                }
            }

            override fun onWebViewLoadStart() {
                if (pb_web != null) {
                    pb_web.visibility = View.VISIBLE
                }
            }

            override fun onWebViewLoading(progress: Int) {
                if (pb_web != null) {
                    pb_web.progress = progress
                }
            }

            override fun onWebViewLoadFinish() {
                if (pb_web != null) {
                    pb_web.visibility = View.GONE
                }
                EventBusManager.postEvent(HomeEventConstant.EVENT_WEB_VIEW_LOAD_FINISH)
            }

            override fun onWebViewLoadError() {
                // 显示错误View
                showErrorView(true)
            }
        }
    }

    open fun setTitle(titleText: String) {
        title_bar_web.setTitle(titleText)
    }

    private fun showErrorView(isShow: Boolean) {
        if (mErrorView != null) {
            mErrorView?.visibility = if (isShow) View.VISIBLE else View.GONE
        }
        if (mNativeWebView != null) {
            mNativeWebView?.visibility = if (!isShow) View.VISIBLE else View.GONE
        }
        if (mX5WebView != null) {
            mX5WebView?.visibility = if (!isShow) View.VISIBLE else View.GONE
        }
    }

    /**加载页面完成后，设置title，如果用户传入title 不为空，则保留，否则使用 H5 页面的title*/
    override fun onLoadFinish(title: String?) {
        if (title != null && !TextUtils.isEmpty(title) && TextUtils.isEmpty(webTitle)) {
            setTitle(title)
        }
    }
}