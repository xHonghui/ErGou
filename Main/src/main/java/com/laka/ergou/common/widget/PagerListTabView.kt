package com.laka.ergou.common.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.StringUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:自定义tab框，配置viewPager使用（我的订单、我的战友、我的消息等）
 */
class PagerListTabView : View {

    companion object {
        const val LEFT_ITEM = 0x100
        const val RIGHT_ITEM = 0x110
        //默认文本大小
        val DEFAULT_TEXT_SIZE = ScreenUtils.sp2px(14f).toFloat()
        //默认圆角半径大小
        val DEFAULT_RADIUS: Float = ScreenUtils.dp2px(5f).toFloat()
    }

    private lateinit var mPaint: Paint
    private var mItemClickListener: ((Int) -> Unit)? = null
    private var mLeftTabStr: String? = "左边"
    private var mRightTabStr: String? = "右边"
    //文本大小
    private var mTxtSize: Float = DEFAULT_TEXT_SIZE
    //文本竖直方向间隙
    private var mVerticalPadding: Float = ScreenUtils.dp2px(10f).toFloat()
    //属性动画
    private val animator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(240)
    //位置
    private var mSliderLeft: Int = 0
    private var mSliderTop: Int = 0
    private var mSliderRight: Int = 0
    private var mSliderBottom: Int = 0
    private var mSliderRangeLeft: Int = 0
    private var mSliderRangeTop: Int = 0
    private var mSliderRangeRight: Int = 0
    private var mSliderRangeBottom: Int = 0
    //滑块可滑动的范围（也可表示为滑块的宽度）
    private var mSliderRange: Int = 0
    private var mRightRadius: Float = 0f
    private var mLeftRadius: Float = 0f
    //当前状态
    private var mCurrentState: Int = LEFT_ITEM
    //是否在执行动画中
    private var mIsAnimatoring: Boolean = false
    //当前控件的宽高
    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs)
        initEvent()
    }

    private fun initView(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerListTab)
            val leftStr = typedArray.getString(R.styleable.PagerListTab_tab_left_txt)
            val rightStr = typedArray.getString(R.styleable.PagerListTab_tab_right_txt)
            if (StringUtils.isNotEmpty(leftStr)) mLeftTabStr = leftStr
            if (StringUtils.isNotEmpty(rightStr)) mRightTabStr = rightStr
            mTxtSize = typedArray.getDimension(R.styleable.PagerListTab_tab_txt_size, DEFAULT_TEXT_SIZE)
        }
        mPaint = Paint()
        mPaint.style = Paint.Style.FILL
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.strokeWidth = ScreenUtils.dp2px(1f).toFloat()
        mPaint.color = ContextCompat.getColor(context, R.color.color_main)
        mPaint.textSize = mTxtSize
        mPaint.isAntiAlias = true
        initAnimator()
    }

    private fun initAnimator() {
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener {
            val percent = it.animatedValue as? Float
            percent?.let {
                setSliderPosAndRadius((it * mSliderRange).toInt(), percent * DEFAULT_RADIUS)
            }
        }

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                mIsAnimatoring = false
                mCurrentState = if (mCurrentState == LEFT_ITEM) RIGHT_ITEM else LEFT_ITEM
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })
    }

    private fun initEvent() {

    }

    private fun setSliderPosAndRadius(left: Int, radius: Float) {
        if (mCurrentState == LEFT_ITEM) {
            mRightRadius = radius
            mLeftRadius = 0f
            mSliderLeft = mSliderRangeLeft + left
            mSliderRight = mSliderLeft + mSliderRange
        } else {
            mRightRadius = 0f
            mLeftRadius = radius
            mSliderLeft = mSliderRangeLeft + mSliderRange - left
            mSliderRight = mSliderLeft + mSliderRange
        }
        LogUtils.info("animator--------mRightRadius=$mRightRadius---mSliderLeft=$mSliderLeft---mSliderRight=$mSliderRight")
        LogUtils.info("animator--------mLeftRadius=$mLeftRadius---mSliderLeft=$mSliderLeft---mSliderRight=$mSliderRight")
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewWidth = measuredWidth
        mViewHeight = (mTxtSize + mVerticalPadding * 2 + paddingTop + paddingBottom).toInt()
        setMeasuredDimension(mViewWidth, mViewHeight)

        mSliderLeft = paddingLeft
        mSliderTop = paddingTop
        mSliderRight = paddingLeft + (mViewWidth - paddingLeft - paddingRight) / 2
        mSliderBottom = mViewHeight - paddingBottom

        mSliderRange = (mViewWidth - paddingLeft - paddingRight) / 2
        mSliderRangeLeft = paddingLeft
        mSliderRangeTop = paddingTop
        mSliderRangeRight = mViewWidth - paddingRight
        mSliderRangeBottom = mViewHeight - paddingBottom
    }

    override fun onDraw(canvas: Canvas?) {
        onDrawBackground(canvas)
        onDrawText(canvas)
    }

    private fun onDrawText(canvas: Canvas?) {
        canvas?.let {
            val top = mPaint.fontMetrics.top
            val bottom = mPaint.fontMetrics.bottom
            val textHeight = Math.abs(top) + Math.abs(bottom)
            val leftTextX = ((2 * mSliderRangeLeft + mSliderRange) / 2).toFloat()
            val leftTextY = (mSliderTop + mSliderBottom) / 2 + textHeight / 2 - bottom
            val rightTextX = ((mSliderRangeRight + (mSliderRangeRight - mSliderRange)) / 2).toFloat()
            val rightTextY = (mSliderTop + mSliderBottom) / 2 + textHeight / 2 - bottom

            drawLeftText(it, ContextCompat.getColor(context, R.color.white),
                    mSliderLeft, mSliderLeft + mSliderRange,
                    leftTextX, leftTextY,
                    mLeftTabStr)
            drawLeftText(it, ContextCompat.getColor(context, R.color.color_main),
                    mSliderRangeLeft, mSliderLeft,
                    leftTextX, leftTextY,
                    mLeftTabStr)
            drawLeftText(it, ContextCompat.getColor(context, R.color.white),
                    mSliderLeft, mSliderLeft + mSliderRange,
                    rightTextX, rightTextY,
                    mRightTabStr)
            drawLeftText(it, ContextCompat.getColor(context, R.color.color_main),
                    mSliderLeft + mSliderRange, mSliderRangeRight,
                    rightTextX, rightTextY,
                    mRightTabStr)
        }
    }

    private fun drawLeftText(canvas: Canvas, @ColorInt color: Int,
                             clipStartX: Int, clipEndX: Int,
                             textX: Float, textY: Float,
                             text: String?) {
        mPaint.color = color
        canvas.save()
        canvas.clipRect(clipStartX.toFloat(), mSliderTop.toFloat(), clipEndX.toFloat(), mSliderBottom.toFloat())
        canvas?.drawText(text, textX, textY, mPaint)
        canvas.restore()
    }

    private fun onDrawBackground(canvas: Canvas?) {
        canvas?.let {
            mPaint.style = Paint.Style.STROKE
            mPaint.color = ContextCompat.getColor(context, R.color.color_main)
            val rectFbg = RectF(mSliderRangeLeft.toFloat(), mSliderRangeTop.toFloat(),
                    mSliderRangeRight.toFloat(), mSliderRangeBottom.toFloat())
            canvas.drawRoundRect(rectFbg, DEFAULT_RADIUS, DEFAULT_RADIUS, mPaint)

            mPaint.style = Paint.Style.FILL
            val path = createPath()
            canvas.drawPath(path, mPaint)
        }
    }

    /**
     * 在path中添加圆弧，使用arcTo()，不适用 addArc()，如果是path对象的首个位置信息，可以使用addArc()
     * */
    private fun createPath(): Path {
        val path = Path()
        if (mCurrentState == LEFT_ITEM) {
            path.moveTo(mSliderLeft + (DEFAULT_RADIUS - mRightRadius), mSliderTop.toFloat())
            path.lineTo(mSliderLeft + mSliderRange - mRightRadius, mSliderTop.toFloat())

            val rectF1 = RectF(mSliderLeft + mSliderRange - mRightRadius * 2,
                    mSliderTop.toFloat(),
                    mSliderRight.toFloat(),
                    mSliderTop + mRightRadius * 2)
            path.arcTo(rectF1, 270f, 90f)
            path.lineTo(mSliderLeft + mSliderRange.toFloat(), mSliderBottom - mRightRadius)

            val rectF2 = RectF(mSliderLeft + mSliderRange - mRightRadius * 2, mSliderBottom - mRightRadius * 2,
                    mSliderLeft + mSliderRange.toFloat(), mSliderBottom.toFloat())
            path.arcTo(rectF2, 0f, 90f)
            path.lineTo(mSliderLeft + (DEFAULT_RADIUS - mRightRadius), mSliderBottom.toFloat())

            val rectF3 = RectF(mSliderLeft.toFloat(), mSliderBottom - (DEFAULT_RADIUS - mRightRadius) * 2,
                    mSliderLeft + (DEFAULT_RADIUS - mRightRadius) * 2, mSliderBottom.toFloat())

            path.arcTo(rectF3, 90f, 90f)
            path.lineTo(mSliderLeft.toFloat(), mSliderTop + (DEFAULT_RADIUS - mRightRadius))

            val rectF4 = RectF(mSliderLeft.toFloat(), mSliderTop.toFloat(), mSliderLeft + (DEFAULT_RADIUS - mRightRadius) * 2, mSliderTop + (DEFAULT_RADIUS - mRightRadius) * 2)
            path.arcTo(rectF4, 180f, 90f)
            path.lineTo(mSliderLeft + (DEFAULT_RADIUS - mRightRadius), mSliderTop.toFloat())
            path.close()

        } else {

            val rectF1 = RectF(mSliderLeft.toFloat(), mSliderTop.toFloat(), mSliderLeft + mLeftRadius * 2, mSliderTop + mLeftRadius * 2)
            path.moveTo(mSliderLeft.toFloat(), mSliderTop + mLeftRadius)
            path.arcTo(rectF1, 180f, 90f)
            path.lineTo(mSliderLeft + mSliderRange - (DEFAULT_RADIUS - mLeftRadius), mSliderTop.toFloat())

            val rectF2 = RectF(mSliderLeft + mSliderRange - (DEFAULT_RADIUS - mLeftRadius) * 2,
                    mSliderTop.toFloat(), mSliderLeft + mSliderRange.toFloat(),
                    mSliderTop + (DEFAULT_RADIUS - mLeftRadius) * 2)

            path.arcTo(rectF2, 270f, 90f)
            path.lineTo(mSliderLeft + mSliderRange.toFloat(), mSliderBottom - DEFAULT_RADIUS + mLeftRadius)

            val rectF3 = RectF(mSliderLeft + mSliderRange - (DEFAULT_RADIUS - mLeftRadius) * 2,
                    mSliderBottom - (DEFAULT_RADIUS - mLeftRadius) * 2,
                    mSliderLeft + mSliderRange.toFloat(), mSliderBottom.toFloat())
            path.arcTo(rectF3, 0f, 90f)
            path.lineTo(mSliderLeft + mLeftRadius, mSliderBottom.toFloat())

            val rectF4 = RectF(mSliderLeft.toFloat(), mSliderBottom - mLeftRadius * 2,
                    mSliderLeft + mLeftRadius * 2, mSliderBottom.toFloat())
            path.arcTo(rectF4, 90f, 90f)
            path.lineTo(mSliderLeft.toFloat(), mSliderTop + mLeftRadius)
            path.close()
        }
        return path
    }

    private var mStartX: Float = 0f
    private var mStartY: Float = 0f
    private var mEndX: Float = 0f
    private var mEndY: Float = 0f
    private var mMoveDistance = ScreenUtils.dp2px(5f)
    private var mIsMoveing: Boolean = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mIsMoveing = false
                mStartX = event.x
                mStartY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                mEndX = event.x
                mEndY = event.y

                val dis = Math.sqrt(((mEndX - mStartX) * (mEndX - mStartX) + (mEndY - mStartY) * (mEndY - mStartY)).toDouble())
                if (dis >= mMoveDistance) {
                    mIsMoveing = true
                }
            }
            MotionEvent.ACTION_UP -> {
                val upX = event.x
                val upY = event.y
                val endDis = Math.sqrt(((upX - mStartX) * (upX - mStartX) + (upY - mStartY) * (upY - mStartY)).toDouble())
                if (!mIsMoveing && endDis < mMoveDistance) {
                    //todo 是点击事件，然后根据按下的点判断点击范围
                    handleClickEvent()
                }
            }
        }
        return true
    }

    private fun handleClickEvent() {
        if (mStartX > mSliderRangeLeft && mStartX <= mSliderRangeLeft + mSliderRange
                && mStartY > mSliderTop && mStartY <= mSliderBottom) {
            // 左边按钮点击
            if (handleLeftButtonCLick()) return
        } else if (mStartX > mSliderRangeLeft + mSliderRange && mStartX <= mSliderRangeRight
                && mStartY > mSliderTop && mStartY <= mSliderBottom) {
            //右边按钮点击
            if (handleRightButtonClick()) return
        }
    }

    //右按钮点击
    private fun handleRightButtonClick(): Boolean {
        if (mCurrentState == RIGHT_ITEM) {
            return true
        }
        if (mIsAnimatoring) {
            return true
        }
        animator.start()
        mIsAnimatoring = true
        mItemClickListener?.invoke(RIGHT_ITEM)
        return false
    }

    //左按钮点击
    private fun handleLeftButtonCLick(): Boolean {
        if (mCurrentState == LEFT_ITEM) {
            return true
        }
        if (mIsAnimatoring) {
            return true
        }
        animator.start()
        mIsAnimatoring = true
        mItemClickListener?.invoke(LEFT_ITEM)
        return false
    }

    fun setLeftTabText(text: String) {
        this.mLeftTabStr = text
        invalidate()
    }

    fun setRightTabStr(text: String) {
        this.mRightTabStr = text
        invalidate()
    }

    fun setItemClickListener(listener: ((Int) -> Unit)) {
        this.mItemClickListener = listener
    }

    fun selectTabItem(type: Int) {
        when (type) {
            LEFT_ITEM -> {
                handleLeftButtonCLick()
            }
            RIGHT_ITEM -> {
                handleRightButtonClick()
            }
            else -> LogUtils.error("Out of Page Range")
        }
    }

    fun switchTab() {
        if (mCurrentState == LEFT_ITEM) {
            handleRightButtonClick()
        } else if (mCurrentState == RIGHT_ITEM) {
            handleLeftButtonCLick()
        }
    }


}