package com.laka.androidlib.widget.refresh.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.laka.androidlib.util.ListUtils;

import java.util.ArrayList;


/**
 * RecyclerView 线性列表的ItemDecoration
 *
 * @author Rayman
 * @date 2017/10/16
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "DividerItemDecoration";
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    /**
     * description:获取系统的分割线
     **/
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    /**
     * 分割线Drawable
     */
    private Drawable mDivider;

    /**
     * 方向
     */
    private int mOrientation;

    /**
     * 设置Item之间的高度(LinearLayoutManger与GridLayoutManager有效)
     */
    private int mItemSpacing = 0;
    private boolean isGridLayout = false;

    /**
     * description:是否绘制最后一列、一行
     **/
    private boolean isDrawLastColumn = false;
    private boolean isDrawLastRow = false;

    /**
     * 设置分割线的左右边距（LinearLayoutManager有效）
     */
    private int dividerLeft = 0, dividerRight = 0;
    private int dividerAlpha = 0;

    /**
     * description:是否目标Item
     **/
    private boolean isTargetItem = false;

    /**
     * description:设置部分特定分割线
     **/
    private ArrayList<DividerInfo> mDividerList;

    public DividerItemDecoration(Builder builder) {
        if (builder.context == null) {
            return;
        }
        final TypedArray a = builder.context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        mItemSpacing = mDivider.getIntrinsicHeight();
        a.recycle();

        this.mOrientation = builder.mOrientation;
        this.isGridLayout = builder.isGridLayout;
        this.isDrawLastRow = builder.isDrawLastRow;
        this.isDrawLastColumn = builder.isDrawLastColumn;
        this.dividerLeft = builder.dividerLeft;
        this.dividerRight = builder.dividerRight;
        this.dividerAlpha = builder.dividerAlpha;
        this.mItemSpacing = builder.mItemSpacing;
        this.mDividerList = builder.mDividerList;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
//        Log.e(TAG, "vertical list left:" + dividerLeft + "\tright:" + dividerRight);
        //从父容器设置Padding的左边开始
        final int left = parent.getPaddingLeft() + dividerLeft;
        final int right = parent.getWidth() - parent.getPaddingRight() - dividerRight;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            //获取每个child
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();

            //获取该View底部到父容器左上角的垂直距离。因为线条是绘制在子View的底部的
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mItemSpacing;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.setAlpha(dividerAlpha);
            mDivider.draw(canvas);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
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
            mDivider.setAlpha(dividerAlpha);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemSpacingPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemSpacingPosition, parent);
        //1.RecyclerView会调用此方法，获取到Item之间的间距，设置Rect矩形

        //获取Item之间的间隔
        int right = mDivider.getIntrinsicWidth();
        int bottom = 0;

        //设置具体位置的高度---针对LinearLayoutManger
        if (ListUtils.isNotEmpty(mDividerList)) {
            // 假若存在特定divider的集合，遍历并设置
            for (int i = 0; i < mDividerList.size(); i++) {
                // 因为我们设置的是目标的位置，所以需要减去1（和position的位置相差1）
                DividerInfo dividerInfo = mDividerList.get(i);
                int targetPosition = dividerInfo.getDividerTargetPosition() - 1;
                isTargetItem = itemSpacingPosition == targetPosition;//是否是目标item
                if (isTargetItem && !isGridLayout) {
                    if (mOrientation == VERTICAL_LIST) {
                        bottom += mDividerList.get(i).getDividerSpacing();
                    } else {
                        right += mDividerList.get(i).getDividerSpacing();
                    }
                }
            }
            if (!isTargetItem) {
                if (mOrientation == VERTICAL_LIST) {
                    // 存在一个BUG就是，假若我有N个dividerInfo，那么这边的逻辑就是就是添加N个间距了。
                    // 因为每次绘制一条分割线的时候，都需要遍历N个dividerInfo。
                    // 所以这里加上一个TargetItem去判断
                    bottom += mItemSpacing;
                } else {
                    right += mItemSpacing;
                }
            }
        } else {
            if (mOrientation == VERTICAL_LIST) {
                bottom += mItemSpacing;
            } else {
                right += mItemSpacing;
            }
        }

        // GridLayoutManger模式下的判断
        if (isGridLayout) {
            if (ListUtils.isNotEmpty(mDividerList)) {
                // 假若存在特定divider的集合，遍历并设置
                for (int i = 0; i < mDividerList.size(); i++) {
                    DividerInfo dividerInfo = mDividerList.get(i);
                    int targetPosition = dividerInfo.getDividerTargetPosition() - 1;
                    isTargetItem = itemSpacingPosition == targetPosition;
                    if (isTargetItem) {
                        // 是否忽略不计
                        if (dividerInfo.isDividerBottomIgnore()) {
                            bottom = 0;
                        } else {
                            bottom += mItemSpacing;
                        }
                        if (dividerInfo.isDividerRightIgnore()) {
                            right = 0;
                        } else {
                            right += mItemSpacing;
                        }
                    }
                }
            } else {
                right += mItemSpacing;
            }
        }

        if (isLastColumn(itemSpacingPosition, parent)) {
            if (isDrawLastColumn) {
                //假若是最后一列，绘制线条
                outRect.set(0, 0, right, bottom);
            } else {
                //假若是最后一列，则不绘制线条
                outRect.set(0, 0, 0, bottom);
            }
        } else if (isLastCow(itemSpacingPosition, parent)) {
            if (isDrawLastRow) {
                //假若是最后一行，绘制线条
                outRect.set(0, 0, right, bottom);
            } else {
                //假若是最后一行，不绘制线条
                outRect.set(0, 0, right, 0);
            }
        } else {
            //垂直的时候，最大间隔就是分割线的高度了
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, right, bottom);
            } else {                //在水平的时候，那么最大的间隔就是分割线的宽度
                outRect.set(0, 0, right, 0);
            }
        }
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

            // 获取子布局数量
            int childCount = parent.getAdapter().getItemCount();
            int lastRowCount = childCount % spanCount;

            Log.e(TAG, "lastRowCount：" + lastRowCount + "\nchildCount：" + childCount);
            if (childAdapterPosition >= childCount - spanCount) {
                // 最后一行的数量小于SpanCount
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

    @IntDef({HORIZONTAL_LIST, VERTICAL_LIST})
    public @interface ITEM_DECORATION_MODE {
    }

    public static class Builder {

        private Context context;

        /**
         * description:设置分割线的方向
         **/
        private int mOrientation;

        /**
         * description:是否为GridLayoutManager
         **/
        private boolean isGridLayout = false;

        /**
         * description:是否绘制最后一列、一行
         **/
        private boolean isDrawLastColumn = false;
        private boolean isDrawLastRow = false;

        /**
         * description:设置垂直方向下Item之间的高度，可以是1dp的分割线。也可以是间距很大的，用于设置类似用户信息页那些
         **/
        private int mItemSpacing;

        /**
         * description:设置垂直方向下分割线离左侧或者右侧的距离
         **/
        private int dividerLeft, dividerRight;

        /**
         * description:设置分割线的透明度。假若RecyclerView背景是灰色的，那么这里可以设置成0.
         * 注意，这个参数的取值范围是0-255
         **/
        private int dividerAlpha;

        /**
         * description:针对部分分割线的设置。（当前只有LinearLayoutManager有效）
         **/
        private ArrayList<DividerInfo> mDividerList;

        public Builder(Context context, @ITEM_DECORATION_MODE int mOrientation) {
            this.context = context;
            this.mOrientation = mOrientation;
        }

        public Builder setGridLayoutManager(boolean isGridLayout) {
            this.isGridLayout = isGridLayout;
            return this;
        }

        public Builder drawLastColumn(boolean isDrawLastColumn) {
            this.isDrawLastColumn = isDrawLastColumn;
            return this;
        }

        public Builder drawLastRow(boolean isDrawLastRow) {
            this.isDrawLastRow = isDrawLastRow;
            return this;
        }

        public Builder setItemSpacing(int mItemSpacing) {
            this.mItemSpacing = mItemSpacing;
            return this;
        }

        public Builder setDividerLeft(int dividerLeft) {
            this.dividerLeft = dividerLeft;
            return this;
        }

        public Builder setDividerRight(int dividerRight) {
            this.dividerRight = dividerRight;
            return this;
        }

        public Builder setDividerAlpha(int dividerAlpha) {
            this.dividerAlpha = dividerAlpha;
            return this;
        }

        public Builder setDividerList(ArrayList<DividerInfo> dividerList) {
            this.mDividerList = dividerList;
            return this;
        }

        public DividerItemDecoration build() {
            return new DividerItemDecoration(this);
        }
    }
}
