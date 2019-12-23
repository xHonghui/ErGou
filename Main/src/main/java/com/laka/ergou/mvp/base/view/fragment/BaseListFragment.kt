package com.laka.ergou.mvp.base.view.fragment

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.laka.androidlib.base.fragment.BaseLazyLoadFragment
import com.laka.androidlib.features.login.OnRequestListener
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.widget.refresh.OnResultListener
import com.laka.ergou.R
import com.laka.ergou.common.widget.refresh.FrogRefreshRecyclerView

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:列表fragment基类
 */
abstract class BaseListFragment : BaseLazyLoadFragment(), OnRequestListener<OnResultListener> {

    protected var mRecyclerView: FrogRefreshRecyclerView? = null
    //kotlin 中如果不想指定泛型类型，可用 * 代替
    protected var mAdapter: BaseQuickAdapter<*, *>? = null
    //跟布局，用于子类扩展视图
    protected var mClRootView: ConstraintLayout? = null
    //第二跟布局，用于子类扩展线性视图
    protected var mLlRootView: LinearLayout? = null

    override fun setContentView(): Int {
        return R.layout.fragment_base_list
    }

    @CallSuper
    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        mClRootView = rootView?.findViewById(R.id.cl_root_view)
        mLlRootView = rootView?.findViewById(R.id.ll_root_view)
        initRecyclerView(view)
    }

    /**initView 之后再进行列表数据初始化以及加载网络，避免加载时机出错*/
    @CallSuper
    override fun initSecondView(rootView: View?, savedInstanceState: Bundle?) {
        initBaseList()//初始化列表
    }

    override fun initDataLazy() {
        if (isLazyLoad()) {
            mRecyclerView?.refresh()
        }
    }

    @CallSuper
    override fun initEvent() {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            onItemClick(adapter, view, position)
        }
    }

    private fun initRecyclerView(view: View?) {
        mRecyclerView = view?.findViewById(R.id.rv_list)
        mRecyclerView?.setOnRequestListener(this)
    }

    private fun initBaseList() {
        mAdapter = initAdapter()
        mAdapter?.let {
            LogUtils.info("adapter-----------$mAdapter")
            mRecyclerView?.adapter = mAdapter
            if (!isLazyLoad()) {
                mRecyclerView?.refresh()
            }
        }
    }

    override fun onRequest(page: Int, resultListener: OnResultListener?): String {
        onRequestListData(page, resultListener)
        return ""
    }

    /**停止刷新动画*/
    protected fun stopRefresh() {
        mRecyclerView?.stopRefresh()
    }

    /**首次加载数据失败时，用来显示加载错误的页面*/
    protected open fun showErrorView() {
        stopRefresh()
        mRecyclerView?.showErrorView(true)
    }

    /**是否懒加载*/
    abstract fun isLazyLoad(): Boolean

    /**加载列表数据*/
    abstract fun onRequestListData(page: Int, resultListener: OnResultListener?)

    /**列表item点击*/
    abstract fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int)

    /**子类初始化adapter*/
    abstract fun initAdapter(): BaseQuickAdapter<*, *>
}