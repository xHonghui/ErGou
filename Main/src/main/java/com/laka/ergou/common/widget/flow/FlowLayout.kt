package com.laka.ergou.common.widget.flow

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.laka.androidlib.util.ListUtils
import com.laka.androidlib.util.screen.ScreenUtils

/**
 * @Author:summer
 * @Date:2019/7/24
 * @Description:
 */
class FlowLayout : ViewGroup {

    //支持的最大行数
    private var mLineCount = 1 /*Int.MAX_VALUE*/
    private var mHorizontalPadding: Int = ScreenUtils.dp2px(3f)
    private var mVerticalPadding: Int = ScreenUtils.dp2px(3f)
    private var mNodePosList: ArrayList<NodePosData> = ArrayList()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getSize(heightMeasureSpec)

        var totalWidth = 0
        var totalHeight = 0

        mNodePosList.clear()
        var lines = 1
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        for (i in 0 until childCount) {
            val preNodeData = ListUtils.get(mNodePosList, i - 1)
            val childView = getChildAt(i)
            val currentNodeData = NodePosData()
            val childWidth = childView.measuredWidth
            val childHeight = childView.measuredHeight
            if (preNodeData == null) {
                currentNodeData.left = paddingLeft + mHorizontalPadding
                currentNodeData.top = paddingTop + mVerticalPadding
                currentNodeData.right = currentNodeData.left + childWidth
                currentNodeData.bottom = currentNodeData.top + childHeight
            } else {
                currentNodeData.left = preNodeData.right + mHorizontalPadding
                currentNodeData.top = preNodeData.top
                currentNodeData.right = currentNodeData.left + childWidth
                currentNodeData.bottom = preNodeData.bottom
                //换行
                if (currentNodeData.right > width - paddingRight - mHorizontalPadding) {
                    if (lines >= mLineCount) {
                        break
                    } else {
                        currentNodeData.left = paddingLeft + mHorizontalPadding
                        currentNodeData.top = preNodeData.bottom + mVerticalPadding
                        currentNodeData.right = currentNodeData.left + childWidth
                        currentNodeData.bottom = currentNodeData.top + childHeight
                        lines++
                    }
                }
            }
            //保存宽度
            if (currentNodeData.right + paddingRight + mHorizontalPadding > totalWidth) {
                totalWidth = currentNodeData.right + paddingRight + mHorizontalPadding
            }
            totalHeight = currentNodeData.top + childHeight + mVerticalPadding
            mNodePosList.add(currentNodeData)
        }

        when (widthMode) {
            MeasureSpec.EXACTLY -> {
                totalWidth = width
            }
            MeasureSpec.AT_MOST -> {
                if (totalWidth > width) {
                    totalWidth = width
                }
            }
            MeasureSpec.UNSPECIFIED -> {

            }
        }
        when (heightMode) {
            MeasureSpec.EXACTLY -> {
                totalHeight = height
            }
            MeasureSpec.AT_MOST -> {
                if (totalHeight > height) {
                    totalHeight = height
                }
            }
            MeasureSpec.UNSPECIFIED -> {

            }
        }
        setMeasuredDimension(totalWidth, totalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val nodeData = ListUtils.get(mNodePosList, i)
            nodeData?.let {
                getChildAt(i).layout(nodeData.left, nodeData.top, nodeData.right, nodeData.bottom)
            }
        }
    }

    class NodePosData(var left: Int = 0,
                      var top: Int = 0,
                      var right: Int = 0,
                      var bottom: Int = 0)

}