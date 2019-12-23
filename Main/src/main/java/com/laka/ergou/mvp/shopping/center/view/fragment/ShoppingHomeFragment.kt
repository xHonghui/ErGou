package com.laka.ergou.mvp.shopping.center.view.fragment

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import com.laka.androidlib.base.adapter.SimpleFragmentPagerAdapter
import com.laka.androidlib.eventbus.Event
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.listener.AppBarStateChangeListener
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ApplicationUtils
import com.laka.androidlib.util.ListUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.page.MZBannerView
import com.laka.ergou.R
import com.laka.ergou.common.util.update.UpdateManager
import com.laka.ergou.common.widget.refresh.ErGouHomeRefreshLayout
import com.laka.ergou.mvp.customer.CustomerModuleNavigator
import com.laka.ergou.mvp.main.HomeModuleNavigator
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import com.laka.ergou.mvp.main.view.fragment.HomeFragment
import com.laka.ergou.mvp.shopping.ShoppingModuleNavigator
import com.laka.ergou.mvp.shopping.center.constant.ShoppingApiConstant
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import com.laka.ergou.mvp.shopping.center.contract.IShoppingHomeContract
import com.laka.ergou.mvp.shopping.center.helper.*
import com.laka.ergou.mvp.shopping.center.model.bean.*
import com.laka.ergou.mvp.shopping.center.presenter.ShoppingHomePresenter
import com.laka.ergou.mvp.shopping.center.weight.GradualChangeBgView
import kotlinx.android.synthetic.main.fragment_shopping.*
import net.lucode.hackware.magicindicator.MagicIndicator

/**
 * @Author:Rayman
 * @Date:2018/12/21
 * @Description:商品主页HOME,Fragment
 */
class ShoppingHomeFragment : HomeFragment(), IShoppingHomeContract.IShoppingHomeView, View.OnClickListener {


    /**
     * description:Fragment中不建议使用Kotlin-ID注入的方式调用控件 .
     * 因为调用的时机必须要在onViewCreate里面，但是我们Base封装的都在onCreateView的时候执行
     **/
    private lateinit var mClRootView: ConstraintLayout
    private lateinit var mBarLayout: AppBarLayout
    private lateinit var mClSearch: ConstraintLayout
    private lateinit var mClTitleBar: ConstraintLayout
    private lateinit var mTvKeyWord: TextView
    private lateinit var mIvSwitchMode: ImageView
    private lateinit var mRefreshLayout: ErGouHomeRefreshLayout

    //private lateinit var mTabLayout: SlidingTabLayout
    private lateinit var mClVideoCourseRootView: ConstraintLayout
    private lateinit var mVpContainer: ViewPager
    private lateinit var mBannerView: MZBannerView
    private lateinit var mAppBarLayout: AppBarLayout
    private lateinit var mTitleBarBg: View
    private lateinit var mBgView: GradualChangeBgView
    private lateinit var mCvPromotionRootView: CardView
    private lateinit var mClTopicRootView: ConstraintLayout
    private lateinit var magicIndicator: MagicIndicator
    private lateinit var magicIndicatorCollapsed: MagicIndicator
    //分隔线
    private lateinit var mLineView0: View
    private lateinit var mLineView1: View
    private lateinit var mLineView2: View
    private lateinit var mLineView3: View
    private lateinit var mLineView4: View

    /**
     * description:页面Fragment信息配置
     **/
    private var mCategoryList = ArrayList<CategoryBean>()
    private var fragmentList = ArrayList<Fragment>()
    private var mCurrentFragment: ShoppingListFragment? = null
    private var uiMode = ShoppingCenterConstant.LIST_UI_TYPE_COMMON

    /**
     * description:页面数据设置
     **/
    private var keyWord = String()
    private lateinit var mAdapter: SimpleFragmentPagerAdapter
    private lateinit var titleList: Array<String>
    private lateinit var mShoppingHomePresenter: ShoppingHomePresenter

    /**
     * item helper
     * */
    private lateinit var mHomeBannerHelper: HomeBannerHelper
    private lateinit var mScrollerTransparentHelper: ScrollerTransparentHelper
    private lateinit var mTopicItemHelper: TopicItemHelper
    private lateinit var mPromotionHelper: PromotionHelper
    private lateinit var mSelectTabHelper: SelectTabHelper
    private lateinit var mHomeTabLayoutHelper: HomeTabLayoutHelper
    private lateinit var mActivitsPopupHelper: ActivitsPopupHelper

