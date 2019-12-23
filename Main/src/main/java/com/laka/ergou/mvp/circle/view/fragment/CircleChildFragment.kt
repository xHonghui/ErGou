package com.laka.ergou.mvp.circle.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.laka.androidlib.base.adapter.SimpleFragmentPagerAdapter
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.net.utils.parse.GsonUtil
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.common.ext.onClick
import com.laka.ergou.mvp.circle.constant.CircleConstant
import com.laka.ergou.mvp.circle.constract.ICircleConstract
import com.laka.ergou.mvp.circle.model.bean.CircleCategory
import com.laka.ergou.mvp.circle.presenter.CirclePresenter
import com.laka.ergou.mvp.commission.model.bean.CommissionNewBean
import com.laka.ergou.mvp.main.view.fragment.HomeFragment
import kotlinx.android.synthetic.main.fragment_child_circle.*
import kotlinx.android.synthetic.main.fragment_child_circle.layout_no_data
import kotlinx.android.synthetic.main.fragment_child_circle.layout_no_network
import kotlinx.android.synthetic.main.fragment_circle.*
import kotlinx.android.synthetic.main.layout_no_data.*
import kotlinx.android.synthetic.main.layout_no_network.*
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView

class CircleChildFragment : HomeFragment(), ICircleConstract.IBaseCircleView {
    private lateinit var mMagicIndicator: MagicIndicator
    private var mTitle = mutableListOf<String>()
    private lateinit var mAdapter: SimpleFragmentPagerAdapter
    private var fragmentList = ArrayList<Fragment>()
    private var mCategoryId = ""//分类ID
    private var data = ""//上次请求数据
    private var mParentPostion = 0//父类的位置
    private var mChildPostion = 0//当前fragment在父类的位置

    companion object {
        fun newInstance(position: Int, category: CircleCategory): CircleChildFragment {
            val bundle = Bundle()
            bundle.putString(CircleConstant.CATEGORY_ID, category.category_id.toString())
            bundle.putInt(CircleConstant.CATEGORY_POSITION, position)
            var fragment = CircleChildFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun showData(data: CommissionNewBean) {

    }

    override fun showErrorMsg(msg: String?) {

    }

    private lateinit var mCirclePresenter: CirclePresenter
    override fun createPresenter(): IBasePresenter<*> {
        mCirclePresenter = CirclePresenter()
        return mCirclePresenter
    }

    override fun setContentView(): Int {
        return R.layout.fragment_child_circle
    }
    //检查分类
    fun checkData(position: Int) {
        mChildPostion = position//设置子fragment的位置
        if (::mCirclePresenter.isInitialized) {
            mCirclePresenter.getCategoryList(mCategoryId)
        }
    }

    override fun initArgumentsData(arguments: Bundle?) {
        arguments?.let {
            mCategoryId = it.getString(CircleConstant.CATEGORY_ID)
            mParentPostion = it.getInt(CircleConstant.CATEGORY_POSITION)
        }
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        mMagicIndicator = magicindicator
    }

    private fun initMagicIndicator1(magicIndicator: MagicIndicator) {
        val commonNavigator = CommonNavigator(context)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return if (mTitle == null) 0 else mTitle.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val clipPagerTitleView = ClipPagerTitleView(context)
                clipPagerTitleView.text = mTitle.get(index)
                clipPagerTitleView.textSize = ScreenUtils.sp2px(14.0f).toFloat()
                clipPagerTitleView.textColor = context.resources.getColor(R.color.color_black_656565)
                clipPagerTitleView.clipColor = context.resources.getColor(R.color.color_main)
                clipPagerTitleView.setOnClickListener { mViewPager.currentItem = index }
                return clipPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {

                return null
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }

    override fun initDataLazy() {
        mCirclePresenter.getCategoryList(mCategoryId)
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
                    fragmentList.add(CircleListFragment.newInstance(mParentPostion, index, list[index].category_id))
                }
                if (!::mAdapter.isInitialized) {
                    mAdapter = SimpleFragmentPagerAdapter(childFragmentManager, fragmentList, mTitle)
                    mViewPager?.adapter = mAdapter
                } else {
                    mAdapter.setFragments(fragmentList, mTitle)
                    mViewPager?.setCurrentItem(0, false)
                }
                initMagicIndicator1(mMagicIndicator)
            } else {
                layout_no_data?.visibility = View.VISIBLE
                layout_no_data.setBackgroundColor(context.resources.getColor(R.color.white))
            }
        } else {
            //数据一样，刷新列表
            if (fragmentList.size > 0 && mChildPostion < fragmentList.size) {
                (fragmentList[mChildPostion] as CircleListFragment).refreshData()
            }
        }
    }

    override fun onNetWorkFail(erroeCode: Int, error: String) {
        dismissLoading()
        layout_no_network?.setBackgroundColor(context.resources.getColor(R.color.white))
        layout_no_network?.visibility = View.VISIBLE
        layout_no_data?.visibility = View.GONE
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onEvent(event: ChildRefreshEvent?) {
//        event?.let {
//            mCategoryListId = it.position
//            mCirclePresenter.getCategoryList(mCategoryId)
//        }
//    }

    override fun initEvent() {
        btn_no_net_work?.onClick {
            showLoading()
            mCirclePresenter.getCategoryList(mCategoryId)
        }
        iv_no_data?.onClick {
            showLoading()
            mCirclePresenter.getCategoryList(mCategoryId)
        }
    }

}