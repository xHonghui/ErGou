package com.laka.ergou.mvp.user.view.activity

import android.text.TextUtils
import com.laka.androidlib.net.thread.ThreadManager
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.ResourceUtils
import com.laka.ergou.R
import com.laka.ergou.common.util.share.WxShareUtils
import com.laka.ergou.mvp.base.webConfig.JSInterfaceCallBack
import com.laka.ergou.mvp.base.webConfig.JSInterfaceDefault
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.view.activity.X5WebActivity
import com.laka.ergou.mvp.shop.view.dialog.ShareDialog
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserApiConstant
import org.json.JSONObject

/**
 * @Author:summer
 * @Date:2019/7/15
 * @Description:邀请用户界面
 */
class InvitationWebActivity : X5WebActivity() {

    /**
     * 双击限制
     * */
    private var mPlaybillClickTime = 0L

    /**
     * description:分享
     **/
    private var mShareUrl: String = "" //下载的url
    private lateinit var shareDialog: ShareDialog
    private var defaultShareTitle = ResourceUtils.getString(R.string.share_invitation_title)
    private var defaultShareContent = ResourceUtils.getString(R.string.share_invitation_content)

    override fun initViews() {
        super.initViews()
        mShareUrl = handleShareUrl()
        shareDialog = ShareDialog(this)
        shareDialog.enableQQShare(false)
                .enableQQZoneShare(false)
    }

    override fun initEvent() {
        super.initEvent()
        shareDialog.setOnItemClickListener {
            when (it) {
                ShareDialog.WEIXIN_CLICK -> {
                    WxShareUtils.getInstance(this@InvitationWebActivity)
                            .shareWebActionBitMap(true, defaultShareTitle, defaultShareContent
                                    , mShareUrl, ResourceUtils.getBitmap(R.mipmap.ic_launcher))
                }
                ShareDialog.FRIEND_CIRCLE_CLICK -> {
                    WxShareUtils.getInstance(this@InvitationWebActivity)
                            .shareWebActionBitMap(false, defaultShareTitle, defaultShareContent
                                    , mShareUrl, ResourceUtils.getBitmap(R.mipmap.ic_launcher))
                }
            }
        }
    }

    private fun handleShareUrl(): String {
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

    override fun jsCallBackInform(jsonObjectStr: String?) {
        LogUtils.info("输出StrJson：$jsonObjectStr")
        val jsonObject = JSONObject(jsonObjectStr)
        val action = jsonObject?.getString(UserApiConstant.JS_ACTION)
        when (action) {
            UserApiConstant.JS_ACTION_SHARE -> {
                ThreadManager.runOnUiThread { shareDialog.show() }
            }
        }
    }
}