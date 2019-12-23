package com.laka.ergou.mvp.shopping.center.helper

import android.content.Context
import android.view.View
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.mvp.shop.utils.BigDecimalUtils
import com.laka.ergou.mvp.shopping.center.weight.GradualChangeBgView

/**
 * @Author:summer
 * @Date:2019/5/8
 * @Description:首页协调者布局滑动监听helper类，主要是实现协调者滑动与titleBar、bgView颜色（透明度）变化的问题
 */
class ScrollerTransparentHelper {

    private var mContext: Context
    //默认滑动距离
    private var mDistanceY: Int = ScreenUtils.dp2px(200f)
    //目前滑动的距离
    private var mCurrentDisY: Int = 0
    private var mAlphaTitleBar: Float = 0f
    private var mAlphaBgView: Float = 1.0f
    private lateinit var mTitleBgView: View
    private lateinit var mBgView: GradualChangeBgView

    constructor(context: Context) {
        this.mContext = context
    }

    fun init(titleBarView: View, bgView: GradualChangeBgView): ScrollerTransparentHelper {
        this.mTitleBgView = titleBarView
        this.mBgView = bgView
        return this
    }

    fun init(titleBarView: View, bgView: GradualChangeBgView, distanceY: Int): ScrollerTransparentHelper {
        this.mTitleBgView = titleBarView
        this.mDistanceY = distanceY
        this.mBgView = bgView
        return this
    }

    fun setScrollerData(verticalOffset: Int) {
        val offsetY = Math.abs(verticalOffset)
        if (offsetY >= mDistanceY && mCurrentDisY >= mDistanceY) { //超出高度
            return
        }
        if (offsetY <= 0 && mCurrentDisY <= 0) {//回到初始位置
            return
        }
        //bgView 慢慢透明消失，titleBar 慢慢恢复透明度
        mCurrentDisY = offsetY
        mAlphaTitleBar = BigDecimalUtils.divi(offsetY.toString(), mDistanceY.toString()).toFloat()
        mAlphaBgView = 1f - mAlphaTitleBar
        mTitleBgView.alpha = mAlphaTitleBar
        mBgView.alpha = mAlphaBgView
        LogUtils.info("verticalOffset---mAlphaTitleBar=$mAlphaTitleBar")
    }


}