package com.laka.ergou.mvp.teamaward.view.fragment

import android.os.Bundle
import android.view.View
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
import com.laka.ergou.R
import com.laka.ergou.common.widget.SpacesListDecoration
import com.laka.ergou.mvp.base.view.fragment.BaseListFragment
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.teamaward.constant.TearmAwardConstant
import com.laka.ergou.mvp.teamaward.constract.ITearmAwardConstract
import com.laka.ergou.mvp.teamaward.model.bean.Order
import com.laka.ergou.mvp.teamaward.model.bean.TearmAwardBean
import com.laka.ergou.mvp.teamaward.presenter.TearmAwardPresenter
import com.laka.ergou.mvp.teamaward.view.adapter.TearmAwardAdapter

/**
 * @Author:summer
 * @Date:2019/7/1
 * @Description:战队提成
 */
class TearmAwardFragment : BaseListFragment(), ITearmAwardConstract.ITearmAwardView {


    var mSourceType: Int = -1
    var mType: Int = -1
    private var mResultListener: OnResultListener? = null
    private var mDataList = ArrayList<Order>()

    //间隔
    private lateinit var mListItemDecoration: SpacesListDecoration

    companion object {
        fun newInstance(mSourceType: Int, type: Int): BaseListFragment {
            val bundle = Bundle()
            bundle.putInt(TearmAwardConstant.PAGE_TYPE, mSourceType)
            bundle.putInt(TearmAwardConstant.TEARM_AWARD_LIST_TYPE, type)
            var fragment = TearmAwardFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun isLazyLoad(): Boolean {
        return true
    }

    override fun onRequestListData(page: Int, resultListener: OnResultListener?) {
        LogUtils.info("team---------onRequestListData")
        mResultListener = resultListener
        mPresenter.onGetComradeSubsidy(mType.toString(), mSourceType.toString(), page)
    }

    override fun onGetComradeSubsidySuccess(result: BaseListBean<Order>, total: Int) {
        mResultListener?.onResponse(result)
    }

    override fun onLoadError(page: Int) {
        mResultListener?.onFailure(-1, "")
    }

    override fun onAuthorFail() {
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            return
        }
        LoginModuleNavigator.startLoginActivity(activity)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

    }

    override fun initAdapter(): BaseQuickAdapter<*, *> {
        return TearmAwardAdapter(R.layout.item_tearm_award, mDataList)
    }

    override fun initArgumentsData(arguments: Bundle?) {
        arguments?.let {
            mSourceType = it.getInt(TearmAwardConstant.PAGE_TYPE)
            mType = it.getInt(TearmAwardConstant.TEARM_AWARD_LIST_TYPE)
        }
    }

    override fun showData(data: TearmAwardBean) {

    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast("$msg")
    }

    lateinit var mPresenter: TearmAwardPresenter
    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = TearmAwardPresenter()
        return mPresenter
    }


    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        super.initView(rootView, savedInstanceState)
        mListItemDecoration = SpacesListDecoration(0, 0, 0, ScreenUtils.dp2px(1f))
        mRecyclerView?.noDataView?.findViewById<ImageView>(R.id.iv_no_data)?.setImageResource(R.drawable.ic_no_order_data)
        mRecyclerView?.noDataView?.findViewById<TextView>(R.id.tv_no_data)?.text = ResourceUtils.getString(R.string.no_order_data_hint)
        mRecyclerView?.addItemDecoration(mListItemDecoration)
        //mRecyclerView?.enableLoadMore(false)
    }

}