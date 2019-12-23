package com.laka.ergou.mvp.message.view.activity

import android.support.v4.view.PagerAdapter
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.R
import com.laka.ergou.mvp.base.view.activity.BasePagerListMvpActivity
import com.laka.ergou.mvp.base.view.adapter.BaseCustomFragmentPagerAdapter
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.message.view.fragment.CommissionMessageFragment
import com.laka.ergou.mvp.message.view.fragment.OtherMessageFragment
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import kotlinx.android.synthetic.main.activity_base_pager_list.*

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:消息中心
 */
class MessageCenterActivity : BasePagerListMvpActivity<String>() {

    private var mFragList: ArrayList<BaseLazyLoadFragment> = arrayListOf(CommissionMessageFragment(), OtherMessageFragment())
    private var index = 0
    private lateinit var mAdapter: BaseCustomFragmentPagerAdapter

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
            index = it.getInt(UserConstant.MESSAGE_TYPE, 0)
        }
    }

    override fun initViews() {
        super.initViews()
        title_bar.setTitle(getString(R.string.message_center))
        setLeftTabText(getString(R.string.commission_message))
        setRightTabText(getString(R.string.other_message))
        view_pager_list.currentItem = index
    }

    override fun initData() {
    }

    override fun initEvent() {

    }
}