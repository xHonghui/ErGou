package com.laka.ergou.mvp.shop.view.activity

import com.laka.androidlib.base.activity.BaseActivity
import com.laka.ergou.R
import kotlinx.android.synthetic.main.activity_taobao_author_fail.*

/**
 * @Author:summer
 * @Date:2019/3/23
 * @Description:淘宝授权成功
 */
class TaoBaoAuthorSuccessActivity : BaseActivity() {

    override fun setContentView(): Int {
        return R.layout.activity_taobao_author_success
    }

    override fun initIntent() {

    }

    override fun initViews() {
        title_bar.setBackGroundColor(R.color.white)
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)
        sb_taobao_author.setOnClickListener {
            finish()
        }
    }

    override fun initData() {

    }

    override fun initEvent() {

    }
}