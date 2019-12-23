package com.laka.ergou.common.util.share;

import android.content.Context;

import com.laka.androidlib.util.toast.ToastHelper;
import com.laka.ergou.R;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

/**
 * @Author:Rayman
 * @Date:2019/3/8
 * @Description:微信登陆帮助类
 */

public class WxLoginUtils {

    private WxHelper mHelper;
    private volatile static WxLoginUtils mInstance;

    private WxLoginUtils(Context context) {
        mHelper = new WxHelper(context);
    }

    public static WxLoginUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (WxLoginUtils.class) {
                if (mInstance == null) {
                    mInstance = new WxLoginUtils(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    public void sendAuthorizationReq() {
        if (!mHelper.isInstallWeChat()) {
            ToastHelper.showToast(R.string.please_install_weixin);
            return;
        }
        SendAuth.Req request = new SendAuth.Req();
        request.scope = "snsapi_userinfo";
        mHelper.sendLoginReq(request);
    }
}
