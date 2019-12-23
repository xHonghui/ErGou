package com.laka.ergou.common.util.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R

/**
 * @Author:summer
 * @Date:2019/4/24
 * @Description:viewpager 公共指示器
 */
class CommonIndicator : RelativeLayout {

    private var mRadius = ScreenUtils.dp2px(3f)
    private var mLeftMargin = ScreenUtils.dp2px(5f)
    private var mIvRedSpot: ImageView? = null
    private var mDataSize: Int = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun initIndicator(size: Int) {
        if (size > 0) {
            mDataSize = size
            val llayout = LinearLayout(context)
            val llayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            for (i in 0 until size) {
                val ivSpot = ImageView(context)
                ivSpot.setImageResource(R.drawable.spot_circlt_gray)
                val layoutParams = LinearLayout.LayoutParams(2 * mRadius, 2 * mRadius)
                layoutParams.leftMargin = mLeftMargin
                if (i == 0) {
                    layoutParams.leftMargin = 0
                }
                llayout.addView(ivSpot, layoutParams)
            }
            addView(llayout, llayoutParams)
            mIvRedSpot = ImageView(context)
            mIvRedSpot?.setImageResource(R.drawable.spot_circlt_theme_color)
            val redSpotLayoutParams = RelativeLayout.LayoutParams(2 * mRadius, 2 * mRadius)
            addView(mIvRedSpot, redSpotLayoutParams)
        }
    }

    /**
     * 设置 indicator 的滑动偏移量
     * */
    fun setScrollData(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (mDataSize > 1) { //无数据或者数据个数为1，都不滑动红点
            val newPos = position % mDataSize
            val redLeftMargin = (positionOffset + newPos) * (2 * mRadius + mLeftMargin)
            val layoutParams = mIvRedSpot?.layoutParams as? RelativeLayout.LayoutParams
            layoutParams?.let {
                it.leftMargin = redLeftMargin.toInt()
                mIvRedSpot?.layoutParams = it
            }
        }
    }

}