package com.laka.ergou.mvp.shopping.center.helper

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.laka.androidlib.util.LogUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.shopping.center.view.fragment.ShoppingListFragment


class SelectTabHelper {
    var llSynthesis: LinearLayout? = null
    var llSales: LinearLayout? = null
    var llPrice: LinearLayout? = null
    private var tvSynthesize: TextView? = null
    private var tvCoupon: TextView? = null
    private var tvCommissionPrice: TextView? = null
    private var tvCommissionHeight: TextView? = null
    private var tvPrice: TextView? = null
    private var tvTime: TextView? = null
    private var tvSales: TextView? = null
    private var tvSynthesis: TextView? = null
    private var ivPrice: ImageView? = null
    private var ivTime: ImageView? = null
    private var ivSales: ImageView? = null
    private var ivSynthesis: ImageView? = null
    private var mCurrentFragment: ShoppingListFragment? = null
    var mContext: Context
    private var menuStateListener: MenuStateListener? = null
    val SYNTHESIZE = "synthesize"
    val CREATE_TIME = "create_time"
    val VOLUME = "volume"
    val ZK_FINAL_PRICE = "zk_final_price"
    val BONUS_RATE = "bonus_rate"//佣金比率
    val BONUS_AMOUNT = "bonus_amount"//佣金金额
    val COUPON_AMOUNT = "coupon_amount"//优惠券
    val DESC = "desc"
    val ASC = "asc"
    var orderSort = ASC
    private var llSelect: FrameLayout? = null
    var llSynthe: LinearLayout? = null
    var vShadow: View? = null

    constructor(context: Context) {
        this.mContext = context
    }

    fun bindView(llSelect: FrameLayout, llTab: LinearLayout): SelectTabHelper {
        this.llSelect = llSelect
        llSynthe = llSelect.findViewById(R.id.ll_synthesize)
        vShadow = llSelect.findViewById(R.id.v_shadow)
        tvSynthesis = llTab.findViewById(R.id.tv_synthesis)
        tvPrice = llTab.findViewById(R.id.tv_price)
        tvTime = llTab.findViewById(R.id.tv_time)
        tvSales = llTab.findViewById(R.id.tv_sales)
        ivSynthesis = llTab.findViewById(R.id.iv_synthesis)
        ivPrice = llTab.findViewById(R.id.iv_price)
        ivSales = llTab.findViewById(R.id.iv_sales)
        llSynthesis = llTab.findViewById(R.id.ll_synthesis)
        llSales = llTab.findViewById(R.id.ll_sales)
        llPrice = llTab.findViewById(R.id.ll_price)
        tvSynthesize = llSelect.findViewById(R.id.tv_synthesize)
        tvCoupon = llSelect.findViewById(R.id.tv_coupon)
        tvCommissionPrice = llSelect.findViewById(R.id.tv_commission_price)
        tvCommissionHeight = llSelect.findViewById(R.id.tv_commission_height)
        initClick()
        return this
    }

