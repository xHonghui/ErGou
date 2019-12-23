package com.laka.ergou.mvp.armsteam.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.androidlib.widget.refresh.OnResultListener
import com.laka.ergou.R
import com.laka.ergou.mvp.base.view.fragment.BaseListFragment
import com.laka.ergou.mvp.armsteam.constant.MyArmsLevelsConstant
import com.laka.ergou.mvp.armsteam.contract.MyArmsLevelsContract
import com.laka.ergou.mvp.armsteam.model.bean.MyArmsLevelsBean
import com.laka.ergou.mvp.armsteam.model.event.MyArmsLevelsEvent
import com.laka.ergou.mvp.armsteam.presenter.MyArmsLevelsPresenter
import com.laka.ergou.mvp.armsteam.view.adapter.MyArmsLevelsAdapter
import com.laka.ergou.mvp.login.LoginModuleNavigator

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:我的战队列表页
 */
class MyArmsListFragment : BaseListFragment(), MyArmsLevelsContract.IMyLowerLevelsView {

    private lateinit var mPresenter: MyArmsLevelsContract.IMyLowerLevelsPresenter
    private var mResultListener: OnResultListener? = null
    private var mDataList: ArrayList<MyArmsLevelsBean> = ArrayList()
    //战友类型，1 app  2 微信
    private var mPageType: Int = 1
    //战友等级，1:一级战友  2：二级战友  3：三级战友
    private var mComradeArmsLevelsType: Int = 1

    companion object {
        fun getInstance(pageType: Int, comradeArmsLevels: Int): BaseListFragment {
            val bundle = Bundle()
            bundle.putInt(MyArmsLevelsConstant.MY_LOWER_LEVELS_PAGE_TYPE, pageType)
            bundle.putInt(MyArmsLevelsConstant.MY_COMRADE_ARMS_LEVELS_TYPE, comradeArmsLevels)
            val fragment = MyArmsListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initArgumentsData(arguments: Bundle?) {
        arguments?.let {
            mPageType = arguments.getInt(MyArmsLevelsConstant.MY_LOWER_LEVELS_PAGE_TYPE, 1)
            mComradeArmsLevelsType = arguments.getInt(MyArmsLevelsConstant.MY_COMRADE_ARMS_LEVELS_TYPE, 1)
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = MyArmsLevelsPresenter()
        return mPresenter
    }

    override fun isLazyLoad(): Boolean {
        return false
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        super.initView(rootView, savedInstanceState)
        mPresenter = MyArmsLevelsPresenter()
        mPresenter.setView(this)
        mRecyclerView?.noDataView?.findViewById<TextView>(R.id.tv_no_data)?.text = ResourceUtils.getString(R.string.no_subordinate_data_hint)
    }

    override fun initAdapter(): BaseQuickAdapter<*, *> {
        R.layout.item_my_lower_levels_top  //我的等级战友介绍item
        return MyArmsLevelsAdapter(activity, R.layout.item_my_lower_levels, mDataList, mPageType, mComradeArmsLevelsType)
    }

    override fun onRequestListData(page: Int, resultListener: OnResultListener?) {
        mResultListener = resultListener
        mPresenter.onLoadMyArmsLevelsData(page, mPageType, mComradeArmsLevelsType)
    }

    //==================================== V 层接口实现 ===========================================
    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        LogUtils.info("列表 item 点击事件")
    }

    override fun onLoadMyArmsLevelsDataSuccess(list: BaseListBean<MyArmsLevelsBean>, total: Int) {
        mResultListener?.onResponse(list)
        EventBusManager.postEvent(MyArmsLevelsEvent(mComradeArmsLevelsType, mPageType, total.toString()))
    }

    override fun onAuthorFail() {
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            return
        }
        LoginModuleNavigator.startLoginActivity(activity)
    }

    override fun onLoadError(page: Int) {
        mResultListener?.onFailure(-1, "")
    }

    override fun showData(data: BaseListBean<MyArmsLevelsBean>) {

    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast("$msg")
    }

}