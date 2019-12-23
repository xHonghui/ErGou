package com.laka.androidlib.widget.refresh;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * @Author Lyf
 * @CreateTime 2018/5/29
 * @Description 兼容5.0以下机子版本
 * stopNestedScroll()必须重写。
 **/
public class SmartRefreshLayoutCompat extends SmartRefreshLayout {

    public SmartRefreshLayoutCompat(Context context) {
        super(context);
    }

    public SmartRefreshLayoutCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartRefreshLayoutCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    @Deprecated
    public boolean startNestedScroll(int axes) {
        return mNestedChild.startNestedScroll(axes);
    }

    @Override
    @Deprecated
    public void stopNestedScroll() {
        mNestedChild.stopNestedScroll();
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
//        mManualNestedScrolling = true;
        if (mNestedChild != null) {
            mNestedChild.setNestedScrollingEnabled(enabled);
        }
    }
}
