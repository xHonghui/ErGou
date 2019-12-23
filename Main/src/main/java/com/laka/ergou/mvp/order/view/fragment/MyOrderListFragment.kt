package com.laka.ergou.mvp.order.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.androidlib.widget.refresh.OnResultListener
import com.laka.ergou.R
import com.laka.ergou.common.widget.SpacesListDecoration
import com.laka.ergou.common.widget.helper.MZBannerHelper
import com.laka.ergou.mvp.advertbanner.adapter.AdvertBannerViewAdapter
import com.laka.ergou.mvp.advertbanner.constant.AdvertBannerConstant
import com.laka.ergou.mvp.advertbanner.model.bean.AdvertBannerBean
import com.laka.ergou.mvp.advertbanner.presenter.AdvertBannerPresenter
import com.laka.ergou.mvp.base.view.fragment.BaseListFragment
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.main.contract.IAdvertBannerConstract
import com.laka.ergou.mvp.order.constant.MyOrderConstant
import com.laka.ergou.mvp.order.constract.IMyOrderConstract
import com.laka.ergou.mvp.order.model.bean.OrderDataBean
import com.laka.ergou.mvp.order.model.bean.OrderListBean
import com.laka.ergou.mvp.order.model.event.OrderEvent
import com.laka.ergou.mvp.order.presenter.MyOrderPresenter
import com.laka.ergou.mvp.order.view.adapter.MyOrderListAdapter
import com.laka.ergou.mvp.shop.ShopDetailModuleNavigator

/**
 * @Author:summer
 * @Date:2019/1/19
 * @Description:订单列表
 */
class MyOrderListFragment : BaseListFragment(), IMyOrderConstract.IMyOrderView {

    private lateinit var mPresenter: IMyOrderConstract.IMyOrderPresenter
    private lateinit var mBannerPresenter: IAdvertBannerConstract.IAdvertBannerPresenter
    private lateinit var mMsgList: BaseListBean<MultiItemEntity>
    private lateinit var mBannerList: ArrayList<AdvertBannerBean>
    private var mResultListener: OnResultListener? = null
    private var mDataList = ArrayList<OrderDataBean>()
    //订单状态,3：订单结算，12：订单付款， 13：订单失效, 99 :  全部
    private var mStatus = -1
    //订单来源（1：app  2:微信）
    private var mSourceType = 1
    //间隔
    private lateinit var mListItemDecoration: SpacesListDecoration

    companion object {
        fun newInstance(orderStatus: Int, sourceType: Int): BaseListFragment {
            val bundle = Bundle()
            bundle.putInt(MyOrderConstant.MY_ORDER_STATUS, orderStatus)
            bundle.putInt(MyOrderConstant.SOURCE_TYPE, sourceType)
            var fragment = MyOrderListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mBannerPresenter = AdvertBannerPresenter()
        mBannerPresenter.setView(advertBannerView)
        mPresenter = MyOrderPresenter()
        return mPresenter
    }

    override fun initArgumentsData(arguments: Bundle?) {
        arguments?.let {
            mStatus = arguments.getInt(MyOrderConstant.MY_ORDER_STATUS, -1)
            mSourceType = arguments.getInt(MyOrderConstant.SOURCE_TYPE, 1)
        }
    }

    override fun isLazyLoad(): Boolean {
        return true
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        super.initView(rootView, savedInstanceState)
        mListItemDecoration = SpacesListDecoration(0, 0, 0, ScreenUtils.dp2px(10f))
        mRecyclerView?.noDataView?.findViewById<ImageView>(R.id.iv_no_data)?.setImageResource(R.drawable.ic_no_order_data)
        mRecyclerView?.noDataView?.findViewById<TextView>(R.id.tv_no_data)?.text = ResourceUtils.getString(R.string.no_order_data_hint)
        mRecyclerView?.addItemDecoration(mListItemDecoration)
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
                .setIsList(false)
                .setRootView(mLlRootView!!)
                .setBannerView(spaceBannerView)
                .setData(list)
                .build()
        mSpaceBannerHelper.setVisiable(View.GONE)
    }

    override fun initAdapter(): BaseQuickAdapter<*, *> {
        return MyOrderListAdapter(R.layout.item_my_order_list, mDataList)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        var order = adapter.getItem(position) as? OrderDataBean
        order?.let {
            ShopDetailModuleNavigator.startShopDetailActivity(activity, order.item_id)
        }
    }

    /**加载数据*/
    override fun onRequestListData(page: Int, resultListener: OnResultListener?) {
        mResultListener = resultListener
        if (page == 1) {
            mBannerPresenter.onLoadAdvertBannerData(AdvertBannerConstant.TYPE_BANNER_CLASSID_ORDER_LIST)
        } else {
            mPresenter.onLoadMyOrderData(mStatus, mSourceType, page)
        }
    }

    //================================== V 层接口实现 =================================================

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showCenterToast("$msg")
    }

    private val advertBannerView = object : AdvertBannerViewAdapter() {
        override fun onLoadAdvertBannerDataSuccess(response: ArrayList<AdvertBannerBean>) {
            mBannerList = response
            mPresenter.onLoadMyOrderData(mStatus, mSourceType, 1)
        }

        override fun onLoadAdvertBannerDataFail(msg: String) {
            mPresenter.onLoadMyOrderData(mStatus, mSourceType, 1)
        }
    }

    override fun onLoadMyOrderDataSuccess(result: BaseListBean<OrderDataBean>, total: Int) {
        mResultListener?.onResponse(result)
        if (mStatus == MyOrderConstant.ORDER_STATUS_ALL) {
            EventBusManager.postEvent(OrderEvent(mSourceType, "$total"))
        }
        //数据为空时
        dataEmpty(result)
    }

    /**
     * 数据为空
     * */
    private fun dataEmpty(result: BaseListBean<OrderDataBean>) {
        if ((mAdapter?.data == null || mAdapter?.data?.isEmpty()!!) && result.isEmpty) {
            handleBannerData(true)
        } else {
            handleBannerData(false)
        }
    }

    override fun onAuthorFail() {
        if (activity == null || activity.isFinishing || activity.isDestroyed) {
            return
        }
        LoginModuleNavigator.startLoginActivity(activity)
    }

    override fun onLoadError(page: Int) {
        mResultListener?.onFailure(-1, "")
        handleBannerData(mRecyclerView?.adapter?.data?.isEmpty()!!)
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

    override fun showData(data: OrderListBean) {

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