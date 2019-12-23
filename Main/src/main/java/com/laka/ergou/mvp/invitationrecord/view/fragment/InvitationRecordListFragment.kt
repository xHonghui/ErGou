package com.laka.ergou.mvp.invitationrecord.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.androidlib.widget.refresh.OnResultListener
import com.laka.ergou.R
import com.laka.ergou.mvp.base.view.fragment.BaseListFragment
import com.laka.ergou.mvp.invitationrecord.constant.InvitationRecordConstant
import com.laka.ergou.mvp.invitationrecord.constract.InvitationRecordConstract
import com.laka.ergou.mvp.invitationrecord.model.bean.InvitationRecord
import com.laka.ergou.mvp.invitationrecord.model.bean.InvitationRecordResponse
import com.laka.ergou.mvp.invitationrecord.presenter.InvitationRecordPresenter
import com.laka.ergou.mvp.invitationrecord.view.adapter.InvitationRecordListAdapter
import com.laka.ergou.mvp.login.LoginModuleNavigator

/**
 * @Author:summer
 * @Date:2019/3/13
 * @Description:邀请记录列表
 */
class InvitationRecordListFragment : BaseListFragment(), InvitationRecordConstract.IInvitationRecordView {

    private var mResultListener: OnResultListener? = null
    private var mDataList: ArrayList<InvitationRecord> = ArrayList()
    private var mType: Int = InvitationRecordConstant.INVITATION_NOT_ACTIVE
    // 1：app来源  2：微信来源
    private var mSourceType: Int = 1
    private lateinit var mPresenter: InvitationRecordConstract.IInvitationRecordPresenter

    companion object {
        fun getInstance(type: Int, sourceType: Int): BaseListFragment {
            val bundle = Bundle()
            bundle.putInt(InvitationRecordConstant.INVITATION_RECORD_TYPE, type)
            bundle.putInt(InvitationRecordConstant.SOURCE_TYPE, sourceType)
            val fragment = InvitationRecordListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initArgumentsData(arguments: Bundle?) {
        arguments?.let {
            mType = arguments.getInt(InvitationRecordConstant.INVITATION_RECORD_TYPE)
            mSourceType = arguments.getInt(InvitationRecordConstant.SOURCE_TYPE)
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = InvitationRecordPresenter()
        return mPresenter
    }

    override fun isLazyLoad(): Boolean {
        return true
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        super.initView(rootView, savedInstanceState)
        if (mType == InvitationRecordConstant.INVITATION_NOT_ACTIVE) {
            val infoView = LayoutInflater.from(activity).inflate(R.layout.item_recoed_activation_info, mLlRootView, false)
            mLlRootView?.addView(infoView, 0)
        }
    }

    override fun initAdapter(): BaseQuickAdapter<*, *> {
        return InvitationRecordListAdapter(R.layout.item_invitation_record, mDataList)
    }


    override fun onRequestListData(page: Int, resultListener: OnResultListener?) {
        mResultListener = resultListener
        mPresenter.onLoadInvitationRecord(mType, mSourceType, page)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        LogUtils.info("item click")
    }

    //======================================= V 层接口实现 ========================================

    override fun showData(data: InvitationRecordResponse) {
    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast("$msg")
    }

    override fun onLoadInvitationRecordSuccess(list: BaseListBean<InvitationRecord>) {
        mResultListener?.onResponse(list)
    }

    override fun onLoadError(page: Int) {
        mResultListener?.onFailure(-1, "")
    }

    override fun onAuthorFail() {
        if (activity != null && !activity.isFinishing && !activity.isDestroyed) {
            activity.finish()
        }
        LoginModuleNavigator.startLoginActivity(activity)
    }
}