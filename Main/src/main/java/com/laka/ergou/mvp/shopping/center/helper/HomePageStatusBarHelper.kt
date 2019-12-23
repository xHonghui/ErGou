package com.laka.ergou.mvp.shopping.center.helper

import android.app.Activity
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.view.View
import com.laka.androidlib.util.StatusBarUtil
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.common.widget.refresh.ErGouHomeRefreshLayout
import com.laka.ergou.mvp.shopping.center.weight.GradualChangeBgView

/**
 * @Author:summer
 * @Date:2019/8/12
 * @Description:
 */
object HomePageStatusBarHelper {

    fun handleHomePageStatusBarOffset(activity: Activity, refreshLayout: ErGouHomeRefreshLayout, clTitleBar: ConstraintLayout, bgView: GradualChangeBgView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return
        }
        //配合透明状态栏调整设置
        val statusBarHeight = StatusBarUtil.getStatusBarHeight(activity)
        val rvLayoutParams = refreshLayout.layoutParams as? ConstraintLayout.LayoutParams
        rvLayoutParams?.topMargin = statusBarHeight
        val tbLayoutParams = clTitleBar.layoutParams as? ConstraintLayout.LayoutParams
        tbLayoutParams?.height = ScreenUtils.dp2px(50f) + statusBarHeight
        //背景
        val leftBottomY = bgView.getLeftBottomY() + statusBarHeight
        val rightBottomY = bgView.getRightBottomY() + statusBarHeight
        bgView.setLeftBottomY(leftBottomY)
        bgView.setRightBottomY(rightBottomY)
    }

}