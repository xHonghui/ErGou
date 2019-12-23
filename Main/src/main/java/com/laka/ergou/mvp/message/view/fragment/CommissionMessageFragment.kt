package com.laka.ergou.mvp.message.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.androidlib.widget.refresh.OnResultListener
import com.laka.ergou.R
import com.laka.ergou.mvp.base.view.fragment.BaseListFragment
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.message.MessageModuleNavigator
import com.laka.ergou.mvp.message.MessageModuleRouter
import com.laka.ergou.mvp.message.constant.MessageConstant
import com.laka.ergou.mvp.message.constract.MessageConstract
import com.laka.ergou.common.widget.helper.MZBannerHelper
import com.laka.ergou.mvp.advertbanner.adapter.AdvertBannerViewAdapter
import com.laka.ergou.mvp.advertbanner.constant.AdvertBannerConstant
import com.laka.ergou.mvp.advertbanner.model.bean.AdvertBannerBean
import com.laka.ergou.mvp.advertbanner.presenter.AdvertBannerPresenter
import com.laka.ergou.mvp.main.contract.IAdvertBannerConstract
import com.laka.ergou.mvp.message.model.bean.MessageResponse
import com.laka.ergou.mvp.message.model.bean.Msg
import com.laka.ergou.mvp.message.presenter.MessagePresenter
import com.laka.ergou.mvp.message.view.adapter.CommissionMessageAdapter
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:补贴消息
 */
class CommissionMessageFragment : BaseListFragment(), MessageConstract.IMessageView {

    private var mResultListener: OnResultListener? = null
    private var mDataList: ArrayList<Msg> = ArrayList()
    private lateinit var mPresenter: MessageConstract.IMessagePresenter
    private lateinit var mBannerPresenter: IAdvertBannerConstract.IAdvertBannerPresenter
    private lateinit var mBannerList: ArrayList<AdvertBannerBean>

    override fun initAdapter(): BaseQuickAdapter<*, *> {
        return CommissionMessageAdapter(R.layout.item_commission_message, mDataList)
    }

    override fun initArgumentsData(arguments: Bundle?) {

    }

    override fun createPresenter(): IBasePresenter<*> {
        mBannerPresenter = AdvertBannerPresenter()
        mBannerPresenter.setView(advertBannerView)
        mPresenter = MessagePresenter()
        return mPresenter
    }

    override fun isLazyLoad(): Boolean {
        return true
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        super.initView(rootView, savedInstanceState)
        mRecyclerView?.noDataView?.findViewById<TextView>(R.id.tv_no_data)?.text = ResourceUtils.getString(R.string.no_commission_msg_hint)
    }

    /**
     * FrogRefreshRecyclerView 需要设置Adapter后才能通过 addHeaderView 添加头布局
     * */
    override fun initSecondView(rootView: View?, savedInstanceState: Bundle?) {
        super.initSecondView(rootView, savedInstanceState)
        initHeadBanner()
    }

    private lateinit var mBannerHelper: MZBannerHelper
    private lateinit var mSpaceBannerHelper: MZBannerHelper

    /**
     * 添加广告banner
     * */
    private fun initHeadBanner() {
        val list = ArrayList<AdvertBannerBean>()
        val bannerView = LayoutInflater.from(activity).inflate(R.layout.view_advert_banner, mRecyclerView, false)
        mBannerHelper = MZBannerHelper.Builder(activity)
                .setIsList(true)
                .setRootView(mRecyclerView!!)
                .setBannerView(bannerView)
                .setData(list)
                .build()
        mBannerHelper.setVisiable(View.GONE)

        //无数据，则显示备用banner
        val spaceBannerView = LayoutInflater.from(activity).inflate(R.layout.view_advert_banner, mLlRootView, false)
        mSpaceBannerHelper = MZBannerHelper.Builder(activity)
                .setIsList(false)//banner添加到列表还是非列表
                .setRootView(mLlRootView!!)
                .setBannerView(spaceBannerView)
                .setData(list)
                .build()
        mSpaceBannerHelper.setVisiable(View.GONE)
    }

