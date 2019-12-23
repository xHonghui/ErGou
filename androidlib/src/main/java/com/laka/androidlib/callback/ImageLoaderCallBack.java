package com.laka.androidlib.callback;

import android.graphics.drawable.Drawable;

/**
 * @Author:Rayman
 * @Date:2018/6/28
 * @Description:图片加载回调类
 */

public interface ImageLoaderCallBack {

    void onBitmapLoaded(Drawable drawable);

    void onBitmapFailed(Exception e);
}
