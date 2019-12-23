package com.laka.ergou.mvp.shopping.center.view.fragment

import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.laka.androidlib.eventbus.Event
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.features.login.OnRequestListener
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ListUtils
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.androidlib.widget.refresh.OnResultListener
import com.laka.androidlib.widget.refresh.RefreshRecycleView
import com.laka.androidlib.widget.refresh.decoration.DividerItemDecoration
import com.laka.ergou.R
import com.laka.ergou.common.ext.onClick
import com.laka.ergou.common.widget.SpacesStaggeredDecoration
import com.laka.ergou.common.widget.refresh.FrogRefreshRecyclerView
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import com.laka.ergou.mvp.shop.ShopDetailModuleNavigator
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import com.laka.ergou.mvp.shopping.center.contract.IShoppingListContract
import com.laka.ergou.mvp.shopping.center.model.bean.CategoryBean
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.shopping.center.presenter.ShoppingListPresenter
import com.laka.ergou.mvp.shopping.center.view.adapter.ProductListAdapter
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Author:Rayman
 * @Date:2018/12/21
 * @Description:主页商品分类子Fragment
 */
class ShoppingListFragment : BaseProductListFragment<ProductWithCoupon>(), IShoppingListContract.IShoppingView,
        OnRequestListener<OnResultListener> {

    /**
     * description:UI配置
     **/
    private lateinit var mRvList: FrogRefreshRecyclerView
    //    private lateinit var listSelectView: ListSelectView
//    private lateinit var rlLayout: RelativeLayout
    private var mListItemDecoration: DividerItemDecoration? = null
    private var mStaggeredGridItemDecoration: SpacesStaggeredDecoration? = null

    /**
     * description:数据层
     **/
    private lateinit var mAdapter: ProductListAdapter
    //    private lateinit var listMenuAdapter: ListMenuAdapter
    private lateinit var mResultListener: OnResultListener
    private var mCategoryId = ""
    private var mCategoryName = ""
    var mOrderField = "synthesize"
    var mOrderSort = "desc"
    private var isNetWorkError = false
    //“滑动到顶部”按钮控制参数
    private var mTotalDy = 0F
    private val mListScrollControlDis = ScreenUtils.dp2px(360F)
    private lateinit var mIvToTop: ImageView

    private var isSendTop = false//发送到首页，协调者布局展开
    /**
     * description:P层数据设置
     **/
    private lateinit var mShoppingListListPresenter: IShoppingListContract.IShoppingListPresenter

    /**
     * description:创建主页Fragment单例
     **/
    companion object {
        fun newInstance(category: CategoryBean): ShoppingListFragment {
            val bundle = Bundle()
            bundle.putString(ShoppingCenterConstant.LIST_TYPE_ID, category.categoryId)
            bundle.putString(ShoppingCenterConstant.LIST_TYPE_NAME, category.title)
            var fragment = ShoppingListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun setContentView(): Int {
        return R.layout.fragment_product_list
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        LogUtils.info("home--------onCreateView-----$mCategoryName")
    }

    override fun createPresenter(): IBasePresenter<*> {
        mShoppingListListPresenter = ShoppingListPresenter()
        return mShoppingListListPresenter
    }

    override fun initArgumentsData(bundle: Bundle?) {
        bundle?.let {
            mCategoryName = bundle.getString(ShoppingCenterConstant.LIST_TYPE_NAME)
            mCategoryId = bundle.getString(ShoppingCenterConstant.LIST_TYPE_ID)
        }
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        mRvList = findViewById(R.id.rv_product_list)
        mIvToTop = findViewById(R.id.iv_to_top)
        if (mStaggeredGridItemDecoration == null) {
            mStaggeredGridItemDecoration = SpacesStaggeredDecoration(object : SpacesStaggeredDecoration.ItemDecorationCallBack {
                override fun getItemType(position: Int?): Int? {
                    return mAdapter.getItemViewType(position!!)
                }

            }, true, ResourceUtils.getDimen(R.dimen.dp_10), ResourceUtils.getDimen(R.dimen.dp_6))
        }
        mRvList.enableTopPadding(4f)
        mRvList.addItemDecoration(mStaggeredGridItemDecoration)
                .setEnableMultiClick(false)
                .setLayoutManager(StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
                .setOnRequestListener(this)
                .setOnResultListener(object : OnResultListener {
                    override fun onFailure(errorCode: Int, errorMsg: String?) {

                    }

                    override fun onResponse(response: BaseListBean<*>?) {

                    }
                })
        mRvList.enableClickLoadMore(true)
        mRvList.enableRefresh(false)
        mRvList.setLoadingView(LayoutInflater.from(context).inflate(R.layout.view_list_loading, null))
        mAdapter = ProductListAdapter(ArrayList())
        mRvList?.adapter = mAdapter
        mAdapter.switchUIMode(ShoppingCenterConstant.LIST_UI_TYPE_GRID)
    }

    override fun initDataLazy() {
        if (!isNetWorkError) {
            mRvList.refresh(false)//不做动画的刷新
            mRvList.showLoadingView()
        }
        mRvList.onItemClickListener = object : RefreshRecycleView.OnItemClickListener<ProductWithCoupon> {
            override fun onItemClick(data: ProductWithCoupon?, position: Int) {
                data?.let {
                    ShopDetailModuleNavigator.startShopDetailActivity(activity, "${data.productId}")
                }
            }

            override fun onChildClick(id: Int, data: ProductWithCoupon?, position: Int) {
            }
        }
    }

    override fun initEvent() {
        mRvList.onScrollListener = RefreshRecycleView.OnScrollListener { recyclerView, _, dy, _, _ ->
            mTotalDy += dy
            // 纯粹累加 dy，最后得出的总和可能为负数，这样是不合理的，所以当 mTotalDy<0 时，将其置为 0
            mTotalDy = if (mTotalDy < 0) 0f else mTotalDy
            if (mTotalDy >= mListScrollControlDis) {
                mIvToTop.visibility = View.VISIBLE
            } else {
                mIvToTop.visibility = View.GONE
            }
            if (mTotalDy <= 0f) {
                if (isSendTop) {
                    isSendTop = false
                    EventBusManager.postEvent(Event(HomeEventConstant.EVENT_APPBARLAYOUT_STATE, ""))
                }
            }
            EventBusManager.postEvent(Event(HomeEventConstant.EVENT_RECYCLER_VIEW_SCROLL, recyclerView.computeVerticalScrollOffset()))
        }
        mIvToTop.onClick {
            isSendTop = true
            mRvList.scrollToTop()
        }
    }

    override fun onRequest(page: Int, resultListener: OnResultListener?): String {
        mResultListener = resultListener!!
        mShoppingListListPresenter.getGoodsData(mCategoryId, page, mOrderField, mOrderSort)
        LogUtils.info("shoppingListFragment---------onRequest")
        return ""
    }

    fun refreshList() {
        showLoading()
        mRvList?.refresh(false)
    }

    /**
     * 外层 ShoppingHomeFragment 触发的刷新
     * */
    fun onRefresh() {
        if (::mRvList.isInitialized) {
            mRvList.refresh(false)
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    override fun onEvent(event: Event?) {
        super.onEvent(event)
        when (event?.name) {
        //以前版本切换首页商品列表为单列竖向列表用到
            ShoppingCenterConstant.EVENT_LIST_UI_TYPE_NORMAL -> {
                mRvList.addItemDecoration(mListItemDecoration)
                mRvList.removeItemDecoration(mStaggeredGridItemDecoration)
                mAdapter?.switchUIMode(ShoppingCenterConstant.LIST_UI_TYPE_COMMON)
            }
            ShoppingCenterConstant.EVENT_LIST_UI_TYPE_GRID -> {
                if (mStaggeredGridItemDecoration == null) {
                    mStaggeredGridItemDecoration = SpacesStaggeredDecoration(object : SpacesStaggeredDecoration.ItemDecorationCallBack {
                        override fun getItemType(position: Int?): Int? {
                            return mAdapter.getItemViewType(position!!)
                        }
                    }, ResourceUtils.getDimen(R.dimen.dp_4))
                }
                mRvList.addItemDecoration(mStaggeredGridItemDecoration)
                mRvList.removeItemDecoration(mListItemDecoration)
                mAdapter?.switchUIMode(ShoppingCenterConstant.LIST_UI_TYPE_GRID)
            }
            HomeEventConstant.EVENT_ON_NETWORK_ERROR -> {
                // 网络失败下，隐藏loadingView，展示errorView。
                // 为什么要先隐藏loading呢？因为网络Resume的时候需要显示loading嘛。
                // 而且loading是在errorView上层，这里就需要先隐藏loadingView，再展示errorView。
                // 需要判断当前列表是否有数据，假若有数据就不显示errorView
                if (ListUtils.isEmpty(mRvList.adapter.data)) {
                    dismissLoading()
                    mRvList.showErrorView()
                }
                isNetWorkError = true
            }
            HomeEventConstant.EVENT_ON_NETWORK_RESUME -> {
                // 当数据为空的时候，重新刷新列表
                // 重新刷新列表，并展示loading框。
                if (ListUtils.isEmpty(mRvList.adapter.data)) {
                    showLoading()
                    isNetWorkError = false
                    mRvList?.refresh(false)
                }
            }
        }
    }


    //====================================== view 层接口 ===========================================

    /**
     * 列表加载动画在头部，普通页面加载动画是一个dialog
     * */
    override fun showLoading() {
        mRvList?.showLoadingView()
    }

    override fun dismissLoading() {
        mRvList?.hideLoadingView()
    }

    /**
     * 请求错误都会回调，RxCustomSubscriber
     * */
    override fun showErrorMsg(errorMsg: String) {
        ToastHelper.showToast(errorMsg)
    }

    override fun showData(data: ProductWithCoupon) {

    }

    //v2.0.0
    override fun onLoadGoodsDataSuccess(data: BaseListBean<ProductWithCoupon>) {
        mRvList.hideLoadingView()
        mResultListener?.onResponse(data)
    }

    //加载数据失败，如果是首页数据就失败，则回调 mResultListener 监听器的失败方法，
    //这样可以恢复列表的显示，而不会出现空白界面的现象
    override fun onLoadGoodsDataFail(msg: String, page: Int) {
        mResultListener?.onFailure(-1, "")
    }

}