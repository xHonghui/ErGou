package com.laka.ergou.mvp.user.view.activity

import com.laka.androidlib.base.activity.BaseActivity
import com.laka.androidlib.util.ResourceUtils
import com.laka.ergou.R
import kotlinx.android.synthetic.main.activity_my_robot.*

/**
 * @Author:Rayman
 * @Date:2019/1/17
 * @Description:添加机器人页面
 */
class AddRobotActivity : BaseActivity() {

    override fun setContentView(): Int {
        return R.layout.activity_add_robot
    }

    override fun initIntent() {
    }

    override fun initViews() {
        title_bar.setTitle(ResourceUtils.getString(R.string.robot_add_friend))
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)
    }

    override fun initData() {
    }

    override fun initEvent() {
    }
}