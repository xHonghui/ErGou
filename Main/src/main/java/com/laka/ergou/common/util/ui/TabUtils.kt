package com.laka.ergou.common.util.ui

import android.support.design.widget.TabLayout
import android.widget.LinearLayout
import android.widget.TextView

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:tablayout 操作类
 */
object TabUtils {

    /**更改tablayout底部indicator宽度*/
    fun setTabLayoutIndicator(tabLayout: TabLayout?) {
        tabLayout?.post {
            try {
                val field = tabLayout.javaClass.getDeclaredField("mTabStrip")
                field.isAccessible = true
                //拿到tabLayout的mTabStrip属性
                val tabStrip = field.get(tabLayout) as LinearLayout
                for (i in 0 until tabStrip.childCount) {
                    val tabView = tabStrip.getChildAt(i)
                    val mTextViewField = tabView.javaClass.getDeclaredField("mTextView")
                    mTextViewField.isAccessible = true
                    val textView = mTextViewField.get(tabView) as TextView
                    tabView.setPadding(0, 0, 0, 0)
                    var textWidth = 0
                    textWidth = textView.width
                    if (textWidth == 0) {
                        textView.measure(0, 0)
                        textWidth = textView.measuredWidth
                    }
                    var tabWidth = 0
                    tabWidth = tabView.width
                    if (tabWidth == 0) {
                        tabView.measure(0, 0)
                        tabWidth = tabView.measuredWidth
                    }
                    val tabViewParams = tabView.layoutParams as LinearLayout.LayoutParams
                    val margin = (tabWidth - textWidth) / 2
                    tabViewParams.leftMargin = margin
                    tabViewParams.rightMargin = margin
                    tabView.layoutParams = tabViewParams
                }
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }
}