package com.laka.ergou.mvp.shopping.search.view.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.laka.androidlib.base.fragment.BaseMvpFragment
import com.laka.androidlib.eventbus.Event
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
import com.laka.ergou.common.widget.SpacesStaggeredDecoration
import com.laka.ergou.mvp.main.constant.HomeConstant
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import com.laka.ergou.mvp.main.contract.ISearchResultContract
import com.laka.ergou.mvp.main.presenter.SearchResultPresenter
import com.laka.ergou.mvp.shop.ShopDetailModuleNavigator
import com.laka.ergou.mvp.shop.SpacesItemDecoration
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.shopping.center.view.adapter.ProductListAdapter
import kotlinx.android.synthetic.main.fragment_search_result.*

/**
 * @Author:Rayman
 * @Date:2019/1/11
 * @Description:主页搜索页面---搜索结果Fragment
 */
class SearchResultFragment : BaseMvpFragment<ProductWithCoupon>(), ISearchResultContract.ISearchResultView,
        OnRequestListener<OnResultListener>, View.OnClickListener {

    /**
     * description:UI设置
     **/
    private var mDrawableAsc: Drawable? = null
    private var mDrawableDesc: Drawable? = null
    private var mDrawableDefault: Drawable? = null
    private var mGridItemDecoration: SpacesStaggeredDecoration? = null

    /**
     * description:数据设置
     **/
    private var keyWord = ""
    @HomeConstant.SEARCH_SORT_TYPE
    private var sortType = HomeConstant.SEARCH_SORT_COMPLEX
    private var isAsc = false
    private var isCoupon = false
    private var mAdapter: ProductListAdapter? = null
    private var mResultPresenter: ISearchResultContract.ISearchResultPresenter? = null
    private var mResultListener: OnResultListener? = null
    private var isNetWorkError = false
    private var mOnItemClickTime = 0L
    private val mClickTimeInterval = 1000

    companion object {

        fun newInstance(keyWord: String): Fragment {
            val instance = SearchResultFragment()
            val bundle = Bundle()
            bundle.putString(HomeConstant.SEARCH_KEY_WORD, keyWord)
            instance.arguments = bundle
            return instance
        }
    }

    override fun setContentView(): Int {
        return R.layout.fragment_search_result
    }

    override fun initArgumentsData(arguments: Bundle?) {
        keyWord = arguments?.getString(HomeConstant.SEARCH_KEY_WORD, "")!!
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        if (mGridItemDecoration == null) {
            mGridItemDecoration = SpacesStaggeredDecoration(object : SpacesStaggeredDecoration.ItemDecorationCallBack {
                override fun getItemType(position: Int?): Int? {
                    return mAdapter?.getItemViewType(position!!)
                }
            },true, ResourceUtils.getDimen(R.dimen.dp_10), ResourceUtils.getDimen(R.dimen.dp_6))
        }
        rv_search_result_list.setOnRequestListener(this)
                .setLayoutManager(StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))
                .addItemDecoration(mGridItemDecoration)
        //设置内边距
        //rv_search_result_list.enableTopPadding(4f)

        tv_search_complex.isSelected = sortType == HomeConstant.SEARCH_SORT_COMPLEX

        mDrawableAsc = ResourceUtils.getDrawable(R.drawable.default_btn_sort_s2)
        mDrawableDesc = ResourceUtils.getDrawable(R.drawable.default_btn_sort_s1)
        mDrawableDefault = ResourceUtils.getDrawable(R.drawable.default_btn_sort_n2)
        mDrawableAsc?.setBounds(0, 0, mDrawableAsc?.intrinsicWidth!!, mDrawableAsc?.intrinsicHeight!!)
        mDrawableDesc?.setBounds(0, 0, mDrawableDesc?.intrinsicWidth!!, mDrawableDesc?.intrinsicHeight!!)
        mDrawableDefault?.setBounds(0, 0, mDrawableDefault?.intrinsicWidth!!, mDrawableDefault?.intrinsicHeight!!)

        rv_search_result_list?.noDataView?.findViewById<TextView>(R.id.tv_no_data)?.text = ResourceUtils.getString(R.string.no_search_result_hint)
    }

    override fun initData() {
        mAdapter = ProductListAdapter(ArrayList())
        rv_search_result_list.adapter = mAdapter
        mAdapter?.switchUIMode(ShoppingCenterConstant.LIST_UI_TYPE_GRID)
        if (!TextUtils.isEmpty(keyWord)) {
            if (!isNetWorkError) {
                rv_search_result_list?.refresh()
            }
        }
        rv_search_result_list.onItemClickListener = object : RefreshRecycleView.OnItemClickListener<ProductWithCoupon> {
            override fun onItemClick(data: ProductWithCoupon?, position: Int) {
                if (System.currentTimeMillis() - mOnItemClickTime > mClickTimeInterval) {
                    mOnItemClickTime = System.currentTimeMillis()
                    ShopDetailModuleNavigator.startShopDetailActivity(activity, "${data?.productId}")
                }
            }

            override fun onChildClick(id: Int, data: ProductWithCoupon?, position: Int) {
            }
        }
    }

    override fun initEvent() {
        setClickView<TextView>(R.id.tv_search_complex)
        setClickView<TextView>(R.id.tv_search_count)
        setClickView<TextView>(R.id.tv_search_price)
        setClickView<View>(R.id.view_search_switch)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search_complex -> {
                sortType = HomeConstant.SEARCH_SORT_COMPLEX
                switchSortTypeUI()
            }
            R.id.tv_search_count -> {
                if (sortType != HomeConstant.SEARCH_SORT_COUNT) {
                    // 假若是从其他筛选方式切换过来，默认都是倒叙
                    isAsc = false
                } else {
                    isAsc = !isAsc
                }
                sortType = HomeConstant.SEARCH_SORT_COUNT
                switchSortTypeUI()
            }
            R.id.tv_search_price -> {
                if (sortType != HomeConstant.SEARCH_SORT_PRICE) {
                    // 假若是从其他筛选方式切换过来，默认都是倒叙
                    isAsc = false
                } else {
                    isAsc = !isAsc
                }
                sortType = HomeConstant.SEARCH_SORT_PRICE
                switchSortTypeUI()
            }
            R.id.view_search_switch -> {
                isCoupon = !isCoupon
                view_search_switch.isSelected = isCoupon
            }
        }
        rv_search_result_list?.refresh()
    }

    override fun onEvent(event: Event?) {
        super.onEvent(event)
        when (event?.name) {
            HomeEventConstant.EVENT_UPDATE_RESULT_SEARCH_BY_KEY_WORD -> {
                // 当用户按下搜索主页的"热门"与"历史"数据的时候，就会发送当前事件。
                // 更新当前列表数据
                keyWord = event.data as String
                if (TextUtils.isEmpty(keyWord)) {
                    rv_search_result_list?.clearAll()
                } else {
                    rv_search_result_list?.refresh()
                }
            }
            HomeEventConstant.EVENT_ON_NETWORK_ERROR -> {
                // 网络失败下，隐藏loadingView，展示errorView。
                // 为什么要先隐藏loading呢？因为网络Resume的时候需要显示loading嘛。
                // 而且loading是在errorView上层，这里就需要先隐藏loadingView，再展示errorView。
                // 需要判断当前列表是否有数据，假若有数据就不显示errorView
                if (ListUtils.isEmpty(rv_search_result_list.adapter.data)) {
                    dismissLoading()
                    rv_search_result_list?.showErrorView()
                }
                isNetWorkError = true
            }
            HomeEventConstant.EVENT_ON_NETWORK_RESUME -> {
                // 当数据为空的时候，重新刷新列表
                //重新刷新列表，并展示loading框。
                if (ListUtils.isEmpty(rv_search_result_list.adapter.data)) {
                    showLoading()
                    isNetWorkError = false
                    rv_search_result_list?.refresh(false)
                }
            }
        }
    }

    private fun switchSortTypeUI() {
        tv_search_complex.isSelected = sortType == HomeConstant.SEARCH_SORT_COMPLEX
        tv_search_count.isSelected = sortType == HomeConstant.SEARCH_SORT_COUNT
        tv_search_price?.isSelected = sortType == HomeConstant.SEARCH_SORT_PRICE

        if (tv_search_complex.isSelected) {
            tv_search_count?.setCompoundDrawables(null, null, mDrawableDefault, null)
            tv_search_price?.setCompoundDrawables(null, null, mDrawableDefault, null)
        } else if (tv_search_count.isSelected) {
            tv_search_count?.setCompoundDrawables(null, null, if (isAsc) mDrawableAsc else mDrawableDesc, null)
            tv_search_price?.setCompoundDrawables(null, null, mDrawableDefault, null)
        } else if (tv_search_price.isSelected) {
            tv_search_count?.setCompoundDrawables(null, null, mDrawableDefault, null)
            tv_search_price?.setCompoundDrawables(null, null, if (isAsc) mDrawableAsc else mDrawableDesc, null)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (isHidden) {
            // 假若隐藏的时候，统一切到综合分类
            sortType = HomeConstant.SEARCH_SORT_COMPLEX
            switchSortTypeUI()
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mResultPresenter = SearchResultPresenter()
        return mResultPresenter!!
    }

    override fun onRequest(page: Int, resultListener: OnResultListener?): String {
        mResultListener = resultListener
        mResultPresenter?.getSearchResult(keyWord, sortType, isAsc, isCoupon, page)
        return ""
    }

    override fun showLoading() {
        rv_search_result_list?.showLoadingView()
    }

    override fun dismissLoading() {
        rv_search_result_list?.hideLoadingView()
    }

    override fun showData(data: ProductWithCoupon) {

    }

    override fun onGetListDataSuccess(baseListBean: BaseListBean<ProductWithCoupon>?) {
        mResultListener?.onResponse(baseListBean)
    }

    override fun showGetDataNetWorkErrorView() {
        rv_search_result_list?.stopRefresh()
        rv_search_result_list?.showErrorView(true)
    }

    override fun showGetDataErrorView() {
        rv_search_result_list?.stopRefresh()
        rv_search_result_list?.showErrorView(false)
    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast(msg)
        rv_search_result_list?.stopRefresh()
    }

    override fun onLoadListDataFail(msg: String, page: Int) {
        LogUtils.info("搜索page:$page")
        if (page == 1) {
            mResultListener?.onFailure(-1, "")
        }
    }
}