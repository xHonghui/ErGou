package com.laka.androidlib.util.image;

import android.content.Context;
import android.net.Uri;

import com.laka.androidlib.callback.IImageLoaderStrategy;

import java.io.File;

/**
 * @Author:Rayman
 * @Date:2018/6/28
 * @Description:图片加载类
 */

public class ImageLoader {

    private static volatile ImageLoader mInstance;
    private static IImageLoaderStrategy mLoader;

    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader();
                }
            }
        }
        return mInstance;
    }

    public void setLoader(IImageLoaderStrategy loaderStrategy) {
        if (loaderStrategy == null) {
            throw new IllegalArgumentException("Loader Strategy can not be null");
        }
        mLoader = loaderStrategy;
    }

    public LoaderOptions with(Context context) {
        return new LoaderOptions(context);
    }

    /**
     * 直接Load的方式，默认的生命周期与Application一致
     *
     * @param path
     * @return
     */
    public LoaderOptions load(String path) {
        return new LoaderOptions(null).load(path);
    }

    public LoaderOptions load(int drawable) {
        return new LoaderOptions(null).load(drawable);
    }

    public LoaderOptions load(File file) {
        return new LoaderOptions(null).load(file);
    }

    public LoaderOptions load(Uri uri) {
        return new LoaderOptions(null).load(uri);
    }

    public LoaderOptions load(byte[] bytes) {
        return new LoaderOptions(null).load(bytes);
    }

    public void loadOptions(LoaderOptions loaderOptions) {
        mLoader.loadImage(loaderOptions);
    }

    public void clearMemoryCache() {
        mLoader.clearMemoryCache();
    }

    public void clearDiskCache() {
        mLoader.clearDiskCache();
    }

    public void resumeRequest() {
        mLoader.resumeRequest();
    }

    public void pauseRequest() {
        mLoader.pauseRequest();
    }
}
