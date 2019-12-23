package com.laka.ergou.common.widget.refresh

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PointF
import android.graphics.drawable.AnimationDrawable
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.shopping.center.weight.GradualChangeBgView

/**
 * @Author:summer
 * @Date:2019/5/9
 * @Description:二购首页定制刷新布局
 */
class ErGouHomeRefreshLayout : LinearLayout {

    private var mCurrentState = IS_IDEA

    companion object {
        const val IS_REFRESHING = 0x1001 //刷新中
        const val IS_RELEASE_ANIMING = 0X1002 //释放动画执行中
        const val IS_RECOVERY_ANIMING = 0x1003 //恢复动画执行中
        const val IS_IDEA = 0X1004 //空闲状态

        //刷新完成头部透明动画的执行时间
        const val RECOVERY_ANIMATOR_TIME: Long = 400
        //滑动到最底部释放，动画执行的时间
        const val RELEASE_ANIMATOR_TIME: Long = 260
        //logo 缩放动画的初始值
        const val SCALE_RETA_FOR_LOGO = 1.0f
        //logo 缩放动画的最大值
        const val SCALE_MAX_FOR_LOG0 = 1.5f
    }

    private lateinit var mHeaderView: View
    private lateinit var mIvLoading: ImageView
    private var mAnimatorDrawable: AnimationDrawable? = null
    //是否可刷新，为true时，则拦截事件进行刷新
    private var mEnableRefresh = false
    //按下坐标（上一个移动点的坐标）
    private var mLastPoint = PointF(0f, 0f)
    //移动坐标
    private var mMovePoint = PointF(0f, 0f)
    //最小处理距离
    private var mMinHandleDistance = 3f
    //最大处理距离
    private var mMaxHandleDistance = (ScreenUtils.getScreenHeight() * 0.6).toInt()

    //触发刷新的距离
    private var mDistanceRefresh = ScreenUtils.dp2px(80f)
    //拖动距离，设置透明度的比例，这个值最好设置比 mRefreshMaxDistanceRate 小，防止出现动画执行的临界值

    //设置刷新headerView 透明度为1时对应 refresh 滑动的距离
    private var mDistanceHeaderViewTransparent = ScreenUtils.dp2px(80f)

