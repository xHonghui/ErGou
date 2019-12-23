package com.laka.ergou.mvp.login.view.activity

import com.laka.androidlib.base.activity.BaseActivity
import com.laka.ergou.R
import com.laka.ergou.mvp.login.view.fragment.TaoBaoAuthorFragment

/**
 * @Author:summer
 * @Date:2019/1/15
 * @Description:淘宝授权
 */
class TaobaoAuthorActivity : BaseActivity() {

    override fun setContentView(): Int {
        return R.layout.activity_taobao_author
    }

    override fun initIntent() {

    }

    override fun initViews() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fl_container, TaoBaoAuthorFragment(), TaoBaoAuthorFragment::class.java.name)
        transaction.commit()
    }

    override fun initData() {

    }

    override fun initEvent() {

    }
}