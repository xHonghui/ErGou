package com.laka.ergou.mvp.shop.weight

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * @Author:summer
 * @Date:2019/1/9
 * @Description:
 */
class CustomViewPager : ViewPager {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        mOnCustomTouchListener?.invoke(ev?.action)
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP || ev?.action == MotionEvent.ACTION_CANCEL) {
            mOnCustomTouchListener?.invoke(ev?.action)
        }
        return super.onTouchEvent(ev)
    }

    private var mOnCustomTouchListener: ((action: Int?) -> Unit)? = null

    fun setOnCustomTouchListener(onCustomTouchListener: ((action: Int?) -> Unit)?) {
        this.mOnCustomTouchListener = onCustomTouchListener
    }

}