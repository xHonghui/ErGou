package com.laka.androidlib.widget.refresh.layoutmanager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * @Author:Rayman
 * @Date:2019/3/9
 * @Description:可控是否支持滚动LinearLayoutManager
 */

public class ScrollableLinearLayoutManager extends LinearLayoutManager {

    private boolean canScrollVertical = true;

    public ScrollableLinearLayoutManager(Context context) {
        super(context);
    }

    public ScrollableLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public ScrollableLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean canScrollVertically() {
        return super.canScrollVertically() && canScrollVertical;
    }

    public void setCanScrollVertical(boolean canScrollVertical) {
        this.canScrollVertical = canScrollVertical;
    }
}
