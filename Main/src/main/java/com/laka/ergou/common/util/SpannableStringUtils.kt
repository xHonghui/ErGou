package com.laka.ergou.common.util

import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.View
import com.laka.androidlib.util.ApplicationUtils

/**
 * @Author:summer
 * @Date:2019/3/6
 * @Description:富文本工具类
 */
object SpannableStringUtils {

    /**
     * 富文本（颜色）*/
    fun makeColorSpannableString(start: Int, end: Int, colorRes: Int, content: String): SpannableString {
        val spann = SpannableString(content)
        spann.setSpan(ForegroundColorSpan(ContextCompat.getColor(ApplicationUtils.getContext(), colorRes)), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return spann
    }

    fun makeColorSpannableString(start: Int, end: Int, colorRes: Int, spann: SpannableString): SpannableString {
        spann.setSpan(ForegroundColorSpan(ContextCompat.getColor(ApplicationUtils.getContext(), colorRes)), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return spann
    }

    /**
     * 富文本（点击）*/
    fun makeClickSpannableString(start: Int, end: Int, content: String, clickListener: ((View) -> Unit)): SpannableString {
        val spann = SpannableString(content)
        val clickSpann = object : ClickableSpan() {
            override fun onClick(widget: View?) {
                clickListener?.invoke(widget!!)
            }

        }
        spann.setSpan(clickSpann, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return spann
    }

    fun makeClickSpannableString(start: Int, end: Int, spann: SpannableString, clickListener: ((View) -> Unit)): SpannableString {
        val clickSpann = object : ClickableSpan() {
            override fun onClick(widget: View?) {
                clickListener?.invoke(widget!!)
            }
        }
        spann.setSpan(clickSpann, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        return spann
    }

    /**
     * 富文本同时设置点击和颜色，选设置点击，再设置颜色*/
    fun makeClickColorSpannableString(start: Int, end: Int, colorRes: Int, content: String, clickListener: (View) -> Unit): SpannableString {
        val spahnClick = makeClickSpannableString(start, end, content, clickListener)
        return makeColorSpannableString(start, end, colorRes, spahnClick)
    }

    /**富文本替换图片*/
    fun makeImageSpannableString(start: Int, end: Int, imageRes: Int, content: String): SpannableString {
        val spann = SpannableString(content)
        val imageSpann = ImageSpan(ApplicationUtils.getContext(), imageRes)
        spann.setSpan(imageSpann, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spann
    }

    /**富文本替换图片*/
    fun makeImageSpannableString(start: Int, end: Int, imageRes: Int, spann: SpannableString): SpannableString {
        val imageSpann = object :ImageSpan(ApplicationUtils.getContext(), imageRes){

        }
        spann.setSpan(imageSpann, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spann
    }


}