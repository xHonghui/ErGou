package com.laka.ergou.mvp.main.helper

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.laka.androidlib.util.ResourceUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.login.LoginModuleNavigator
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView

class MagicTabHelper {


    private var isInit = false

    var listener: onSelectIndexListener? = null
    private lateinit var mMagicIndicator: MagicIndicator
    private lateinit var mContext: Context


    private var mTitle = arrayOf(ResourceUtils.getString(R.string.module_home),
            ResourceUtils.getString(R.string.module_chat),
            ResourceUtils.getString(R.string.module_circle),
            ResourceUtils.getString(R.string.module_user))
    val DURATION = 30
    internal var ids = intArrayOf(R.mipmap.tab_icon_home00, R.mipmap.tab_icon_home01,
            R.mipmap.tab_icon_home02, R.mipmap.tab_icon_home03, R.mipmap.tab_icon_home04,
            R.mipmap.tab_icon_home05, R.mipmap.tab_icon_home06, R.mipmap.tab_icon_home07,
            R.mipmap.tab_icon_home08, R.mipmap.tab_icon_home09, R.mipmap.tab_icon_home10,
            R.mipmap.tab_icon_home11, R.mipmap.tab_icon_home12, R.mipmap.tab_icon_home13)


    internal var ids3 = intArrayOf(R.mipmap.tab_icon_mine00, R.mipmap.tab_icon_mine01,
            R.mipmap.tab_icon_mine02, R.mipmap.tab_icon_mine03, R.mipmap.tab_icon_mine04,
            R.mipmap.tab_icon_mine05, R.mipmap.tab_icon_mine06, R.mipmap.tab_icon_mine07,
            R.mipmap.tab_icon_mine08, R.mipmap.tab_icon_mine09, R.mipmap.tab_icon_mine10,
            R.mipmap.tab_icon_mine11, R.mipmap.tab_icon_mine12, R.mipmap.tab_icon_mine13)
    internal var ids2 = intArrayOf(R.mipmap.tab_icon_fa00, R.mipmap.tab_icon_fa01,
            R.mipmap.tab_icon_fa02, R.mipmap.tab_icon_fa03, R.mipmap.tab_icon_fa04,
            R.mipmap.tab_icon_fa05, R.mipmap.tab_icon_fa06, R.mipmap.tab_icon_fa07,
            R.mipmap.tab_icon_fa08, R.mipmap.tab_icon_fa09, R.mipmap.tab_icon_fa10,
            R.mipmap.tab_icon_fa11, R.mipmap.tab_icon_fa12, R.mipmap.tab_icon_fa13)
    var mTvMsgCount: TextView? = null
    fun initMagicIndicator(context: Context, magicIndicator: MagicIndicator, mViewPager: ViewPager) {
        this.mMagicIndicator = magicIndicator
        mContext = context
        val commonNavigator = CommonNavigator(context)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return mTitle.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val commonPagerTitleView = CommonPagerTitleView(context)

                // load custom layout
                val customLayout = LayoutInflater.from(context).inflate(R.layout.item_home_tab, null)
                val titleImg = customLayout.findViewById(R.id.title_img) as ImageView
                val titleText = customLayout.findViewById(R.id.title_text) as TextView
                val tvMsgCount = customLayout.findViewById(R.id.tv_msg_count) as TextView

                if (index == 1) {
                    mTvMsgCount = tvMsgCount
                }

                titleText.setText(mTitle.get(index))
                commonPagerTitleView.setContentView(customLayout)
                commonPagerTitleView.onPagerTitleChangeListener = object : CommonPagerTitleView.OnPagerTitleChangeListener {

                    override fun onSelected(index: Int, totalCount: Int) {
                        titleText.setTextColor(context.resources.getColor(R.color.color_main))
                    }

                    override fun onDeselected(index: Int, totalCount: Int) {
                        titleText.setTextColor(context.resources.getColor(R.color.color_font))
                    }

                    override fun onLeave(index: Int, totalCount: Int, leavePercent: Float, leftToRight: Boolean) {

                        when (index) {
                            0 -> {
                                val anim = AnimationDrawable()
                                for (i in ids.indices.reversed()) {
                                    val drawable = context.getResources().getDrawable(ids[i])
                                    anim.addFrame(drawable, DURATION)
                                }
                                anim.isOneShot = true
                                titleImg.setImageDrawable(anim)
                                anim.start()
                            }
                            1 -> {
                                titleImg.setImageResource(R.mipmap.tab_icon_goux00)
                            }
                            2 -> {
                                if (isInit) {
                                    val anim = AnimationDrawable()
                                    for (i in ids.indices.reversed()) {
                                        val drawable = context.getResources().getDrawable(ids2[i])
                                        anim.addFrame(drawable, DURATION)
                                    }
                                    anim.isOneShot = true
                                    titleImg.setImageDrawable(anim)
                                    anim.start()
                                } else {
                                    titleImg.setImageDrawable(context.getResources().getDrawable(ids2[0]))
                                }
                                isInit = true
                            }
                            3 -> {
                                if (isInit) {
                                    val anim = AnimationDrawable()
                                    for (i in ids.indices.reversed()) {
                                        val drawable = context.getResources().getDrawable(ids3[i])
                                        anim.addFrame(drawable, DURATION)
                                    }
                                    anim.isOneShot = true
                                    titleImg.setImageDrawable(anim)
                                    anim.start()
                                } else {
                                    titleImg.setImageDrawable(context.getResources().getDrawable(ids3[0]))
                                }
                                isInit = true
                            }
                        }


                    }

                    override fun onEnter(index: Int, totalCount: Int, enterPercent: Float, leftToRight: Boolean) {
                        when (index) {
                            0 -> {
                                val anim = AnimationDrawable()
                                for (i in ids.indices) {
                                    val drawable = context.getResources().getDrawable(ids[i])
                                    anim.addFrame(drawable, DURATION)
                                }
                                anim.isOneShot = true
                                titleImg.setImageDrawable(anim)
                                anim.start()
                            }
                            1 -> {
                                titleImg.setImageResource(R.mipmap.tab_icon_goux00)
                            }
                            2 -> {
                                val anim = AnimationDrawable()
                                for (i in ids.indices) {
                                    val drawable = context.getResources().getDrawable(ids2[i])
                                    anim.addFrame(drawable, DURATION)
                                }
                                anim.isOneShot = true
                                titleImg.setImageDrawable(anim)
                                anim.start()
                            }
                            3 -> {
                                val anim = AnimationDrawable()
                                for (i in ids.indices) {
                                    val drawable = context.getResources().getDrawable(ids3[i])
                                    anim.addFrame(drawable, DURATION)
                                }
                                anim.isOneShot = true
                                titleImg.setImageDrawable(anim)
                                anim.start()
                            }
                        }


                    }
                }

                commonPagerTitleView.setOnClickListener {
                    if (index == 3 && !UserUtils.isLogin()) {
                        LoginModuleNavigator.startLoginActivity(context)
                    } else if (index == 1 && !UserUtils.isLogin()) {
                        LoginModuleNavigator.startLoginActivity(context)
                    } else {
                        listener?.let {
                            it.onSelectIndex(index)
                        }
                    }
                }

                return commonPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                return null
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }

    fun setMsgNums(unReadCount: Int) {
//        mTvMsgCount?.visibility = View.VISIBLE
        if (unReadCount > 0) {
            mTvMsgCount?.visibility = View.VISIBLE
            if (unReadCount >= 100) {
                mTvMsgCount?.text = "99+"
            } else {
                mTvMsgCount?.text = unReadCount.toString()
            }
        } else {
            mTvMsgCount?.visibility = View.GONE
        }


    }

    fun setOnSelectIndexListener(listener: onSelectIndexListener) {
        this.listener = listener
    }

    interface onSelectIndexListener {
        fun onSelectIndex(index: Int)
    }
}