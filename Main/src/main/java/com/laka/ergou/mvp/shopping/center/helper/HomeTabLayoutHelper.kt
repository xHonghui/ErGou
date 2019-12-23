package com.laka.ergou.mvp.shopping.center.helper

import android.content.Context
import android.graphics.Typeface
import android.support.v4.view.ViewPager
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.laka.ergou.R
import com.laka.ergou.mvp.shopping.center.weight.ScaleTransitionPagerTitleView
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

class HomeTabLayoutHelper {

    fun bindCommonNavigator(context: Context, titleList: MutableList<String>, magicIndicator: MagicIndicator, mVpContainer: ViewPager, normalColor: Int, selectedColor: Int, indicatorColor: Int): HomeTabLayoutHelper {
        var commonNavigator = CommonNavigator(context)
        commonNavigator?.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return if (titleList == null) 0 else titleList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.setText(titleList.get(index))
                simplePagerTitleView.setTextSize(16f)
                simplePagerTitleView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                simplePagerTitleView.setNormalColor(context.resources.getColor(normalColor))
                simplePagerTitleView.setSelectedColor(context.resources.getColor(selectedColor))
                simplePagerTitleView.setOnClickListener {
                    mVpContainer.setCurrentItem(index)
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                var indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 20.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(context.resources.getColor(indicatorColor))
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mVpContainer)
        return this
    }
}