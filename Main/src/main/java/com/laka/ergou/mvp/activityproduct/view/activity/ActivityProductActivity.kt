package com.laka.ergou.mvp.activityproduct.view.activity

import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.features.login.OnRequestListener
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.StringUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.androidlib.widget.refresh.OnResultListener
import com.laka.androidlib.widget.refresh.RefreshRecycleView
import com.laka.androidlib.widget.titlebar.TitleBarView
import com.laka.ergou.R
import com.laka.ergou.common.widget.SpacesListDecoration
import com.laka.ergou.common.widget.refresh.FrogRefreshRecyclerView
import com.laka.ergou.mvp.activityproduct.constant.ActivityProductConstant
import com.laka.ergou.mvp.activityproduct.constract.ActivityProductConstract
import com.laka.ergou.mvp.activityproduct.presenter.ActivityProductPresenter
import com.laka.ergou.mvp.activityproduct.view.adapter.ActivityProductAdapter
import com.laka.ergou.mvp.shop.ShopDetailModuleNavigator
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import kotlinx.android.synthetic.main.activity_free_admission.*

/**
 * @Author:summer
 * @Date:2019/8/5
 * @Description:活动产品专题
 */
class ActivityProductActivity : BaseMvpActivity<BaseListBean<ProductWithCoupon>>(), OnRequestListener<OnResultListener>, ActivityProductConstract.IActivityProductView {

    private lateinit var mPresenter: ActivityProductPresenter
    private var mBannerImageUrl: String = ""
    private var mActivityId: String = ""
    private var mTitle: String = "活动产品专题"
    private lateinit var mListItemDecoration: SpacesListDecoration
    private lateinit var mTitleView: TitleBarView<*>
    private lateinit var mIvBanner: ImageView
    private lateinit var mRvList: FrogRefreshRecyclerView
    private lateinit var mSkeleton: ViewSkeletonScreen
    private var mOnResultListener: OnResultListener? = null
    private var mDataList = ArrayList<ProductWithCoupon>()

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = ActivityProductPresenter()
        return mPresenter
    }

    override fun setContentView(): Int {
        return R.layout.activity_free_admission
    }

    override fun initIntent() {
        mBannerImageUrl = intent.getStringExtra(ActivityProductConstant.KEY_BANNER_URL)
        mActivityId = intent.getStringExtra(ActivityProductConstant.KEY_ACTIVITY_ID)
        val title = intent.getStringExtra(ActivityProductConstant.KEY_TITLE)
        if (!TextUtils.isEmpty(title)) {
            mTitle = title
        }
    }

    override fun initViews() {
        mTitleView = findViewById(R.id.title_bar)
        mRvList = findViewById(R.id.rv_list)
        mTitleView.setTitle(mTitle)
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)
        mListItemDecoration = SpacesListDecoration(ScreenUtils.dp2px(15f), 0, ScreenUtils.dp2px(15f), ScreenUtils.dp2px(10f))
        mRvList.onItemClickListener = object : RefreshRecycleView.OnItemClickListener<ProductWithCoupon> {
            override fun onItemClick(data: ProductWithCoupon?, position: Int) {
                data?.let {
                    ShopDetailModuleNavigator.startShopDetailActivity(this@ActivityProductActivity, "${data.productId}")
                }
            }

            override fun onChildClick(id: Int, data: ProductWithCoupon?, position: Int) {
            }
        }
        mRvList.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
        mRvList.addItemDecoration(mListItemDecoration)
        mRvList.setOnRequestListener(this)
        mRvList.enableRefresh(false)
        var adapter = ActivityProductAdapter(R.layout.item_free_admission_product_list, mDataList)
        val headerView = layoutInflater.inflate(R.layout.item_shopping_special_header, mRvList as ViewGroup, false)
        mIvBanner = headerView.findViewById(R.id.iv_banner)
        adapter.addHeaderView(headerView)
        mListItemDecoration.isAddHeader = true
        mRvList.adapter = adapter

        mRvList.refresh(false)
        //为了解决这一个问题，所以添加多一个 view_place，用来提供给 Skeleton 绑定
        mSkeleton = Skeleton.bind(cl_root_view)
                .load(R.layout.skeleton_activity_list_special)
                .duration(1000)
                .color(R.color.shimmer_color)
                .angle(30)
                .show()
    }

    override fun initData() {

    }

    override fun onRequest(page: Int, resultListener: OnResultListener?): String {
        mOnResultListener = resultListener
        mPresenter.onLoadActivityProductList(page, mActivityId)
        return ""
    }

    override fun initEvent() {

    }

    override fun onLoadActivityProductListSuccess(list: BaseListBean<ProductWithCoupon>, imgPath: String, page: Int) {
        if (page == 1) { //避免加载第一页太快，ui闪动
            Handler().postDelayed({
                if (::mSkeleton.isInitialized) {
                    mSkeleton.hide()
                }
                mOnResultListener?.onResponse(list)
            }, 150)
        } else {
            mOnResultListener?.onResponse(list)
        }
        //加载banner图片
        if (!StringUtils.isEmpty(imgPath)) {
            mBannerImageUrl = imgPath
        }
        GlideUtil.loadImage(this, mBannerImageUrl, R.drawable.default_img, mIvBanner)
    }

    /**首次加载数据失败时，用来显示加载错误的页面*/
    private fun showErrorView() {
        mOnResultListener?.onFailure(-1, "")
        if (::mSkeleton.isInitialized) {
            mSkeleton.hide()
        }
    }

    override fun onLoadFail(msg: String, page: Int) {
        if (page == 1) {
            Handler().postDelayed({
                showErrorView()
            }, 100)
        } else {
            showErrorView()
        }
    }

    override fun showData(data: BaseListBean<ProductWithCoupon>) {

    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast("$msg")
    }
}