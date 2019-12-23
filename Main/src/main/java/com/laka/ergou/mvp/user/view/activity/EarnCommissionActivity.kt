package com.laka.ergou.mvp.user.view.activity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.ViewTreeObserver
import android.webkit.WebView
import android.webkit.WebViewClient
import com.laka.androidlib.base.activity.BaseFragmentActivity
import com.laka.androidlib.eventbus.Event
import com.laka.androidlib.util.EncryptUtils
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.image.BitmapUtils
import com.laka.androidlib.util.rx.RxSchedulerComposer
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.ergou.R
import com.laka.ergou.common.util.share.WxShareUtils
import com.laka.ergou.common.widget.SharePostDialog
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import com.laka.ergou.mvp.main.model.bean.ShareInfo
import com.laka.ergou.mvp.main.model.repository.HomeApiRepository
import com.laka.ergou.mvp.main.view.fragment.WebFragment
import com.laka.ergou.mvp.shop.view.dialog.ShareDialog
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_web.*

/**
 * @Author:Rayman
 * @Date:2019/1/21
 * @Description:用户模块---赚取补贴页面（已弃用）
 */
class EarnCommissionActivity : BaseFragmentActivity() {

    private lateinit var webFragment: WebFragment

    /**
     * description:WebView宽高
     **/
    private var contentHeightJs = "document.getElementsByTagName('section')[2].offsetTop + " +
            "document.getElementsByTagName('section')[2].offsetHeight"
    private var webViewHeight = 0
    private var webViewWidth = 0
    private var webViewScale = 0f
    private var webContentHeight = 0
    private var isPageLoadFinish = false
    private var viewTreeObserver: ViewTreeObserver? = null

    /**
     * description:分享
     **/
    private lateinit var shareDialog: SharePostDialog
    private var mSnapShotByteArray: ByteArray? = null
    private var robotId = ""
    private var robotName = ""
    private var webUrl = ""
    private var encryParams = ""
    private var isSharePost = false
    private var defaultShareTitle = ResourceUtils.getString(R.string.share_default_title)
    private var defaultShareContent = ResourceUtils.getString(R.string.share_default_content)

    override fun initIntent() {
        intent?.extras?.let {
            robotId = it.getString(UserConstant.ROBOT_ID)
            robotName = it.getString(UserConstant.ROBOT_NAME)
        }
    }

    override fun initViews() {
        super.initViews()
        shareDialog = SharePostDialog(this)
        shareDialog.enableQQShare(false)
                .enableQQZoneShare(false)
        shareDialog.setOnDismissListener {
            shareDialog.showShareItem(true)
            isSharePost = false
        }
    }

    override fun initData() {
        getShareInfo()
    }

    override fun initEvent() {
        shareDialog.setOnItemClickListener {
            when (it) {
                ShareDialog.WEIXIN_CLICK -> {
                    if (!isSharePost) {
                        WxShareUtils.getInstance(this@EarnCommissionActivity)
                                .shareWebAction(true, defaultShareTitle, defaultShareContent
                                        , webUrl, null)
                    } else {
                        WxShareUtils.getInstance(this@EarnCommissionActivity)
                                .shareImageAction(true, defaultShareTitle, defaultShareContent,
                                        mSnapShotByteArray)
                    }
                }
                ShareDialog.FRIEND_CIRCLE_CLICK -> {
                    if (!isSharePost) {
                        WxShareUtils.getInstance(this@EarnCommissionActivity)
                                .shareWebAction(false, defaultShareTitle, defaultShareContent
                                        , webUrl, ResourceUtils.getBitmap(R.mipmap.ic_launcher))
                    } else {
                        WxShareUtils.getInstance(this@EarnCommissionActivity)
                                .shareImageAction(false, defaultShareTitle, defaultShareContent,
                                        mSnapShotByteArray)
                    }
                }
                ShareDialog.CREATE_POST -> {
                    if (mSnapShotByteArray == null && isPageLoadFinish) {
                        createSharePost()
                    } else if (!isPageLoadFinish) {
                        ToastHelper.showToast("页面正在加载")
                    } else {
                        shareDialog.showSharePostImage(mSnapShotByteArray!!)
                        shareDialog.showShareItem(false)
                        isSharePost = true
                    }
                }
            }
        }
    }

