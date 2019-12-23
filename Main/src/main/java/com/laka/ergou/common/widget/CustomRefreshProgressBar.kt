package com.laka.ergou.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import com.laka.ergou.R

/**
 * @Author:summer
 * @Date:2019/5/16
 * @Description:适配6.0以上通过 indeterminateDrawable 设置 ProgressBar 不显示的问题
 */
class CustomRefreshProgressBar : ProgressBar {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
//        var drawable = if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
//            LogUtils.info("android 小于 6.0")
//            context.resources.getDrawable(R.drawable.anim_ergou_pull_refresh)
//        } else {
//            LogUtils.info("android 大于等于 6.0")
//            context.resources.getDrawable(R.drawable.anim_ergou_pull_refresh_for50)
//        }
        val drawable = context.resources.getDrawable(R.drawable.anim_ergou_pull_refresh_whitebg)
        indeterminateDrawable = drawable
    }
}