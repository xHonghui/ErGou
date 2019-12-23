package com.laka.ergou.common.ext


import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget


import com.laka.androidlib.util.GlideUtil
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.screen.ScreenUtils
import com.laka.ergou.R

/**
 * Created by aa on 2019-08-02.
 * @auto sming
 */


fun ImageView.loadImage(url: String, placeholderRes: Int = R.drawable.default_img, errorRes: Int = R.drawable.default_img) {
    GlideUtil.loadImage(context, url, placeholderRes, errorRes, this)
}

fun ImageView.loadImage(targetRes: Int, placeholderRes: Int = R.drawable.default_img, errorRes: Int = R.drawable.default_img) {
    GlideUtil.loadImage(context, targetRes, placeholderRes, errorRes, this)
}

fun ImageView.loadImageNoCrop(targetUrl: String, placeholderRes: Int = R.drawable.default_img) {
    GlideUtil.loadImageNoCrop(context, targetUrl, placeholderRes, this)
}


fun ImageView.loadCircleImage(targetUrl: Int) {
    GlideUtil.loadCircleImage(context, targetUrl, this)
}

fun ImageView.loadCircleImage(targetUrl: String) {
    GlideUtil.loadCircleImage(context, targetUrl, this)
}

fun ImageView.loadCircleImage(targetUrl: String, placeholderRes: Int) {
    GlideUtil.loadCircleImage(context, targetUrl, placeholderRes, this)
}

fun ImageView.loadFilletImage(targetUrl: String, placeRes: Int = R.drawable.default_img, errorRes: Int = R.drawable.default_img) {
    GlideUtil.loadFilletImage(context, targetUrl, placeRes, errorRes, this)
}

fun ImageView.loadFilletImage(targetUrl: String, placeRes: Int = R.drawable.default_img, errorRes: Int = R.drawable.default_img, filletSize: Int) {
    GlideUtil.loadFilletImage(context, targetUrl, placeRes, errorRes, this, filletSize)
}

fun ImageView.downloadBitmapForAdapterImg(url: String, defaultImg: Int = R.drawable.default_img) {
    GlideUtil.downloadBitmapForAdapterImg(context, url, R.drawable.default_img, this)
}