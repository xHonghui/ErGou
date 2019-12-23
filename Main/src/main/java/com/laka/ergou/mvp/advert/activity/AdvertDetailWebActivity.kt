package com.laka.ergou.mvp.advert.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.view.KeyEvent
import android.webkit.*
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.net.thread.ThreadManager
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.ResourceUtils
import com.laka.ergou.R
import com.laka.ergou.common.util.share.WxShareUtils
import com.laka.ergou.common.widget.SharePostDialog
import com.laka.ergou.mvp.advert.constant.AdvertConstant
import com.laka.ergou.mvp.advert.dialog.ErGouDialog
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.model.bean.AddLetteryTimeResponse
import com.laka.ergou.mvp.main.model.bean.ShareInfo
import com.laka.ergou.mvp.main.model.repository.HomeApiRepository
import com.laka.ergou.mvp.shop.utils.AliPageUtils
import com.laka.ergou.mvp.shop.view.dialog.ShareDialog
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserApiConstant
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_tmall_web.*
import org.json.JSONObject

/**
 * @Author:summer
 * @Date:2019/4/24
 * @Description:广告详情页
 */
class AdvertDetailWebActivity : BaseMvpActivity<String>() {

    private var mUrl: String = ""
    private var mTitle: String = "二购周年庆"
    //loading view 显示的时间
    private var mLoadingViewShowTime: Long = 0
    //loading view 显示出来的最小时间，由于打开一个页面，onPageStarted()方法有时会出现走多次的情况，，onPageFinish()也会相应走多次
    //如果只是单纯在 onPageStarted() 显示 loadingView 在 onPageFinish() 隐藏 ，就会引起闪动的效果，体验是很不好的，所以设置 mTimeSpace
    //当loadingView 显示的时间少于指定值时，onPageFinish 方法中就不隐藏，等 handler 定时任务来隐藏。
    private var mTimeSpace: Long = 800
    private var mMyCommissionClickTime: Long = 1000
    //当前webView 的状态，1：默认状态  2：加载状态
    private var mStatus = AdvertConstant.ADVERT_DETAIL_STATUS_NORMAL
    private lateinit var shareDialog: SharePostDialog //活动分享
    private lateinit var shareDownloadDialog: ShareDialog //app 下载了解分享
    private var defaultShareDownloadTitle = ResourceUtils.getString(R.string.share_invitation_title)
    private var defaultShareDownloadContent = ResourceUtils.getString(R.string.share_invitation_content)
    private var webUrl = handleShareDownloadUrl()

    private var mHandler = Handler()
    private var mRunnable = Runnable {
        if (dialog.isShowing
                && mStatus == AdvertConstant.ADVERT_DETAIL_STATUS_NORMAL) {
            if (::dialog.isInitialized) {
                dialog.dismiss()
            }

        }
    }

    private var postId = -1
    private var mShareInfo: ShareInfo? = null
    private val REQUEST_CODE = 1 // 返回的结果码
    lateinit var dialog: ErGouDialog
    override fun showData(data: String) {

    }

    override fun showErrorMsg(msg: String?) {

    }

    override fun createPresenter(): IBasePresenter<*>? {
        return null
    }

    override fun setContentView(): Int {
        return R.layout.activity_advert_detail_web
    }

    override fun initIntent() {
        mUrl = intent.getStringExtra(AdvertConstant.KEY_ADVERT_DETAIL_URL)
        val title = intent.getStringExtra(AdvertConstant.KEY_ADVERT_DETAIL_TITLE)
        if (!TextUtils.isEmpty(title)) {
            mTitle = title
        }
    }

