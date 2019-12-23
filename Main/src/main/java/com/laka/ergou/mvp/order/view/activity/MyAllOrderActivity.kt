package com.laka.ergou.mvp.order.view.activity

import android.support.v4.view.PagerAdapter
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.ergou.R
import com.laka.ergou.mvp.base.view.activity.BasePagerListMvpActivity
import com.laka.ergou.mvp.order.constant.MyOrderConstant
import com.laka.ergou.mvp.order.model.event.OrderEvent
import com.laka.ergou.mvp.order.view.adapter.MyOrderClassifyPagerAdapter
import com.laka.ergou.mvp.order.view.fragment.OrderPagerFragment
import kotlinx.android.synthetic.main.activity_base_pager_list.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:我的订单（app订单/微信订单）
 */
class MyAllOrderActivity : BasePagerListMvpActivity<String>() {

    private var mFragList: ArrayList<BaseLazyLoadFragment> = arrayListOf(OrderPagerFragment.getInstance(MyOrderConstant.APP_TYPE), OrderPagerFragment.getInstance(MyOrderConstant.WECHAT_TYPE))
    //订单分类数量（3：订单结算，12：订单付款， 13：订单失效, 99 :  全部）
    private var mOrderTypeCount = 3

    override fun showData(data: String) {

    }

    override fun showErrorMsg(msg: String?) {
    }

    override fun createPresenter(): IBasePresenter<*>? {
        return null
    }

    override fun getPagerAdapter(): PagerAdapter {
        return MyOrderClassifyPagerAdapter(supportFragmentManager, mFragList)
    }

    override fun initIntent() {
    }

    override fun initViews() {
        super.initViews()
        title_bar.setTitle(getString(R.string.my_order))
        setLeftTabText(getString(R.string.app_order))
        setRightTabText(getString(R.string.wechat_order))
    }

    override fun initData() {
    }

    override fun initEvent() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onOrderEvent(event: OrderEvent) {
        when (event.type) {
            MyOrderConstant.APP_TYPE -> {
                setLeftTabText("APP订单（${event.data}）")
            }
            MyOrderConstant.WECHAT_TYPE -> {
                setRightTabText("微信订单（${event.data}）")
            }
        }
    }
}