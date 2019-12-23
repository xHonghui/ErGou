package com.laka.androidlib.util.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.laka.androidlib.R;
import com.laka.androidlib.callback.ImageLoaderCallBack;
import com.laka.androidlib.util.LogUtils;

import java.io.File;

/**
 * @Author:Rayman
 * @Date:2018/6/28
 * @Description:图片加载配置类,一些API的常规配置 todo 暂时Bitmap和byte数组的显示会有问题。。迟点修复
 */

public class LoaderOptions {

    private Context context;
    private String url;
    private File file;
    private Uri uri;
    private byte[] bytes;
    private Bitmap bitmap;
    private int drawableResId;
    private int placeHolderResId;
    private int errorResId;
    private Drawable placeHolderDrawable;
    private Drawable errorDrawable;

    private boolean isCenterCrop;
    private boolean isCenterInside;
    private float bitmapCorner;
    private float bitmapDegrees;
    private boolean isCircle;

    private boolean isSkipMemoryCache;
    private boolean isSkipLocalCache;
    private int targetWidth;
    private int targetHeight;
    private Bitmap.Config bitmapConfig = Bitmap.Config.RGB_565;

    private ImageView targetView;
    private ImageLoaderCallBack callBack;

    public LoaderOptions(Context context) {
        this.context = context;
    }

    public LoaderOptions load(String url) {
        this.url = url;
        return this;
    }

    public LoaderOptions load(File file) {
        this.file = file;
        return this;
    }

    public LoaderOptions load(Uri uri) {
        this.uri = uri;
        return this;
    }

    public LoaderOptions load(byte[] bytes) {
        this.bytes = bytes;
        return this;
    }

    public LoaderOptions load(@DrawableRes int drawableResId) {
        LogUtils.info("加载drawableId：" + drawableResId);
        this.drawableResId = drawableResId;
        return this;
    }

    public LoaderOptions load(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    public LoaderOptions load(Object object) {
        if (object instanceof String) {
            return load((String) object);
        } else if (object instanceof Uri) {
            return load((Uri) object);
        } else if (object instanceof Bitmap) {
            return load((Bitmap) object);
        } else if (object instanceof File) {
            return load((File) object);
        } else if (object instanceof Integer) {
            return load((Integer) object);
        }
        return null;
    }

    public LoaderOptions placeholder(@DrawableRes int placeHolderResId) {
        this.placeHolderResId = placeHolderResId;
        return this;
    }

    public LoaderOptions placeholder(Drawable drawable) {
        this.placeHolderDrawable = drawable;
        return this;
    }

    public LoaderOptions error(@DrawableRes int errorResId) {
        this.errorResId = errorResId;
        return this;
    }

    public LoaderOptions error(Drawable drawable) {
        this.errorDrawable = drawable;
        return this;
    }

    public LoaderOptions isCenterCrop(boolean isCenterCrop) {
        this.isCenterCrop = isCenterCrop;
        return this;
    }

    public LoaderOptions isCenterInside(boolean isCenterInside) {
        this.isCenterInside = isCenterInside;
        return this;
    }

    public LoaderOptions setBitmapCorners(float bitmapCorner) {
        this.bitmapCorner = bitmapCorner;
        return this;
    }

    public LoaderOptions rotate(float bitmapDegrees) {
        this.bitmapDegrees = bitmapDegrees;
        return this;
    }

    public LoaderOptions isCircle(boolean isCircle) {
        this.isCircle = isCircle;
        return this;
    }

    public LoaderOptions skipMemoryCache(boolean isSkipMemoryCache) {
        this.isSkipMemoryCache = isSkipMemoryCache;
        return this;
    }

    public LoaderOptions isSkipLocalCache(boolean isSkipLocalCache) {
        this.isSkipLocalCache = isSkipLocalCache;
        return this;
    }

    public LoaderOptions bitmapResize(int targetWidth, int targetHeight) {
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        return this;
    }

    public LoaderOptions bitmapConfig(Bitmap.Config bitmapConfig) {
        this.bitmapConfig = bitmapConfig;
        return this;
    }

    public void into(ImageView targetView) {
        this.targetView = targetView;
        ImageLoader.getInstance().loadOptions(this);
    }

    public void callBack(ImageLoaderCallBack callBack) {
        this.callBack = callBack;
        ImageLoader.getInstance().loadOptions(this);
    }

    public Context getContext() {
        return context;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public File getFile() {
        return file;
    }

    public Uri getUri() {
        return uri;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getDrawableResId() {
        return drawableResId;
    }

    public int getPlaceHolderResId() {
        return placeHolderResId;
    }

    public int getErrorResId() {
        return errorResId;
    }

    public Drawable getPlaceHolderDrawable() {
        return placeHolderDrawable;
    }

    public Drawable getErrorDrawable() {
        return errorDrawable;
    }

    public boolean isCenterCrop() {
        return isCenterCrop;
    }

    public boolean isCenterInside() {
        return isCenterInside;
    }

    public float getBitmapCorner() {
        return bitmapCorner;
    }

    public float getBitmapDegrees() {
        return bitmapDegrees;
    }

    public boolean isCircle() {
        return isCircle;
    }

    public boolean isSkipMemoryCache() {
        return isSkipMemoryCache;
    }

    public boolean isSkipLocalCache() {
        return isSkipLocalCache;
    }

    public int getTargetWidth() {
        return targetWidth;
    }

    public int getTargetHeight() {
        return targetHeight;
    }

    public Bitmap.Config getBitmapConfig() {
        return bitmapConfig;
    }

    public ImageView getTargetView() {
        return targetView;
    }

    public ImageLoaderCallBack getCallBack() {
        return callBack;
    }
}
