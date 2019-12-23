package com.laka.ergou.mvp.shop.view.activity

import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.laka.androidlib.base.activity.BaseActivity
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.shop.utils.AliPageUtils
import kotlinx.android.synthetic.main.activity_receive.*

/**
 * @Author:summer
 * @Date:2019/9/19
 * @Description:
 */
class ReceiveCouponActivity : BaseActivity() {

    private lateinit var mWebUrl: String
    private lateinit var mPid: String
    private lateinit var mAzoneId: String

    override fun setContentView(): Int {
        return R.layout.activity_receive
    }

    override fun initIntent() {
        intent?.extras?.let {
            mWebUrl = it.getString(HomeConstant.WEB_URL, "")
            mPid = it.getString(HomeConstant.USER_PID, "")
            mAzoneId = it.getString(HomeConstant.USER_ADZONE_ID, "")
        }
        LogUtils.info("receiv-------------mWebUrl=$mWebUrl")
    }

    override fun initViews() {

    }

    override fun initData() {
        AliPageUtils.openAliPageForH5(this, web_view, WebViewClient(), WebChromeClient(), mWebUrl)
    }

    override fun initEvent() {

    }
}