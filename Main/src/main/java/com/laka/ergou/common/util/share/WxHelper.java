package com.laka.ergou.common.util.share;

import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @Author:Rayman
 * @Date:2019/1/23
 * @Description:微信分享帮助类，需要在这里配置微信的SDKKey
 */

public class WxHelper {

    private static final String WE_CHAT_SDK_ID = "wx79ef5965b2cbb35a";
    private static final String WECAHT_LOGIN_SCOPE = "snsapi_userinfo";
    public static final int WE_CHAT_PAY_TYPE_SUCCESS = 0;
    public static final int WE_CHAT_PAY_TYPE_FAIL = 1;
    public static final int WE_CHAT_PAY_TYPE_CANCEL = 2;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI mApi;

    public WxHelper(Context context) {
        register(context);
    }

    public void register(Context context) {
        if (mApi == null) {
            // 通过WXAPIFactory工厂，获取IWXAPI的实例
            mApi = WXAPIFactory.createWXAPI(context, WE_CHAT_SDK_ID);
            mApi.registerApp(WE_CHAT_SDK_ID);
        }
    }

    public void unRegister() {
        if (mApi != null) {
            mApi.unregisterApp();
            mApi.detach();
            mApi = null;
        }
    }

    public void handleIntent(Intent intent, IWXAPIEventHandler handler) {
        mApi.handleIntent(intent, handler);
    }

    public void sendPayReq(PayReq payReq) {
        mApi.sendReq(payReq);
    }

    public boolean isInstallWeChat() {
        return mApi.isWXAppInstalled();
    }

    public void sendShareReq(SendMessageToWX.Req req) {
        mApi.sendReq(req);
    }

    public void sendLoginReq(SendAuth.Req req) {
        mApi.sendReq(req);
    }

    public void sendReq(SendAuth.Req req) {
        mApi.sendReq(req);
    }

    public void onWechatAuthor() {
        SendAuth.Req request = new SendAuth.Req();
        request.scope = WECAHT_LOGIN_SCOPE;
        sendReq(request);
    }

}
