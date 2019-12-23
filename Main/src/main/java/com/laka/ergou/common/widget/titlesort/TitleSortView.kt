package com.laka.ergou.common.widget.titlesort

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.laka.androidlib.util.ResourceUtils
import com.laka.ergou.R

/**
 * @Author:Rayman
 * @Date:2019/1/11
 * @Description:
 */
class TitleSortView : ITitleSortView, View {


    /**
     * description:参数设置
     **/
    private lateinit var mTextPaint: Paint
    private lateinit var mDrawablePaint: Paint
    private var textColor = ResourceUtils.getColor(R.color.color_font)
    private var drawableColor = ResourceUtils.getColor(R.color.color_main)
    private var textSize = 14f

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initViews(context, attrs)
        initProperty(context, attrs)
    }

    override fun initViews(context: Context?, attrs: AttributeSet?) {
        mTextPaint = Paint()
        mTextPaint.textSize = textSize
        mTextPaint.color = textColor

        mDrawablePaint = Paint()
        mDrawablePaint.color = drawableColor
    }

    override fun initProperty(context: Context?, attrs: AttributeSet?) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun updateData(data: String?) {
    }

    override fun setTitle(title: String): ITitleSortView {
        return this
    }

    override fun setTitleColor(redId: Int): ITitleSortView {
        return this
    }

    override fun setSortColor(resId: Int): ITitleSortView {
        return this
    }
}