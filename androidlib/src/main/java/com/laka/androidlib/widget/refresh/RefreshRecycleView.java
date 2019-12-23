package com.laka.androidlib.widget.refresh;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.laka.androidlib.features.login.OnRequestListener;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.widget.refresh.layoutmanager.SmoothScrollLinearLayoutManager;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:Rayman
 * @Date:2018/5/3
 * @Description:按照协议简单封装SmartRefresh和RecycleView
 */
public class RefreshRecycleView extends FrameLayout implements IPageListLayout<RecyclerView, BaseQuickAdapter, BaseListBean>,
        View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private static final int DEFAULT_TIME_OFFSET = 0;
    public static final int DEFAULT_PAGE = 1;
    public static final int PAGE_NO_LIMIT = 99999;
    public static final int DEFAULT_LIMIT = 10;
    public static final int ERROR_STATE_NO_DATA = 0;
    public static final int ERROR_STATE_NETWORK = 1;
    public static final int ERROR_STATE_DATA_EXCEPTION = 2;
    protected static final int CLICK_INTERVAL = 500;

    @IntDef({ERROR_STATE_NO_DATA,
            ERROR_STATE_NETWORK,
            ERROR_STATE_DATA_EXCEPTION})
    @Retention(RetentionPolicy.SOURCE)
    @interface ERROR_STATE {
    }

    /**
     * description:刷新UI配置
     * 这里需要区分的是，一个是Refresh的Footer，而一个是Adapter的Footer。
     **/
    private SmartRefreshLayoutCompat mRefresh;
    private RefreshHeader mHeader;
    private RefreshFooter mFooter;

    private RecyclerView mRvList;
    protected BaseQuickAdapter mAdapter;

    /**
     * description:状态View配置
     **/
    private View mNoDataView;
    private View mErrorView;
    private View mLoadingView;
    private View mNoMoreView;

    /**
     * description:Adapter的Header和Footer管理器
     **/
    private List<View> mAdapterHeaderList;
    private List<View> mAdapterFooterList;

    /**
     * description:配置TAG类型滚动
     **/
    private LinkedHashMap<Integer, Integer> typePositionMap;

    /**
     * description:页面数据配置，例如加载页数等
     **/
    private int mDefaultPage = DEFAULT_PAGE + 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private boolean isEnableMultiClick = false;
    private boolean isEnableNoMoreDataView = true;
    private boolean isPureMode = false;
    private boolean isEnableManualHandleEmptyData = false;
    private long firClickTime = 0;
    private Map<Integer, Long> clickTimeMap;


    protected OnRequestListener<OnResultListener> onRequestListener;
    protected OnResultListener onResultListener;
    private OnItemClickListener onItemClickListener;
    private OnErrorStateClickListener onErrorStateClickListener;
    private OnScrollListener onScrollListener;

    public RefreshRecycleView(Context context) {
        this(context, null);
    }

    public RefreshRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecycleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProperty(context, attrs);
    }

    @Override
    public void initProperty(Context context, AttributeSet attrs) {
        typePositionMap = new LinkedHashMap<>();
        mAdapterHeaderList = new ArrayList<>();
        mAdapterFooterList = new ArrayList<>();
        clickTimeMap = new HashMap<>();
        initViews(context, attrs);
    }

    @Override
    public void initViews(Context context, @Nullable AttributeSet attrs) {
        setClipToPadding(false);
        mRefresh = new SmartRefreshLayoutCompat(context);
        mRefresh.setEnableNestedScroll(true);
        mRefresh.setEnableAutoLoadMore(true);
        mRefresh.setEnableOverScrollBounce(true);
        mRefresh.setEnableFooterFollowWhenLoadFinished(false);
        mRefresh.setEnableLoadMoreWhenContentNotFull(false);
        mRefresh.setDisableContentWhenRefresh(false);
        mRefresh.setDisableContentWhenLoading(false);
        mRefresh.setClipToPadding(false);

        mRvList = new RecyclerView(context);
        mRvList.setVerticalScrollBarEnabled(true);
        mRvList.setClipToPadding(false);
        mRvList.setOverScrollMode(OVER_SCROLL_NEVER); //取消RecyclerView默认阴影效果
        mRvList.setLayoutManager(new SmoothScrollLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LogUtils.info("scroll-------onScrollStateChanged");
                if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                    int[] first = new int[layoutManager.getSpanCount()];
                    layoutManager.findFirstCompletelyVisibleItemPositions(first);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)) {
                        LogUtils.info("scroll-------滑动到顶部，刷新间距");
                        layoutManager.invalidateSpanAssignments();
                    }
                }
            }
        });
        mRefresh.addView(mRvList);

        this.addView(mRefresh);
        initEvent();
    }

    /**
     * 初始化数据
     */
    private void initEvent() {
        mRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.info("shoppingListFragment---------onLoadMore-------mCurrentPage=" + mCurrentPage + "--------mDefaultPage=" + mDefaultPage);
                /*
                * 由于是通过当前 page+1 的方式设置 mDefaultPage，所以当加载失败时，调用 stopRefresh ，停止加载动画，此时 mCurrentPage = mDefaultPage
                * 不会走下面的逻辑
                * */
                if (mCurrentPage < mDefaultPage) {
                    mCurrentPage++;
                    if (onRequestListener != null) {
                        mRvList.stopScroll();
                        onRequestListener.onRequest(mCurrentPage, new OnResultListener() {
                            @Override
                            public void onFailure(int errorCode, String errorMsg) {

                                //加载失败，如果列表还没有数据（首次加载）,则显示加载错误UI
                                //如果列表已经有显示的数据了，那么就算加载接口失败，也不用显示加载错误的UI
                                if (mAdapter.getData() == null || mAdapter.getData().isEmpty()) {
                                    showErrorView();
                                }
                                //加载失败，停止刷新动画，mCurrentPage - 1
                                stopRefresh();
                                mCurrentPage -= 1;

                                if (onResultListener != null) {
                                    onResultListener.onFailure(errorCode, errorMsg);
                                }
                            }

                            @Override
                            public void onResponse(BaseListBean response) {
                                // 兼容靠手动设置setDefaultPage的请求。
                                if (response.getPageTotalCount() != -1) {
                                    setDefaultPage(response.getPageTotalCount());
                                }
                                if (onResultListener != null) {
                                    onResultListener.onResponse(response);
                                }
                                addData(response);
                            }
                        });
                    }
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                doRequest();
            }
        });

        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                recyclerView.getScrollX();
                int firstPosition = 0;
                int lastPosition = 0;
                int lastViewType = -1;
                int firstViewType = -1;
                if (mRvList.getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager manager = (LinearLayoutManager) mRvList.getLayoutManager();
                    firstPosition = manager.findFirstVisibleItemPosition();
                    //根据当前Item的ViewType判断位置回调
                    firstViewType = mAdapter.getItemViewType(firstPosition);
                    lastPosition = manager.findLastVisibleItemPosition();
                    lastViewType = mAdapter.getItemViewType(lastPosition);
                } else if (mRvList.getLayoutManager() instanceof GridLayoutManager) {
                    GridLayoutManager manager = (GridLayoutManager) mRvList.getLayoutManager();
                    firstPosition = manager.findFirstVisibleItemPosition();
                    firstViewType = mAdapter.getItemViewType(firstPosition);
                    lastPosition = manager.findLastVisibleItemPosition();
                    lastViewType = mAdapter.getItemViewType(lastPosition);
                } else if (mRvList.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) mRvList.getLayoutManager();
                    //当前可见item的position集合
                    int[] firstArrayPos = null;
                    int[] lastArrayPos = null;
                    //返回每个span(每一列)的首个可见item在适配器中的位置
                    firstArrayPos = manager.findFirstVisibleItemPositions(firstArrayPos);
                    lastArrayPos = manager.findLastVisibleItemPositions(lastArrayPos);
                    if (lastArrayPos != null && lastArrayPos.length > 0
                            && firstArrayPos != null && firstArrayPos.length > 0) {
                        firstPosition = firstArrayPos[0];
                        firstViewType = mAdapter.getItemViewType(firstPosition);
                        lastPosition = lastArrayPos[lastArrayPos.length - 1];
                        lastViewType = mAdapter.getItemViewType(lastPosition);
                    }
                }

                if (onScrollListener != null) {
                    onScrollListener.onScroll(recyclerView, dx, dy, firstViewType, lastViewType);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
//                    ImageLoader.getInstance().pauseRequest();
//                } else {
//                    ImageLoader.getInstance().resumeRequest();
//                }
            }
        });
    }

    /**
     * 加载数据
     */
    private void doRequest() {
        LogUtils.info("shoppingListFragment---------onRefresh");
        mCurrentPage = DEFAULT_PAGE;
        if (mDefaultPage < mCurrentPage) {
            LogUtils.error("mDefaultPage（" + mDefaultPage + "） must be >= mCurrentPage（" + mCurrentPage + "）.");
        }
        mRefresh.setNoMoreData(false);
        if (onRequestListener != null) {
            //这样传递的意思是，我每次onRequest的回调，都可以拿到一个onResult的回调。然后直接回调onResult.onResponse
            //就可以回调到这个View上面处理加载的逻辑了，所以外层只需要定义个onResultListener，然后在数据回调的时候调用
            //onResult.onResponse就可以了
            onRequestListener.onRequest(mCurrentPage, new OnResultListener() {
                @Override
                public void onFailure(int errorCode, String errorMsg) {

                    //加载失败，如果列表还没有数据（首次加载）,则显示加载错误UI
                    //如果列表已经有显示的数据了，那么就算加载接口失败，也不用显示加载错误的UI
                    if (mAdapter.getData() == null || mAdapter.getData().isEmpty()) {
                        showErrorView();
                    }

                    if (onResultListener != null) {
                        onResultListener.onFailure(errorCode, errorMsg);
                    }
                }

                @Override
                public void onResponse(BaseListBean response) {
                    // 兼容靠手动设置setDefaultPage的请求。
                    if (response.getPageTotalCount() > 0) {
                        setDefaultPage(response.getPageTotalCount());
                    } else {
                        // 假若Page不正常，显示EmptyView
                        stopRefresh();
                        showEmptyView();
                        return;
                    }
                    if (onResultListener != null) {
                        onResultListener.onResponse(response);
                    }
                    //LogUtils.info("RefreshRecyclerView数据回调时间：" + System.currentTimeMillis());
                    upDateData(response);
                }
            });
            mRvList.scrollTo(0, 0);
        }
    }

    @Override
    public void onClick(View view) {
        if (mNoDataView != null && view.getId() == mNoDataView.getId()) {
            if (onErrorStateClickListener != null) {
                onErrorStateClickListener.onErrorStateClick(ERROR_STATE_NO_DATA);
            }
        } else if (mErrorView != null && view.getId() == mErrorView.getId()) {
            if (onErrorStateClickListener != null) {
                onErrorStateClickListener.onErrorStateClick(ERROR_STATE_DATA_EXCEPTION);
            }
        }
    }

    @Override
    public void refresh() {
        refresh(true);
    }

    @Override
    public void refresh(boolean isAnimation) {
        if (isAnimation) {
            mRefresh.autoRefresh();
        } else {
            doRequest();
        }
    }

    @Override
    public void refresh(int delayed, final int duration, final float dragRate) {
//        mRefresh.autoRefresh(delayed, duration, dragRate);
        mRefresh.autoRefresh(delayed, duration, dragRate, true);
    }

    public RefreshState getState() {
        return mRefresh.getState();
    }

    @Override
    public void stopRefresh() {
        if (mLoadingView != null && mLoadingView.getVisibility() == VISIBLE) {
            mLoadingView.setVisibility(GONE);
        }
        if (getCurrentPage() == DEFAULT_PAGE) {
            mRefresh.finishRefresh(DEFAULT_TIME_OFFSET);
        } else {
            mRefresh.finishLoadMore(DEFAULT_TIME_OFFSET);
        }
    }


    @Override
    public void getNextPage() {

    }

    @Override
    public void loadData() {
        mRefresh.autoLoadMore();
    }

    @Override
    public void showEmptyView() {
        if (mRefresh != null && mRefresh.getVisibility() == VISIBLE) {
            mRefresh.setVisibility(GONE);
        }

        if (mErrorView != null && mErrorView.getVisibility() == VISIBLE) {
            mErrorView.setVisibility(GONE);
        }

        if (mLoadingView != null && mLoadingView.getVisibility() == VISIBLE) {
            mLoadingView.setVisibility(GONE);
        }

        if (mNoDataView != null && mNoDataView.getVisibility() == GONE) {
            mNoDataView.setVisibility(VISIBLE);
        }
    }

    @Override
    public void showErrorView() {
        if (mRefresh != null && mRefresh.getVisibility() == VISIBLE) {
            mRefresh.setVisibility(GONE);
        }

        if (mErrorView != null && mErrorView.getVisibility() == GONE) {
            mErrorView.setVisibility(VISIBLE);
        }

        if (mLoadingView != null && mLoadingView.getVisibility() == VISIBLE) {
            mLoadingView.setVisibility(GONE);
        }

        if (mNoDataView != null && mNoDataView.getVisibility() == VISIBLE) {
            mNoDataView.setVisibility(GONE);
        }
    }

    @Override
    public void showLoadingView() {
        if (mRefresh != null && mRefresh.getVisibility() == VISIBLE) {
            mRefresh.setVisibility(GONE);
        }

        if (mErrorView != null && mErrorView.getVisibility() == VISIBLE) {
            mErrorView.setVisibility(GONE);
        }

        if (mLoadingView != null && mLoadingView.getVisibility() == GONE) {
            mLoadingView.setVisibility(VISIBLE);
        }

        if (mNoDataView != null && mNoDataView.getVisibility() == VISIBLE) {
            mNoDataView.setVisibility(GONE);
        }
    }

    @Override
    public void showLoadMoreView() {

    }

    @Override
    public void hideEmptyView() {
        if (mRefresh != null && mRefresh.getVisibility() == GONE) {
            mRefresh.setVisibility(VISIBLE);
        }
        if (mNoDataView != null && mNoDataView.getVisibility() == VISIBLE) {
            mNoDataView.setVisibility(GONE);
        }
    }

    @Override
    public void hideErrorView() {
        if (mRefresh != null && mRefresh.getVisibility() == GONE) {
            mRefresh.setVisibility(VISIBLE);
        }
        if (mErrorView != null && mErrorView.getVisibility() == VISIBLE) {
            mErrorView.setVisibility(GONE);
        }
    }

    @Override
    public void hideLoadingView() {
        if (mRefresh != null && mRefresh.getVisibility() == GONE) {
            mRefresh.setVisibility(VISIBLE);
        }
        if (mLoadingView != null && mLoadingView.getVisibility() == VISIBLE) {
            mLoadingView.setVisibility(GONE);
        }
    }

    @Override
    public void hideLoadMoreView() {

    }

    @Override
    public void clearAll() {
        mAdapter.setNewData(new ArrayList());
    }

    @Override
    public void resetPage() {
        mCurrentPage = DEFAULT_PAGE;
    }

    @Override
    public int getCurrentPage() {
        return mCurrentPage;
    }

    @Override
    public int getDefaultPage() {
        return mDefaultPage;
    }

    @Override
    public RecyclerView getContentView() {
        return mRvList;
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void addData(BaseListBean newData) {
        resetRefreshState();
        if (newData.isEmpty() && !isEnableManualHandleEmptyData) {
            return;
        }

        if (mAdapter != null) {
            mAdapter.addData(newData.getList());
//            LogUtils.error("添加数据：" + newData);
        }

        if (newData.getList().size() > 0 && typePositionMap.size() == 0) {
            for (int i = 0, size = newData.getList().size(); i < size; i++) {
                if (!typePositionMap.containsKey(mAdapter.getItemViewType(i))) {
                    typePositionMap.put(mAdapter.getItemViewType(i), i);
                }
            }
        }
    }

    @Override
    public void upDateData(BaseListBean newData) {
        hideLoadingView();
        hideErrorView();
        resetRefreshState();
        if (newData.isEmpty() && !isEnableManualHandleEmptyData) {
            if (mAdapter != null) {
                mAdapter.getData().clear();
            }
            showEmptyView();
            LogUtils.info("数据为空");
            return;
        }

        if (mAdapter == null) {
            return;
        }

        hideEmptyView();
        if (mAdapter != null) {
            // 更新数据（下拉刷新）
            //LogUtils.info("输出列表数据：" + newData.getList()
            //        + "\n列表数据大小：" + newData.getList().size());
            mAdapter.setNewData(newData.getList());
            //LogUtils.info("RefreshRecyclerViewAdapter 数据设置时间：" + System.currentTimeMillis());
            //LogUtils.error("更新数据：" + newData);
        }

        if (mAdapter.getData().size() > 0 && typePositionMap.size() == 0) {
            for (int i = 0, size = mAdapter.getData().size(); i < size; i++) {
                if (!typePositionMap.containsKey(mAdapter.getItemViewType(i))) {
                    typePositionMap.put(mAdapter.getItemViewType(i), i);
                }
            }
        }
        //LogUtils.error("输出mAdatper数据量大小：" + mAdapter.getData().size());
    }

    /**
     * 用于手动添加ItemViewType
     */
    public void notifyDataSetChanged() {
        if (mAdapter.getData().size() > 0/* && typePositionMap.size() == 0*/) {
            for (int i = 0, size = mAdapter.getData().size(); i < size; i++) {
                if (!typePositionMap.containsKey(mAdapter.getItemViewType(i))) {
                    typePositionMap.put(mAdapter.getItemViewType(i), i);
                }
            }
        }
    }

    /**
     * 设置Refresh的Header（刷新View）
     *
     * @param refreshHeader
     * @return
     */
    @Override
    public IPageListLayout setRefreshHeader(RefreshHeader refreshHeader) {
        mHeader = refreshHeader;
        if (mHeader != null) {
            mRefresh.setRefreshHeader(mHeader);
        }
        return this;
    }

    /**
     * 设置Refresh的Footer（加载的View）
     *
     * @param refreshFooter
     * @return
     */
    @Override
    public IPageListLayout setRefreshFooter(RefreshFooter refreshFooter) {
        mFooter = refreshFooter;
        if (mFooter != null) {
            mRefresh.setRefreshFooter(mFooter);
        }
        return this;
    }

    @Override
    public IPageListLayout enableRefresh(boolean enable) {
        mRefresh.setEnableRefresh(enable);
        return this;
    }

    @Override
    public IPageListLayout enableLoadMore(boolean enable) {
        mRefresh.setEnableLoadMore(enable);
        if (mNoMoreView != null && !enable) {
            mNoMoreView.setVisibility(View.GONE);
        } else if (mNoMoreView != null && enable) {
            mNoMoreView.setVisibility(View.VISIBLE);
        }
        return this;
    }

    @Override
    public IPageListLayout setAdapter(BaseQuickAdapter adapter) {
        if (adapter == null) {
            return this;
        }
        mAdapter = adapter;
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (onItemClickListener != null) {
                    if (checkClickValid(view.getId())) {
                        onItemClickListener.onItemClick(adapter.getItem(position), position);
                    }
                }
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (onItemClickListener != null) {
                    if (checkClickValid(view.getId())) {
                        onItemClickListener.onChildClick(view.getId(), adapter.getItem(position), position);
                    }
                }
            }
        });

        if (mAdapter.getData().size() > 0) {
            for (int i = 0, size = mAdapter.getData().size(); i < size; i++) {
                if (!typePositionMap.containsKey(mAdapter.getItemViewType(i))) {
                    typePositionMap.put(mAdapter.getItemViewType(i), i);
                }
            }
        }
        mRvList.setAdapter(mAdapter);
        return this;
    }

    @Override
    public IPageListLayout addAdapterHeader(View view) {
        if (view == null) {
            return this;
        }
        if (mAdapter != null && !mAdapterHeaderList.contains(view)) {
            mAdapter.addHeaderView(view);
            mAdapterHeaderList.add(view);
        }
        return this;
    }

    @Override
    public IPageListLayout removeAdapterHeader(View view) {
        if (view == null) {
            return this;
        }
        if (mAdapter != null && mAdapterHeaderList.contains(view)) {
            mAdapter.removeHeaderView(view);
            mAdapterHeaderList.remove(view);
        }
        return this;
    }

    @Override
    public IPageListLayout addAdapterFooter(View view) {
        if (view == null) {
            return this;
        }
        if (mAdapter != null && !mAdapterFooterList.contains(view)) {
            mAdapter.addFooterView(view);
            mAdapterFooterList.add(view);
        }
        return this;
    }

    @Override
    public IPageListLayout removeAdapterFooter(View view) {
        if (view == null) {
            return this;
        }
        if (mAdapter != null && mAdapterFooterList.contains(view)) {
            mAdapter.removeFooterView(view);
            mAdapterFooterList.remove(view);
        }
        return this;
    }

    @Override
    public IPageListLayout setNoDataView(View view) {
        if (mNoDataView != null) {
            removeView(mNoDataView);
            mNoDataView = null;
        }
        mNoDataView = view;
        if (mNoDataView != null) {
            this.addView(mNoDataView);
            mNoDataView.setVisibility(GONE);
            mNoDataView.setOnClickListener(this);
        }
        return this;
    }

    @Override
    public IPageListLayout setLoadingView(View view) {
        if (mLoadingView != null) {
            removeView(mLoadingView);
            mLoadingView = null;
        }
        mLoadingView = view;
        if (mLoadingView != null) {
            this.addView(mLoadingView);
            mLoadingView.setVisibility(GONE);
        }
        return this;
    }

    @Override
    public IPageListLayout setErrorView(View errorView) {
        mErrorView = errorView;
        if (mErrorView != null) {
            this.addView(mErrorView);
            mErrorView.setVisibility(GONE);
            mErrorView.setOnClickListener(this);
        }
        return this;
    }

    @Override
    public IPageListLayout setNoMoreDataView(View noMoreDataView) {
        this.mNoMoreView = noMoreDataView;
        return this;
    }

    @Override
    public IPageListLayout setPureScrollMode(boolean isPureMode) {
        this.isPureMode = isPureMode;
        mRefresh.setEnablePureScrollMode(isPureMode);
        return this;
    }

    @Override
    public IPageListLayout setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager == null) {
            return this;
        }
        mRvList.setLayoutManager(layoutManager);
        return this;
    }

    @Override
    public IPageListLayout addItemDecoration(RecyclerView.ItemDecoration decoration) {
        if (decoration == null) {
            return this;
        }
        mRvList.addItemDecoration(decoration);
        return this;
    }

    @Override
    public IPageListLayout removeItemDecoration(RecyclerView.ItemDecoration decoration) {
        if (decoration == null) {
            return this;
        }
        mRvList.removeItemDecoration(decoration);
        return this;
    }

    @Override
    public IPageListLayout setItemAnimator(RecyclerView.ItemAnimator animator) {
        if (animator == null) {
            return this;
        }
        mRvList.setItemAnimator(animator);
        return this;
    }

    /**
     * 设置是否可以多次点击
     *
     * @param isEnableMultiClick
     */
    @Override
    public IPageListLayout setEnableMultiClick(boolean isEnableMultiClick) {
        this.isEnableMultiClick = isEnableMultiClick;
        return this;
    }

    @Override
    public IPageListLayout enableFooterNoMoreData(boolean isEnable) {
        isEnableNoMoreDataView = isEnable;
        //假若不开启，就移除对应的footer
        if (!isEnable && mAdapterFooterList.contains(mNoDataView)) {
            mAdapter.removeFooterView(mNoMoreView);
            mAdapterFooterList.remove(mNoDataView);
        }
        return this;
    }

    @Override
    public IPageListLayout enableManualHandleEmptyData(boolean isManual) {
        this.isEnableManualHandleEmptyData = isManual;
        return this;
    }

    /**
     * 设置默认Page，在第一次接口获取之后需要传入
     * 假若链式调用，必须要放在enableLoadMore之前
     * 用于加载更多。
     *
     * @param defaultPage
     * @return
     */
    @Override
    public IPageListLayout setDefaultPage(int defaultPage) {
        this.mDefaultPage = defaultPage;
        //假若DefaultPage小于等于当前Page的情况下，禁止加载更多
        //假若我后台的page只有1，我们默认也是1，那么就禁止加载。同时添加一个footer
        boolean isHaveMoreData = mDefaultPage > mCurrentPage;
        mRefresh.setNoMoreData(!isHaveMoreData);
        mRefresh.setEnableLoadMore(isHaveMoreData);
        if (mAdapter != null && isEnableNoMoreDataView) {
            if (!isHaveMoreData) {
                if (!mAdapterFooterList.contains(mNoDataView)) {
                    mAdapter.addFooterView(mNoMoreView);
                    mAdapterFooterList.add(mNoDataView);
                }
            } else {
                if (mAdapterFooterList.contains(mNoDataView)) {
                    mAdapter.removeFooterView(mNoMoreView);
                    mAdapterFooterList.remove(mNoDataView);
                }
            }
        }
        return this;
    }

    /**
     * 计算总共有多少页数据
     *
     * @listSize: 总的数据条数
     * @limit: 每页数据条数
     */
    @Override
    public IPageListLayout setDefaultPage(int listSize, int limit) {
//        LogUtils.error(TAG, "设置DefaultPage：" + listSize + "\nLimit：" + limit);
        if (listSize == 0) {
            setDefaultPage(1);
        } else {
            double maxPage = Math.ceil(listSize / (double) limit);
            if (maxPage > 0) {
                setDefaultPage((int) maxPage);
            }
        }
        return this;
    }

    @Override
    public IPageListLayout setOnRequestListener(OnRequestListener<OnResultListener> onRequestListener) {
        this.onRequestListener = onRequestListener;
        return this;
    }

    @Override
    public IPageListLayout setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
        return this;
    }

    public IPageListLayout setBackGroundColor(int color) {
        mRvList.setBackgroundColor(color);
        return this;
    }

    @Override
    public void scrollToPositionByType(int itemType) {
        if (typePositionMap != null && typePositionMap.containsKey(itemType)) {
            mRvList.smoothScrollToPosition(typePositionMap.get(itemType));
        }
    }

    @Override
    public void scrollToPositionByTypeWithOffset(int itemType, int offset) {
        if (typePositionMap != null && typePositionMap.containsKey(itemType)) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRvList.getLayoutManager();
            layoutManager.scrollToPositionWithOffset(typePositionMap.get(itemType), offset);
        }
    }

    @Override
    public void scrollToPosition(int position) {
        mRvList.smoothScrollToPosition(position);
    }

    @Override
    public void scrollToPositionWithOffset(int position, int offset) {
        if (mRvList != null) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRvList.getLayoutManager();
            layoutManager.scrollToPositionWithOffset(position, offset);
        }
    }

    /**
     * 根据itemType找Position
     */
    public int getPositionByItemType(int itemType) {
        if (typePositionMap != null && typePositionMap.containsKey(itemType)) {
            return typePositionMap.get(itemType);
        } else {
            return 0;
        }
    }

    /**
     * 根据Position和Id找对应的View
     *
     * @param position
     * @param id
     * @return
     */
    public View findViewInAdapter(@NonNull int position, @IdRes int id) {
        if (mAdapter == null) {
            return null;
        }
        return mAdapter.getViewByPosition(mRvList, position, id);
    }

    /**
     * 重置刷新状态，主要是重置是否有更多数据的状态重置
     */
    protected void resetRefreshState() {
        if (mRefresh.getState() == RefreshState.Refreshing) {
            mRefresh.finishRefresh(DEFAULT_TIME_OFFSET);
        }

        if (mRefresh.getState() == RefreshState.Loading) {
            if (mCurrentPage < mDefaultPage) {
                mRefresh.finishLoadMore(DEFAULT_TIME_OFFSET);
            } else {
                mRefresh.finishLoadMoreWithNoMoreData();
            }
        }
    }

    /**
     * 检查当前ID是否在点击有效时间内
     *
     * @return
     */
    protected boolean checkClickValid(int viewId) {
        if (isEnableMultiClick) {
            return true;
        } else {
            boolean isValid;
            firClickTime = clickTimeMap.get(viewId) == null ? firClickTime : clickTimeMap.get(viewId);
            if (System.currentTimeMillis() - firClickTime > CLICK_INTERVAL) {
                firClickTime = System.currentTimeMillis();
                isValid = true;
            } else {
                firClickTime = System.currentTimeMillis();
                isValid = false;
            }
            clickTimeMap.put(viewId, firClickTime);
            return isValid;
        }
    }

    public void setOnItemClickListener(RefreshRecycleView.OnItemClickListener OnItemClickListener) {
        this.onItemClickListener = OnItemClickListener;
    }

    public RefreshRecycleView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnErrorStateClickListener(RefreshRecycleView.OnErrorStateClickListener onErrorStateClickListener) {
        this.onErrorStateClickListener = onErrorStateClickListener;
    }

    public RefreshRecycleView.OnErrorStateClickListener getOnErrorStateClickListener() {
        return onErrorStateClickListener;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }

    public SmartRefreshLayoutCompat getSmartRefreshLayout() {
        return mRefresh;
    }

    /**
     * Item点击回调
     *
     * @param <T>
     */
    public interface OnItemClickListener<T> {
        /**
         * RecyclerView点击事件
         *
         * @param data
         * @param position
         */
        void onItemClick(T data, int position);

        /**
         * Adapter 子Item点击事件
         *
         * @param id
         * @param data
         * @param position
         */
        void onChildClick(int id, T data, int position);
    }

    /**
     * 错误状态下点击回调
     */
    public interface OnErrorStateClickListener {
        /**
         * 根据错误类型的点击回调事件
         *
         * @param errorState
         */
        void onErrorStateClick(@ERROR_STATE int errorState);
    }

    public interface OnScrollListener {

        /**
         * 滚动回调方法
         *
         * @param recyclerView
         * @param dx
         * @param dy
         * @param viewType                当前Item的ViewType，当前页面首个item的viewType
         * @param lastVisibleItemViewType 当前页面最后一个显示的 item 的 viewType
         */
        void onScroll(@NonNull RecyclerView recyclerView, float dx, float dy, int viewType, int lastVisibleItemViewType);

    }
}
