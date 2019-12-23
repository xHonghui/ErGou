package com.laka.ergou.mvp.login.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.View
import com.laka.androidlib.base.fragment.BaseFragment
import com.laka.ergou.R
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * @Author:summer
 * @Date:2019/1/7
 * @Description: 登录方式选择fragment（已废弃）
 */
class LoginFragment : BaseFragment() {

    override fun setContentView(): Int {
        return R.layout.fragment_login
    }

    override fun initArgumentsData(arguments: Bundle?) {
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    override fun initEvent() {
        text_view_phone_login.setOnClickListener {
            showMultiFragment(PhoneLoginFragment())
        }
        text_view_taobao_login.setOnClickListener {
            showMultiFragment(TaoBaoAuthorFragment())
        }
        btn_back.setOnClickListener {
            activity.finish()
        }
        btn_bugly_test.setOnClickListener {
            CrashReport.testJavaCrash()
        }
    }

    private fun showMultiFragment(fragment: Fragment) {
        val transaction= activity.supportFragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.add(R.id.fl_container,fragment,fragment::class.java.name)
        transaction.addToBackStack(fragment::class.java.name)
        transaction.commit()
    }
}