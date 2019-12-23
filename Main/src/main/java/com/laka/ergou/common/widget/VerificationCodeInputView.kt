package com.laka.ergou.common.widget

import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import com.laka.androidlib.R
import com.laka.androidlib.util.KeyboardHelper
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.screen.ScreenUtils

/**
 * @Author:summer
 * @Date:2019/4/28
 * @Description:验证码输入view
 */
class VerificationCodeInputView : View {

    //默认值
    companion object {
        private val NUMBER_TEXT_COLOR = "#525252"
        private val BOTTOM_INDICATOR_COLOR = "#c3c3c3"
        private val MIN_VIEW_WIDTH = ScreenUtils.dp2px(50f)
        private val CRITICAL = 5
    }

    //paint
    private var mPaint = Paint()

    //当前控件宽高
    private var mWidth = 0
    private var mHeight = 0

    //每个节点宽高
    private var mNodeWidth = 0
    private var mNodeHeight = 0

    //间隙宽度
    private var mSpaceWidthPrecent = 0.059
    private var mSpaceWidth = 0

    //颜色
    private var mNumberTextColor: String? = ""
    private var mBottomIndicatorColor: String? = ""

    //节点数量
    private var mNodeCount = 4
    //indicator高度
    private var mIndicatorHeight = ScreenUtils.dp2px(1f)

    constructor(context: Context?) : super(context) {
        initAttrStyle(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initAttrStyle(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrStyle(attrs)
    }

    private fun initAttrStyle(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeInputView)
            mNumberTextColor = typedArray.getString(R.styleable.VerificationCodeInputView_number_text_color)
            mBottomIndicatorColor = typedArray.getString(R.styleable.VerificationCodeInputView_bottom_indicator_color)
            mNodeCount = typedArray.getInt(R.styleable.VerificationCodeInputView_node_count, mNodeCount)
        }
        if (TextUtils.isEmpty(mNumberTextColor)) {
            mNumberTextColor = NUMBER_TEXT_COLOR
        }
        if (TextUtils.isEmpty(mBottomIndicatorColor)) {
            mBottomIndicatorColor = BOTTOM_INDICATOR_COLOR
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = resolveSize(MIN_VIEW_WIDTH, widthMeasureSpec)
        mSpaceWidth = (mSpaceWidthPrecent * mWidth).toInt()
        //node 的宽高相等
        mNodeWidth = (mWidth - (mNodeCount - 1) * mSpaceWidth) / mNodeCount
        mHeight = resolveSize(mNodeWidth, heightMeasureSpec)
        mNodeHeight = mHeight
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.parseColor(mBottomIndicatorColor)
        mPaint.strokeWidth = mIndicatorHeight.toFloat()
        mPaint.isAntiAlias = true
        //绘制下划线
        for (i in 0 until mNodeCount) {
            canvas?.drawLine((i * (mNodeWidth + mSpaceWidth)).toFloat(), mNodeHeight.toFloat(), ((i + 1) * mNodeWidth + i * mSpaceWidth).toFloat(), mNodeHeight.toFloat(), mPaint)
        }

        //1、监听键盘输入

        //2、绘制光标


        drawText(canvas)
    }

    private fun drawText(canvas: Canvas?) {
        mPaint.color = Color.parseColor(mNumberTextColor)
        mPaint.textSize = (mNodeWidth / 2).toFloat()
        mPaint.typeface = Typeface.DEFAULT_BOLD
        val rect = Rect()
        for (i in 0 until mNodeCount) {
            val text = "${i + 1}"
            mPaint.getTextBounds(text, 0, text.length, rect)
            val txtHeight = rect.height()
            val txtWidth = rect.width()
            val txtLeft = (mNodeWidth + mSpaceWidth) * i + mNodeWidth / 2 - txtWidth / 2
            val txtBottom = mHeight - (mHeight - txtHeight) / 2
            canvas?.drawText(text, txtLeft.toFloat(), txtBottom.toFloat(), mPaint)
        }
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DEL -> {

            }
            KeyEvent.KEYCODE_0,
            KeyEvent.KEYCODE_1,
            KeyEvent.KEYCODE_2,
            KeyEvent.KEYCODE_3,
            KeyEvent.KEYCODE_4,
            KeyEvent.KEYCODE_5,
            KeyEvent.KEYCODE_6,
            KeyEvent.KEYCODE_7,
            KeyEvent.KEYCODE_8,
            KeyEvent.KEYCODE_9 -> {

            }
        }
        LogUtils.info("keycode----:${event?.characters}")
        return super.onKeyDown(keyCode, event)
    }

    override fun onFocusChanged(gainFocus: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect)
        if (gainFocus) {
            KeyboardHelper.openKeyBoard(context, this)
        } else {
            KeyboardHelper.hideKeyBoard(context, this)
        }
    }

    private var mStartX = 0
    private var mStartY = 0
    private var mIsClick = true

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                LogUtils.info("keycode----:down")
                mStartX = event.x.toInt()
                mStartY = event.y.toInt()
                mIsClick = true
            }
            MotionEvent.ACTION_MOVE -> {
                LogUtils.info("keycode----:move")
                val moveX = event.x.toInt()
                val moveY = event.y.toInt()
                val dis = Math.sqrt(Math.pow((moveX - mStartX).toDouble(), 2.0) + Math.pow((moveY - mStartY).toDouble(), 2.0))
                if (dis > CRITICAL) {
                    mIsClick = false
                }
            }
            MotionEvent.ACTION_UP -> {
                if (mIsClick) {
                    LogUtils.info("keycode----:弹起键盘")
                    KeyboardHelper.openKeyBoard(context, this)
                }
                LogUtils.info("keycode----:up")
            }
        }
        return true
    }


}