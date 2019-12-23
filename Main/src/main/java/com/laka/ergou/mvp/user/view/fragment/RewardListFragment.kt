package com.laka.ergou.mvp.user.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.androidlib.widget.refresh.OnResultListener
import com.laka.ergou.R
import com.laka.ergou.common.widget.SpacesListDecoration
import com.laka.ergou.mvp.base.view.fragment.BaseListFragment
import com.laka.ergou.mvp.user.UserModuleNavigator
import com.laka.ergou.mvp.user.constant.UserConstant
import com.laka.ergou.mvp.user.contract.IRewardListConstract
import com.laka.ergou.mvp.user.model.bean.RewardListBean
import com.laka.ergou.mvp.user.presenter.RewardListPresenter
import com.laka.ergou.mvp.user.view.adapter.RewardListAdapter

/**
 * @Author:summer
 * @Date:2019/6/26
 * @Description:其他奖励
 */
class RewardListFragment : BaseListFragment(), IRewardListConstract.IRewardListView {

    private lateinit var mPresenter: IRewardListConstract.IRewardListPresenter
    private lateinit var mListItemDecoration: SpacesListDecoration
    private var mDataList: ArrayList<RewardListBean> = ArrayList()
    private var mResultListener: OnResultListener? = null
    //1:app奖励  2:维信奖励
    private var mSourceType: Int = 1

    companion object {
        fun newInstance(sourceType: Int): BaseListFragment {
            val bundle = Bundle()
            bundle.putInt(UserConstant.REWARD_SOOURCE_TYPE, sourceType)
            var fragment = RewardListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initAdapter(): BaseQuickAdapter<*, *> {
        return RewardListAdapter(R.layout.item_reward_list, mDataList)
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = RewardListPresenter()
        return mPresenter
    }

    override fun initArgumentsData(arguments: Bundle?) {
        arguments?.let {
            mSourceType = it.getInt(UserConstant.REWARD_SOOURCE_TYPE, UserConstant.TYPE_REWARD_APP)
        }
    }

    override fun isLazyLoad(): Boolean {
        return true
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        super.initView(rootView, savedInstanceState)
        mListItemDecoration = SpacesListDecoration(0, 0, 0, ScreenUtils.dp2px(1f))
        mRecyclerView?.noDataView?.findViewById<TextView>(R.id.tv_no_data)?.text = "暂无奖励数据"
        if (::mListItemDecoration.isInitialized) {
            mRecyclerView?.addItemDecoration(mListItemDecoration)
        }
    }

    override fun onRequestListData(page: Int, resultListener: OnResultListener?) {
        mResultListener = resultListener
        mPresenter.onLoadRewardListData(page, mSourceType)
    }


    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val rewardListBean: RewardListBean? = adapter?.getItem(position) as? RewardListBean
        rewardListBean?.let {
            //奖励item点击
        }
    }

    //======================================== view 层接口实现 =====================================
    override fun showData(data: RewardListBean) {

    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast("$msg")
    }

    override fun onLoadRewardListSuccess(result: BaseListBean<RewardListBean>) {
        mResultListener?.onResponse(result)
    }

    override fun onLoadError(page: Int) {
        mResultListener?.onFailure(-1, "")
    }

    override fun onAuthorFail() {
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            return
        }
        UserModuleNavigator.startLoginActivity(activity)
        activity.finish()
    }
}