    override fun initViews() {
        dialog = ErGouDialog(this)
        title_bar.setTitle(mTitle)
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setRightIcon(R.drawable.seletor_nav_btn_share)
                .setTitleTextSize(16)
                .setOnLeftClickListener { onWebViewGoback() }
                .setOnRightClickListener {
                    if (mShareInfo == null) {
                        showLoading()
                        getShareInfo(true)
                    } else {
                        shareDialog.show()
                    }
                }
        AliPageUtils.openAliPageForH5(this, web_view, mWebViewClient, mWebChromeClient, mUrl)
        val wetSettings = web_view.settings
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wetSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        web_view.addJavascriptInterface(InvitationJsInterface(), "WebViewJavascriptBridge")
        shareDialog = SharePostDialog(this)
        shareDialog.enableQQShare(false)
        shareDialog.enableQQZoneShare(false)

        shareDownloadDialog = ShareDialog(this)
        shareDownloadDialog.enableQQShare(false).enableQQZoneShare(false)
    }

    private var mWebChromeClient: WebChromeClient = WebChromeClient()
    private var mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            LogUtils.info("test----shouldOverrideUrlLoading:${view?.url}")
            return true
        }


        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            LogUtils.info("test-----pageStart-------currentTime:${System.currentTimeMillis()}")
            super.onPageStarted(view, url, favicon)
            if (::dialog.isInitialized && !dialog.isShowing) {
                if (!isFinishing) {
                    dialog.show()
                }
                mStatus = AdvertConstant.ADVERT_DETAIL_STATUS_LOADING
                mLoadingViewShowTime = System.currentTimeMillis()
                mHandler.removeCallbacksAndMessages(null)
                mHandler.postDelayed(mRunnable, mTimeSpace)
            }
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            LogUtils.info("test-----pageFinish-----currentTime:${System.currentTimeMillis()}")
            mStatus = AdvertConstant.ADVERT_DETAIL_STATUS_NORMAL
            if (System.currentTimeMillis() - mLoadingViewShowTime > mTimeSpace) {
                if (::dialog.isInitialized && dialog.isShowing) {
                    dialog.dismiss()
                }

            }
            setWebTitle(view?.title)
            super.onPageFinished(view, url)
        }
    }

    private fun setWebTitle(title: String?) {
        if (!TextUtils.isEmpty(title)) {
            mTitle = "$title"
            title_bar.setTitle(mTitle)
        }
    }

    override fun initData() {
        getShareInfo()
    }

    override fun initEvent() {
        shareDialog.setOnItemClickListener {
            when (it) {
                ShareDialog.WEIXIN_CLICK -> {
                    mShareInfo?.let {
                        addLotteryTime()
                        WxShareUtils.getInstance(this@AdvertDetailWebActivity)
                                .shareWebActionBitMap(true, it.shareTitle, it.shareDetail
                                        , it.shareUrl, ResourceUtils.getBitmap(R.mipmap.ic_launcher))

                    }

                }
                ShareDialog.FRIEND_CIRCLE_CLICK -> {
                    mShareInfo?.let {
                        addLotteryTime()
                        WxShareUtils.getInstance(this)
                                .shareWebActionBitMap(false, it.shareTitle, it.shareDetail
                                        , it.shareUrl, ResourceUtils.getBitmap(R.mipmap.ic_launcher))
                    }

                }
                ShareDialog.CREATE_POST -> {
                    UserModuleNavigator.startInvitationPlaybillActivity(this, postId, REQUEST_CODE)
                }
            }
        }

        shareDownloadDialog?.setOnItemClickListener {
            when (it) {
                ShareDialog.WEIXIN_CLICK -> {
                    WxShareUtils.getInstance(this)
                            .shareWebActionBitMap(true, defaultShareDownloadTitle, defaultShareDownloadContent
                                    , webUrl, ResourceUtils.getBitmap(R.mipmap.ic_launcher))
                }
                ShareDialog.FRIEND_CIRCLE_CLICK -> {
                    WxShareUtils.getInstance(this)
                            .shareWebActionBitMap(false, defaultShareDownloadTitle, defaultShareDownloadContent
                                    , webUrl, ResourceUtils.getBitmap(R.mipmap.ic_launcher))
                }
            }
        }
    }

    //下载链接生成
    private fun handleShareDownloadUrl(): String {
        val phoneAgentCode = UserUtils.getUserAgentCode()
        val weChatAgentCode = UserUtils.getWxAgentCode()

        var urlBuffer = StringBuffer(HomeApiConstant.URL_INVITATION_SHARE)
        val isPhoneEmpty = TextUtils.isEmpty(phoneAgentCode)
        val isWeChatEmpty = TextUtils.isEmpty(weChatAgentCode)
        if (!isPhoneEmpty || !isWeChatEmpty) {
            urlBuffer.append("?")
        }
        if (!isPhoneEmpty) {
            urlBuffer.append("phoneAgentCode=$phoneAgentCode")
        }

        if (!isPhoneEmpty && !isWeChatEmpty) {
            urlBuffer.append("&")
        }

        if (!isWeChatEmpty) {
            urlBuffer.append("wechatAgentCode=$weChatAgentCode")
        }
        return urlBuffer.toString()
    }

    private fun getShareInfo(isReLoad: Boolean = false) {
        HomeApiRepository.getWxSharePromotion()
                .subscribe(object : Observer<ShareInfo> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(shareInfo: ShareInfo) {
                        dismissLoading()
                        shareInfo?.let {
                            mShareInfo = it
                            if (isReLoad) {
                                shareDialog.show()
                            }
//                            shareDialog.show()
                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                })
    }

    private fun addLotteryTime() {
        if (!WxShareUtils.getInstance(this).isInstallWeChat) return
        HomeApiRepository.addLotteryTime()
                .subscribe(object : Observer<AddLetteryTimeResponse> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(response: AddLetteryTimeResponse) {
                        web_view.loadUrl("javascript:updateRemainTimes(${response.number})")
                    }

                    override fun onError(e: Throwable) {

                    }

                })
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

    inner class InvitationJsInterface {

        @JavascriptInterface
        fun jsCallBack(jsonObjectStr: String?) {
            LogUtils.info("输出StrJson：$jsonObjectStr")
            val jsonObject = JSONObject(jsonObjectStr)
            val action = jsonObject?.getString(UserApiConstant.JS_ACTION)
            when (action) {
                UserApiConstant.JS_ACTION_WINNER -> { //分享抽奖活动
                    if (jsonObject.has(UserApiConstant.JS_PARAMETERS)) {
                        val parameters = jsonObject?.getString(UserApiConstant.JS_PARAMETERS)
                        var jsonParameters = JSONObject(parameters)
                        postId = jsonParameters.getString(UserApiConstant.JS_ITEM_ID).toInt()
                        runOnUiThread {
                            if (mShareInfo == null) {
                                showLoading()
                                getShareInfo(true)
                            } else {
                                shareDialog.show()
                            }
                        }
                    }
                }
                UserApiConstant.JS_ACTION_MINE_COMMISSION -> { // 跳转我的佣金
                    if (System.currentTimeMillis() - mMyCommissionClickTime > 1000) {
                        runOnUiThread {
                            LogUtils.info("输出StrJson----------：$action")
                            UserModuleNavigator.startMyCommissionActivity(this@AdvertDetailWebActivity)
                            mMyCommissionClickTime = System.currentTimeMillis()
                        }
                    }
                }
                UserApiConstant.JS_ACTION_SHARE -> { //分享下载链接
                    ThreadManager.runOnUiThread { shareDownloadDialog.show() }
                }
                UserApiConstant.JS_ACTION_POSTER -> { //跳转海报分享页面
                    UserModuleNavigator.startInvitationPlaybillActivity(this@AdvertDetailWebActivity)
                }
            }
        }

        @JavascriptInterface
        fun loginToken(): String {
            return UserUtils.getUserToken()
        }

        @JavascriptInterface
        fun loginUserID(): String {
            return UserUtils.getUserId().toString()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val shareAddTime = data.getBooleanExtra(AdvertConstant.SHARE_ADD_TIME, false)
            if (shareAddTime) {
                addLotteryTime()
            }
        }
    }
}