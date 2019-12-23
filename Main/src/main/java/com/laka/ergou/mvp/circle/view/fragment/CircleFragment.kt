package com.laka.ergou.mvp.circle.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import com.laka.androidlib.base.adapter.SimpleFragmentPagerAdapter
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.net.utils.parse.GsonUtil
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.StatusBarUtil
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.common.ext.onClick
import com.laka.ergou.mvp.circle.weight.MyPagerTitleView
import com.laka.ergou.mvp.circle.constract.ICircleConstract
import com.laka.ergou.mvp.circle.model.bean.CircleCategory
import com.laka.ergou.mvp.circle.model.bean.ParentRefreshEvent
import com.laka.ergou.mvp.circle.presenter.CirclePresenter
import com.laka.ergou.mvp.commission.model.bean.CommissionNewBean
import com.laka.ergou.mvp.main.view.fragment.HomeFragment
import kotlinx.android.synthetic.main.fragment_circle.*
import kotlinx.android.synthetic.main.fragment_circle.layout_no_data
import kotlinx.android.synthetic.main.fragment_circle.layout_no_network
import kotlinx.android.synthetic.main.layout_no_data.*
import kotlinx.android.synthetic.main.layout_no_network.*
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class CircleFragment : HomeFragment(), ICircleConstract.IBaseCircleView {

    private var fragmentList = ArrayList<Fragment>()
    private lateinit var mCirclePresenter: CirclePresenter
    private var mTitle = mutableListOf<String>()
    private lateinit var mAdapter: SimpleFragmentPagerAdapter
    private var data = ""
    private var mChildPostion = 0//由子Fragment传来的位置
    private var mChildChildPostion = 0//由子子Fragment传来的位置
    override fun createPresenter(): IBasePresenter<*> {
        mCirclePresenter = CirclePresenter()
        return mCirclePresenter
    }

    override fun setContentView(): Int {
        return R.layout.fragment_circle
    }

    override fun initDataLazy() {
        mCirclePresenter.getCategoryList("0")
    }

    override fun showData(data: CommissionNewBean) {

    }

    override fun showErrorMsg(msg: String?) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ParentRefreshEvent?) {
        //刷新分类
        event?.let {
            //设置子fragment在viewPager的位置
            mChildPostion = it.oneParentPosition
            //设置子子fragment在viewPager的位置
            mChildChildPostion = it.twoParentPosition
            mCirclePresenter.getCategoryList("0")
        }
    }

    override fun initArgumentsData(arguments: Bundle?) {

    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
//        mNetError = rootView?.findViewById(R.id.layout_no_network)
//        mBtnReload = btn_no_net_work
        handleStatusBarOffset()
    }

    private fun handleStatusBarOffset() {
        val layoutParams = cl_tab_root.layoutParams as? LinearLayout.LayoutParams
        layoutParams?.let {
            val statusBarHeight = StatusBarUtil.getStatusBarHeight(activity)
            it.height = ScreenUtils.dp2px(44f) + statusBarHeight
        }
    }

    override fun categoryListRespone(list: List<CircleCategory>) {
        dismissLoading()
        if (layout_no_network?.visibility == View.VISIBLE) {
            layout_no_network?.visibility = View.GONE
        }
        //对新的请求数据转换成字符串
        var newData = GsonUtil.listToJson(list)
        //检查上次的数据与本次数据是否一样
        if (!newData.equals(data)) {
            //不一样重新设置
            data = newData
            if (list.size > 0) {
                //重新设置fragment
                if (layout_no_data?.visibility == View.VISIBLE) {
                    layout_no_data?.visibility = View.GONE
                }
                mTitle.clear()
                fragmentList.clear()
                for (index in list.indices) {
                    mTitle.add(list[index].name)
                    fragmentList.add(CircleChildFragment.newInstance(index, list[index]))
                }
                if (!::mAdapter.isInitialized) {
                    mAdapter = SimpleFragmentPagerAdapter(childFragmentManager, fragmentList, mTitle)
                    vpView?.adapter = mAdapter
                } else {
                    mAdapter.setFragments(fragmentList, mTitle)
                    vpView.setCurrentItem(0, false)
                }
                initMagicIndicator(magic_indicator, vpView)
            } else {
                layout_no_data?.visibility = View.VISIBLE
                layout_no_data.setBackgroundColor(context.resources.getColor(R.color.white))
            }
        } else {
            //数据一样，检查子fragment的分类
            if (fragmentList.size > 0 && mChildPostion < fragmentList.size) {
                (fragmentList[mChildPostion] as CircleChildFragment).checkData(mChildChildPostion)
            }

        }
    }

    override fun onNetWorkFail(erroeCode: Int, error: String) {
        dismissLoading()
        layout_no_network?.setBackgroundColor(context.resources.getColor(R.color.white))
        layout_no_network?.visibility = View.VISIBLE
        layout_no_data?.visibility = View.GONE
    }

    override fun initEvent() {
        btn_no_net_work?.onClick {
            showLoading()
            mCirclePresenter.getCategoryList("0")
        }
        iv_no_data?.onClick {
            showLoading()
            mCirclePresenter.getCategoryList("0")
        }
    }

    private fun initMagicIndicator(magicIndicator: MagicIndicator, viewPager: ViewPager) {
        var commonNavigator = CommonNavigator(context)
        commonNavigator.leftPadding = ScreenUtils.dp2px(30f)
        commonNavigator.rightPadding = ScreenUtils.dp2px(30f)
        commonNavigator?.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return if (mTitle == null) 0 else mTitle.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = MyPagerTitleView(context)
                simplePagerTitleView.setText(mTitle.get(index))
                simplePagerTitleView.setTextSize(16f)
                simplePagerTitleView.setNormalColor(context.resources.getColor(R.color.white))
                simplePagerTitleView.setSelectedColor(context.resources.getColor(R.color.white))
                simplePagerTitleView.isUnSelectBold = false
                simplePagerTitleView.isSelectBold = true
                simplePagerTitleView.setOnClickListener {
                    vpView.setCurrentItem(index)
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                var indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 2.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 40.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(context.resources.getColor(R.color.white))
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, viewPager)
    }
}