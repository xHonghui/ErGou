package com.laka.ergou.common.widget.refresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.laka.androidlib.util.screen.ScreenUtils;
import com.laka.ergou.R;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;

/**
 * @Author:Rayman
 * @Date:2018/5/3
 * @Description:项目刷新Footer封装
 */

public class FrogRefreshFooter extends InternalAbstract implements RefreshFooter {

    private final String TAG = this.getClass().getSimpleName();

    private TextView mTvHint;
    private ProgressBar mProgress;

    private int padding;

    public static FrogRefreshFooter newInstance(Context context) {
        View wrapper = LayoutInflater.from(context).inflate(R.layout.layout_frog_refresh, null);
        return new FrogRefreshFooter(wrapper);
    }

    protected FrogRefreshFooter(@NonNull View wrapper) {
        super(wrapper);
        padding = ScreenUtils.dp2px(10);
        wrapper.setPadding(0, 4, 0, 4);
        mTvHint = wrapper.findViewById(R.id.pull_to_refresh_text);
        mProgress = wrapper.findViewById(R.id.pull_to_refresh_progress);
        mProgress.getLayoutParams().width = ScreenUtils.dp2px(56);
        mProgress.getLayoutParams().height = ScreenUtils.dp2px(56);
    }


    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        super.onStateChanged(refreshLayout, oldState, newState);
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return super.onFinish(refreshLayout, success);
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        super.onStartAnimator(refreshLayout, height, maxDragHeight);
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        if (!noMoreData) {
            if (mProgress.getVisibility() == View.GONE) {
                mProgress.setVisibility(View.VISIBLE);
            }
            mTvHint.setText("正在加载...");
        } else {
            if (mProgress.getVisibility() == View.VISIBLE) {
                mProgress.setVisibility(View.GONE);
            }
            mTvHint.setText("已全部加载完毕");
        }
        return noMoreData;
    }
}
