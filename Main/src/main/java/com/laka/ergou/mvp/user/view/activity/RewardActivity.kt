package com.laka.ergou.mvp.user.view.activity

import android.support.v4.view.PagerAdapter
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.mvp.base.view.activity.BasePagerListMvpActivity
import com.laka.ergou.mvp.base.view.adapter.BaseCustomFragmentPagerAdapter
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.view.fragment.RewardListFragment
import kotlinx.android.synthetic.main.activity_base_pager_list.*

/**
 * @Author:summer
 * @Date:2019/6/26
 * @Description:
 */
class RewardActivity : BasePagerListMvpActivity<String>() {

    private var mFragList: ArrayList<BaseLazyLoadFragment> = arrayListOf(RewardListFragment.newInstance(UserConstant.TYPE_REWARD_APP), RewardListFragment.newInstance(UserConstant.TYPE_REWARD_WECHAT))
    private lateinit var mAdapter: BaseCustomFragmentPagerAdapter
    private var mIndex: Int = 0 //当前选中标签

    override fun showData(data: String) {

    }

    override fun showErrorMsg(msg: String?) {

    }

    override fun createPresenter(): IBasePresenter<*>? {
        return null
    }

    override fun getPagerAdapter(): PagerAdapter {
        mAdapter = BaseCustomFragmentPagerAdapter(supportFragmentManager, mFragList)
        return mAdapter
    }

    override fun initIntent() {
        intent.extras?.let {
            mIndex = it.getInt(UserConstant.MESSAGE_TYPE, 0)
        }
    }

    override fun initViews() {
        super.initViews()
        title_bar.setTitle("其他奖励")
        setLeftTabText("APP奖励")
        setRightTabText("微信奖励")
        view_pager_list.currentItem = mIndex
    }

    override fun initData() {

    }

    override fun initEvent() {

    }
}