    override fun setFragment(): Fragment {
        webFragment = WebFragment.newInstance(encryptionShareUrl(), true)
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

    private fun onFragmentViewInitCompleted() {
        val webView = webFragment?.nativeWebView
        viewTreeObserver = webView.viewTreeObserver
        viewTreeObserver?.addOnGlobalLayoutListener {
            webViewWidth = webView.width
            webViewHeight = webView.height
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                isPageLoadFinish = true
                webView.evaluateJavascript(contentHeightJs, {
                    it?.let {
                        if (!TextUtils.isEmpty(it) && it != "null") {
                            webContentHeight += it.toInt()
                        }
//                    LogUtils.info("输出当前WebView---Url：${webView.url}" +
//                            "\n--ContentHeight：$webContentHeight" +
//                            "\nWebView的高度：$webViewHeight")
                    }
                    createSharePost()
                })
            }

            override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
                super.onScaleChanged(view, oldScale, newScale)
                webViewScale = newScale
            }
        }

        // 页面加载完毕
        webFragment?.setTitle(ResourceUtils.getString(R.string.robot_earn_commission))
        webFragment?.title_bar_web
                ?.setRightIcon(R.drawable.ic_share)
                ?.showDivider(false)
                ?.setOnRightClickListener {
                    // 弹出分享框
                    shareDialog.show()
                }
    }

    private fun getShareInfo() {
        HomeApiRepository.getWxShareOfficial()
                .subscribe(object : Observer<ShareInfo> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(shareInfo: ShareInfo) {
                        shareInfo?.let {
                            defaultShareTitle = if (TextUtils.isEmpty(shareInfo?.shareTitle)) {
                                defaultShareTitle
                            } else {
                                shareInfo?.shareTitle
                            }

                            defaultShareContent = if (TextUtils.isEmpty(shareInfo?.shareContent)) {
                                defaultShareContent
                            } else {
                                shareInfo?.shareContent
                            }
                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                })
    }

    private fun encryptionShareUrl(): String {
        // 对参数加密后拼接到链接上
        var params = "&id=$robotId&account=$robotName&userId=${UserUtils.getUserId()}&t=${System.currentTimeMillis() / 1000L}"
        var sign = "?sign=${EncryptUtils.encryptMD5(params)}"
        encryParams = "$sign$params"
        webUrl = "${HomeApiConstant.EARN_COMMISSION}$encryParams"
        return webUrl
    }

    /**
     * description:截取WebView作为Bitmap
     **/
    private fun createSharePost() {
        var originBitmap = createWebViewSnapShot()
        Observable.create<ByteArray> {
            it.onNext(BitmapUtils.compressBitmapToByte(originBitmap, 30,
                    true))
            it.onComplete()
        }.compose(RxSchedulerComposer.normalSchedulersTransformer())
                .subscribe {
                    mSnapShotByteArray = it
                }
    }

    /**
     * description:通过WebView，draw一个Bitmap
     **/
    private fun createWebViewSnapShot(): Bitmap {
        var mWebView = webFragment.mWebContainer
        var bmp: Bitmap
        try {
            mWebView?.buildDrawingCache()
            bmp = Bitmap.createBitmap(mWebView?.drawingCache)
        } catch (e: Exception) {
            bmp = Bitmap.createBitmap(webViewWidth, webViewHeight,
                    Bitmap.Config.RGB_565)
            var canvas = Canvas(bmp)
            mWebView?.draw(canvas)
        }
        return bmp
    }
}