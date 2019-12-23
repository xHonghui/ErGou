package com.laka.ergou.mvp.login.view.activity

import android.content.Intent
import com.alibaba.baichuan.trade.biz.login.AlibcLogin
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback
import com.laka.androidlib.base.activity.BaseActivity
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.SPHelper
import com.laka.ergou.R
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.login.view.fragment.LoginFragment
import com.laka.ergou.mvp.login.view.fragment.PhoneLoginFragment
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant

/**
 * @Author:summer
 * @Date:2019/1/7
 * @Description: 手机登录（已废弃）
 */
class PhoneLoginActivity : BaseActivity() {

    override fun setContentView(): Int {
        return R.layout.activity_login
    }

    override fun initIntent() {

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        initIntent()
    }

    override fun initViews() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fl_container, PhoneLoginFragment(), LoginFragment::class.java.name)
        transaction.commit()
    }


    override fun initData() {
    }

    override fun initEvent() {

    }

}