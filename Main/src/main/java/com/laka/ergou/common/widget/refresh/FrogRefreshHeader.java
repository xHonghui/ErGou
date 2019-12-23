package com.laka.ergou.common.widget.refresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.laka.ergou.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.internal.InternalAbstract;

/**
 * @Author:Rayman
 * @Date:2018/5/3
 * @Description:项目中刷新Header封装
 */

public class FrogRefreshHeader extends InternalAbstract implements RefreshHeader {

    private TextView mTvRefreshHint;
    private ProgressBar mProgress;

    public static FrogRefreshHeader newInstance(Context context) {
        View wrapper = LayoutInflater.from(context).inflate(R.layout.layout_frog_refresh, null);
        return new FrogRefreshHeader(wrapper);
    }

    private FrogRefreshHeader(@NonNull View wrapper) {
        super(wrapper);
        mProgress = wrapper.findViewById(R.id.pull_to_refresh_progress);
        mTvRefreshHint = wrapper.findViewById(R.id.pull_to_refresh_text);
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        super.onStateChanged(refreshLayout, oldState, newState);
        if (oldState == RefreshState.None) {
            mTvRefreshHint.setText("下拉刷新数据....");
        } else if (newState == RefreshState.ReleaseToRefresh) {
            mTvRefreshHint.setText("松开刷新数据....");
        } else if (newState == RefreshState.Refreshing) {
            mTvRefreshHint.setText("正在刷新数据....");
        } else if (newState == RefreshState.RefreshFinish) {
//            mTvRefreshHint.setText("刷新完毕....");
        }
    }
}
