package com.laka.ergou.common.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent


/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:
 */
class NoScrollViewPager : ViewPager {

    private var mEnableScroll = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun setEnableScroll(enable: Boolean) {
        this.mEnableScroll = enable
    }

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        return if (!mEnableScroll)
            false
        else
            super.onTouchEvent(arg0)
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return if (!mEnableScroll)
            false
        else
            super.onInterceptTouchEvent(arg0)
    }


}