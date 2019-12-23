package com.laka.ergou.mvp.teamaward.view.activity

import android.support.v4.view.PagerAdapter
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.R
import com.laka.ergou.mvp.armsteam.constant.MyArmsLevelsConstant
import com.laka.ergou.mvp.base.view.activity.BasePagerListMvpActivity
import com.laka.ergou.mvp.base.view.adapter.BaseCustomFragmentPagerAdapter
import com.laka.ergou.mvp.teamaward.view.fragment.TearmAwardPagerFragment
import kotlinx.android.synthetic.main.activity_base_pager_list.*

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:战队奖励
 */
class TeamAwardActivity : BasePagerListMvpActivity<String>() {

    private var mFragList: ArrayList<BaseLazyLoadFragment> = arrayListOf(TearmAwardPagerFragment.getInstance(MyArmsLevelsConstant.TYPE_APP), TearmAwardPagerFragment.getInstance(MyArmsLevelsConstant.TYPE_WECHAT))

    override fun showData(data: String) {

    }

    override fun showErrorMsg(msg: String?) {
    }

    override fun createPresenter(): IBasePresenter<*>? {
        return null
    }

    override fun getPagerAdapter(): PagerAdapter {
        return BaseCustomFragmentPagerAdapter(supportFragmentManager, mFragList)
    }

    override fun initIntent() {
    }

    override fun initViews() {
        super.initViews()
        title_bar.setTitle(getString(R.string.team_award))
        setLeftTabText(getString(R.string.app_team_award))
        setRightTabText(getString(R.string.wechat_team_award))
    }



    override fun initData() {
    }

    override fun initEvent() {
    }


}