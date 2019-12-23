package com.laka.androidlib.callback;

import com.laka.androidlib.util.image.LoaderOptions;

/**
 * @Author:Rayman
 * @Date:2018/6/28
 * @Description:图片加载策略类。主要作用于不同ImageLoader实现这个方法
 */

public interface IImageLoaderStrategy {

    void loadImage(LoaderOptions options);

    void clearDiskCache();

    void clearMemoryCache();

    void pauseRequest();

    void resumeRequest();

}
