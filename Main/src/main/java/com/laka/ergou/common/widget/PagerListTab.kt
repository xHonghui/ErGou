package com.laka.ergou.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.R

/**
 * @Author:summer
 * @Date:2019/3/7
 * @Description:自定义tab框，配置viewPager使用（我的订单、我的战友、我的消息等）
 */
class PagerListTab : RelativeLayout {

    companion object {
        const val LEFT_ITEM = 0x100
        const val RIGHT_ITEM = 0x110
    }

    private var mItemClickListener: ((Int) -> Unit)? = null
    private var mRootView: View? = null
    private var mTvLeftTab: TextView? = null
    private var mTvRightTab: TextView? = null
    private var mLeftTabStr: String? = ""
    private var mRightTabStr: String? = ""

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(attrs)
        initEvent()
    }

    fun initView(attrs: AttributeSet?) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.view_custom_tab, this)
        mTvLeftTab = findViewById(R.id.tv_tab_left)
        mTvRightTab = findViewById(R.id.tv_tab_right)
        // 属性相关
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerListTab)
            mLeftTabStr = typedArray.getString(R.styleable.PagerListTab_tab_left_txt)
            mRightTabStr = typedArray.getString(R.styleable.PagerListTab_tab_right_txt)
        }
        mTvLeftTab?.text = if (mLeftTabStr == null) "" else mLeftTabStr
        mTvRightTab?.text = if (mRightTabStr == null) "" else mRightTabStr
    }

    private fun initEvent() {
        mTvLeftTab?.setOnClickListener {
            if (mTvLeftTab?.isSelected!!) {
                return@setOnClickListener
            }
            mTvLeftTab?.isSelected = true
            mTvRightTab?.isSelected = false
            mItemClickListener?.invoke(LEFT_ITEM)
        }
        mTvRightTab?.setOnClickListener {
            if (mTvRightTab?.isSelected!!) {
                return@setOnClickListener
            }
            mTvRightTab?.isSelected = true
            mTvLeftTab?.isSelected = false
            mItemClickListener?.invoke(RIGHT_ITEM)
        }
    }

    fun selectTabItem(type: Int) {
        when (type) {
            LEFT_ITEM -> {
                mTvLeftTab?.isSelected = true
                mTvRightTab?.isSelected = false
            }
            RIGHT_ITEM -> {
                mTvRightTab?.isSelected = true
                mTvLeftTab?.isSelected = false
            }
            else -> LogUtils.error("Out of Page Range")
        }
    }

    fun setLeftTabText(text: String) {
        this.mLeftTabStr = text
        mTvLeftTab?.text = mLeftTabStr
    }

    fun setRightTabStr(text: String) {
        this.mRightTabStr = text
        mTvRightTab?.text = mRightTabStr
    }

    fun setItemClickListener(listener: ((Int) -> Unit)) {
        this.mItemClickListener = listener
    }

}