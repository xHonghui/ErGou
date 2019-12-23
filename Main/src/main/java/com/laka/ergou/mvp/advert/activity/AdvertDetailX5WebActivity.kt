package com.laka.ergou.mvp.advert.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import com.laka.androidlib.net.thread.ThreadManager
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.StatusBarUtil
import com.laka.ergou.R
import com.laka.ergou.common.util.share.WxShareUtils
import com.laka.ergou.common.widget.SharePostDialog
import com.laka.ergou.mvp.advert.constant.AdvertConstant
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.model.bean.AddLetteryTimeResponse
import com.laka.ergou.mvp.main.model.bean.ShareInfo
import com.laka.ergou.mvp.main.model.repository.HomeApiRepository
import com.laka.ergou.mvp.main.view.activity.X5WebActivity
import com.laka.ergou.mvp.shop.view.dialog.ShareDialog
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserApiConstant
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_advert_detail_x5_web.*
import org.json.JSONObject

class AdvertDetailX5WebActivity :X5WebActivity(){

    private lateinit var shareDialog: SharePostDialog //活动分享
    private lateinit var shareDownloadDialog: ShareDialog //app 下载了解分享
    private var defaultShareDownloadTitle = ResourceUtils.getString(R.string.share_invitation_title)
    private var defaultShareDownloadContent = ResourceUtils.getString(R.string.share_invitation_content)
    private var mMyCommissionClickTime: Long = 1000
    private var webUrl = handleShareDownloadUrl()
    private var postId = -1
    private var mShareInfo: ShareInfo? = null
    private val REQUEST_CODE = 1 // 返回的结果码


    override fun initViews() {
        super.initViews()
        shareDialog = SharePostDialog(this)
        shareDialog.enableQQShare(false)
        shareDialog.enableQQZoneShare(false)

        shareDownloadDialog = ShareDialog(this)
        shareDownloadDialog.enableQQShare(false).enableQQZoneShare(false)
    }


    override fun initTitle() {
        title_bar_web.setTitle(mWebTitle)
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
    }


    override fun initData() {
        super.initData()
        getShareInfo()
    }

    override fun initEvent() {
        super.initEvent()
        shareDialog.setOnItemClickListener {
            when (it) {
                ShareDialog.WEIXIN_CLICK -> {
                    mShareInfo?.let {
                        addLotteryTime()
                        WxShareUtils.getInstance(this@AdvertDetailX5WebActivity)
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

    override fun setStatusBarColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this,R.color.white), 0)
            StatusBarUtil.setLightModeNotFullScreen(this, true)
        } else {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, color), 0)
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
                        mWebView?.loadUrl("javascript:updateRemainTimes(${response.number})")
                    }

                    override fun onError(e: Throwable) {

                    }

                })
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

    override fun jsCallBackInform(jsonObjectStr: String?) {
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
            UserApiConstant.JS_ACTION_SHARE -> { //分享下载链接
                ThreadManager.runOnUiThread { shareDownloadDialog.show() }
            }
        }
    }

}