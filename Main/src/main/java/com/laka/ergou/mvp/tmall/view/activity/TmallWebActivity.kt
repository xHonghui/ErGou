package com.laka.ergou.mvp.tmall.view.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.StringUtils
import com.laka.ergou.R
import com.laka.ergou.common.util.regex.RegexUtils
import com.laka.ergou.mvp.advert.dialog.ErGouDialog
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.helper.TmallHelper
import com.laka.ergou.mvp.main.model.bean.InnerPageBean
import com.laka.ergou.mvp.shop.ShopDetailModuleNavigator
import com.laka.ergou.mvp.tmall.constant.TmallConstant
import kotlinx.android.synthetic.main.activity_tmall_web.*

/**
 * @Author:summer
 * @Date:2019/4/24
 * @Description:天猫国际&天猫超市&聚划算
 */
class TmallWebActivity : BaseMvpActivity<String>() {

    private var mUrl: String = ""
    private var mTitle: String = ""
    private lateinit var mTallHelper: TmallHelper
    //特殊详情url匹配正则表达式
    private val mRegexStr = "/i([0-9]+)\\.htm"

    //loading view 显示的时间
    private var mLoadingViewShowTime: Long = 0
    //loading view 显示出来的最小时间，由于打开一个页面，onPageStarted()方法有时会出现走多次的情况，，onPageFinish()也会相应走多次
    //如果只是单纯在 onPageStarted() 显示 loadingView 在 onPageFinish() 隐藏 ，就会引起闪动的效果，体验是很不好的，所以设置 mTimeSpace
    //当loadingView 显示的时间少于指定值时，onPageFinish 方法中就不隐藏，等 handler 定时任务来隐藏。
    private var mTimeSpace: Long = 300
    //当前webView 的状态，1：默认状态  2：加载状态
    private var mStatus = TmallConstant.TMALL_STATUS_NORMAL
    //商品ID
    private var mProductId: String = ""

    private var mHandler = Handler()
    lateinit var dialog: ErGouDialog
    private var mRunnable = Runnable {

        if (dialog.isShowing
                && mStatus == TmallConstant.TMALL_STATUS_NORMAL) {
            if (::dialog.isInitialized) {
                dialog.dismiss()
            }

        }
    }

    override fun showData(data: String) {

    }

    override fun showErrorMsg(msg: String?) {

    }

    override fun createPresenter(): IBasePresenter<*>? {
        return null
    }

    override fun setContentView(): Int {
        return R.layout.activity_tmall_web
    }

    override fun initIntent() {
        mUrl = intent.getStringExtra(TmallConstant.KEY_H5_TMALL_URL)
        mTitle = intent.getStringExtra(TmallConstant.KEY_H5_TMALL_TITLE)
    }

    override fun initViews() {
        mTallHelper = TmallHelper(cl_root, this)
        dialog = ErGouDialog(this)
        title_bar.setTitle(mTitle)
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)
                .setOnLeftClickListener { onWebViewGoback() }
        //AliPageUtils.openAliPageForH5(this, web_view, mWebViewClient, mWebChromeClient, mUrl)

        val setting = web_view.settings
        setting.javaScriptEnabled = true//设置WebView是否允许执行JavaScript脚本,默认false
        setting.setSupportZoom(false)//WebView是否支持使用屏幕上的缩放控件和手势进行缩放,默认值true
        setting.builtInZoomControls = true//是否使用内置的缩放机制
        setting.displayZoomControls = false//使用内置的缩放机制时是否展示缩放控件,默认值true

        setting.useWideViewPort = true//是否支持HTML的“viewport”标签或者使用wide viewport
        setting.loadWithOverviewMode = true//是否允许WebView度超出以概览的方式载入页面,默认false
        setting.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN//设置布局,会引起WebView的重新布局(relayout),默认值NARROW_COLUMNS

