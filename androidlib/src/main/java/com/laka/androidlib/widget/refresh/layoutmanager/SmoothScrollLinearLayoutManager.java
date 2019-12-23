package com.laka.androidlib.widget.refresh.layoutmanager;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;

/**
 * @Author:Rayman
 * @Date:2018/5/15
 * @Description:可滚动Smooth滚动LinearLayout，配合RecyclerView的SmoothScrollTo
 */

public class SmoothScrollLinearLayoutManager extends LinearLayoutManager {

    private ToTopScroller topScroller;

    public SmoothScrollLinearLayoutManager(Context context) {
        this(context, VERTICAL, false);
    }

    public SmoothScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        topScroller = new ToTopScroller(context);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        topScroller.setTargetPosition(position);
        startSmoothScroll(topScroller);
    }

    private class ToTopScroller extends LinearSmoothScroller {

        public ToTopScroller(Context context) {
            super(context);
        }

        @Nullable
        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return SmoothScrollLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
        }

//        /**
//         * 计算滚动速率
//         *
//         * @param displayMetrics
//         * @return
//         */
//        @Override
//        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
//            return 10f / displayMetrics.densityDpi;
//        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }
}
