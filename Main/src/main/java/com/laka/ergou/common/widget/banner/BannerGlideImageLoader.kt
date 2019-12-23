package com.laka.ergou.common.widget.banner

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.laka.androidlib.util.GlideUtil
import com.laka.ergou.R
import com.youth.banner.loader.ImageLoader

/**
 * @Author:Rayman
 * @Date:2019/1/30
 * @Description:BannerGlide加载器
 */
class BannerGlideImageLoader(var isUseCorner: Boolean = false) : ImageLoader() {

    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        if (isUseCorner) {
            GlideUtil.loadFilletImage(context, path as String,
                    R.drawable.default_img,
                    R.drawable.default_img, imageView)
        } else {
            val drawableRequestBuilder = Glide.with(context)
                    .load(path)
                    .placeholder(R.drawable.default_img)
                    .error(R.drawable.default_img)
                    .fitCenter()
            drawableRequestBuilder.into(imageView)
        }
    }
}