        setting.setRenderPriority(WebSettings.RenderPriority.HIGH)//线程优先级(在API18以上已废弃。不建议调整线程优先级，未来版本不会支持这样做)
        setting.setEnableSmoothTransition(true)//已废弃,将来会成为空操作（no-op）,设置当panning或者缩放或者持有当前WebView的window没有焦点时是否允许其光滑过渡,若为true,WebView会选择一个性能最大化的解决方案。例如过渡时WebView的内容可能不更新。若为false,WebView会保持精度（fidelity）,默认值false。
        setting.cacheMode = WebSettings.LOAD_NO_CACHE//重写使用缓存的方式，默认值LOAD_DEFAULT
        setting.pluginState = WebSettings.PluginState.ON//在API18以上已废弃。未来将不支持插件,不要使用
        setting.javaScriptCanOpenWindowsAutomatically = true//让JavaScript自动打开窗口,默认false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting.mixedContentMode = 2
        }
        //webview 中localStorage无效的解决方法
        setting.domStorageEnabled = true//DOM存储API是否可用,默认false
        setting.setAppCacheMaxSize((1024 * 1024 * 8).toLong())//设置应用缓存内容的最大值
        val appCachePath = ApplicationUtils.getContext().cacheDir.absolutePath
        setting.setAppCachePath(appCachePath)//设置应用缓存文件的路径
        setting.allowFileAccess = true//是否允许访问文件,默认允许
        setting.setAppCacheEnabled(true)//应用缓存API是否可用,默认值false,结合setAppCachePath(String)使用

        web_view.webChromeClient = mWebChromeClient
        web_view.webViewClient = mWebViewClient
        web_view.loadUrl(mUrl)
    }

    private var mPreItemClickTime: Long = 0L

    private var mWebChromeClient: WebChromeClient = WebChromeClient()

    private var mWebViewClient: WebViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            LogUtils.info("test----shouldOverrideUrlLoading:$url")
            var innerPager = isProductDetailPage("$url")
            if (innerPager.isPager) { //详情页
                if (System.currentTimeMillis() - mPreItemClickTime > 700) {
                    val params = RegexUtils.findParamsForUrl("$url")
                    var id = "${params[innerPager.urlData.key]}"
                    if (StringUtils.isNotEmpty(id)) {
                        mProductId = id
                        LogUtils.info("test-------id1=$id")
                        mPreItemClickTime = System.currentTimeMillis()
                        ShopDetailModuleNavigator.startShopDetailActivity(this@TmallWebActivity, mProductId)
                        return true
                    } else {
                        //针对特殊链接获取商品ID
                        //比如：https://a.m.taobao.com/i587971675049.htm?
                        id = RegexUtils.findTergetStrForRegex("$url", mRegexStr, 1)
                        if (StringUtils.isNotEmpty(id)) {
                            mProductId = id
                            LogUtils.info("test-------id2=$id")
                            mPreItemClickTime = System.currentTimeMillis()
                            ShopDetailModuleNavigator.startShopDetailActivity(this@TmallWebActivity, mProductId)
                            return true
                        }
                    }
                } else {
                    return true
                }
            }
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            LogUtils.info("test-----pageStart-------url:${view?.url}")
            super.onPageStarted(view, url, favicon)
            if (::dialog.isInitialized && !dialog.isShowing) {
                if (!isFinishing) {
                    dialog.show()
                }
                mStatus = TmallConstant.TMALL_STATUS_LOADING
                mLoadingViewShowTime = System.currentTimeMillis()
                mHandler.removeCallbacksAndMessages(null)
                mHandler.postDelayed(mRunnable, mTimeSpace)
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            LogUtils.info("test-----pageFinish-----url:${view?.url}")
            mStatus = TmallConstant.TMALL_STATUS_NORMAL
            if (System.currentTimeMillis() - mLoadingViewShowTime > mTimeSpace
                    && dialog.isShowing) {
                if (::dialog.isInitialized) {
                    dialog.dismiss()
                }

            }
//            目前需求是直接跳转本地详情页面，不再需要在H5中遮挡
//            if (::mTallHelper.isInitialized) {
//                mTallHelper.onPageFinished("${view?.url}")
//            }
            setWebTitle(view?.title)
            super.onPageFinished(view, url)
        }
    }

    private fun isProductDetailPage(url: String): InnerPageBean {
        var innerPager = InnerPageBean()
        if (StringUtils.isNotEmpty(url)) {
            for (i in 0 until HomeApiConstant.URL_TMALL_PREFIX_LIST.size) {
                val bean = HomeApiConstant.URL_TMALL_PREFIX_LIST[i]
                if (url.contains(bean.host)) {
                    innerPager.isPager = true
                    innerPager.urlData = bean
                    break
                }
            }
        }
        return innerPager
    }

    private fun setWebTitle(title: String?) {
        if (StringUtils.isNotEmpty(title)) {
            mTitle = "$title"
            title_bar.setTitle(mTitle)
        }
    }

    override fun initData() {

    }

    override fun initEvent() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onWebViewGoback()
        }
        return true
    }

    private fun onWebViewGoback() {
        //判断当前WebView是否仍然可回退，不可回退则直接退出当前activity
        if (web_view.canGoBack()) {
            web_view.goBack()
        } else {
            onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (::mTallHelper.isInitialized) {
            mTallHelper.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mTallHelper.isInitialized) {
            mTallHelper.onViewDestroy()
        }
        mHandler.removeCallbacksAndMessages(null)
    }
}