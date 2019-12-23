package com.laka.androidlib.widget.refresh.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @Author:Rayman
 * @Date:2018/5/8
 * @Description:
 */

public class GridViewItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "RecyclerView";

    //获取系统的分割线
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private int mOrientation = -1;

    private int mSpaceHeight = 0, mSpaceWidth = 0;

    public GridViewItemDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    public GridViewItemDecoration(Context context, int orientation) {
        this(context);
        setOrientation(orientation);
    }

    /**
     * GridLayoutItemDecoration With Item Spacing
     *
     * @param context
     * @param orientation
     * @param SpaceWidth  Item Spacing Horizontal
     * @param SpaceHeight Item Spacing Vertical
     */
    public GridViewItemDecoration(Context context, int orientation, int SpaceWidth, int SpaceHeight) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        this.mSpaceWidth = SpaceWidth;
        this.mSpaceHeight = SpaceHeight;
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //调用这个方法绘制分割线
//        Log.i(TAG, "recyclerview - itemdecoration---onDraw()");
        if (mOrientation != -1) {
            if (mOrientation == VERTICAL_LIST) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        } else {
            drawVertical(c, parent);
            drawHorizontal(c, parent);
        }
    }

    /**
     * 绘制水平线
     *
     * @param c      画布
     * @param parent 父容器
     */
    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();       //从父容器设置Padding的左边开始
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);    //获取每个child
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;    //获取该View底部到父容器左上角的垂直距离。因为线条是绘制在子View的底部的
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.setAlpha(0);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.setAlpha(0);
            mDivider.draw(c);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
        //1.RecyclerView会调用此方法，获取到Item之间的间距，设置Rect矩形
        //获取Item之间的间隔。

        int right = mDivider.getIntrinsicWidth() + mSpaceWidth;
        int bottom = mDivider.getIntrinsicHeight() + mSpaceHeight;
        if (isLastColumn(itemPosition, parent)) {
            //假若是最后一列，则不绘制线条
            right = 0;
        }

        if (isLastCow(itemPosition, parent)) {
            //假若是最后一行，不绘制线条
            bottom = 0;
        }
        outRect.set(0, 0, right, bottom);
    }


    /**
     * 判断是否为最后一列
     *
     * @param childAdapterPosition 子View的Position
     * @param parent               父容器
     * @return
     */
    private boolean isLastColumn(int childAdapterPosition, RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int spanCount = getSpanCount(parent);
            if (spanCount == 0) {
                return false;
            }
            if ((childAdapterPosition + 1) % spanCount == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为最后一行
     *
     * @param childAdapterPosition 子View的Position
     * @param parent               父容器
     * @return
     */
    private boolean isLastCow(int childAdapterPosition, RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int spanCount = getSpanCount(parent);
            if (spanCount == 0) {
                return false;
            }

            int childCount = parent.getAdapter().getItemCount();    //获取子布局数量
            int lastRowCount = childCount % spanCount;
//            Log.e(TAG, "lastRowCount：" + lastRowCount + "\nchildCount：" + childCount);
            if (childAdapterPosition >= childCount - spanCount) {
                //最后一行的数量小于SpanCount
                if (lastRowCount < spanCount || lastRowCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager glm = (GridLayoutManager) layoutManager;
            int spanCount = glm.getSpanCount();
            return spanCount;
        }
        return 0;
    }
}
