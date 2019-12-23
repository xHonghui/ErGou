package com.laka.ergou.mvp.main.view.activity

import android.content.res.Configuration
import android.graphics.PixelFormat
import android.view.View
import android.view.WindowManager
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.base.view.activity.BaseDefaultWebActivity
import com.laka.ergou.mvp.chat.constant.ChatEventConstant
import kotlinx.android.synthetic.main.activity_x5_web.*

open class X5WebActivity : BaseDefaultWebActivity() {


    override fun setContentView(): Int = R.layout.activity_x5_web

    override fun initViews() {
        super.initViews()
        initTitle()
        initUrl()
        // 网页中的视频，上屏幕的时候，可能出现闪烁的情况，需要如下设置
        window.setFormat(PixelFormat.TRANSLUCENT)
        //避免输入法界面弹出后遮挡输入光标的问题
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

    }


    override fun initData() {
        LogUtils.info("webview---url=$mWebUrl")
    }

    open fun initUrl() {
        loadUrl(mWebUrl)
    }

    open fun initTitle() {
        title_bar_web.setLeftIcon(R.drawable.ic_arrow_back_white)
                .setBackGroundColor(R.color.color_main)
                .setTitleTextColor(R.color.white)
                .setTitleTextSize(16)
                .setOnLeftClickListener {
                    finish()
                }
        title_bar_web.setTitle(mWebTitle)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            title_bar_web.visibility = View.GONE
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            title_bar_web.visibility = View.VISIBLE
        }
    }

    override fun jsCallBackInform(jsonObjectStr: String?) {
        LogUtils.info("jsonObjectStr------$jsonObjectStr")
    }

    override fun setWebTitle(title: String?) {
        title?.let {
            mWebTitle = "$title"
            title_bar_web.setTitle(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 发送事件，解除--ChatActivity多次发送Copy到的文本限制
        EventBusManager.postEvent(ChatEventConstant.EVENT_BLOCK_COPY_FROM_WEB, false)
    }
}