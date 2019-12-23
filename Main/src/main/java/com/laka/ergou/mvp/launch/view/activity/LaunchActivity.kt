package com.laka.ergou.mvp.launch.view.activity

import android.content.Intent
import android.os.Handler
import com.laka.androidlib.base.activity.BaseActivity
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.StatusBarUtil
import com.laka.androidlib.util.network.NetworkUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.launch.LaunchNavigator
import com.laka.ergou.mvp.launch.constant.LaunchConstant
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import com.laka.ergou.mvp.shopping.center.model.bean.AdvertBean
import java.io.File

/**
 * @Author:summer
 * @Date:2019/1/15
 * @Description:
 */
class LaunchActivity : BaseActivity() {

    private val mHandler = Handler()

    private val mRunnable = Runnable {
        if (NetworkUtils.isNetworkAvailable()) {
            val advertBean = SPHelper.getObject(ShoppingCenterConstant.SP_KEY_ADVERT, AdvertBean::class.java)
            var path = SPHelper.getString(ShoppingCenterConstant.SP_KEY_ADVERT_PATH, "")
            val imgPath = "${cacheDir.absolutePath}/${ShoppingCenterConstant.SP_KEY_ADVERT_IMG_NAME}"
            val file = File(imgPath)
            advertBean?.let {
                if (path == it.img_path
                        && file.exists()
                        && it.scene_extra != null
                ) {
                    var time = it.scene_extra[LaunchConstant.KEY_ADVERT_TIMESTAMP]
                    time?.let {
                        if (System.currentTimeMillis() / 1000 < it.toLong()) {
                            LaunchNavigator.startMainActivity(this, true)
                            finish()
                        } else {
                            startMain()
                        }
                    } ?: startMain()
                } else {
                    startMain()
                }
            } ?: startMain()
        } else {
            startMain()
        }
    }


    override fun setContentView(): Int {
        return R.layout.activity_launch
    }

    override fun initIntent() {

    }

    override fun initViews() {
        //首次启动 Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT 为 0，再次点击图标启动时就不为零了
        if (!this.isTaskRoot) {
            val intent = intent
            if (intent != null) {
                val action = intent.action
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    mHandler.removeCallbacksAndMessages(null)
                    finish()
                    return
                }
            }
        }
        mHandler.removeCallbacks(mRunnable)
        mHandler.postDelayed(mRunnable, 500)
    }

    override fun initData() {
        LogUtils.info("屏幕密度-------${ScreenUtils.getDensity(this)}")
    }

    override fun initEvent() {

    }

    private fun startMain() {
        LaunchNavigator.startMainActivity(this)
        finish()
    }

    override fun setStatusBarColor(color: Int) {
        StatusBarUtil.setTranslucentForImageView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler?.removeCallbacks(mRunnable)
    }
}