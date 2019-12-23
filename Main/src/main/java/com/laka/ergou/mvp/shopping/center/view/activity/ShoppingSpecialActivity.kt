package com.laka.ergou.mvp.shopping.center.view.activity

import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.ViewSkeletonScreen
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.features.login.OnRequestListener
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.ResourceUtils
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
import com.laka.ergou.mvp.shop.ShopDetailModuleNavigator
import com.laka.ergou.common.widget.SpacesStaggeredDecoration
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import com.laka.ergou.mvp.shopping.center.contract.IShoppingSpecialContract
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.shopping.center.presenter.ShoppingSpecialPresenter
import com.laka.ergou.mvp.shopping.center.view.adapter.ShoppingSpecialGridAdapter
import com.laka.ergou.mvp.shopping.center.view.adapter.ShoppingSpecialListAdapter
import kotlinx.android.synthetic.main.activity_shopping_special.*

/**
 * @Author:summer
 * @Date:2019/4/29
 * @Description:商品专题页面
 */
class ShoppingSpecialActivity : BaseMvpActivity<ArrayList<ProductWithCoupon>>(), OnRequestListener<OnResultListener>, IShoppingSpecialContract.IShoppingSpecialView {

    private var mTitle: String = "商品专题"
    //10: 9块9包邮, 20: 全网爆款, 30: 新人专享, 40: 高佣精品, 50: 二购严选, 60: 超值优惠
    private var mCid: String = ""
    //顶部大图
    private var mBigImageUrl: String = ""
    private var mResultListener: OnResultListener? = null
    private var mDataList: ArrayList<ProductWithCoupon> = ArrayList()
    private lateinit var mListItemDecoration: SpacesListDecoration
    private lateinit var mGridItemDecoration: SpacesStaggeredDecoration
    private lateinit var mGridAdapter: ShoppingSpecialGridAdapter
    private lateinit var mListAdapter: ShoppingSpecialListAdapter
    private lateinit var mSkeleton: ViewSkeletonScreen
    private lateinit var mPresenter: IShoppingSpecialContract.IShoppingSpecialPresenter
    private lateinit var mTitleView: TitleBarView<*>
    private lateinit var mIvBanner: ImageView
    private lateinit var mRvList: FrogRefreshRecyclerView

    override fun createPresenter(): IBasePresenter<IShoppingSpecialContract.IShoppingSpecialView> {
        mPresenter = ShoppingSpecialPresenter()
        return mPresenter
    }

    override fun setContentView(): Int {
        return R.layout.activity_shopping_special
    }

    override fun initIntent() {
        mCid = intent.getStringExtra(ShoppingCenterConstant.SPECIAL_ID)
        mBigImageUrl = intent.getStringExtra(ShoppingCenterConstant.SPECIAL_BIG_IMAGE)
        val title = intent.getStringExtra(ShoppingCenterConstant.SPECIAL_TITLE)
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

        mListItemDecoration = SpacesListDecoration(ScreenUtils.dp2px(15f), 0, ScreenUtils.dp2px(15f), ScreenUtils.dp2px(10f))
        mGridItemDecoration = SpacesStaggeredDecoration(object : SpacesStaggeredDecoration.ItemDecorationCallBack {
            override fun getItemType(position: Int?): Int? {
                //return mGridAdapter.getItemViewType(position!!)
                //非多样性列表直接返回一个固定值即可
                return ShoppingCenterConstant.LIST_UI_TYPE_GRID
            }
        }, true, ResourceUtils.getDimen(R.dimen.dp_10), ResourceUtils.getDimen(R.dimen.dp_6))
        mRvList.onItemClickListener = object : RefreshRecycleView.OnItemClickListener<ProductWithCoupon> {
            override fun onItemClick(data: ProductWithCoupon?, position: Int) {
                data?.let {
                    ShopDetailModuleNavigator.startShopDetailActivity(this@ShoppingSpecialActivity, "${data.productId}")
                }
            }

            override fun onChildClick(id: Int, data: ProductWithCoupon?, position: Int) {
            }
        }
        mRvList.enableRefresh(false)
        mRvList.setOnRequestListener(this)
        when (mCid) {
            ShoppingCenterConstant.SPECIAL_NEW_USER_VIP,
            ShoppingCenterConstant.SPECIAL_ERGOU_STRICT_CHOISE,
            ShoppingCenterConstant.SPECIAL_NINE_YUAN_NINE_FREE_SHIPPING -> {
                handleGridUi()
            }
            ShoppingCenterConstant.SPECIAL_HIGH_DISCOUNT,
            ShoppingCenterConstant.SPECIAL_SELL_WELL_STYLE,
            ShoppingCenterConstant.SPECIAL_HIGH_COMMISSON_BOUTIQUE -> {
                handleListUi()
            }
            else -> {
                handleGridUi()
            }
        }
        mRvList.refresh(false)
    }

