package com.laka.ergou.mvp.circle.weight

import android.content.Context
import android.graphics.Typeface
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

class MyPagerTitleView(context: Context) : ColorTransitionPagerTitleView(context) {
    private var mMinScale = 0.75f
     var isSelectBold = false
     var isUnSelectBold = false
    override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
        super.onEnter(index, totalCount, enterPercent, leftToRight)    // 实现颜色渐变
        scaleX = mMinScale + (1.0f - mMinScale) * enterPercent
        scaleY = mMinScale + (1.0f - mMinScale) * enterPercent
        if (isSelectBold) {
            setTypeface(Typeface.DEFAULT_BOLD)
        }
    }

    override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {
        super.onLeave(index, totalCount, leavePercent, leftToRight)    // 实现颜色渐变
        scaleX = 1.0f + (mMinScale - 1.0f) * leavePercent
        scaleY = 1.0f + (mMinScale - 1.0f) * leavePercent
        if (!isUnSelectBold){
            setTypeface(Typeface.DEFAULT)
        }
    }

    fun getMinScale(): Float {
        return mMinScale
    }

    fun setMinScale(minScale: Float) {
        mMinScale = minScale
    }

}