    private fun initClick() {
        tvSynthesize?.setOnClickListener {
            resetColor(tvSynthesize!!)
            tvSynthesis?.text = "综合"
            orderSort = DESC
            setTab(tvSynthesis, ivSynthesis)
            menuStateListener?.let {
                it.menuOpen(0, SYNTHESIZE, orderSort)
            }
            startAnimator()
        }
        vShadow?.setOnClickListener {
            startAnimator()
        }
        tvCoupon?.setOnClickListener {
            resetColor(tvCoupon!!)
            tvSynthesis?.text = "优惠券"
            orderSort = DESC
            setTab(tvSynthesis, ivSynthesis)
            menuStateListener?.let {
                it.menuOpen(0, COUPON_AMOUNT, orderSort)
            }
            startAnimator()
        }
        tvCommissionPrice?.setOnClickListener {
            resetColor(tvCommissionPrice!!)
            tvSynthesis?.text = "佣金金额"
            orderSort = DESC
            setTab(tvSynthesis, ivSynthesis)
            menuStateListener?.let {
                it.menuOpen(0, BONUS_AMOUNT, orderSort)
            }
            startAnimator()
        }
        tvCommissionHeight?.setOnClickListener {
            resetColor(tvCommissionHeight!!)
            tvSynthesis?.text = "佣金比率"
            orderSort = DESC
            setTab(tvSynthesis, ivSynthesis)
            menuStateListener?.let {
                it.menuOpen(0, BONUS_RATE, orderSort)
            }
            startAnimator()
        }
        llSynthesis?.setOnClickListener {
            setTab(tvSynthesis, null)
            menuStateListener?.let {
                menuStateListener?.let {
                    it.menuOpen(-1, "", "")
                }
                startAnimator()
            }
        }
        tvTime?.setOnClickListener {
            setTab(tvTime, ivTime)
            if (mCurrentFragment?.mOrderField != CREATE_TIME) {
                orderSort = DESC
                menuStateListener?.let {
                    it.menuOpen(1, CREATE_TIME, orderSort)
                }
            }

        }
        llSales?.setOnClickListener {
            if (mCurrentFragment?.mOrderField != VOLUME) {
                orderSort = DESC
            } else {
                resetOrderSort()
            }
            setTab(tvSales, ivSales)
            menuStateListener?.let {
                it.menuOpen(2, VOLUME, orderSort)
            }
        }
        llPrice?.setOnClickListener {
            if (mCurrentFragment?.mOrderField != ZK_FINAL_PRICE) {
                orderSort = DESC
            } else {
                resetOrderSort()
            }
            setTab(tvPrice, ivPrice)
            menuStateListener?.let {
                it.menuOpen(3, ZK_FINAL_PRICE, orderSort)
            }
        }
    }

    private fun resetOrderSort() {
        if (orderSort == ASC) {
            orderSort = DESC
        } else {
            orderSort = ASC
        }
    }

