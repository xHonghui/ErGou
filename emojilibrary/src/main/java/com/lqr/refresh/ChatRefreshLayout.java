package com.lqr.refresh;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lqr.emoji.R;
import com.lqr.utils.RefreshUtils;

/**
 * @Author:summer
 * @Date:2019/2/26
 * @Description:聊天列表页面刷新控件
 */
public class ChatRefreshLayout extends LinearLayout {

    private static final String TAG = "CustomRefreshLayout";
    private boolean mIsLoadingMoreEnabled = true;
    private View mLoadMoreFooterView;
    private TextView mFooterStatusTv;
    private ImageView mFooterChrysanthemumIv;
    private AnimationDrawable mFooterChrysanthemumAd;
    private String mLodingMoreText = "加载中...";
    private int mLoadMoreFooterViewHeight;
    private Handler mHandler;
    /**
     * 整个加载更多控件的背景颜色资源id
     */
    private int mLoadMoreBackgroundColorRes = -1;
    /**
     * 整个加载更多控件的背景drawable资源id
     */
    private int mLoadMoreBackgroundDrawableRes = -1;
    /**
     * 是否设置了滚动监听器
     */
    private boolean mIsInitedContentViewScrollListener;
    /**
     * 列表控件（目前只处理RecyclerView）
     */
    private RecyclerView mRecyclerView;
    /**
     * 是否处于正在加载更多状态
     */
    private boolean mIsLoadingMore;
    /**
     * 上拉加载更多的代理
     */
    private BGARefreshLayoutDelegate mDelegate;
    /**
     * 当前刷新状态
     */
    private RefreshStatus mCurrentRefreshStatus = RefreshStatus.IDLE;
    /**
     * 触发上拉加载更多时是否显示加载更多控件
     */
    private boolean mIsShowLoadingMoreView = true;
    /**
     * 是否还有数据
     */
    private boolean mHasMoreData = true;

    public ChatRefreshLayout(Context context) {
        this(context, null);
    }

