package com.laka.androidlib.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.laka.androidlib.util.screen.ScreenUtils;
import com.laka.androidlib.util.transform.GlideRoundTransform;

/**
 * @Author:summer
 * @Date:2019/1/11
 * @Description: Glide 工具类
 */
public class GlideUtil {

    private static int sRadius = 5;

    public static BitmapRequestBuilder<String, Bitmap> loadImage(Context context, String url, ImageView imageView) {
        return loadImage(context, url, -1, imageView);
    }

    public static BitmapRequestBuilder<Integer, Bitmap> loadImage(Context context, int targetRes, ImageView imageView) {
        return loadImage(context, targetRes, -1, imageView);
    }

    public static BitmapRequestBuilder<String, Bitmap> loadImage(Context context, String targetUrl, int placeholderRes, ImageView imageView) {
        return loadImage(context, targetUrl, placeholderRes, placeholderRes, imageView);
    }

    public static BitmapRequestBuilder<Integer, Bitmap> loadImage(Context context, int targetRes, int placeholderRes, ImageView imageView) {
        return loadImage(context, targetRes, placeholderRes, placeholderRes, imageView);
    }

    public static BitmapRequestBuilder<String, Bitmap> loadImageNoCrop(Context context, String targetUrl, int placeholderRes, ImageView imageView) {
        BitmapRequestBuilder<String, Bitmap> request = Glide.with(context).load(targetUrl).asBitmap();
        if (placeholderRes != -1) {
            request.placeholder(placeholderRes);
        }
        if (placeholderRes != -1) {
            request.error(placeholderRes);
        }
        // 设置缓存策略，只缓存原图
        request.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        request.into(imageView);
        return request;
    }

    public static BitmapRequestBuilder<String, Bitmap> loadImage(Context context, String targetUrl, int placeholderRes, int errorRes, ImageView imageView) {
        BitmapRequestBuilder<String, Bitmap> request = Glide.with(context).load(targetUrl).asBitmap().centerCrop();
        if (placeholderRes != -1) {
            request.placeholder(placeholderRes);
        }
        if (errorRes != -1) {
            request.error(errorRes);
        }
        // 设置缓存策略，只缓存原图
        request.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        request.into(imageView);
        return request;
    }

    public static BitmapRequestBuilder<Integer, Bitmap> loadImage(Context context, int targetRes, int placeholderRes, int errorRes, ImageView imageView) {
        BitmapRequestBuilder<Integer, Bitmap> request = Glide.with(context).load(targetRes).asBitmap().centerCrop();
        if (placeholderRes != -1) {
            request.placeholder(placeholderRes);
        }
        if (errorRes != -1) {
            request.error(errorRes);
        }
        // 设置缓存策略，只缓存原图
        request.diskCacheStrategy(DiskCacheStrategy.SOURCE);
        request.into(imageView);
        return request;
    }

    public static void loadCircleImage(Context context, String targetUrl, ImageView imageView) {
        BitmapRequestBuilder<String, Bitmap> requestBuilder = loadImage(context, targetUrl, imageView);
        circleIntoByString(context, requestBuilder, imageView);
    }

    public static void loadCircleImage(Context context, int targetRes, ImageView imageView) {
        BitmapRequestBuilder<Integer, Bitmap> requestBuilder = loadImage(context, targetRes, imageView);
        circleIntoByInt(context, requestBuilder, imageView);
    }

    public static void loadCircleImage(Context context, String targetUrl, int placeRes, ImageView imageView) {
        BitmapRequestBuilder<String, Bitmap> requestBuilder = loadImage(context, targetUrl, placeRes, imageView);
        circleIntoByString(context, requestBuilder, imageView);
    }

    private static void circleIntoByInt(final Context context, BitmapRequestBuilder<Integer, Bitmap> requestBuilder, final ImageView imageView) {
        requestBuilder.into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circleBatmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circleBatmapDrawable.setCircular(true);
                imageView.setImageDrawable(circleBatmapDrawable);
            }
        });
    }

    private static void circleIntoByString(final Context context, BitmapRequestBuilder<String, Bitmap> requestBuilder, final ImageView imageView) {
        requestBuilder.into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circleBatmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circleBatmapDrawable.setCircular(true);
                imageView.setImageDrawable(circleBatmapDrawable);
            }
        });
    }

    public static void loadCircleImage2(Context context, String targetUrl, ImageView imageView) {
        BitmapRequestBuilder<String, Bitmap> requestBuilder = loadImage(context, targetUrl, imageView);
        circleIntoByString(context, requestBuilder, imageView);
    }


    public static void circleInto2(final Context context, BitmapRequestBuilder<String, Bitmap> requestBuilder, final ImageView imageView) {
        requestBuilder.transform(new GlideCircleTransform(context)).centerCrop().into(imageView);
    }


    /**
     * 加载圆角图片方法一
     */
    public static void loadFilletImage(Context context, String targetUrl, int placeRes, int errorRes, final ImageView imageView) {
        loadFilletImage(context, targetUrl, placeRes, errorRes, imageView, sRadius);
    }

    /**
     * 指定圆角大小
     */
    public static void loadFilletImage(Context context, String targetUrl, int placeRes, int errorRes, final ImageView imageView, int filletSize) {
        Glide.with(context)
                .load(targetUrl)
                .transform(new CenterCrop(context), new GlideRoundTransform(context, filletSize))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeRes)
                .error(errorRes)
                .dontAnimate()
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载圆角图片方法二
     */
    public static void loadFilletImage(Context context, int targetUrl, int placeRes, int errorRes, final ImageView imageView) {
        Glide.with(context)
                .load(targetUrl)
                .transform(new CenterCrop(context), new GlideRoundTransform(context, sRadius))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeRes)
                .error(errorRes)
                .dontAnimate()
                .crossFade()
                .into(imageView);
    }

    /**
     * 加载圆形图片
     */
    static class GlideCircleTransform implements Transformation<Bitmap> {

        private BitmapPool mBitmapPool;

        public GlideCircleTransform(Context context) {
            this(Glide.get(context).getBitmapPool());
        }

        public GlideCircleTransform(BitmapPool pool) {
            this.mBitmapPool = pool;
        }

        @Override
        public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
            Bitmap source = resource.get();
            int size = Math.min(source.getWidth(), source.getHeight());
            int width = (source.getWidth() - size) / 2;
            int height = (source.getHeight() - size) / 2;

            Bitmap bitmap = mBitmapPool.get(size, size, Bitmap.Config.ARGB_8888);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader =
                    new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            if (width != 0 || height != 0) {
                // source isn't square, move viewport to center
                Matrix matrix = new Matrix();
                matrix.setTranslate(-width, -height);
                shader.setLocalMatrix(matrix);
            }
            paint.setShader(shader);
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return BitmapResource.obtain(bitmap, mBitmapPool);
        }

        @Override
        public String getId() {
            return "CropCircleTransformation()";
        }
    }


    /**
     * drawable 转换为 bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * 下载图片
     */
    public static void downloadBitmapForAdapterImg(Context context, String url, int defaultImg, final ImageView imageView) {
        Glide.with(context).load(url).asBitmap().placeholder(defaultImg).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource != null) {
                    LogUtils.info("bitmap----height=" + resource.getHeight() + "----width=" + resource.getWidth());
                    int width = ScreenUtils.getScreenWidth();
                    double rotate = resource.getHeight() / (double) resource.getWidth();
                    double height = width * rotate;
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                    params.width = width;
                    params.height = (int) height;
                    imageView.setLayoutParams(params);
                    imageView.setImageBitmap(resource);
                }
            }
        });
    }

}
