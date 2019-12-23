package com.laka.ergou.mvp.shopping.center.weight

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R
import com.laka.ergou.mvp.shopping.center.model.bean.TagItemBean
import java.lang.Exception


class TagLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = HORIZONTAL
    }


    fun setTagList(tag: MutableList<TagItemBean>,textSize:Float = 10f) {
        removeAllViews()
        tag.forEach {
            val tv = TextView(context)
            it.name?.let {
                tv.text = it
            }
            val params = LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
            )
            if (it.word_color.isNotEmpty()) {
                tv.setTextColor(Color.parseColor(it.word_color))
            }

            tv.textSize = textSize
            if (it.bg_color.isNotEmpty()) {
                val drawable = resources.getDrawable(R.drawable.bg_home_tag) as GradientDrawable
                drawable.setColor(Color.parseColor(it.bg_color))
                tv.setBackgroundDrawable(drawable)
            }
            tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
            tv.gravity = Gravity.CENTER
            params.setMargins(0, 0, ScreenUtils.dp2px(5f), 0)
            tv.layoutParams = params
            tv.maxLines = 1
            tv.ellipsize = TextUtils.TruncateAt.END
            if (it.image.isNotBlank()){
                Glide.with(context)
                        .load(it.image)
                        .asBitmap()
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                resource?.let {
                                    var drawable = bitmap2Drawable(it)
                                    // / 这一步必须要做,否则不会显示.
                                    drawable.setBounds(
                                            0, 0, drawable.minimumWidth,
                                            drawable.minimumHeight
                                    )
                                    tv.setPadding(0, ScreenUtils.dp2px(1f), ScreenUtils.dp2px(4f), ScreenUtils.dp2px(1f))
                                    tv?.setCompoundDrawables(drawable, null, null, null)
                                }
                            }

                            override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                                super.onLoadFailed(e, errorDrawable)
                                tv.setPadding(ScreenUtils.dp2px(4f), ScreenUtils.dp2px(1f), ScreenUtils.dp2px(4f), ScreenUtils.dp2px(1f))
                            }

                        })
            }
            else{
                tv.setPadding(ScreenUtils.dp2px(4f), ScreenUtils.dp2px(1f), ScreenUtils.dp2px(4f), ScreenUtils.dp2px(1f))
            }

            addView(tv)
        }
    }
    fun setTagNumsList(tag: MutableList<TagItemBean>,textSize:Float = 10f) {
        removeAllViews()

        for (index in tag.indices){

            var tagItemBean = tag[index]
            if (index==3){
                return
            }
            val tv = TextView(context)

            tagItemBean.name?.let {
                tv.text = it
            }

            val params = LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
            )
            if (tagItemBean.word_color.isNotEmpty()) {
                tv.setTextColor(Color.parseColor(tagItemBean.word_color))
            }

            tv.textSize = textSize
            if (tagItemBean.bg_color.isNotEmpty()) {
                val drawable = resources.getDrawable(R.drawable.bg_home_tag) as GradientDrawable
                drawable.setColor(Color.parseColor(tagItemBean.bg_color))
                tv.setBackgroundDrawable(drawable)
            }
            tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
            tv.gravity = Gravity.CENTER
            params.setMargins(0, 0, ScreenUtils.dp2px(5f), 0)
            tv.layoutParams = params
            tv.maxLines = 1
            tv.ellipsize = TextUtils.TruncateAt.END
            if (tagItemBean.image.isNotBlank()){
                Glide.with(context)
                        .load(tagItemBean.image)
                        .asBitmap()
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                resource?.let {
                                    var drawable = bitmap2Drawable(it)
                                    // / 这一步必须要做,否则不会显示.
                                    drawable.setBounds(
                                            0, 0, drawable.minimumWidth,
                                            drawable.minimumHeight
                                    )
                                    tv.setPadding(0, ScreenUtils.dp2px(1f), ScreenUtils.dp2px(4f), ScreenUtils.dp2px(1f))
                                    tv?.setCompoundDrawables(drawable, null, null, null)
                                }
                            }

                            override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                                super.onLoadFailed(e, errorDrawable)
                                tv.setPadding(ScreenUtils.dp2px(4f), ScreenUtils.dp2px(1f), ScreenUtils.dp2px(4f), ScreenUtils.dp2px(1f))
                            }

                        })
            }
            else{
                tv.setPadding(ScreenUtils.dp2px(4f), ScreenUtils.dp2px(1f), ScreenUtils.dp2px(4f), ScreenUtils.dp2px(1f))
            }

            addView(tv)

        }
    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        for (index in 0..childCount - 1) {
            val text = getChildAt(index)
            if (text is TextView) {
                val l = text.getLayout()
                if (l != null) {
                    val lines = l.getLineCount();
                    if (lines > 0) {
                        if (l.getEllipsisCount(lines - 1) > 0) {
                            text?.setCompoundDrawables(null, null, null, null)
                        }
                    }
                }
            }
        }
    }


    fun bitmap2Drawable(bitmap: Bitmap): Drawable {
        return BitmapDrawable(bitmap)
    }
}
