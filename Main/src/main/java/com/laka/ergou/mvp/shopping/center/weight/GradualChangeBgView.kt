package com.laka.ergou.mvp.shopping.center.weight

import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.common.util.color.ColorUtils
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils


/**
 * @Author:summer
 * @Date:2019/5/6
 * @Description:线性渐变背景墙 view，底部使用贝塞尔曲线绘制曲线，能够配个列表滑动做贝塞尔曲线形变动画
 */
class GradualChangeBgView : View {

    //默认渐变颜色
    private val defaultStartColor: String = "#778998"
    private val defaultEndColor: String = "#5b6a6d"

    //startColor 在底部，endColor 在顶部
    private var mStartColor: String? = defaultStartColor
    private var mEndColor: String? = defaultEndColor
    private lateinit var mPaint: Paint
    //内赛尔曲线内容部分宽高
    private var mContentWidth = 0f
    private var mContentHeight = 0f
    //底部贝塞尔曲线控制点
    private var mQuadX = 0f
    private var mQuadY = 0f
    //右下角轨迹的点
    private var mRightBottomX = 0f
    private var mRightBottomY = 0f
    private var mInitialRightHeight = ScreenUtils.dp2px(170f).toFloat() //右下角初始高度
    //左下角轨迹的点
    private var mLeftBottomX = 0f
    private var mLeftBottomY = 0f
    private var mInitialLeftHeight = ScreenUtils.dp2px(95f).toFloat() //左下角初始高度
    //绘制的内容区的固定高度，当配合列表滑动时，底部圆弧会做形变动画，到mLeftBottomY=0,
    //添加一个 mQuadFixedHeight 高度控制后，底部圆弧形变动画只能做到 mLeftBottomY = mQuadDixedHeight
    private var mQuadFixedHeight = 0
    //整个贝塞尔形变过程中，mLeftBottomY 变化的范围
    private var mLeftBottomSpotMoveDistance = 0
    //整个贝塞尔形变过程中，mRightBottomY 变化的范围
    private var mRightBottomSpotMoveDistance = 0


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun init(attrs: AttributeSet?) {
        mPaint = Paint()
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
        attrs?.let {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.GradualChangeBgView)
            mStartColor = typeArray.getString(R.styleable.GradualChangeBgView_startColor)
            mEndColor = typeArray.getString(R.styleable.GradualChangeBgView_endColor)
            if (TextUtils.isEmpty(mStartColor)) mStartColor = defaultStartColor
            if (TextUtils.isEmpty(mEndColor)) mEndColor = defaultEndColor
        }
        //视图树监听
        initEvent()
    }

    private fun initEvent() {
        this.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //本来放在 onMeasure 方法内部进行宽高计算的，但是由于后面设置了属性动画，从而导致做动画的过程中该view不断重新测量
                //导致数据又变回初始值，这不是我们想要的
                mContentWidth = measuredWidth.toFloat()
                mQuadX = mContentWidth / 2f
                mRightBottomX = mContentWidth
                this@GradualChangeBgView.viewTreeObserver.removeOnGlobalLayoutListener(this)

            }
        })
        mContentHeight = mInitialRightHeight
        mQuadY = mInitialRightHeight
        mRightBottomY = mInitialRightHeight
        mLeftBottomX = 0f
        mLeftBottomY = mInitialLeftHeight
    }

    fun getLeftBottomY(): Float {
        return mLeftBottomY
    }

    fun setLeftBottomY(leftBottomY: Float) {
        mInitialLeftHeight = leftBottomY
        mLeftBottomY = mInitialLeftHeight
    }

    fun getRightBottomY(): Float {
        return mRightBottomY
    }

    fun setRightBottomY(rightBottomY: Float) {
        mInitialRightHeight = rightBottomY
        mRightBottomY = mInitialRightHeight
        mContentHeight = mRightBottomY
        mQuadY = mRightBottomY
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val startX = BigDecimalUtils.divi("$mContentWidth", "2").toFloat()
        val startY = mInitialRightHeight
        val endX = startX
        val endY = 0f
        val shader = LinearGradient(startX, startY, endX, endY, Color.parseColor(mStartColor), Color.parseColor(mEndColor), Shader.TileMode.CLAMP)
        mPaint.shader = shader

        val path = Path()
        path.moveTo(mContentWidth, mRightBottomY)
        path.lineTo(mContentWidth, 0f)
        path.lineTo(0f, 0f)
        path.lineTo(0f, mLeftBottomY)
        path.quadTo(mQuadX, mQuadY, mRightBottomX, mRightBottomY)
        canvas?.drawPath(path, mPaint)
    }

    fun setStartColor(startColor: Int) {
        setStartColor(ColorUtils.changeColor2String(context, startColor))
    }

    fun setStartColor(color: String) {
        mStartColor = if (TextUtils.isEmpty(color)) {
            defaultStartColor
        } else {
            color
        }
        invalidate()
    }

    fun setEndColor(endColor: Int) {
        setEndColor(ColorUtils.changeColor2String(context, endColor))
    }

    fun setEndColor(color: String) {
        mEndColor = if (TextUtils.isEmpty(color)) {
            defaultEndColor
        } else {
            color
        }
        invalidate()
    }

    fun setQuadFiexdHeight(height: Int) {
        this.mQuadFixedHeight = height
        mLeftBottomSpotMoveDistance = (mInitialLeftHeight - mQuadFixedHeight).toInt()
        mRightBottomSpotMoveDistance = (mInitialRightHeight - mQuadFixedHeight).toInt()
        invalidate()
    }

    /**
     * 配合 协调者布局 进行形变动画，也就是整个页面往上推的时候，背景图片做回收动画，直到收回到外层页面的 titleBar 里面
     * 这里的算法大概是：mLeftBottomY 、mQuadY、mRightBottomY 等三个值随着列表上划慢慢变小，直到 mLeftBottomY==mQuadY==mRightBottomY==mTitleBar.height()
     * 有了初始值和最终目标值，加上参数  verticalOffest ，就很容易计算出变化过程中这三个点的值了
     * */
    fun setScrollerData(verticalOffest: Int) {
        val temp = (mInitialRightHeight + verticalOffest)
        //竖直范围限制
        if (temp > mInitialRightHeight && mRightBottomY >= mInitialRightHeight) {
            return
        }
        if (temp < mQuadFixedHeight && mRightBottomY <= mQuadFixedHeight) {
            return
        }
        //下拉刷新时，协调者布局会一直回调 verticalOffest=0 ，这里做一个拦截
        if (mLeftBottomY >= mInitialLeftHeight && verticalOffest == 0) {
            return
        }
        //滑动范围参数判断（0不能作为除数）
        if (mLeftBottomSpotMoveDistance <= 0 || mRightBottomSpotMoveDistance <= 0) {
            return
        }
        //计算贝塞尔曲线的几个控制点，目前的逻辑是，左边滑动到顶部，右边也要相应的滑动到顶部
        mRightBottomY = (mInitialRightHeight + verticalOffest)
        mQuadY = mRightBottomY
        mLeftBottomY = mInitialLeftHeight + (verticalOffest * (mLeftBottomSpotMoveDistance / mRightBottomSpotMoveDistance.toFloat()))
        //越界处理
        if (mRightBottomY > mInitialRightHeight) {
            mRightBottomY = mInitialRightHeight
        }
        //当右下角滑到到顶部，则左下角不用在计算，直接也回到顶部（因为浮点型计算可能存在误差）
        if (mRightBottomY < mQuadFixedHeight) {
            mRightBottomY = mQuadFixedHeight.toFloat()
            mLeftBottomY = mQuadFixedHeight.toFloat()
            mQuadY = mRightBottomY
            LogUtils.info("invalidate------：leftBottomY=$mLeftBottomY------rightBottomY=$mRightBottomY---------quadY=$mQuadY-------verticalOffest=$verticalOffest")
            invalidate()
            return
        }
        if (mLeftBottomY > mInitialLeftHeight) {
            mLeftBottomY = mInitialLeftHeight
        }
        //当左下角已经滑动到顶部，右下角不用计算，直接也回到顶部（因为浮点型计算可能存在误差）
        if (mLeftBottomY < mQuadFixedHeight) {
            mLeftBottomY = mQuadFixedHeight.toFloat()
            mRightBottomY = mQuadFixedHeight.toFloat()
            mQuadY = mRightBottomY
            LogUtils.info("invalidate------：leftBottomY=$mLeftBottomY------rightBottomY=$mRightBottomY---------quadY=$mQuadY-------verticalOffest$verticalOffest")
            invalidate()
            return
        }
        LogUtils.info("invalidate------：leftBottomY=$mLeftBottomY------rightBottomY=$mRightBottomY---------quadY=$mQuadY-------verticalOffest$verticalOffest")
        invalidate()
    }

    /**
     * 配合 refreshLayout 进行形变动画，该方法和 setScrollerData() 会有一定冲突问题，所以如果要修改这个方法，则需要在 setScrollerData() 中是适当的拦截操作
     * 原因是因为当 refreshLayout 下拉刷新动作时，外层协调者布局仍然会继续走滑动的回调方法，回调的 verticaloffest==0，就会一直触发 setScrollerData()
     * 为了避免起冲突，做好就是做好拦截，
     * 当前方法是随着 refreshLayout 下滑做贝塞尔形变的，算法很简单
     * */
    fun deformForRefreshLayout(verticalY: Int) {
        // 方案二：起点、控制点、终点都变化，起点的变化速率是 控制点和终点的两倍
        var nextLeftY = mInitialLeftHeight + verticalY + verticalY / 5
        var nextRightY = mInitialRightHeight + verticalY / 2
        if (nextLeftY >= nextRightY) {
            nextRightY = nextLeftY
        }
        mQuadY = nextRightY
        mLeftBottomY = nextLeftY
        mRightBottomY = nextRightY
        invalidate()


//        mLeftBottomY = mInitialLeftHeight + verticalY
//        mQuadY = mInitialRightHeight + verticalY
//        mRightBottomY = mQuadY
//        invalidate()

    }

}