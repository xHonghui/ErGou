package com.laka.ergou.common.widget.flow

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.TextView
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R

/**
 * @Author:summer
 * @Date:2019/7/24
 * @Description:流布局节点控件
 */
class FlowNode : TextView {

    private lateinit var mPaint: Paint
    private var mRadius = ScreenUtils.dp2px(3f).toFloat()
    @ColorInt
    private var mBgColor: Int = ContextCompat.getColor(context, R.color.color_main)

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        mPaint = Paint()
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
        mPaint.color = mBgColor
        attrs?.let {
            val typeArray = context.obtainStyledAttributes(it, R.styleable.FlowNode)
            mBgColor = typeArray.getColor(R.styleable.FlowNode_bgColor, ContextCompat.getColor(context, R.color.color_main))
            mRadius = typeArray.getDimension(R.styleable.FlowNode_radius, mRadius)
            mPaint.color = mBgColor
        }
    }

    fun setRadius(radius: Float) {
        this.mRadius = radius
    }

    fun setBgColor(res: Int) {
        mBgColor = res
        mPaint.color = mBgColor
    }

    override fun onDraw(canvas: Canvas?) {
        val rectF = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
        canvas?.drawRoundRect(rectF, mRadius, mRadius, mPaint)
        super.onDraw(canvas)
    }


}