package com.laka.ergou.mvp.freedamission.view.activity

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
import com.laka.ergou.mvp.freedamission.constant.FreeAdmissionContant
import com.laka.ergou.mvp.freedamission.constract.FreeAdmissionConstract
import com.laka.ergou.mvp.freedamission.presenter.FreeAdmissionPresenter
import com.laka.ergou.mvp.freedamission.view.adapter.FreeAdmissionAdapter
import com.laka.ergou.mvp.main.RouterNavigator
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeNavigatorConstant
import com.laka.ergou.mvp.shop.ShopDetailModuleNavigator
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import kotlinx.android.synthetic.main.activity_shopping_special.*
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.set

/**
 * @Author:summer
 * @Date:2019/6/5
 * @Description:0元购
 */
class FreeAdmissionActivity : BaseMvpActivity<BaseListBean<ProductWithCoupon>>(), OnRequestListener<OnResultListener>, FreeAdmissionConstract.IFreeAdmissionView {

    private lateinit var mPresenter: FreeAdmissionPresenter
    private var mBannerImageUrl: String = ""
    private var mTitle: String = "0元购"
    private lateinit var mListItemDecoration: SpacesListDecoration
    private lateinit var mTitleView: TitleBarView<*>
    private lateinit var mIvBanner: ImageView
    private lateinit var mRvList: FrogRefreshRecyclerView
    private lateinit var mSkeleton: ViewSkeletonScreen
    private var mOnResultListener: OnResultListener? = null
    private var mDataList = ArrayList<ProductWithCoupon>()

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = FreeAdmissionPresenter()
        return mPresenter
    }

    override fun setContentView(): Int {
        return R.layout.activity_free_admission
    }

    override fun initIntent() {
        mBannerImageUrl = intent.getStringExtra(FreeAdmissionContant.KEY_FREE_ADMISSION_BANNER_URL)
        val title = intent.getStringExtra(FreeAdmissionContant.KEY_TITLE_FREE_ADMISSION)
        if (!TextUtils.isEmpty(title)) {
            mTitle = title
        }
    }

    override fun initViews() {
        mTitleView = findViewById(R.id.title_bar)
//        mIvBanner = findViewById(R.id.iv_banner)
        mRvList = findViewById(R.id.rv_list)
        mTitleView.setTitle(mTitle)
                .setLeftIcon(R.drawable.seletor_nav_btn_back)
                .setTitleTextColor(R.color.black)
                .setTitleTextSize(16)
                .setRightText("规则说明")
                .setOnRightClickListener {
                    val params = HashMap<String, String>()
                    params[HomeConstant.TITLE] = "0元购规则"
                    params[HomeNavigatorConstant.ROUTER_VALUE] = HomeApiConstant.URL_AERO_BUY
                    RouterNavigator.handleAppInternalNavigator(this, RouterNavigator.bannerRouterReflectMap[5].toString(), params)
                }
        mListItemDecoration = SpacesListDecoration(ScreenUtils.dp2px(15f), 0, ScreenUtils.dp2px(15f), ScreenUtils.dp2px(10f))
        mRvList.onItemClickListener = object : RefreshRecycleView.OnItemClickListener<ProductWithCoupon> {
            override fun onItemClick(data: ProductWithCoupon?, position: Int) {
                data?.let {
                    ShopDetailModuleNavigator.startShopDetailActivity(this@FreeAdmissionActivity, "${data.productId}")
                }
            }

            override fun onChildClick(id: Int, data: ProductWithCoupon?, position: Int) {
            }
        }
        mRvList.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
        mRvList.addItemDecoration(mListItemDecoration)
        mRvList.setOnRequestListener(this)
        mRvList.enableRefresh(false)
        var adapter = FreeAdmissionAdapter(R.layout.item_free_admission_product_list, mDataList)
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
        mPresenter.onLoadFreeAdmissionProductList(page)
        return ""
    }

    override fun initEvent() {

    }

    override fun onLoadFreeAdmissionProductListSuccess(list: BaseListBean<ProductWithCoupon>, imgPath: String, page: Int) {
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