    /**
     * grid list UI 处理
     * */
    private fun handleListUi() {
        mListAdapter = ShoppingSpecialListAdapter(R.layout.item_product_list, mDataList)
        val headerView = layoutInflater.inflate(R.layout.item_shopping_special_header, mRvList as ViewGroup, false)
        mIvBanner = headerView.findViewById(R.id.iv_banner)
        mListAdapter.addHeaderView(headerView)
        mListItemDecoration.isAddHeader = true
        mRvList.setLayoutManager(LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
        mRvList.addItemDecoration(mListItemDecoration)
        mRvList.adapter = mListAdapter

        //使用 skeleton 替换，bind(root_view)时，Skeleton 与 RecyclerView 会产生滑动冲突
        //为了解决这一个问题，所以添加多一个 view_place，用来提供给 Skeleton 绑定
        mSkeleton = Skeleton.bind(view_place)
                .load(R.layout.skeleton_activity_list_special)
                .duration(1000)
                .color(R.color.shimmer_color)
                .angle(30)
                .show()
    }

    /**
     * list 列表ui处理
     * */
    private fun handleGridUi() {
        val headerView = layoutInflater.inflate(R.layout.item_shopping_special_header, mRvList as ViewGroup, false)
        mIvBanner = headerView.findViewById(R.id.iv_banner)
        mGridAdapter = ShoppingSpecialGridAdapter(R.layout.item_product_grid, mDataList)
        mGridAdapter.addHeaderView(headerView)
        mGridItemDecoration.isAddHeader(true)
        mRvList.setLayoutManager(StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
        mRvList.addItemDecoration(mGridItemDecoration)
        mRvList.adapter = mGridAdapter

        //使用 skeleton 替换
        mSkeleton = Skeleton.bind(view_place)
                .load(R.layout.skeleton_activity_grid_special)
                .duration(1000)
                .color(R.color.shimmer_color)
                .angle(30)
                .show()
    }

    override fun initData() {

    }

    override fun initEvent() {
    }

    override fun onInternetChange(isLostInternet: Boolean) {
        if (!isLostInternet) {
            if (mDataList.isEmpty()) {
                //页面为加载有数据，重新获取网络时，重新加载数据
                mRvList.refresh(false)
            }
        } else {
            if (mDataList.isEmpty()) {
                showErrorView()
            }
        }
    }

    override fun onRequest(page: Int, resultListener: OnResultListener?): String {
        LogUtils.info("specialActivity--------加载数据：page=$page")
        mResultListener = resultListener
        mPresenter.onLoadProductSpecialData(mCid, page)
        return ""
    }

    /**首次加载数据失败时，用来显示加载错误的页面*/
    private fun showErrorView() {
        if (::mSkeleton.isInitialized) {
            mSkeleton.hide()
        }
        mResultListener?.onFailure(-1, "")
    }

    //=====================================  view 接口实现  ========================================

    override fun onLoadProductDataSuccess(list: BaseListBean<ProductWithCoupon>, imgPath: String, page: Int) {
        //隐藏骨架框，使用skeleton需要注意，在显示skeleton 阶段，不能通过kotlin 的插件直接使用 view 的id 操作view，
        //会出现空指针的问题，因为替换成了 skeleton 后，整体view 改变了，所以通过id访问不到相应的view
        //解决方法：可以在使用skeleton前使用findViewById 获取view
        mDataList.addAll(list.list)
        if (page == 1) { //避免加载第一页太快，ui闪动
            Handler().postDelayed({
                if (::mSkeleton.isInitialized) {
                    mSkeleton.hide()
                }
                mResultListener?.onResponse(list)
            }, 150)
        } else {
            mResultListener?.onResponse(list)
        }

        if (!StringUtils.isEmpty(imgPath)) {
            mBigImageUrl = imgPath
        }
        GlideUtil.loadImage(this, mBigImageUrl, R.drawable.default_img, mIvBanner)
    }

    override fun onLoadFail(msg: String, page: Int) {
        if (page == 1) {
            Handler().postDelayed({
                showErrorView()
            }, 150)
        } else {
            showErrorView()
        }
    }

    override fun showData(data: ArrayList<ProductWithCoupon>) {

    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast("$msg")
    }

}