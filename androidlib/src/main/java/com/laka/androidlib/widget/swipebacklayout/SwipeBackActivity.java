
package com.laka.androidlib.widget.swipebacklayout;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * all the subClass extends from this class to use swipe-back function
 */
public class SwipeBackActivity extends FragmentActivity implements SwipeBackActivityBase {

    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public <T extends View> T findViewById(int id) {

        T v = super.findViewById(id);

        if (v == null && mHelper != null)
            return mHelper.findViewById(id);

        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    /**
     * 设置滚动阈值（主要是判断手势到这个位置就finish）
     *
     * @param threshold
     */
    public void setScrollThresHold(float threshold) {
        if (checkSwipeLayoutEnable()) {
            mHelper.getSwipeBackLayout().setScrollThresHold(threshold);
        }
    }

    /**
     * 设置手势滑动的有效范围
     *
     * @param edgeSize
     */
    public void setScrollEdge(int edgeSize) {
        if (checkSwipeLayoutEnable()) {
            mHelper.getSwipeBackLayout().setEdgeSize(edgeSize);
        }
    }

    private boolean checkSwipeLayoutEnable() {
        return mHelper != null && mHelper.getSwipeBackLayout() != null;
    }
}