    public ChatRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ChatRefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mHandler = new Handler(Looper.getMainLooper());
        initLoadMoreView(context);
    }

    private void initLoadMoreView(Context context) {
        Log.i(TAG, "initLoadMoreView: ");
        mLoadMoreFooterView = getLoadMoreFooterView(context);
        if (mLoadMoreFooterView != null) {
            // 测量上拉加载更多控件的高度
            mLoadMoreFooterView.measure(0, 0);
            mLoadMoreFooterViewHeight = mLoadMoreFooterView.getMeasuredHeight();
            mLoadMoreFooterView.setVisibility(GONE);
        }
    }

    @Nullable
    private View getLoadMoreFooterView(Context context) {
        Log.i(TAG, "getLoadMoreFooterView: ");
        if (!mIsLoadingMoreEnabled) {
            return null;
        }
        if (mLoadMoreFooterView == null) {
            mLoadMoreFooterView = LayoutInflater.from(context).inflate(R.layout.view_normal_refresh_footer, this, false);
            mLoadMoreFooterView.setBackgroundColor(Color.TRANSPARENT);
            if (mLoadMoreBackgroundColorRes != -1) {
                mLoadMoreFooterView.setBackgroundResource(mLoadMoreBackgroundColorRes);
            }
            if (mLoadMoreBackgroundDrawableRes != -1) {
                mLoadMoreFooterView.setBackgroundResource(mLoadMoreBackgroundDrawableRes);
            }
            mFooterStatusTv = mLoadMoreFooterView.findViewById(R.id.tv_normal_refresh_footer_status);
            mFooterChrysanthemumIv = mLoadMoreFooterView.findViewById(R.id.iv_normal_refresh_footer_chrysanthemum);
            mFooterChrysanthemumAd = (AnimationDrawable) mFooterChrysanthemumIv.getDrawable();
            mFooterStatusTv.setText(mLodingMoreText);
            mFooterStatusTv.setVisibility(GONE);
        }
        return mLoadMoreFooterView;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 被添加到窗口后再设置监听器，这样开发者就不必烦恼先初始化RefreshLayout还是先设置自定义滚动监听器
        Log.i(TAG, "onAttachedToWindow -- mIsInitedContentViewScrollListener：" + mIsInitedContentViewScrollListener + "-----mLoadMoreFooterView:" + mLoadMoreFooterView);
        if (!mIsInitedContentViewScrollListener && mLoadMoreFooterView != null) {
            setRecyclerViewOnScrollListener();
            addView(mLoadMoreFooterView, 0);//添加到顶部
            mIsInitedContentViewScrollListener = true;
        }
    }

    @Override
    protected void onFinishInflate() {
        Log.i(TAG, "onFinishInflate: ");
        super.onFinishInflate();
        View contentView = getChildAt(0);
        if (contentView instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) contentView;
        } else {
            Log.i(TAG, "目前只处理RecyclerView，请确保CustomRefreshLayout内部列表控件是RecyclerView");
        }
    }

    //======================================= listener =========================================

    /**
     * 设置RecyclerView 滚动监听器
     */
    private void setRecyclerViewOnScrollListener() {
        Log.i(TAG, "setRecyclerViewOnScrollListener: mRecyclerView " + mRecyclerView);
        if (mRecyclerView != null) {
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    Log.i(TAG, "newState: " + newState);
                    if ((newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_SETTLING)
                            && shouldHandleRecyclerViewLoadingMore(mRecyclerView)) {
                        Log.i(TAG, "beginLoadingMore:");
                        beginLoadingMore();
                    }
                }
            });
        }
    }

    public boolean shouldHandleRecyclerViewLoadingMore(RecyclerView recyclerView) {
        if (mIsLoadingMore || mCurrentRefreshStatus == RefreshStatus.REFRESHING
                || mLoadMoreFooterView == null || mDelegate == null
                || recyclerView.getAdapter() == null || recyclerView.getAdapter().getItemCount() == 0) {
            return false;
        }
        return RefreshUtils.isRecyclerViewToBottom(recyclerView);
    }

    /**
     * 开始上拉加载更多，会触发delegate的onBGARefreshLayoutBeginRefreshing方法
     */
    public void beginLoadingMore() {
        if (!mIsLoadingMore && mLoadMoreFooterView != null && mDelegate != null && mDelegate.hasMoreData(this)) {
            mIsLoadingMore = true;
            if (mIsShowLoadingMoreView) {
                Log.i(TAG, "beginLoadingMore: ");
                showLoadingMoreView();
                //加载更多回调
                mDelegate.onBGARefreshLayoutBeginLoadingMore(this);
            }
        }
    }

    /**
     * 显示上拉加载更多控件
     */
    private void showLoadingMoreView() {
        changeToLoadingMore();
        mLoadMoreFooterView.setVisibility(VISIBLE);
        //只处理RecyclerView
        //模拟数据的时候，由于获取的数据都是本地组装的，速度非常快，并且是先执行加载更多的回调才会走当前方法，
        //从而导致执行到这里，列表已经刷新了新一轮的数据，则就会出现每次刷新都会滚动到底部的问题。
        Log.i(TAG, "----------------------showLoadingMoreView: 列表滑动到底部------------------------");
        RefreshUtils.scrollToBottom(mRecyclerView);
    }

    /**
     * 进入加载更多状态
     */
    public void changeToLoadingMore() {
        if (mIsLoadingMoreEnabled && mFooterChrysanthemumAd != null) {
            mFooterChrysanthemumAd.start();
        }
    }

    /**
     * 结束上拉加载更多
     */
    public void onEndLoadingMore() {
        if (mIsLoadingMoreEnabled && mFooterChrysanthemumAd != null) {
            mFooterChrysanthemumAd.stop();
        }
    }

    /**
     * 结束上拉加载更多（外层调用）
     */
    public void endLoadingMore() {
        if (mIsLoadingMore) {
            if (mIsShowLoadingMoreView) {
                // 避免WiFi环境下请求数据太快，加载更多控件一闪而过
                mHandler.postDelayed(mDelayHiddenLoadingMoreViewTask, 50);
            } else {
                mIsLoadingMore = false;
            }
        }
    }


    private Runnable mDelayHiddenLoadingMoreViewTask = new Runnable() {
        @Override
        public void run() {
            mIsLoadingMore = false;
            onEndLoadingMore();
            mLoadMoreFooterView.setVisibility(GONE);
        }
    };


    public enum RefreshStatus {
        IDLE, PULL_DOWN, RELEASE_REFRESH, REFRESHING
    }

    //======================================== 刷新监听器 ==========================================

    /**
     * 设置下拉刷新上拉加载更多代理
     *
     * @param delegate
     */
    public void setDelegate(BGARefreshLayoutDelegate delegate) {
        mDelegate = delegate;
    }

    public interface BGARefreshLayoutDelegate {

        /**
         * 是否有更多数据
         */
        boolean hasMoreData(ChatRefreshLayout refreshLayout);

        /**
         * 开始加载更多。由于监听了ScrollView、RecyclerView、AbsListView滚动到底部的事件，所以这里采用返回boolean来处理是否加载更多。否则使用endLoadingMore方法会失效
         *
         * @param refreshLayout
         * @return 如果要开始加载更多则返回true，否则返回false。（返回false的场景：没有网络、一共只有x页数据并且已经加载了x页数据了）
         */
        void onBGARefreshLayoutBeginLoadingMore(ChatRefreshLayout refreshLayout);
    }


    //==================================== setting ========================================
    public void setLoadingMoreEnabled(boolean loadingMoreEnabled) {
        mIsLoadingMoreEnabled = loadingMoreEnabled;
    }

    public void setLodingMoreText(String lodingMoreText) {
        mLodingMoreText = lodingMoreText;
    }

    public void setLoadMoreBackgroundColorRes(int loadMoreBackgroundColorRes) {
        mLoadMoreBackgroundColorRes = loadMoreBackgroundColorRes;
    }

    public void setLoadMoreBackgroundDrawableRes(int loadMoreBackgroundDrawableRes) {
        mLoadMoreBackgroundDrawableRes = loadMoreBackgroundDrawableRes;
    }

    public void setHasMoreData(boolean hasMoreData) {
        mHasMoreData = hasMoreData;
    }
}
