package com.laka.ergou.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * @Author:summer
 * @Date:2019/3/29
 * @Description:跑马灯
 */
class MarqueeTextView : TextView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun isFocused(): Boolean {
        return true
    }
}