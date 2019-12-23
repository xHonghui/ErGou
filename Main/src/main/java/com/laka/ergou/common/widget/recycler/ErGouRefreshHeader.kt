package com.laka.ergou.common.widget.recycler

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.R
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.internal.InternalAbstract

/**
 * @Author:summer
 * @Date:2019/5/7
 * @Description:二购刷新头部布局
 */
class ErGouRefreshHeader : InternalAbstract, RefreshHeader {

    private var mIvLoading: ImageView
    private var mDrawable: AnimationDrawable?

    companion object {
        @JvmStatic
        fun newInstance(context: Context): ErGouRefreshHeader {
            val view = LayoutInflater.from(context).inflate(R.layout.header_refresh_layout, null)
            return ErGouRefreshHeader(view)
        }
    }

    constructor(wrapped: View) : super(wrapped) {
        mIvLoading = wrapped.findViewById(R.id.iv_loading)
        mIvLoading.setImageResource(R.drawable.anim_ergou_pull_refresh_blackbg)
        mDrawable = mIvLoading.drawable as? AnimationDrawable
    }

    override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
        super.onMoving(isDragging, percent, offset, height, maxDragHeight)
        LogUtils.info("ErGouRefreshHeader-------------isDragging=$isDragging，percent=$percent，offset=$offset，height=$height，maxDragHeight=$maxDragHeight")
        if (offset <= 0.0 && mDrawable != null) {
            mDrawable?.stop()
        }
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        super.onReleased(refreshLayout, height, maxDragHeight)
        LogUtils.info("ErGouRefreshHeader-------------释放时刻")
        if (mDrawable != null) {
            mDrawable?.start()
        }
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        super.onStartAnimator(refreshLayout, height, maxDragHeight)
        LogUtils.info("ErGouRefreshHeader-------------开始动画")
    }

}