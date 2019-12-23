package com.laka.ergou.mvp.shopping.center.view.activity

import android.support.v7.widget.GridLayoutManager
import com.ali.auth.third.ui.context.CallbackContext.activity
import com.laka.androidlib.base.activity.BaseMvpActivity
import com.laka.androidlib.features.login.OnRequestListener
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ListUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.BaseListBean
import com.laka.androidlib.widget.refresh.OnResultListener
import com.laka.androidlib.widget.refresh.RefreshRecycleView
import com.laka.androidlib.widget.refresh.decoration.DividerItemDecoration
import com.laka.ergou.R
import com.laka.ergou.common.widget.refresh.FrogRefreshRecyclerView
import com.laka.ergou.mvp.shop.ShopDetailModuleNavigator
import com.laka.ergou.mvp.shopping.center.constant.ShoppingCenterConstant
import com.laka.ergou.mvp.shopping.center.contract.IShoppingFavoriteContract
import com.laka.ergou.mvp.shopping.center.model.bean.ProductWithCoupon
import com.laka.ergou.mvp.shopping.center.presenter.ShoppingFavoritePresenter
import com.laka.ergou.mvp.shopping.center.view.adapter.ProductListAdapter
import kotlinx.android.synthetic.main.activity_favorite_product.*

/**
 * @Author:Rayman
 * @Date:2019/1/30
 * @Description:精选商品列表
 */
class FavoriteProductListActivity : BaseMvpActivity<ProductWithCoupon>(),
        IShoppingFavoriteContract.IShoppingFavoriteView, OnRequestListener<OnResultListener> {

    /**
     * description:UI配置
     **/
    private lateinit var mRvList: FrogRefreshRecyclerView
    private var mGridItemDecoration: DividerItemDecoration? = null

    /**
     * description:数据层
     **/
    private var favoriteId = ""
    private var favoriteTitle = ""
    private lateinit var mAdapter: ProductListAdapter
    private lateinit var mResultListener: OnResultListener
    private lateinit var mPresenter: IShoppingFavoriteContract.IShoppingFavoritePresenter

    override fun setContentView(): Int {
        return R.layout.activity_favorite_product
    }

    override fun initIntent() {
        intent?.extras?.let {
            favoriteTitle = it.getString(ShoppingCenterConstant.FAVORITE_TITLE)
            favoriteId = it.getString(ShoppingCenterConstant.FAVORITE_ID)
        }
    }

    override fun initViews() {
        title_bar.setLeftIcon(R.drawable.ic_arrow_back_white)
                .setBackGroundColor(R.color.color_main)
                .setTitleTextColor(R.color.white)
                .setTitleTextSize(16)
                .setTitle(favoriteTitle)

        mGridItemDecoration = DividerItemDecoration.Builder(this, DividerItemDecoration.VERTICAL_LIST)
                .setGridLayoutManager(true)
                .drawLastRow(true)
                .setItemSpacing(ScreenUtils.dp2px(5f))
                .build()
        mRvList = findViewById(R.id.rv_favorite_product_list)
        mRvList.enableTopPadding(4f)
        mRvList.addItemDecoration(mGridItemDecoration)
                .setEnableMultiClick(false)
                .setLayoutManager(GridLayoutManager(this, 2))
                .setOnRequestListener(this)
        mRvList.enableClickLoadMore(true)
        mAdapter = ProductListAdapter(ArrayList())
        mRvList.adapter = mAdapter
        mAdapter.switchUIMode(ShoppingCenterConstant.LIST_UI_TYPE_GRID)

        mRvList.onItemClickListener = object : RefreshRecycleView.OnItemClickListener<ProductWithCoupon> {
            override fun onItemClick(data: ProductWithCoupon?, position: Int) {
                data?.let {
                    ShopDetailModuleNavigator.startShopDetailActivity(this@FavoriteProductListActivity, "${data?.productId}")
                }
            }

            override fun onChildClick(id: Int, data: ProductWithCoupon?, position: Int) {
            }
        }
    }

    override fun initData() {
        mRvList.refresh()
    }

    override fun initEvent() {

    }

    override fun onInternetChange(isLostInternet: Boolean) {
        super.onInternetChange(isLostInternet)
        if (isLostInternet) {
            if (ListUtils.isEmpty(mRvList.adapter.data)) {
                dismissLoading()
                mRvList.showErrorView()
            }
        } else {
            if (ListUtils.isEmpty(mRvList.adapter.data)) {
                showLoading()
                mRvList?.refresh(false)
            }
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mPresenter = ShoppingFavoritePresenter()
        return mPresenter
    }

    override fun onRequest(page: Int, resultListener: OnResultListener?): String {
        this.mResultListener = resultListener!!
        mPresenter.getFavoriteProductList(favoriteId = favoriteId, page = page)
        return ""
    }

    override fun showData(data: ProductWithCoupon) {

    }

    override fun onGetListDataSuccess(baseListBean: BaseListBean<ProductWithCoupon>?) {
        mResultListener.onResponse(baseListBean)
    }

    override fun showGetDataNetWorkErrorView() {
        mRvList?.stopRefresh()
        mRvList?.showErrorView(true)
    }

    override fun showGetDataErrorView() {
        mRvList?.stopRefresh()
        mRvList?.showErrorView(false)
    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast(msg)
    }

}