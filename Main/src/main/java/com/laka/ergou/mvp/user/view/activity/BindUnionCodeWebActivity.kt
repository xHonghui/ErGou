package com.laka.ergou.mvp.user.view.activity

import android.content.Intent
import android.text.TextUtils
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.laka.androidlib.base.activity.BaseActivity
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.R
import com.laka.ergou.common.util.regex.RegexUtils
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.shop.utils.AliPageUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import kotlinx.android.synthetic.main.activity_bind_union_code.*
import kotlinx.android.synthetic.main.activity_user_info.title_bar

/**
 * @Author:summer
 * @Date:2019/3/22
 * @Description:淘宝联盟授权CodeUrl 页面
 */
class BindUnionCodeWebActivity : BaseActivity() {

    private var mWebUrl = ""
    private var mWebTitle = ""

    override fun setContentView(): Int {
        return R.layout.activity_bind_union_code
    }

    override fun initIntent() {
        intent?.extras?.let {
            mWebUrl = it.getString(HomeConstant.WEB_URL, "")
            mWebTitle = it.getString(HomeConstant.WEB_TITLE, "")
        }
    }

    override fun initViews() {
        title_bar.setTitle(mWebTitle)
                .setBackGroundColor(R.color.white)
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)
    }

    override fun initData() {
        AliPageUtils.openAliPageForH5(this, web_view, webViewClient, WebChromeClient(), mWebUrl)
    }

    override fun initEvent() {

    }

    private val webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
            LogUtils.info("onActivityResult:------load url:$url")
            if (!TextUtils.isEmpty(url)) {
                if (url?.contains("code=")!! && url.contains("state=getRelationIdByCode")) {
                    onCustomActivityResult(url)
                }
            }
            return super.shouldOverrideUrlLoading(webView, url)
        }
    }

    private fun onCustomActivityResult(url: String) {
        val regex1 = "code=([a-zA-Z0-9]+)"
        val regex2 = "state=([a-zA-Z0-9]+)"
        var code = RegexUtils.findTergetStrForRegex(url, regex1, 1)
        var state = RegexUtils.findTergetStrForRegex(url, regex2, 1)
        val intent = Intent()
        intent.putExtra(UserConstant.UNION_CODE, code)
        intent.putExtra(UserConstant.UNION_STATE, state)
        setResult(UserConstant.BIND_UNION_RESULT_CODE, intent)
        finish()
    }

}