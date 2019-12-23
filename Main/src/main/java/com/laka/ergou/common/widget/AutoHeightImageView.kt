package com.laka.ergou.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * @Author:summer
 * @Date:2019/1/18
 * @Description:
 */
class AutoHeightImageView : ImageView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val drawable = drawable
        if (drawable != null) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = Math.ceil(((width * drawable.intrinsicHeight) / drawable.intrinsicWidth).toDouble()).toInt()
            setMeasuredDimension(width, height)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}