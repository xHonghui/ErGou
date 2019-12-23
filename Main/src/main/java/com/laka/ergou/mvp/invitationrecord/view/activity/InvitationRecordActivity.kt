package com.laka.ergou.mvp.invitationrecord.view.activity

import android.support.v4.view.PagerAdapter
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.R
import com.laka.ergou.mvp.base.view.activity.BasePagerListMvpActivity
import com.laka.ergou.mvp.base.view.adapter.BaseCustomFragmentPagerAdapter
import com.laka.ergou.mvp.invitationrecord.constant.InvitationRecordConstant
import com.laka.ergou.mvp.invitationrecord.model.bean.InvitationRecordResponse
import com.laka.ergou.mvp.invitationrecord.model.event.InvitationRecordEvent
import com.laka.ergou.mvp.invitationrecord.view.fragment.InvitationRecordListFragment
import com.laka.ergou.mvp.invitationrecord.view.fragment.InvitationRecordPageFragment
import com.laka.ergou.mvp.order.constant.MyOrderConstant
import com.laka.ergou.mvp.order.view.fragment.OrderPagerFragment
import kotlinx.android.synthetic.main.activity_base_pager_list.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Author:summer
 * @Date:2019/3/13
 * @Description:邀请记录
 */
class InvitationRecordActivity : BasePagerListMvpActivity<InvitationRecordResponse>() {

    private var mFragList: ArrayList<BaseLazyLoadFragment> = arrayListOf(InvitationRecordPageFragment.getInstance(InvitationRecordConstant.APP_TYPE), InvitationRecordPageFragment.getInstance(InvitationRecordConstant.WECHAT_TYPE))

    override fun showData(data: InvitationRecordResponse) {

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
        title_bar.setTitle(getString(R.string.title_invitation_record))
        setLeftTabText("APP邀请记录")
        setRightTabText("微信邀请记录")
    }

    override fun initData() {
    }

    override fun initEvent() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onInvitationRecordEvent(event: InvitationRecordEvent) {
        when (event.eventType) {
            InvitationRecordConstant.INVITATION_NOT_ACTIVE -> {
                setLeftTabText("未激活（${event.data}）")
            }
            InvitationRecordConstant.INVITATION_ACTIVED -> {
                setRightTabText("已激活（${event.data}）")
            }
        }
    }

}