    override fun onRequestListData(page: Int, resultListener: OnResultListener?) {
        mResultListener = resultListener
        if (page == 1) {
            mBannerPresenter.onLoadAdvertBannerData(AdvertBannerConstant.TYPE_BANNER_CLASSID_MESSAGE_COMMISSION)
        } else {
            mPresenter.onLoadMessage(page, MessageConstant.MESSAGE_COMMISSION_TYPE)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        //TODO 补贴消息点击，配置消息路由
        val msgItem: Msg? = adapter?.getItem(position) as? Msg
        msgItem?.let {
            MessageModuleRouter.startCommissionMessageRouter(activity, msgItem)
        }
    }

    //========================================= V 层接口实现 ======================================
    override fun showData(data: MessageResponse) {

    }

    override fun showErrorMsg(msg: String?) {

    }

    private val advertBannerView = object : AdvertBannerViewAdapter() {
        override fun onLoadAdvertBannerDataSuccess(response: ArrayList<AdvertBannerBean>) {
            mBannerList = response
            mPresenter.onLoadMessage(1, MessageConstant.MESSAGE_COMMISSION_TYPE)
        }

        override fun onLoadAdvertBannerDataFail(msg: String) {
            mPresenter.onLoadMessage(1, MessageConstant.MESSAGE_COMMISSION_TYPE)
        }
    }

    override fun onLoadMessageSuccess(result: BaseListBean<MultiItemEntity>) {
        UserUtils.clearCommissionUnReadMsg()
        EventBusManager.postEvent(UserEvent(UserConstant.READ_COMMISSION_MSG_EVENT))
        mResultListener?.onResponse(result)
        //数据为空时
        dataEmpty(result)
    }

    /**
     * 数据为空
     * */
    private fun dataEmpty(result: BaseListBean<MultiItemEntity>) {
        if ((mAdapter?.data == null || mAdapter?.data?.isEmpty()!!) && result.isEmpty) {
            handleBannerData(true)
        } else {
            handleBannerData(false)
        }
    }

    /**
     * 广告banner渲染
     * */
    private fun handleBannerData(isMsgEmpty: Boolean) {
        if (::mBannerList.isInitialized) {
            mBannerHelper.notifiDataSet(mBannerList)
            mSpaceBannerHelper.notifiDataSet(mBannerList)
        }
        if (isMsgEmpty) {
            if (!::mBannerList.isInitialized || mBannerList.isEmpty()) {
                mBannerHelper.setVisiable(View.GONE)
                mSpaceBannerHelper.setVisiable(View.GONE)
            } else {
                mBannerHelper.setVisiable(View.GONE)
                mSpaceBannerHelper.setVisiable(View.VISIBLE)
            }
        } else {
            if (!::mBannerList.isInitialized || mBannerList.isEmpty()) {
                mBannerHelper.setVisiable(View.GONE)
                mSpaceBannerHelper.setVisiable(View.GONE)
            } else {
                mBannerHelper.setVisiable(View.VISIBLE)
                mSpaceBannerHelper.setVisiable(View.GONE)
            }
        }
    }

    override fun onAuthorFail() {
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            return
        }
        MessageModuleNavigator.startLoginActivity(activity)
        activity.finish()
    }

    override fun onLoadError(page: Int) {
        mResultListener?.onFailure(-1, "")
        handleBannerData(mRecyclerView?.adapter?.data?.isEmpty()!!)
    }

    override fun onStart() {
        super.onStart()
        mBannerHelper.onStart()
        mSpaceBannerHelper.onStart()
    }

    override fun onPause() {
        mBannerHelper.onPause()
        mSpaceBannerHelper.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mBannerHelper.release()
        mSpaceBannerHelper.release()
        super.onDestroy()
    }
}