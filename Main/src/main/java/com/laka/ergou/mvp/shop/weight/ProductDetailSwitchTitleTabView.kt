package com.laka.ergou.mvp.shop.weight

import android.content.Context
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.common.widget.refresh.FrogRefreshRecyclerView
import com.laka.ergou.mvp.shop.constant.ShopDetailConstant

/**
 * @Author:summer
 * @Date:2019/5/5
 * @Description:产品详情页面 --> 自定义title
 */
class ProductDetailSwitchTitleTabView : FrameLayout, View.OnClickListener {

    private lateinit var mRootView: ViewGroup
    private lateinit var mClTabView: ConstraintLayout
    private lateinit var mFlBack: FrameLayout
    private lateinit var mFlBack1: FrameLayout
    private lateinit var mLlBaby: LinearLayout
    private lateinit var mLlDetail: LinearLayout
    private lateinit var mLlRecommend: LinearLayout
    private lateinit var mTvBaby: TextView
    private lateinit var mTvDetail: TextView
    private lateinit var mTvRecommend: TextView
    private lateinit var mViewBaby: View
    private lateinit var mViewDetail: View
    private lateinit var mViewRecommend: View
    private lateinit var mIvBack: ImageView
    private lateinit var mRecyclerView: FrogRefreshRecyclerView
    //lisener
    private lateinit var mBackClickListener: ((view: View) -> Unit)
    private lateinit var mTabClickListener: ((view: View, type: Status) -> Unit)
    //透明过度的高度
    private var mScrollTransparentHeight = ScreenUtils.getScreenWidth() / 2
    private var mScrollHeight = 0.0f
    private var mScrollPercent = 0.0f

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun init(attrs: AttributeSet?) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.title_bar_product_detail, this, true) as ViewGroup
        mClTabView = mRootView.findViewById(R.id.cl_tab_view)
        mFlBack = mRootView.findViewById(R.id.fl_back)
        mFlBack1 = mRootView.findViewById(R.id.fl_back1)
        mLlBaby = mRootView.findViewById(R.id.ll_baby)
        mLlDetail = mRootView.findViewById(R.id.ll_detail)
        mLlRecommend = mRootView.findViewById(R.id.ll_recommend)
        mTvBaby = mRootView.findViewById(R.id.tv_baby)
        mTvDetail = mRootView.findViewById(R.id.tv_detail)
        mTvRecommend = mRootView.findViewById(R.id.tv_recommend)
        mViewBaby = mRootView.findViewById(R.id.view_baby)
        mViewDetail = mRootView.findViewById(R.id.view_detail)
        mViewRecommend = mRootView.findViewById(R.id.view_recommend)
        mIvBack = mRootView.findViewById(R.id.iv_back)
        mLlBaby.isSelected = true
        initEvent()
    }

    private fun initEvent() {
        mFlBack.setOnClickListener(this)
        mFlBack1.setOnClickListener(this)
        mLlBaby.setOnClickListener(this)
        mLlDetail.setOnClickListener(this)
        mLlRecommend.setOnClickListener(this)
        mLlBaby.tag = Status.PRODUCT_BABY
        mLlDetail.tag = Status.PRODUCT_DETAIL
        mLlRecommend.tag = Status.PRODUCT_RECOMMEND
    }

    fun bindRecyclerView(recyclerView: FrogRefreshRecyclerView) {
        this.mRecyclerView = recyclerView
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.fl_back1,
            R.id.fl_back -> {
                if (::mBackClickListener.isInitialized) {
                    mBackClickListener.invoke(view)
                }
            }
            R.id.ll_baby,
            R.id.ll_detail,
            R.id.ll_recommend -> {
                if (mScrollPercent > 0.5f) { //透明度大于0.5才可点击
                    if (::mTabClickListener.isInitialized) {
                        mTabClickListener.invoke(view, mCurrentStatus)
                    }
                    handleTabItemClick(view?.id)
                }
            }
        }
    }

    /**
     * 处理tab item 点击，列表滑动到相应位置
     * */
    private fun handleTabItemClick(id: Int) {
        when (id) {
            R.id.ll_baby -> {
                //switchTab(Status.PRODUCT_BABY)
                mRecyclerView.scrollToPositionByType(ShopDetailConstant.SHOP_DETAIL_BANNER)
            }
            R.id.ll_detail -> {
                //switchTab(Status.PRODUCT_DETAIL)
                mRecyclerView.scrollToPositionByType(ShopDetailConstant.SHOP_DETAIL_IMAGE_DETAIL)
            }
            R.id.ll_recommend -> {
                //switchTab(Status.PRODUCT_RECOMMEND)
                mRecyclerView.scrollToPositionByType(ShopDetailConstant.SHOP_DETAIL_RECOMMEND_ITEM)
            }
        }
    }

    /**
     * 列表滑动
     * */
    fun onScroll(recyclerView: RecyclerView, dx: Float, dy: Float, viewType: Int, alphaListener: ((Float) -> Unit)) {
        when (viewType) {
            ShopDetailConstant.SHOP_DETAIL_BASIC,
            ShopDetailConstant.SHOP_DETAIL_STORE_DETAIL,
            ShopDetailConstant.SHOP_DETAIL_ADVERT_BANNER,
            ShopDetailConstant.SHOP_DETAIL_BANNER -> { //宝贝
                switchTab(Status.PRODUCT_BABY)
            }
            ShopDetailConstant.SHOP_DETAIL_MORE,
            ShopDetailConstant.SHOP_DETAIL_IMAGE_DETAIL -> { //详情
                switchTab(Status.PRODUCT_DETAIL)
            }
            ShopDetailConstant.SHOP_DETAIL_RECOMMEND_TITLE,
            ShopDetailConstant.SHOP_DETAIL_RECOMMEND_ITEM -> { //推荐
                switchTab(Status.PRODUCT_RECOMMEND)
            }
        }
        handleTtitleBarAlpha(dy, alphaListener)
        LogUtils.info("ProductDetailSwitchTitleTabView-------:dx=$dx,,,dy=$dy,,,viewType=$viewType")
    }

    /**
     * 处理 title bar 根据列表滑动设置透明度
     * */
    private fun handleTtitleBarAlpha(dy: Float, alphaListener: ((Float) -> Unit)) {
        mScrollHeight += dy
        // 纯粹累加 dy，最后得出的总和可能为负数，这样是不合理的，所以当 mScrllHeight<0 时，将其置为 0
        mScrollHeight = if (mScrollHeight < 0) 0.0f else mScrollHeight
        mScrollPercent = mScrollHeight / mScrollTransparentHeight
        mClTabView.alpha = mScrollPercent
        mFlBack1.alpha = 1 - mScrollPercent
        alphaListener?.invoke(mScrollPercent)
    }

    fun setOnBackClickListener(listener: ((view: View) -> Unit)) {
        this.mBackClickListener = listener
    }

    fun setOnTabClickListener(listener: ((view: View, type: Status) -> Unit)) {
        this.mTabClickListener = listener
    }

    /**
     * 切换tab状态
     * */
    private fun switchTab(status: Status) {
        if (mCurrentStatus == status) return
        mCurrentStatus = status
        when (status) {
            Status.PRODUCT_BABY -> {
                mTvBaby.isSelected = true
                mViewBaby.visibility = View.VISIBLE
                mTvDetail.isSelected = false
                mViewDetail.visibility = View.GONE
                mTvRecommend.isSelected = false
                mViewRecommend.visibility = View.GONE
            }
            Status.PRODUCT_DETAIL -> {
                mTvBaby.isSelected = false
                mViewBaby.visibility = View.GONE
                mTvDetail.isSelected = true
                mViewDetail.visibility = View.VISIBLE
                mTvRecommend.isSelected = false
                mViewRecommend.visibility = View.GONE
            }
            Status.PRODUCT_RECOMMEND -> {
                mTvBaby.isSelected = false
                mViewBaby.visibility = View.GONE
                mTvDetail.isSelected = false
                mViewDetail.visibility = View.GONE
                mTvRecommend.isSelected = true
                mViewRecommend.visibility = View.VISIBLE
            }
        }
    }

    fun setStatusBarHeight(height: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val layoutParams = mFlBack1.layoutParams as? ConstraintLayout.LayoutParams
            val topMargin = layoutParams?.topMargin ?: 0
            layoutParams?.topMargin = topMargin + height
            mClTabView.setPadding(mClTabView.paddingLeft, mClTabView.paddingTop + height, mClTabView.paddingRight, mClTabView.paddingBottom)
        }
    }

    private var mCurrentStatus = Status.PRODUCT_BABY

    enum class Status {
        PRODUCT_BABY,
        PRODUCT_DETAIL,
        PRODUCT_RECOMMEND
    }

}