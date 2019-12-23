package com.laka.ergou.mvp.shopping.search.view.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.laka.androidlib.base.fragment.BaseMvpFragment
import com.laka.androidlib.eventbus.Event
import com.laka.androidlib.mvp.IBasePresenter
import com.laka.androidlib.util.ResourceUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.androidlib.util.toast.ToastHelper
import com.laka.androidlib.widget.refresh.decoration.DividerItemDecoration
import com.laka.ergou.R
import com.laka.ergou.mvp.main.constant.HomeApiConstant
import com.laka.ergou.mvp.main.constant.HomeEventConstant
import com.laka.ergou.mvp.shopping.ShoppingModuleNavigator
import com.laka.ergou.mvp.shopping.center.model.bean.BaseProduct
import com.laka.ergou.mvp.shopping.search.contract.ISearchHomeContract
import com.laka.ergou.mvp.shopping.search.model.bean.SearchHistoryBean
import com.laka.ergou.mvp.shopping.search.presenter.SearchHomePresenter
import com.laka.ergou.mvp.shopping.search.view.adapter.SearchHistoryAdapter
import kotlinx.android.synthetic.main.fragment_search_home.*

/**
 * @Author:Rayman
 * @Date:2019/1/11
 * @Description:主页搜索页面---主页面Fragment
 */
class SearchHomeFragment : BaseMvpFragment<List<BaseProduct>>(), ISearchHomeContract.ISearchHomeView, View.OnClickListener {

    /**
     * description:UI设置
     **/
    private lateinit var mClearAllDrawable: Drawable

    /**
     * description:数据配置
     **/
    //private lateinit var mHotAdapter: SearchHotAdapter
    private lateinit var mHistoryAdapter: SearchHistoryAdapter
    private lateinit var mSearchPresenter: ISearchHomeContract.ISearchHomePresenter

    companion object {
        fun newInstance(): Fragment {
            var instance = SearchHomeFragment()
            return instance
        }
    }

    override fun setContentView(): Int {
        return R.layout.fragment_search_home
    }

    override fun initArgumentsData(arguments: Bundle?) {
    }

    override fun initView(rootView: View?, savedInstanceState: Bundle?) {
        //todo 热门搜索关键词
//        rv_search_hot.layoutManager = FlowLayoutManager()
//        rv_search_hot.addItemDecoration(DividerItemDecoration.Builder(mActivity, DividerItemDecoration.VERTICAL_LIST)
//                .setItemSpacing(ScreenUtils.dp2px(4f))
//                .setGridLayoutManager(true)
//                .build())

        rv_search_history.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_search_history.addItemDecoration(DividerItemDecoration.Builder(mActivity, DividerItemDecoration.VERTICAL_LIST)
                .setItemSpacing(1)
                .build())


        mClearAllDrawable = ResourceUtils.getDrawable(R.drawable.ic_clear_all)
        mClearAllDrawable.setBounds(0, 0, ScreenUtils.dp2px(16f), ScreenUtils.dp2px(16f))
        tv_search_clear_all.setCompoundDrawables(mClearAllDrawable, null, null, null)
    }

    override fun initData() {
        //todo 热门搜索关键词
        //mHotAdapter = SearchHotAdapter()
        mHistoryAdapter = SearchHistoryAdapter()

        //todo 热门搜索关键词
        //rv_search_hot.adapter = mHotAdapter

        rv_search_history.adapter = mHistoryAdapter
        mSearchPresenter.getSearchHistoryData()
        mSearchPresenter.getSearchHotData()
    }

    override fun initEvent() {
        setClickView<TextView>(R.id.tv_video_course_see)
        setClickView<TextView>(R.id.tv_search_clear_all)
        //热门搜索关键词
//        mHotAdapter.setOnItemChildClickListener { adapter, view, position ->
//            mHotAdapter.data.get(position)
//        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_search_clear_all -> {
                clearAllHistoryData()
            }
            R.id.tv_video_course_see ->{
                ShoppingModuleNavigator.startVideoCourseActivity(activity, getString(R.string.video_course_txt), HomeApiConstant.URL_USER_TUTORIAL)
            }
        }
    }

    override fun onEvent(event: Event?) {
        super.onEvent(event)
        when (event?.name) {
            HomeEventConstant.EVENT_UPDATE_RESULT_SEARCH_BY_KEY_WORD -> {
                // 添加数据到本地缓存
                mSearchPresenter.saveSearchHistoryData(event?.data as String)
            }
            HomeEventConstant.EVENT_DELETE_SEARCH_HISTORY -> {
                // 删除对应的历史记录
                mSearchPresenter.deleteSearchHistoryData(event?.data as String)
                handleSearchHistoryView()
            }
        }
    }

    /**
     * 搜索历史是否为空，确定控件显示隐藏
     * */
    private fun handleSearchHistoryView() {
        if (mHistoryAdapter.data.isEmpty()) {
            tv_search_clear_all.visibility = View.GONE
            rv_search_history.visibility = View.GONE
            tv_emptry_history.visibility = View.VISIBLE
        } else {
            tv_search_clear_all.visibility = View.VISIBLE
            rv_search_history.visibility = View.VISIBLE
            tv_emptry_history.visibility = View.GONE
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!isHidden) {
            // 重新更新历史数据
            mSearchPresenter?.getSearchHistoryData()
        }
    }

    override fun createPresenter(): IBasePresenter<*> {
        mSearchPresenter = SearchHomePresenter()
        return mSearchPresenter
    }

    override fun showData(data: List<BaseProduct>) {

    }

    override fun showErrorMsg(msg: String?) {
        ToastHelper.showToast(msg)
    }

    override fun showHotData(hotData: List<String>) {
        //热门搜索关键词
        //mHotAdapter.setNewData(hotData)
    }

    override fun showHistoryData(historyData: List<SearchHistoryBean>) {
        mHistoryAdapter.setNewData(historyData)
        handleSearchHistoryView()
    }

    private fun clearAllHistoryData() {
        // 清除全部历史记录
        mHistoryAdapter.clearAll()
        mSearchPresenter.clearSearchHistoryData()

        handleSearchHistoryView()
    }
}
