package com.laka.ergou.mvp.commission.view.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.androidlib.widget.refresh.OnResultListener
import com.laka.androidlib.widget.refresh.RefreshRecycleView
import com.laka.ergou.R
import com.laka.ergou.common.widget.SpacesListDecoration
import com.laka.ergou.mvp.base.view.fragment.BaseListFragment
import com.laka.ergou.mvp.commission.constant.CommissionConstant
import com.laka.ergou.mvp.commission.constract.ICommissionConstract
import com.laka.ergou.mvp.commission.model.bean.CommissionDIYBean
import com.laka.ergou.mvp.commission.model.bean.CommissionNewBean
import com.laka.ergou.mvp.commission.presenter.CommissionPresenter
import com.laka.ergou.mvp.commission.view.activity.CommissionActivity
import com.laka.ergou.mvp.commission.view.adapter.CommissionListAdapter
import com.laka.ergou.mvp.invitationrecord.InvitationRecordModuleNavigator
import com.laka.ergou.mvp.order.OrderModuleNavigator
import com.laka.ergou.mvp.teamaward.TeamAwardModelNavigator
import com.laka.ergou.mvp.user.UserModuleNavigator

/**
 * @Author:summer
 * @Date:2019/7/1
 * @Description:佣金列表
 */
class CommissionFragment : BaseListFragment(), ICommissionConstract.IBaseCommissonView {

    lateinit var mCommissionPresenter: CommissionPresenter
    private var mResultListener: OnResultListener? = null
    var commissionType: String = ""
    private var mDataList = ArrayList<CommissionDIYBean>()
    //间隔
    private lateinit var mListItemDecoration: SpacesListDecoration
    var mHeaderView: View? = null
    var allFrozen: TextView? = null
    var allBalance: TextView? = null

    companion object {
        fun newInstance(type: String): CommissionFragment {
            val bundle = Bundle()
            var fragment = CommissionFragment()
            bundle.putString(CommissionConstant.COMMISSION_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mCommissionPresenter = CommissionPresenter()
        LogUtils.info("createPresenter")
        return mCommissionPresenter
    }

    override fun isLazyLoad(): Boolean {
        return true
    }

    override fun onRequestListData(page: Int, resultListener: OnResultListener?) {
        mResultListener = resultListener
        mCommissionPresenter.onGetMyCommissonData(commissionType, page.toString())
    }

    fun reloadData() {
        mRecyclerView?.enableRefresh(true)
        mRecyclerView?.refresh()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

    }

    override fun initAdapter(): BaseQuickAdapter<*, *> {
        return CommissionListAdapter(R.layout.item_commission_list, mDataList)
    }

    override fun initArgumentsData(arguments: Bundle?) {
        arguments?.let {
            commissionType = it.getString(CommissionConstant.COMMISSION_TYPE, "")
        }
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        super.initView(rootView, savedInstanceState)
        mListItemDecoration = SpacesListDecoration(ScreenUtils.dp2px(10f), ScreenUtils.dp2px(15f), ScreenUtils.dp2px(10f), 0)
        mRecyclerView?.noDataView?.findViewById<ImageView>(R.id.iv_no_data)?.setImageResource(R.drawable.ic_no_order_data)
        mRecyclerView?.noDataView?.findViewById<TextView>(R.id.tv_no_data)?.text = ResourceUtils.getString(R.string.no_order_data_hint)
//        mRecyclerView?.addItemDecoration(mListItemDecoration)
        mRecyclerView?.enableLoadMore(false)
    }

    override fun initEvent() {
        super.initEvent()
        mRecyclerView?.onItemClickListener = object : RefreshRecycleView.OnItemClickListener<CommissionDIYBean> {
            override fun onItemClick(data: CommissionDIYBean?, position: Int) {

            }

            override fun onChildClick(id: Int, data: CommissionDIYBean?, position: Int) {
                LogUtils.info("onChildClick")
                when (id) {
                    R.id.tv_detail,
                    R.id.iv_detail -> {
                        when (position) {
                            1 -> { //跳转我的订单
                                OrderModuleNavigator.startOrderActivity(activity)
                            }
                            2 -> { //跳转战队提成
                                TeamAwardModelNavigator.startTeamAwardActivity(activity)
                            }
                            3 -> { //跳转邀请记录
                                InvitationRecordModuleNavigator.startInvitationRecordActivity(activity)
                            }
                            4 -> { //跳转其他奖励
                                UserModuleNavigator.startRewardListActivity(activity)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onGetMyCommissonDataSuccess(result: CommissionNewBean, list: BaseListBean<CommissionDIYBean>) {
        if (commissionType == CommissionConstant.MONTH && activity is CommissionActivity) {
            (activity as CommissionActivity).setData(result)
        }
        mRecyclerView?.enableRefresh(false)
        if (mHeaderView == null) {
            mHeaderView = layoutInflater.inflate(R.layout.item_commission_header, mRecyclerView as ViewGroup, false)
            allFrozen = mHeaderView?.findViewById<TextView>(R.id.tv_all_frozen)
            allBalance = mHeaderView?.findViewById<TextView>(R.id.tv_all_balance)
            mAdapter?.addHeaderView(mHeaderView)
        }
        result.subsidy?.total.let {
            allFrozen?.text = "¥${it.frozen}"
            allBalance?.text = "¥${it.balance}"
        }
        mResultListener?.onResponse(list)
    }

    override fun onLoadError() {
        mResultListener?.onFailure(-1, "")
    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast("$msg")
    }

    override fun showData(data: CommissionNewBean) {

    }
}