    private lateinit var appBarStateChangeListener: AppBarStateChangeListener
//    private lateinit var simplePagerTitleView: ScaleTransitionPagerTitleView

    /**
     * home page data
     * */
    private lateinit var mHomePageData: HomePageResponse
//    private var commonNavigator: CommonNavigator? = null
//    private var commonNavigatorCollapsed: CommonNavigator? = null

    override fun setContentView(): Int {
        return R.layout.fragment_shopping
    }

    override fun initArgumentsData(arguments: Bundle?) {

    }

    override fun createPresenter(): IBasePresenter<*> {
        mShoppingHomePresenter = ShoppingHomePresenter()
        return mShoppingHomePresenter
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        mRefreshLayout = refresh_layout
        mClRootView = cl_root_view
        mClTitleBar = cl_title
        mBannerView = banner_view
        mBarLayout = app_bar_layout
        mClSearch = cl_shopping_keyword
        mIvSwitchMode = iv_shopping_switch_type
        mTvKeyWord = tv_shopping_search_key_word
        //mTabLayout = tab_shopping_type
        mVpContainer = vp_shopping_list
        magicIndicator = magic_indicator
        magicIndicatorCollapsed = magic_indicator_collapsed
        mBgView = view_bg
        //通过include 引入的view，需要findViewById
        mClVideoCourseRootView = findViewById(R.id.cl_video_course_root)
        mClTopicRootView = findViewById(R.id.cl_topic_root_view)
        mCvPromotionRootView = findViewById(R.id.cv_promotion_root_view)
        //分割线
        mLineView1 = findViewById(R.id.view_line1)
        mLineView2 = findViewById(R.id.view_line2)
        mLineView3 = findViewById(R.id.view_line3)
        mLineView4 = findViewById(R.id.view_line4)
        mLineView0 = findViewById(R.id.view_line0)
        mAppBarLayout = findViewById(R.id.app_bar_layout)
        mTitleBarBg = findViewById(R.id.title_bar_bg)
        mTitleBarBg.alpha = 0f

        //是否显示新手教程
        val isShow = SPHelper.getBoolean(ShoppingApiConstant.IS_SHOW_VIDEO_COURSE, true)
        LogUtils.info("是否显示video教程$isShow")
        if (isShow) {
            mLineView1.visibility = View.VISIBLE
            mClVideoCourseRootView.visibility = View.VISIBLE
        } else {
            mLineView1.visibility = View.GONE
            mClVideoCourseRootView.visibility = View.GONE
        }
        //todo 协调者布局滑动
        mScrollerTransparentHelper = ScrollerTransparentHelper(context)
        mScrollerTransparentHelper.init(mTitleBarBg, mBgView)
        //todo 专题item
        mTopicItemHelper = TopicItemHelper(context)
        mTopicItemHelper.bindTopicView(mClTopicRootView)
        //todo 活动专区
        mPromotionHelper = PromotionHelper(context)
        mPromotionHelper.bindView(mCvPromotionRootView)
        mSelectTabHelper = SelectTabHelper(context)
        mSelectTabHelper.bindView(ll_select, ll_tab)
        mSelectTabHelper.setMenuStateListener(object : SelectTabHelper.MenuStateListener {
            override fun menuOpen(position: Int, type: String, orderSort: String) {
                LogUtils.debug("ShopHomeFragment---->$type ---- $orderSort")
                if (appBarStateChangeListener.getmCurrentState() != AppBarStateChangeListener.State.COLLAPSED) {
                    mBarLayout.setExpanded(false)
                }
                if (position != -1) {
                    mCurrentFragment?.mOrderField = type
                    mCurrentFragment?.mOrderSort = orderSort
                    mCurrentFragment?.refreshList()
                }
            }
        })
        initRefreshLayout()
        mHomeTabLayoutHelper = HomeTabLayoutHelper()
        appBarStateChangeListener = object : AppBarStateChangeListener() {

            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                if (state == State.COLLAPSED) {
                    ll_tab?.setBackgroundColor(context.resources.getColor(R.color.color_655ADD))
                    magicIndicator.visibility = View.GONE
                    magicIndicatorCollapsed.visibility = View.VISIBLE
                } else {
                    ll_tab?.setBackgroundColor(context.resources.getColor(R.color.color_common_bg))
                    magicIndicator.visibility = View.VISIBLE
                    magicIndicatorCollapsed.visibility = View.GONE
                }
            }
        }
        mAppBarLayout.addOnOffsetChangedListener(appBarStateChangeListener)
        HomePageStatusBarHelper.handleHomePageStatusBarOffset(activity, mRefreshLayout, mClTitleBar, mBgView)
    }

    private fun initRefreshLayout() {
        // new refresh layout
        //绑定需要做动画的 view
        mRefreshLayout.bindAnimatorView(mBgView, mClTitleBar)
        mRefreshLayout.setOnRefreshListener {
            mShoppingHomePresenter.refreshHomePageData()
        }
        mRefreshLayout.setOnRefreshScrollListener { _, scrollY, transparency ->
            //滑动刷新，背景墙和titleBar 做渐变动画
            //背景墙和头部bar透明度变化的速率要比刷新控件透明度变化的速率快
            val alpha = if (1 - (transparency * 2f) < 0) 0f else 1 - (transparency * 2f)
            if (mRefreshLayout.isNeedRefresh()) { //释放刷新，mBgView恢复回来
            } else {
                mClTitleBar.alpha = alpha
            }
            LogUtils.info("scrollY-----$scrollY")
            mBgView.deformForRefreshLayout(scrollY)
        }
    }

    override fun onResume() {
        super.onResume()
        if (::mHomeBannerHelper.isInitialized) {
            mHomeBannerHelper.onStart()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::mHomeBannerHelper.isInitialized) {
            mHomeBannerHelper.onPause()
        }
    }


    /*是否显示未读消息白点*/
    fun hasUnReadMsgCount(visible: Int) {
        iv_spot?.visibility = visible
    }

    override fun initDataLazy() {
        mShoppingHomePresenter.getHomePageDataFirst()
        mShoppingHomePresenter.getAdvert(context)
        //mShoppingHomePresenter.getH5Url()
        checkUpdate()
    }

    private fun checkUpdate() {
        ApplicationUtils.setIsShowUpdateDialog(true)
        val updateManager = UpdateManager(activity)
        updateManager.checkUpdate(false)
    }

    override fun initEvent() {
        setClickView<TextView>(R.id.cl_shopping_keyword)
        setClickView<ImageView>(R.id.iv_shopping_switch_type)
        setClickView<View>(R.id.layout_no_network)
        setClickView<View>(R.id.cl_message)
        setClickView<View>(R.id.video_course_view)
        setClickView<View>(R.id.iv_video_course_close)
        setClickView<View>(R.id.iv_customer)
        //todo 监听 AppBarLayout，解决滑动冲突问题
        initAppbarLayoutEvent()
        //todo 监听控件inflate时机
        initViewTreeObserver()
    }

    private fun initViewTreeObserver() {
        mClRootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mBgView.setQuadFiexdHeight(mClTitleBar.measuredHeight)
                mClRootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    /**
     * 协调者滑动监听
     * */
    private fun initAppbarLayoutEvent() {
        mAppBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
            //根据协调者布局的滑动情况，设置 refreshLayout 是否可刷新
            mRefreshLayout.isEnabled = verticalOffset >= 0
            mRefreshLayout.enableRefresh(verticalOffset >= 0)
            LogUtils.info("verticalOffset----=$verticalOffset----${mRefreshLayout.isEnableRefresh()}")
            //titleBar 与 bgView 的透明动画 helper 类
            mScrollerTransparentHelper.setScrollerData(verticalOffset)
            //bgView 贝塞尔形变
            mBgView.setScrollerData(verticalOffset)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cl_shopping_keyword -> HomeModuleNavigator.startSearchActivity(mActivity)
            R.id.iv_shopping_switch_type -> {
                uiMode = if (uiMode == ShoppingCenterConstant.LIST_UI_TYPE_COMMON) ShoppingCenterConstant.LIST_UI_TYPE_GRID
                else ShoppingCenterConstant.LIST_UI_TYPE_COMMON

                // 更新当前切换列表Icon的UI
                iv_shopping_switch_type.setImageResource(if (uiMode == ShoppingCenterConstant.LIST_UI_TYPE_COMMON) R.drawable.btn_list_normal
                else R.drawable.btn_list_grid)

                // 通知其他Fragment切换列表
                var modeEventName = if (uiMode == ShoppingCenterConstant.LIST_UI_TYPE_COMMON) ShoppingCenterConstant.EVENT_LIST_UI_TYPE_NORMAL
                else ShoppingCenterConstant.EVENT_LIST_UI_TYPE_GRID
                EventBusManager.postStickyEvent(modeEventName)
            }
            R.id.layout_no_network -> {
                onReload()
            }
            R.id.cl_message -> {
                ShoppingModuleNavigator.startMessageActivity(activity) //我的消息
            }
            R.id.video_course_view -> { //视频教程
                ShoppingModuleNavigator.startVideoCourseActivity(activity, getString(R.string.video_course_txt), HomeApiConstant.URL_USER_TUTORIAL)
            }
            R.id.iv_video_course_close -> { //视频教程入口关闭
                mLineView1.visibility = View.GONE
                mClVideoCourseRootView.visibility = View.GONE
                SPHelper.putBoolean(ShoppingApiConstant.IS_SHOW_VIDEO_COURSE, false)
            }
            R.id.iv_customer -> { //客服
                CustomerModuleNavigator.startCustomerActivity(activity, "客服", HomeApiConstant.URL_CUSTOMER)
            }
        }
    }


    override fun onEvent(event: Event?) {
        super.onEvent(event)
        when (event?.name) {
            HomeEventConstant.EVENT_ON_NETWORK_ERROR -> {
                if (ListUtils.isEmpty(mCategoryList)) {
                    showNetWorkErrorView()
                }
            }
            HomeEventConstant.EVENT_ON_NETWORK_RESUME -> {
                // 判断ParentType列表是否为空，为空则重新获取数据
                onReload()
            }
            HomeEventConstant.EVENT_RECYCLER_VIEW_SCROLL -> { //recyclerView 滑动监听

            }
            HomeEventConstant.EVENT_APPBARLAYOUT_STATE -> {
                if (appBarStateChangeListener.getmCurrentState() != AppBarStateChangeListener.State.EXPANDED) {
                    mBarLayout.setExpanded(true)
                }
            }

        }
    }

    private fun onReload() {
        if (!::mHomePageData.isInitialized) {
            mShoppingHomePresenter.refreshHomePageData()
        }
    }

    override fun showData(data: ArrayList<ProductParentType>) {

    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast(msg)
    }

    override fun showNetWorkErrorView() {
        // 获取数据失败，显示错误的View
        layout_no_network.visibility = View.VISIBLE
        cl_container.visibility = View.GONE
    }

    private fun initViewPager(data: ArrayList<CategoryBean>) {
        fragmentList.clear()
        mCategoryList.clear()
        mCategoryList.addAll(data)
        // 初始化数据
        titleList = Array(data.size, { String() })
        for ((index, category) in data.withIndex()) {
            fragmentList.add(ShoppingListFragment.newInstance(category))
            titleList[index] = category.title
        }
        mHomeTabLayoutHelper
                .bindCommonNavigator(context, titleList.toMutableList(), magicIndicator, mVpContainer,
                        R.color.color_black_656565, R.color.color_black_303030, R.color.color_main)
                .bindCommonNavigator(context, titleList.toMutableList(), magicIndicatorCollapsed, mVpContainer,
                        R.color.white, R.color.white, R.color.white)

        if (!::mAdapter.isInitialized) {
            mAdapter = SimpleFragmentPagerAdapter(fragmentManager, fragmentList, titleList.asList())
            mVpContainer?.adapter = mAdapter
            mVpContainer.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    if (position >= 0 && position < fragmentList.size) {
                        mCurrentFragment = fragmentList[position] as? ShoppingListFragment
                        mSelectTabHelper.setCurrentFragment(mCurrentFragment)
                    }
                }

            })
        } else {
            mAdapter.setFragments(fragmentList, titleList.asList())
            mVpContainer.setCurrentItem(0, false)
        }
        if (fragmentList.isNotEmpty()) {
            mCurrentFragment = fragmentList[0] as? ShoppingListFragment
            mSelectTabHelper.setCurrentFragment(mCurrentFragment)
        }
    }


    //=======================================  View 层接口实现  ====================================
    override fun onGetHomePageDataSuccess(response: HomePageResponse) {
        if (ApplicationUtils.isVaildActivity(activity)) {
            layout_no_network?.visibility = View.GONE
            cl_container?.visibility = View.VISIBLE
            mHomePageData = response
            mRefreshLayout?.finishRefresh(true)
            handleBanner(response)
            handleTopic(response)
            handlePromotion(response)
            handleCategory(response)

            val params = mAppBarLayout.layoutParams as? CoordinatorLayout.LayoutParams
            val behavior = params?.behavior as? AppBarLayout.Behavior
            behavior?.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return true
                }
            })
        }
    }

    private fun handleCategory(response: HomePageResponse) {
//        测试代码
//        val data = response.categoryList
//        val customData = ArrayList<CategoryBean>()
//        for (i in 0 until (Math.random() * 10).toInt()) {
//            customData.add(data[i])
//        }
//        initViewPager(customData)

        val data = response.categoryList
        if (data.size != mCategoryList.size) {
            initViewPager(data)
        } else {
            for (i in 0 until data.size) {
                if (data[i] != mCategoryList[i]) {
                    initViewPager(data)
                    break
                }
            }
        }
    }

    private fun handlePromotion(response: HomePageResponse) {
        if (::mPromotionHelper.isInitialized) {
            if (response.promotionList.isEmpty()) {

                mLineView3.visibility = View.GONE
                mCvPromotionRootView.visibility = View.GONE
                return

                // 测试
//                mLineView3.visibility = View.VISIBLE
//                mCvPromotionRootView.visibility = View.VISIBLE

            } else {
                mLineView3.visibility = View.VISIBLE
                mCvPromotionRootView.visibility = View.VISIBLE
                mPromotionHelper.setData(response.promotionList)
            }
        }
    }

    private fun handleTopic(response: HomePageResponse) {
        if (::mTopicItemHelper.isInitialized) {
            if (response.topicList.isEmpty()) {
                mLineView2.visibility = View.GONE
                mClTopicRootView.visibility = View.GONE
                return
            } else {
                mLineView2.visibility = View.VISIBLE
                mClTopicRootView.visibility = View.VISIBLE
                mTopicItemHelper.setData(response.topicList)
            }
        }
    }

    private fun handleBanner(response: HomePageResponse) {
        val banner = response.bannerList

        //测试代码
        //val bannerList = ArrayList<BannerBean>()
        //val random = (Math.random() * 10 % banner.size).toInt()
        //LogUtils.info("random----------$random")
        //for (i in 0 until random) {
        //  bannerList.add(banner[i])
        //}

        if (banner.isEmpty()) {
            mLineView0.visibility = View.GONE
            mBannerView.visibility = View.GONE
            if (::mHomeBannerHelper.isInitialized) {
                mHomeBannerHelper.onPause()
            }
            return
        } else {
            mLineView0.visibility = View.VISIBLE
            mBannerView.visibility = View.VISIBLE
        }
        if (::mHomeBannerHelper.isInitialized) {
            mHomeBannerHelper.updateBannerData(banner)
        } else {
            mHomeBannerHelper = HomeBannerHelper(context, mBannerView, banner)
            mHomeBannerHelper.bindBgView(mBgView)
        }
        mHomeBannerHelper.onStart()
    }

    override fun onGetHomePageDataFinish() {
        mRefreshLayout?.finishRefresh(true)
    }

    /**
     * 刷新 currentFragment
     * */
    override fun onRefreshFragmentData() {
        mCurrentFragment?.onRefresh()
    }

    override fun onGetH5UrlSuccess(response: HomeUrlBean) {
//        response?.let {
//            it?.promotion.let {
//                EventBusManager.postEvent(PromotionEvent(it.expire, it.img_url, it.show, it.url))
//            }
//        }
    }

    //获取弹窗数据成功
    override fun onLoadPopupDataSuccess(popupBean: HomePopupBean?) {
        if (!::mActivitsPopupHelper.isInitialized) {
            mActivitsPopupHelper = ActivitsPopupHelper()
        }
        mActivitsPopupHelper.downloadShopDetailImage(activity, popupBean)
    }


}