    //refreshLayout滑动速度与手势滑动速度的比值
    private var mSpeedRate = 0.2f
    //最小速率，防止速度为0后无法恢复滑动
    private var mMinSpeedRate = 0.1f
    //释放动画
    private lateinit var mAnimator: ValueAnimator
    //正在刷新中...
    private var mIsNeedRefresh = false
    //刷新监听器
    private lateinit var mRefreshListener: ((view: View) -> Unit)
    //下拉滑动距离监听，scrollY：竖直方向滑动的距离，transparency：headerView的透明度
    //目前的主要用在首页刷新控件滑动时，控制外层的view根据滑动距离设置动画
    private lateinit var mRefreshScrollListener: ((view: View, scrollY: Int, transparency: Float) -> Unit)

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        mHeaderView = LayoutInflater.from(context).inflate(R.layout.home_header_refresh_layout, this, false)
        mIvLoading = mHeaderView.findViewById(R.id.iv_loading)
        mIvLoading.setImageResource(R.drawable.anim_ergou_pull_refresh_blackbg)
        mAnimatorDrawable = mIvLoading.drawable as? AnimationDrawable
        mHeaderView.alpha = 0f
        addView(mHeaderView, 0)
    }

    private var mProgressBarWidth: Int = 0
    private var mProgressBarHeight: Int = 0

    private var mInterceptDownPoint: PointF = PointF(0f, 0f)
    private var mInterceptMovePoint: PointF = PointF(0f, 0f)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        LogUtils.info("onInterceptTouchEvent-----mEnableRefresh=$mEnableRefresh")
        when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mInterceptDownPoint.set(ev.x, ev.y)
            }
            MotionEvent.ACTION_MOVE -> {
                mInterceptMovePoint.set(ev.x, ev.y)
                val distanceX = mInterceptMovePoint.x - mInterceptDownPoint.x
                val distanceY = mInterceptMovePoint.y - mInterceptDownPoint.y
                //竖直方向并且滑动距离超过最小限度
                if (Math.abs(distanceX) >= Math.abs(distanceY) || Math.abs(distanceY) < mMinHandleDistance) {
                    LogUtils.info("onInterceptTouchEvent-----不拦截事件1-----mEnableRefresh=$mEnableRefresh")
                    return false //不拦截
                } else {
                    //控件在顶部并且仍然向上滑动，此时不拦截事件，留给内层RecyclerView+协调者布局 滑动
                    if (scrollY == 0 && distanceY < 0) {
                        if (mCurrentState == IS_REFRESHING || mCurrentState == IS_RECOVERY_ANIMING) {
                            //刷新或者刷新完成动画执行中，直接拦截事件，onTouchEvent 不处理事件
                            LogUtils.info("onInterceptTouchEvent-----刷新动画执行中-----mEnableRefresh=$mEnableRefresh")
                            return mEnableRefresh
                        }
                        LogUtils.info("onInterceptTouchEvent-----不拦截事件2-----mEnableRefresh=$mEnableRefresh")
                        return false
                    }
                    //向下滑动，并且 scrollY=0，直接拦截事件
                    if (mEnableRefresh) { //拦截事件
                        mLastPoint.set(mInterceptMovePoint.x, mInterceptMovePoint.y)
                        mCurrentPointId = ev.getPointerId(ev.actionIndex)
                    }
                    LogUtils.info("onInterceptTouchEvent-----向下滑动拦截事件-----mEnableRefresh=$mEnableRefresh")
                    return mEnableRefresh
                }
            }
        }
        LogUtils.info("onInterceptTouchEvent-----super.onInterceptTouchEvent(ev)-----mEnableRefresh=$mEnableRefresh")
        return super.onInterceptTouchEvent(ev)
    }

    private var mCurrentPointId = 0
    private var mInvalidPointId = -1

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (mCurrentState == IS_REFRESHING
                || mCurrentState == IS_RELEASE_ANIMING
                || mCurrentState == IS_RECOVERY_ANIMING) {
            //记录最后一次滑动的坐标，也是需要处理多点触碰时，PointerIndex 越界的问题（某个手指抬起了，mCurrentPointId 却记录下来并且在 ACTION_MOVE 中使用）
            event?.let {
                mLastPoint.set(event.x, event.y)
                mCurrentPointId = event.getPointerId(event.actionIndex)
                when (event.actionMasked) {
                    MotionEvent.ACTION_POINTER_UP -> {
                        handleActionPointerUp(event)
                    }
                    MotionEvent.ACTION_UP -> {
                        mCurrentPointId = mInvalidPointId
                        mLastPoint.set(0f, 0f)
                    }
                }
            }
            return true
        }
        mIsNeedRefresh = false
        LogUtils.info("ergouRefreshLayout---onTouchEvent--action=${event?.actionMasked}")
        when (event?.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN, //非第一个手指按下
            MotionEvent.ACTION_DOWN -> {//第一个手指按下
                //将新按下的手指作为活动手指，并记录下当前活动手机的 pointId
                mCurrentPointId = event.getPointerId(event.actionIndex)
                mLastPoint.set(event.getX(event.findPointerIndex(mCurrentPointId)), event.getY(event.findPointerIndex(mCurrentPointId)))
                LogUtils.info("ergouRefreshLayout---onTouchEvent--down--mDownX=${mLastPoint.x}------mDownY=${mLastPoint.y}-----pointerId=$mCurrentPointId------mEnableRefresh=$mEnableRefresh")
            }
            MotionEvent.ACTION_MOVE -> {
                LogUtils.info("ergouRefreshLayout---onTouchEvent--mCurrentPointId=$mCurrentPointId")
                if (mCurrentPointId == mInvalidPointId) return mEnableRefresh
                mMovePoint.set(event.getX(event.findPointerIndex(mCurrentPointId)), event.getY(event.findPointerIndex(mCurrentPointId)))

                //速率，根据已经滑动的距离来计算将要滑动的速度
                mSpeedRate = (mMaxHandleDistance + scrollY * 3) / mMaxHandleDistance.toFloat()
                mSpeedRate = if (mSpeedRate < mMinSpeedRate) mMinSpeedRate else mSpeedRate
                //设置刷新头部的透明度
                handleHeaderViewTransparent(scrollY)
                LogUtils.info("ergouRefreshLayout---onTouchEvent--mSpeedRate=$mSpeedRate")
                LogUtils.info("ergouRefreshLayout---onTouchEvent--mDownX=${mLastPoint.x}------mDownY=${mLastPoint.y}----mMoveX=${mMovePoint.x}------mMoveY=${mMovePoint.y}-----scrollY=$scrollY------mEnableRefresh=$mEnableRefresh")
                val distanceX = mMovePoint.x - mLastPoint.x
                val distanceY = mMovePoint.y - mLastPoint.y
                LogUtils.info("ergouRefreshLayout---onTouchEvent--distanceX=$distanceX-----distanceY=$distanceY")
                //方向判断
                if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                    updateDownLocation()
                    return mEnableRefresh
                }
                //滑动距离要大于滑动单位
                if (Math.abs(distanceY) < mMinHandleDistance) {
                    return mEnableRefresh
                }
                //边界判断
                //上边界
                if (scrollY - distanceY >= 0) {
                    if (scrollY == 0) {
                        updateDownLocation()
                        return mEnableRefresh
                    }
                    LogUtils.info("ergouRefreshLayout---onTouchEvent---scrollTo-----超出边界，滑动到（0,0）------scrollY=$scrollY------distanceY=$distanceY")
                    scrollTo(0, 0)
                    updateDownLocation()
                    return mEnableRefresh
                }
                //下边界
                if (Math.abs(scrollY) + distanceY >= mMaxHandleDistance) {
                    if (Math.abs(scrollY) == mMaxHandleDistance) {
                        updateDownLocation()
                        return mEnableRefresh
                    }

                    if (mSpeedRate * distanceY < mMaxHandleDistance - Math.abs(scrollY)) {
                        LogUtils.info("ergouRefreshLayout---onTouchEvent---scrollTo-----超出边界，scrollBy----${(mSpeedRate * distanceY).toInt()}")
                        scrollBy(0, -(mSpeedRate * distanceY).toInt())
                    } else {
                        LogUtils.info("ergouRefreshLayout---onTouchEvent---scrollTo-----超出边界，scrollTo----mMaxHandleDistance=$mMaxHandleDistance")
                        scrollTo(0, -mMaxHandleDistance)
                    }
                    updateDownLocation()
                    return mEnableRefresh
                }
                LogUtils.info("ergouRefreshLayout---onTouchEvent--正常滑动scrollBy--------distanceY=$distanceY")
                scrollBy(0, -(distanceY * mSpeedRate).toInt())

                //更新开始位置
                updateDownLocation()
            }
            MotionEvent.ACTION_POINTER_UP -> {//非最后一个手指弹起
                handleActionPointerUp(event)
            }
            MotionEvent.ACTION_UP -> {
                //执行回弹动画，动画执行过程中，不响应任何触摸事件
                LogUtils.info("ergouRefreshLayout---onTouchEvent------Math.abs(scrollY)=${Math.abs(scrollY)}--------mMaxHandleDistance=$mMaxHandleDistance")
                mIsNeedRefresh = Math.abs(scrollY) > mDistanceRefresh && ::mRefreshListener.isInitialized
                handleReleaseAnim()
                mCurrentPointId = mInvalidPointId
            }
        }
        return mEnableRefresh
    }

    /**
     * 处理单个手指抬起
     * */
    private fun handleActionPointerUp(event: MotionEvent) {
        val skipIndex = event.actionIndex
        val count = event.pointerCount
        LogUtils.info("ergouRefreshLayout---onTouchEvent---actionIndex=$skipIndex-------count=$count")
        mCurrentPointId = if (skipIndex == count - 1 && (skipIndex - 1) >= 0) { //最后一个手指抬起，则活动手指变为其前一个
            event.getPointerId(skipIndex - 1)
        } else {
            event.getPointerId(count - 1)
        }
        //将新的活动手指坐标复制mLastPoint
        mLastPoint.set(event.getX(event.findPointerIndex(mCurrentPointId)), event.getY(event.findPointerIndex(mCurrentPointId)))
    }

    /**
     * 释放回弹动画处理
     * */
    private fun handleReleaseAnim() {
        //执行回弹动画中
        mCurrentState = IS_RELEASE_ANIMING

        mAnimator = ValueAnimator.ofInt(Math.abs(scrollY), 0)
        mAnimator.interpolator = DecelerateInterpolator()
        mAnimator.duration = RELEASE_ANIMATOR_TIME
        mAnimator.addUpdateListener {
            val animValue = it.animatedValue as? Int
            animValue?.let {
                scrollTo(0, -animValue) //移动动画
                if (!mIsNeedRefresh) {
                    handleHeaderViewTransparent(animValue) //透明动画
                } else {
                    invokeScrollListener(Math.abs(animValue), handleHeaderViewAlpha(animValue))
                }
            }
        }
        mAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                mCurrentState = if (mIsNeedRefresh) { //做完动画再进行外层监听回调
                    if (::mRefreshListener.isInitialized) {
                        mRefreshListener.invoke(this@ErGouHomeRefreshLayout)
                        IS_REFRESHING
                    } else {
                        IS_IDEA
                    }
                } else {
                    IS_IDEA //恢复空闲状态
                }
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })
        mAnimator.start()

        //手指释放，执行帧动画
        if (mAnimatorDrawable != null) {
            mAnimatorDrawable?.start()
        }
    }

    /**
     * 设置刷新头部控件的透明度
     * */
    private fun handleHeaderViewTransparent(scrollY: Int) {
        val verticalScrollY = Math.abs(scrollY)
        var transpire = handleHeaderViewAlpha(verticalScrollY)
        mHeaderView.alpha = transpire
        invokeScrollListener(verticalScrollY, transpire)

        // refresh logo 缩放动画
        val scale = if ((SCALE_RETA_FOR_LOGO + transpire) > SCALE_MAX_FOR_LOG0) SCALE_MAX_FOR_LOG0 else (SCALE_RETA_FOR_LOGO + transpire)
        if (mProgressBarWidth == 0) mProgressBarWidth = mIvLoading.measuredWidth
        if (mProgressBarHeight == 0) mProgressBarHeight = mIvLoading.measuredHeight
        val layoutParams = mIvLoading.layoutParams
        layoutParams.width = (scale * mProgressBarWidth).toInt()
        layoutParams.height = (scale * mProgressBarHeight).toInt()
        LogUtils.info("refreshLayout")
        mIvLoading.layoutParams = layoutParams
    }

    /**
     * 计算刷新头部view的透明度
     * */
    private fun handleHeaderViewAlpha(scrollY: Int): Float {
        var verticalScrollY = Math.abs(scrollY)
        var transpire = verticalScrollY / mDistanceHeaderViewTransparent.toFloat()
        if (transpire > 1) {
            transpire = 1f
        }
        if (transpire < 0) {
            transpire = 0f
        }
        return transpire
    }

    /**
     * 回调滑动事件
     * */
    private fun invokeScrollListener(verticalScrollY: Int, transpire: Float) {
        if (::mRefreshScrollListener.isInitialized) {
            mRefreshScrollListener.invoke(this, verticalScrollY, transpire)
        }
    }

    /**
     * 完成刷新
     * */
    fun finishRefresh(finishRefresh: Boolean) {
        if (mCurrentState == IS_REFRESHING) {
            this.mIsNeedRefresh = !finishRefresh
            handleFinishRefreshAnim()
        }
    }

    /**
     * 刷新完成后，执行刷新完成渐变动画
     * */
    private fun handleFinishRefreshAnim() {
        mCurrentState = IS_RECOVERY_ANIMING
        val recoveryAnim = ValueAnimator.ofFloat(1f, 0f)
        recoveryAnim.duration = RECOVERY_ANIMATOR_TIME
        recoveryAnim.addUpdateListener {
            val animValue = it.animatedValue as? Float
            animValue?.let {
                mHeaderView.alpha = animValue
                // if (::mBgView.isInitialized) {
                //   mBgView.alpha = 1 - animValue
                // }
                if (::mTitleBar.isInitialized) {
                    //todo 方案一：透明渐变动画
                    mTitleBar.alpha = 1 - animValue
                    //todo 方案二：titleBar由上往下滑动动画
                    //mTitleBar.alpha = 1 - animValue
                    //mTitleBar.translationY = -mTitleBar.measuredHeight * animValue
                }

                //列表回滚回去，headerView 的缩放动画
                val addHeight = (mIvLoading.measuredHeight - mProgressBarHeight) * animValue
                val addWidth = (mIvLoading.measuredWidth - mProgressBarWidth) * animValue
                val layoutParams = mIvLoading.layoutParams
                layoutParams.width = (mProgressBarWidth + addWidth).toInt()
                layoutParams.height = (mProgressBarHeight + addHeight).toInt()
                LogUtils.info("refreshLayout----addHeight=$addHeight----addWidth=$addWidth---mProgressBarWidth=$mProgressBarWidth----mProgressBarHeight=$mProgressBarHeight")
                LogUtils.info("refreshLayout---- layoutParams.width=${layoutParams.width}----layoutParams.height=${layoutParams.height}")
                mIvLoading.layoutParams = layoutParams
            }
        }
        recoveryAnim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                mCurrentState = IS_IDEA
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })
        recoveryAnim.start()

        //刷星动画完成，停止帧动画
        if (mAnimatorDrawable != null) {
            mAnimatorDrawable?.stop()
        }
    }

    private lateinit var mBgView: GradualChangeBgView
    private lateinit var mTitleBar: ConstraintLayout

    /**
     * 更新开始位置
     * */
    private fun updateDownLocation() {
        mLastPoint.set(mMovePoint.x, mMovePoint.y)
    }

    fun enableRefresh(refresh: Boolean) {
        this.mEnableRefresh = refresh
    }

    fun isEnableRefresh(): Boolean {
        return this.mEnableRefresh
    }

    fun bindAnimatorView(bgView: GradualChangeBgView, titleBar: ConstraintLayout) {
        this.mBgView = bgView
        this.mTitleBar = titleBar
    }

    fun getCurrentState(): Int {
        return this.mCurrentState
    }

    fun isNeedRefresh(): Boolean {
        return mIsNeedRefresh
    }

    fun setOnRefreshListener(listener: ((view: View) -> Unit)) {
        this.mRefreshListener = listener
    }

    fun setOnRefreshScrollListener(listener: (view: View, scrolleY: Int, transparency: Float) -> Unit) {
        this.mRefreshScrollListener = listener
    }

}