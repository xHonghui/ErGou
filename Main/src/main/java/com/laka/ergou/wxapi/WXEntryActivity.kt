package com.laka.ergou.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.common.util.share.WxHelper
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.event.LoginEvent
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

/**
 * @Author:Rayman
 * @Date:2019/1/23
 * @Description:微信分享回调
 */
class WXEntryActivity : Activity(), IWXAPIEventHandler {

    private val TAG = "WXEntryActivity"
    private var mWxHelper: WxHelper? = null
    var type = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mWxHelper = WxHelper(this)
        mWxHelper?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        mWxHelper?.handleIntent(intent, this)
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    override fun onReq(req: BaseReq) {
        when (req.type) {
            ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX -> {
            }
            ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX -> {
            }
            else -> {
            }
        }
        LogUtils.info("wechat req:" + req.type)
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    override fun onResp(resp: BaseResp) {
        val type = resp.type
        when (resp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                LogUtils.debug("分享成功 type=" + type + " resp=" + resp.toString())
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                LogUtils.debug("分享取消 type=" + type + " resp=" + resp.toString())
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                LogUtils.debug("分享失败 type=" + type + " resp=" + resp.toString())
            }
            BaseResp.ErrCode.ERR_UNSUPPORT -> {

            }
            else -> {
            }
        }

        if (type == ConstantsAPI.COMMAND_SENDAUTH) {//授权登录
            val response = resp as? SendAuth.Resp
            response?.let {
                EventBusManager.postEvent(LoginConstant.EVENT_LOGIN_WX, response)
            }
        }
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        mWxHelper!!.unRegister()
    }
}