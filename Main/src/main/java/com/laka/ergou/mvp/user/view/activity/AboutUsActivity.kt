package com.laka.ergou.mvp.user.view.activity

import android.view.View
import com.laka.androidlib.base.activity.BaseActivity
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.SystemUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import kotlinx.android.synthetic.main.activity_about_us.*

/**
 * @Author:summer
 * @Date:2019/1/12
 * @Description:关于我们页面
 */
class AboutUsActivity : BaseActivity(), View.OnClickListener {

    override fun setContentView(): Int {
        return R.layout.activity_about_us
    }

    override fun initIntent() {

    }

    override fun initViews() {
        title_bar.setTitle(ResourceUtils.getString(R.string.about_us))
                .showDivider(false)
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setBackGroundColor(R.color.white)
                .setTitleTextColor(R.color.black)

        tv_app_version.text = ResourceUtils.getStringWithArgs(R.string.app_version, SystemUtils.getVersionName())
    }

    override fun initData() {

    }

    override fun initEvent() {
        cl_privacy.setOnClickListener(this)
        cl_about_us.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cl_privacy -> {
                val params = HashMap<String, String>()
                params[HomeNavigatorConstant.ROUTER_VALUE] = HomeApiConstant.ABOUT_PRIVACY
                params[HomeConstant.TITLE] = ResourceUtils.getString(R.string.about_private)
                RouterNavigator.handleAppInternalNavigator(this, RouterNavigator.bannerRouterReflectMap[5].toString(), params)
            }
            R.id.cl_about_us -> {
                val params = HashMap<String, String>()
                params[HomeNavigatorConstant.ROUTER_VALUE] = HomeApiConstant.ABOUT_LAKA
                params[HomeConstant.TITLE] = ResourceUtils.getString(R.string.about_laka)
                RouterNavigator.handleAppInternalNavigator(this, RouterNavigator.bannerRouterReflectMap[5].toString(), params)
            }
        }
    }
}