    private fun resetColor(itemView: TextView?) {
        tvSynthesize?.setTextColor(mContext.resources.getColor(R.color.gray_88))
        tvCoupon?.setTextColor(mContext.resources.getColor(R.color.gray_88))
        tvCommissionPrice?.setTextColor(mContext.resources.getColor(R.color.gray_88))
        tvCommissionHeight?.setTextColor(mContext.resources.getColor(R.color.gray_88))
//        tvSales?.setTextColor(mContext.resources.getColor(R.color.gray_88))
        val drawable = mContext.getResources().getDrawable(
                R.drawable.default_icon_selected)
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight())
        tvSynthesize?.setCompoundDrawables(null, null, null, null)
        tvCoupon?.setCompoundDrawables(null, null, null, null)
        tvCommissionPrice?.setCompoundDrawables(null, null, null, null)
        tvCommissionHeight?.setCompoundDrawables(null, null, null, null)
//        tvSales?.setCompoundDrawables(null, null, null, null)
        itemView?.setCompoundDrawables(null, null, drawable, null)
        itemView?.setTextColor(mContext.resources.getColor(R.color.color_main))
    }

    fun setCurrentFragment(fragment: ShoppingListFragment?) {
        mCurrentFragment = fragment
        val orderField = mCurrentFragment?.mOrderField
        val orderSort = mCurrentFragment?.mOrderSort
        when (orderField) {
            CREATE_TIME -> {
//                resetColor()
                resetTab(tvTime)
                if (orderSort == DESC) {
                    ivTime?.setImageResource(R.drawable.default_btn_sort_s1)
                } else {
                    ivTime?.setImageResource(R.drawable.default_btn_sort_s2)
                }
            }
            VOLUME -> {
                resetTab(tvSales)
                if (orderSort == DESC) {
                    ivSales?.setImageResource(R.drawable.default_btn_sort_s1)
                } else {
                    ivSales?.setImageResource(R.drawable.default_btn_sort_s2)
                }
            }
            ZK_FINAL_PRICE -> {
                resetTab(tvPrice)
                if (orderSort == DESC) {
                    ivPrice?.setImageResource(R.drawable.default_btn_sort_s1)
                } else {
                    ivPrice?.setImageResource(R.drawable.default_btn_sort_s2)
                }
            }
            else -> {
                when (orderField) {
                    BONUS_RATE -> {
                        tvSynthesis?.text = "佣金比率"
                        resetColor(tvCommissionHeight)
                    }
                    BONUS_AMOUNT -> {
                        tvSynthesis?.text = "佣金金额"
                        resetColor(tvCommissionPrice)
                    }
                    COUPON_AMOUNT -> {
                        tvSynthesis?.text = "优惠券"
                        resetColor(tvCoupon)
                    }
                    SYNTHESIZE -> {
                        tvSynthesis?.text = "综合"
                        resetColor(tvSynthesize)
                    }
                }
                resetTab(tvSynthesis)
                ivSynthesis?.setImageResource(R.drawable.default_btn_sort_s1)
            }
        }
    }

    private fun resetTab(textView: TextView?) {
        tvTime?.setTextColor(mContext.resources.getColor(R.color.gray_88))
        tvPrice?.setTextColor(mContext.resources.getColor(R.color.gray_88))
        tvSales?.setTextColor(mContext.resources.getColor(R.color.gray_88))
        tvSynthesis?.setTextColor(mContext.resources.getColor(R.color.gray_88))
        textView?.setTextColor(mContext.resources.getColor(R.color.color_main))
        ivSynthesis?.setImageResource(R.drawable.default_btn_sort_n)
        ivPrice?.setImageResource(R.drawable.default_btn_sort_n2)
        ivSales?.setImageResource(R.drawable.default_btn_sort_n2)
    }

    private fun setTab(textView: TextView?, imageView: ImageView?) {
        tvSynthesis?.setTextColor(mContext.resources.getColor(R.color.gray_88))
        tvTime?.setTextColor(mContext.resources.getColor(R.color.gray_88))
        tvSales?.setTextColor(mContext.resources.getColor(R.color.gray_88))
        tvPrice?.setTextColor(mContext.resources.getColor(R.color.gray_88))
        textView?.setTextColor(mContext.resources.getColor(R.color.color_main))
        ivSynthesis?.setImageResource(R.drawable.default_btn_sort_n)
        ivPrice?.setImageResource(R.drawable.default_btn_sort_n2)
        ivSales?.setImageResource(R.drawable.default_btn_sort_n2)
        if (orderSort == ASC) {
            imageView?.setImageResource(R.drawable.default_btn_sort_s2)
        } else {
            imageView?.setImageResource(R.drawable.default_btn_sort_s1)
        }
        if (textView !== tvSynthesis) {
            resetColor(tvSynthesize)
            tvSynthesis?.text = "综合"
            if (llSelect?.visibility == View.VISIBLE) {
                startAnimator(false)
                ivSynthesis?.setImageResource(R.drawable.default_btn_sort_n)
            }
        } else {

        }
    }


    private fun startAnimator(showType: Boolean = true) {
        val wm = (mContext as Activity).getWindowManager()
        val height = wm.defaultDisplay.height.toFloat()
        if (llSelect?.getVisibility() == View.GONE) {
            val translationY = ObjectAnimator.ofFloat(llSynthe, "translationY", -height, 0f)
            val alpha = ObjectAnimator.ofFloat(vShadow, "alpha", 0f, 1f)
            val animator = AnimatorSet()
            animator.playTogether(translationY, alpha)
            animator.setDuration(500)
            animator.setInterpolator(DecelerateInterpolator())
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    llSelect?.setVisibility(View.VISIBLE)
                    ivSynthesis?.setImageResource(R.drawable.default_btn_sort_s2)
                }
            })
            animator.start()
        } else {
            val translationY = ObjectAnimator.ofFloat(llSynthe, "translationY", 0f, -height)
            val alpha = ObjectAnimator.ofFloat(vShadow, "alpha", 1f, 0f)
            val animator = AnimatorSet()
            animator.playTogether(translationY, alpha)
            animator.setDuration(500)
            animator.setInterpolator(DecelerateInterpolator())
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    llSelect?.setVisibility(View.GONE)
                    if (showType) {
                        ivSynthesis?.setImageResource(R.drawable.default_btn_sort_s1)
                    } else {
                        ivSynthesis?.setImageResource(R.drawable.default_btn_sort_n)
                    }

                }
            })
            animator.start()
        }
    }

    fun setMenuStateListener(listener: MenuStateListener) {
        this.menuStateListener = listener
    }

    interface MenuStateListener {
        fun menuOpen(position: Int, type: String, orderSort: String)
    }
}