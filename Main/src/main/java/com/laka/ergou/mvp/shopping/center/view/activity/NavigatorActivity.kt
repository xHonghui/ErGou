package com.laka.ergou.mvp.shopping.center.view.activity

import android.os.Handler
import android.view.View
import android.webkit.WebView
import com.laka.androidlib.base.activity.BaseActivity
import com.laka.androidlib.util.CacheUtil
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.widget.dialog.LoadingDialog
import com.laka.androidlib.widget.dialog.PhotoDialog
import com.laka.ergou.R
import com.laka.ergou.mvp.main.HomeModuleNavigator
import com.laka.ergou.mvp.shopping.ShoppingModuleNavigator
import kotlinx.android.synthetic.main.activity_navigator.*

/**
 * @Author:Rayman
 * @Date:2018/12/19
 * @Description:主页简单跳转页面
 */
class NavigatorActivity : BaseActivity(), View.OnClickListener {

    private val imageUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547186386671&di=610c027d805c794ced59ac0e40ef289e&imgtype=0&src=http%3A%2F%2F07.imgmini.eastday.com%2Fmobile%2F20180707%2F20997cf3e53bb9af247539754575581c_wmk.jpeg"

    override fun setContentView(): Int {
        return R.layout.activity_navigator
    }

    override fun initIntent() {
    }

    private val mHandler = object : Handler(){}

    override fun initViews() {
//        GlideUtil.loadCircleImage(this,imageUrl,image_view1)
//        GlideUtil.loadCircleImage2(this,imageUrl,image_view2)
    }

    override fun initData() {
        val cacheSuze = CacheUtil.getTotalCacheSize(this)
        tv_cache.text = "缓存大小：" + cacheSuze
    }

    override fun initEvent() {
        btn_center_detail.setOnClickListener(this)
        btn_center_list.setOnClickListener(this)
        btn_home.setOnClickListener(this)
        btn_get_user_agent.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        btn_clear_cache.setOnClickListener(this)
        btn_loading_dialog.setOnClickListener(this)
    }

    var loadingDialog: LoadingDialog? = null

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_center_list -> HomeModuleNavigator.startSearchActivity(this)
            R.id.btn_center_detail -> ShoppingModuleNavigator.startProductDetailActivity(this)
            R.id.btn_home -> HomeModuleNavigator.startMainActivity(this)
            R.id.btn_get_user_agent -> {
                var webView = WebView(this)
                val userAgentString = webView.settings.userAgentString
                LogUtils.error(userAgentString)
            }
            R.id.btn_login -> {
                // ShoppingModuleNavigator.startLoginActivity(this)
                val phoneDialog = PhotoDialog(this, R.style.commonDialog)
                phoneDialog.show()
                phoneDialog.setOnClickListener {}
            }
            R.id.btn_clear_cache -> {
                CacheUtil.clearAllCache(this)
                tv_cache.text = CacheUtil.getTotalCacheSize(this)
            }
            R.id.btn_loading_dialog -> {
                showLoading()
                mHandler.postDelayed({ finish() }, 2000)
            }
        }
    }
}