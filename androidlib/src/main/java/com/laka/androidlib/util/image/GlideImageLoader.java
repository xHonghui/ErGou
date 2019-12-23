package com.laka.androidlib.util.image;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.laka.androidlib.R;
import com.laka.androidlib.callback.IImageLoaderStrategy;
import com.laka.androidlib.callback.ImageLoaderCallBack;
import com.laka.androidlib.util.ContextUtil;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.ResourceUtils;
import com.laka.androidlib.util.rx.RxSchedulerComposer;

import java.io.ByteArrayOutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @Author:Rayman
 * @Date:2018/6/28
 * @Description:Glide图片加载器
 */

public class GlideImageLoader implements IImageLoaderStrategy {

    private final String TAG = this.getClass().getSimpleName();
    private static final String CONER_CODER_KEY = "0x22233344";
    private static final String TRANS_CODER_KEY = "0x1112233";

    private Context context;
    private RequestManager requestManager;
    private DrawableTypeRequest request = null;

    public GlideImageLoader(Context context) {
        this.context = context;
        requestManager = Glide.with(context);
    }

    @Override
    public void loadImage(final LoaderOptions options) {

        if (ContextUtil.isValidContext(options.getContext())) {
            requestManager = Glide.with(options.getContext());
        }

        if (options.getUrl() != null) {
            request = requestManager.load(options.getUrl());
        } else if (options.getFile() != null) {
            request = requestManager.load(options.getFile());
        } else if (options.getUri() != null) {
            request = requestManager.load(options.getUri());
        } else if (options.getDrawableResId() != 0) {
            LogUtils.info("加载drawable数据：" + options.getDrawableResId());
            request = requestManager.load(options.getDrawableResId());
        } else if (options.getBitmap() != null) {
            //加载Bitmap的时候，需要转换成byte数组
            byte[] bytes = BitmapUtils.transBitmapToByte(options.getBitmap(), false);
            LogUtils.info("加载Bitmap数据");
            for (byte byte1 : bytes) {
                LogUtils.info("输出byte：" + byte1);
            }
            request = requestManager.load(BitmapUtils.transBitmapToByte(options.getBitmap(), false));
        } else if (options.getBytes() != null) {
            byte[] bytes = options.getBytes();
            request = requestManager.load(bytes);
        }
        //如果前面都不符合条件，给个默认的空字符串
        if (request == null) {
            request = requestManager.load("");
        }
        if (options.getPlaceHolderResId() != 0) {
            request.placeholder(options.getPlaceHolderResId());
        }

        if (options.getErrorResId() != 0) {
            request.error(options.getErrorResId());
        }

        if (options.isCenterCrop()) {
            request.centerCrop();
        }

        if (options.isCenterInside()) {
            request.fitCenter();
        }

        if (options.getBitmapCorner() != 0) {
            request.bitmapTransform(new RoundCornerBitmapTransform(
                    context,
                    options.getBitmapConfig(),
                    options.getBitmapCorner()));
        }

        if (options.isCircle()) {
            request.bitmapTransform(new CircleImageBitmapTransform(
                    context,
                    options.getBitmapConfig()));
        }

        request.skipMemoryCache(options.isSkipMemoryCache());
        if (options.isSkipLocalCache()) {
            request.diskCacheStrategy(DiskCacheStrategy.NONE);
        }

        if (options.getTargetWidth() > 0 && options.getTargetHeight() > 0) {
            request.into(new GlideTargetView(options.getTargetWidth(), options.getTargetHeight(), options.getCallBack()));
        }

        if (options.getTargetView() != null) {
            request.into(options.getTargetView());
        }

        if (options.getCallBack() != null) {
            request.into(new GlideTargetView(options.getCallBack()));
        }

    }

    @Override
    public void clearDiskCache() {

    }

    @Override
    public void clearMemoryCache() {

    }

    @Override
    public void pauseRequest() {
        requestManager.pauseRequests();
    }

    @Override
    public void resumeRequest() {
        requestManager.resumeRequests();
    }

    /**
     * GlideTargetView，用于做后期的处理。例如回调，裁剪等
     */
    class GlideTargetView extends SimpleTarget {

        private int targetWidth, targetHeight;
        ImageLoaderCallBack callBack;

        public GlideTargetView(ImageLoaderCallBack callBack) {
            this.callBack = callBack;
        }

        public GlideTargetView(int targetWidth, int targetHeight, ImageLoaderCallBack callBack) {
            super(targetWidth, targetHeight);
            this.targetWidth = targetWidth;
            this.targetHeight = targetHeight;
            this.callBack = callBack;
        }

        @Override
        public void onLoadStarted(Drawable drawable) {

        }

        @Override
        public void onLoadFailed(Exception e, Drawable drawable) {
            if (callBack != null) {
                callBack.onBitmapFailed(e);
            }
        }

        @Override
        public void onResourceReady(Object object, GlideAnimation glideAnimation) {
            if (callBack != null) {
                Drawable drawable = (Drawable) object;
                if (targetWidth != 0 && targetHeight != 0) {
                    drawable.setBounds(0, 0, targetWidth, targetHeight);
                }
                callBack.onBitmapLoaded(drawable);
            }
        }

        @Override
        public void onLoadCleared(Drawable drawable) {

        }

        @Override
        public void onStart() {
        }

        @Override
        public void onStop() {
        }

        @Override
        public void onDestroy() {
        }
    }

    /**
     * 圆角裁剪
     */
    class RoundCornerBitmapTransform extends BitmapTransformation {

        private Bitmap.Config bitmapConfig;
        private float radius = 0f;

        public RoundCornerBitmapTransform(Context context, Bitmap.Config bitmapConfig, float dp) {
            super(context);
            this.bitmapConfig = bitmapConfig;
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i1) {
            if (bitmap == null) {
                return null;
            }

            Bitmap result = bitmapPool.get(bitmap.getWidth(), bitmap.getHeight(),
                    bitmapConfig == null ? Bitmap.Config.ARGB_8888 : bitmapConfig);
            if (result == null) {
                result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                        bitmapConfig == null ? Bitmap.Config.ARGB_8888 : bitmapConfig);
            }

            //使用BitmapShader裁剪
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(bitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return CONER_CODER_KEY;
        }
    }

    /**
     * 圆形裁剪
     */
    class CircleImageBitmapTransform extends BitmapTransformation {

        private Bitmap.Config bitmapConfig;

        public CircleImageBitmapTransform(Context context, Bitmap.Config bitmapConfig) {
            super(context);
            this.bitmapConfig = bitmapConfig;
        }

        @Override
        protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i1) {
            if (bitmap == null) {
                return null;
            }

            int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
            int x = (bitmap.getWidth() - size) / 2;
            int y = (bitmap.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(bitmap, x, y, size, size);

            Bitmap result = bitmapPool.get(size, size,
                    bitmapConfig == null ? Bitmap.Config.ARGB_8888 : bitmapConfig);
            if (result == null) {
                result = Bitmap.createBitmap(size, size,
                        bitmapConfig == null ? Bitmap.Config.ARGB_8888 : bitmapConfig);
            }

            //使用BitmapShader绘制裁剪
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        /**
         * GetId不能为空，否则会抛出一个空指针转换异常
         *
         * @return
         */
        @Override
        public String getId() {
            return TRANS_CODER_KEY;
        }
    }
}
