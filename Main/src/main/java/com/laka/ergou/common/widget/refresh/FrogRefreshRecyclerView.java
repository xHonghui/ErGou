package com.laka.ergou.common.widget.refresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.screen.ScreenUtils;
import com.laka.androidlib.widget.refresh.BaseListBean;
import com.laka.androidlib.widget.refresh.OnResultListener;
import com.laka.androidlib.widget.refresh.RefreshRecycleView;
import com.laka.ergou.R;
import com.laka.ergou.common.widget.recycler.ErGouRefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * @Author:Rayman
 * @Date:2018/7/16
 * @Description:项目中通用的刷新控件。这里需要做一层针对该项目的配置，例如UI等
 */

public class FrogRefreshRecyclerView extends RefreshRecycleView {

    /**
     * description:UI的配置
     **/
    private View mLoadingView;
    private ProgressBar mLoadingProgress;
    private View mNoDataView;
    private View mErrorView;
    private View mErrorButton;
    private View mNoMoreDataView;

    private RefreshHeader mRefreshHeader;
    private RefreshFooter mRefreshFooter;

    private boolean isEnableClickLoadMore = true;

    public FrogRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public FrogRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrogRefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
        initEvent();
    }

    private void initView(Context context, AttributeSet attrs) {
        //mRefreshHeader = new ClassicsHeader(context);
        mRefreshHeader = ErGouRefreshHeader.newInstance(context);
        mRefreshFooter = new ClassicsFooter(context);

        mNoDataView = LayoutInflater.from(context).inflate(R.layout.layout_no_data, this, false);
        mLoadingView = LayoutInflater.from(context).inflate(R.layout.dialog_frog_running, this, false);
        mLoadingProgress = mLoadingView.findViewById(R.id.progress_loading);
        mErrorView = LayoutInflater.from(context).inflate(R.layout.layout_no_network, this, false);
        mErrorButton = mErrorView.findViewById(R.id.btn_no_net_work);
        mNoMoreDataView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_no_more_data, this, false);

        // 添加View的顺序必须要NoData->ErrorView->LoadingView。
        // 因为当初定义RefreshRecyclerView选型是一个FrameLayout，所以添加View的顺序必须要这样
        setRefreshHeader(mRefreshHeader);
        setRefreshFooter(mRefreshFooter);
        setNoDataView(mNoDataView);
        setErrorView(mErrorView);
        setLoadingView(mLoadingView);
        setNoMoreDataView(mNoMoreDataView);
    }

    private void initEvent() {
        mNoDataView.findViewById(R.id.iv_no_data).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkClickValid(v.getId())) {
                    return;
                }
                if (isEnableClickLoadMore) {
                    requestNewData(false);
                }
            }
        });
        mErrorButton.setOnClickListener(mErrorClickListener);
        mErrorView.setOnClickListener(mErrorClickListener);
    }

    private OnClickListener mErrorClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            LogUtils.info("列表错误页面点击。。。");
            if (!checkClickValid(v.getId())) {
                return;
            }
            if (isEnableClickLoadMore) {
                requestNewData(true);
            }
        }
    };

    /**
     * 显示错误View
     *
     * @param isNetWorkError 是否网络错误View
     */
    public void showErrorView(boolean isNetWorkError) {
        if (isNetWorkError) {
            super.showErrorView();
        } else {
            super.showEmptyView();
        }
    }

    public void enableClickLoadMore(boolean isEnable) {
        this.isEnableClickLoadMore = isEnable;
    }

    public void enableLoadingView(boolean isEnable) {
        setLoadingView(isEnable ? mLoadingView : null);
    }

    /**
     * 固定模式---该模式下不能刷新，不能加载更多
     *
     * @param isEnable
     */
    public void enableFixedModel(boolean isEnable) {
        enableRefresh(!isEnable);
        enableLoadMore(!isEnable);
        enableClickLoadMore(!isEnable);
        enableLoadMore(!isEnable);
        enableLoadingView(!isEnable);
        enableFooterNoMoreData(false);
    }

    /**
     * 设置Loading 小人显示的位置。因为不同页面的布局问题
     * 可能有的地方需要上移或者下移
     *
     * @param offset 传入具体的dp
     */
    public void setLoadingVerticalOffset(int offset) {
        MarginLayoutParams params = (MarginLayoutParams) mLoadingProgress.getLayoutParams();
        params.topMargin = ScreenUtils.dp2px(offset);
    }

    /**
     * 设置NoDataView展示的偏移量，因为不同页面的布局问题
     * 可能有的地方需要上移或者下移
     *
     * @param offset
     */
    public void setNoDataViewOffset(int offset) {
        MarginLayoutParams params = (MarginLayoutParams) mNoDataView.getLayoutParams();
        params.topMargin = ScreenUtils.dp2px(offset);
    }

    public void scrollToTop() {
        scrollToPosition(0);
    }

    public void enableTopPadding(float padding) {
        //设置padding
        int paddingTop = ScreenUtils.dp2px(padding);
        getContentView().setClipToPadding(false);
        getContentView().setPadding(0, paddingTop, 0, 0);
    }

    /**
     * 点击空数据页面、加载错误页面 时调用的加载方法
     */
    private void requestNewData(final boolean isError) {
        showLoadingView();
        LogUtils.info("列表错误页面点击--onRequestListener ：" + onRequestListener);
        if (onRequestListener != null) {
            // 重新刷新数据，外层传递了requestListener。这里就用这个Request重新请求
            onRequestListener.onRequest(DEFAULT_PAGE, new OnResultListener() {
                @Override
                public void onFailure(int errorCode, String errorMsg) {
                    hideLoadingView();

                    //加载失败，如果列表还没有数据（首次加载）,则显示加载错误UI
                    //如果列表已经有显示的数据了，那么就算加载接口失败，也不用显示加载错误的UI
                    if (mAdapter.getData() == null || mAdapter.getData().isEmpty()) {
                        if (isError) {
                            showErrorView();
                        } else {
                            showEmptyView();
                        }
                    }

                    if (onResultListener != null) {
                        onResultListener.onFailure(errorCode, errorMsg);
                    }
                }

                @Override
                public void onResponse(BaseListBean response) {
                    hideLoadingView();
                    LogUtils.info("列表错误页面点击--重新加载成功。。。");
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
                    if (isError) {
                        hideErrorView();
                    } else {
                        hideEmptyView();
                    }
                    upDateData(response);
                }
            });
        }
    }

    public View getNoDataView() {
        return mNoDataView;
    }
}
