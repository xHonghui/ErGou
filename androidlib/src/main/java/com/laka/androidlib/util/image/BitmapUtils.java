package com.laka.androidlib.util.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.view.View;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.laka.androidlib.listener.IBitmapGainListener;
import com.laka.androidlib.util.ApplicationUtils;
import com.laka.androidlib.util.FileUtils;
import com.laka.androidlib.util.IOUtils;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.screen.ScreenUtils;
import com.laka.androidlib.util.StringUtils;
import com.laka.androidlib.util.ViewUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @ClassName: BitmapUtils
 * @Description: 对bitmap操作的工具类
 * @Author: chuan
 * @Date: 23/01/2018
 */

/**
 * 封装对bitmap的相关操作。
 * bitmap自android 3.0后存储在程序的虚拟机堆上。很容易造成oom，所以使用的时候需谨慎。
 * 本类中的一些方法（主要是获取bitmap是没有进行压缩的时候），没有对bitmap的大小进行判断，所以有造成oom的风险，请谨慎使用。
 * 有很多建议将图片的色彩模式从Android默认的ARGB_8888改为RGB_565,可减少一半的大小，但清晰度也相应打折，本类不采用。
 */

public final class BitmapUtils {
    private final static String TAG = BitmapUtils.class.getSimpleName();

    private BitmapUtils() {
        throw new UnsupportedOperationException("do not instantiate me , please.");
    }

    /**
     * 获取bitmap，通过imageView获取图片宽高
     *
     * @param url       资源url
     * @param imageView SimpleDraweeView
     * @param listener  获取结果回调
     */
    public static void gainBitmapFromUrl(String url, SimpleDraweeView imageView,
                                         final IBitmapGainListener listener) {
        if (StringUtils.isTrimEmpty(url) || imageView == null) {
            if (listener != null) {
                listener.onFail(IBitmapGainListener.FAIL_CODE_URL_NULL, null);
            }
            return;
        }

        int width = ViewUtils.getWidth(imageView);
        int height = ViewUtils.getHeight(imageView);

        if (width <= 0) {
            width = ScreenUtils.getScreenWidth() / 2;
        }
        if (height <= 0) {
            height = ScreenUtils.getScreenHeight() / 2;
        }

        gainBitmapFromUrl(url, width, height, listener);
    }

    /**
     * 获取bitmap
     *
     * @param url      资源url
     * @param width    图片的宽
     * @param height   图片的高
     * @param listener 获取结果回调
     */
    public static void gainBitmapFromUrl(String url, final int width, final int height,
                                         final IBitmapGainListener listener) {
        if (StringUtils.isTrimEmpty(url)) {
            if (listener != null) {
                listener.onFail(IBitmapGainListener.FAIL_CODE_URL_NULL, null);
            }
            return;
        }

        Uri uri = Uri.parse(url);
        ImageRequest imageRequest;
        if (width != 0 && height != 0) {
            uri = ImageLoadUtils.buildQiniuUri(url, width, height);
            imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
        } else {
            imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)
                    .build();
        }

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, ApplicationUtils.getApplication());

        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
            @Override
            public void onNewResultImpl(
                    DataSource<CloseableReference<CloseableImage>> dataSource) {

                if (!dataSource.isFinished()) {
                    LogUtils.debug(TAG, "Not yet finished - this is just another progressive scan.");
                }

                CloseableReference<CloseableImage> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    try {
                        CloseableBitmap image = (CloseableBitmap) imageReference.get();
                        Bitmap loadedImage = image.getUnderlyingBitmap();
                        if (loadedImage != null) {
                            listener.onSuccess(loadedImage);
                        } else {
                            listener.onFail(IBitmapGainListener.FAIL_CODE_OTHER, null);
                        }
                    } finally {
                        imageReference.close();
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                if (listener != null) {
                    listener.onFail(IBitmapGainListener.FAIL_CODE_OTHER,
                            dataSource.getFailureCause());
                }
            }
        }, CallerThreadExecutor.getInstance());
    }

    /**
     * 警告：容易造成oom
     * 从Content uri中获取bitmap.
     * 没有对bitmap进行压缩，所以得到的bitmap大小是未知的。请确保读入的bitmap较小，否则易造成oom
     *
     * @param uri Content Uri
     * @return 获取到的bitmap.或者，当uri为null的时候，返回null
     */
    public static Bitmap gainBitmapFromContentUri(Uri uri) {
        if (uri == null || StringUtils.isTrimEmpty(uri.toString())) {
            return null;
        }

        Bitmap bitmap;

        try {
            bitmap = BitmapFactory.decodeStream(ApplicationUtils.getApplication()
                    .getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return bitmap;
    }

    /**
     * 从Content uri中获取bitmap.针对传入的宽和高进行压缩.
     * 此压缩只会改变获取到的bitmap，不会改变原文件大小。
     *
     * @param uri    Content Uri
     * @param width  期望bitmap的宽，便于对bitmap进行压缩
     * @param height 期望bitmap的高，便于对bitmap进行压缩
     * @return 获取到的bitmap.或者，当uri为null的时候，返回null
     */
    public static Bitmap gainBitmapFromContentUri(Uri uri, int width, int height) {
        if (uri == null || StringUtils.isTrimEmpty(uri.toString())) {
            return null;
        }

        Bitmap bitmap;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(ApplicationUtils.getApplication().getContentResolver()
                    .openInputStream(uri), null, opts);
            opts.inSampleSize = calculateInSampleSize(opts, width, height);
            opts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(ApplicationUtils.getApplication().getContentResolver()
                    .openInputStream(uri), null, opts);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 警告：容易造成oom
     * 从Resource中获取bitmap.
     * 没有对bitmap进行压缩，所以得到的bitmap大小是未知的。请确保读入的bitmap较小，否则易造成oom
     *
     * @param drawableId Resource id
     * @return 获取到的bitmap.或者，当drawableId不可用的时候，返回null
     */
    public static Bitmap gainBitmapFromDrawable(int drawableId) {
        if (drawableId <= 0) {
            return null;
        }

        return BitmapFactory.decodeResource(
                ApplicationUtils.getApplication().getResources(), drawableId);
    }

    /**
     * 从Resource中获取bitmap.针对传入的宽和高进行压缩.
     * 此压缩只会改变获取到的bitmap，不会改变原文件大小。
     *
     * @param drawableId Resource id
     * @param width      期望bitmap的宽，便于对bitmap进行压缩
     * @param height     期望bitmap的高，便于对bitmap进行压缩
     * @return 获取到的bitmap.或者，当drawableId不可用的时候，返回null
     */
    public static Bitmap gainBitmapFromDrawable(int drawableId,
                                                int width, int height) {
        if (drawableId <= 0) {
            return null;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(ApplicationUtils.getApplication().getResources(), drawableId, opts);
        opts.inSampleSize = calculateInSampleSize(opts, width, height);
        opts.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(ApplicationUtils.getApplication().getResources(), drawableId, opts);
    }

    /**
     * 警告：容易造成oom
     * 从文件中获取bitmap.
     * 没有对bitmap进行压缩，所以得到的bitmap大小是未知的。请确保读入的bitmap较小，否则易造成oom
     *
     * @param file File文件
     * @return 获取到的bitmap.或者，当文件不可用的时候，返回null
     */
    public static Bitmap gainBitmapFromFile(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }

        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    /**
     * 从文件中获取bitmap.针对传入的宽和高进行压缩.
     * 此压缩只会改变获取到的bitmap，不会改变原文件大小。
     *
     * @param file   File文件
     * @param width  期望bitmap的宽，便于对bitmap进行压缩
     * @param height 期望bitmap的高，便于对bitmap进行压缩
     * @return 获取到的bitmap.或者，当文件不可用的时候，返回null
     */
    public static Bitmap gainBitmapFromFile(File file, int width, int height) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
        opts.inSampleSize = calculateInSampleSize(opts, width, height);
        opts.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
    }

    /**
     * 警告：容易造成oom
     * 从byte数组中获取bitmap.
     * 没有对bitmap进行压缩，所以得到的bitmap大小是未知的。请确保读入的bitmap较小，否则易造成oom
     *
     * @param bytes byte数组
     * @return 获取到的bitmap.或者，当byte数组为空的时候，返回null
     */
    public static Bitmap gainBitmapFromByte(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 从byte数组中获取bitmap.针对传入的宽和高进行压缩.
     * 此压缩只会改变获取到的bitmap，不会改变原文件大小。
     *
     * @param bytes  byte数组
     * @param width  期望bitmap的宽，便于对bitmap进行压缩
     * @param height 期望bitmap的高，便于对bitmap进行压缩
     * @return 获取到的bitmap.或者，当byte数组为空的时候，返回null
     */
    public static Bitmap gainBitmapFromByte(byte[] bytes, int width, int height) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        opts.inSampleSize = calculateInSampleSize(opts, width, height);
        opts.inJustDecodeBounds = false;

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
    }

    /**
     * 从View中获取Bitmap
     * 使用RGB_565的色彩模式，减少Bitmap的大小
     *
     * @param view 获取Bitmap的View
     * @return 获取到的bitmap.或者，当View为空的时候，返回null
     */
    public static Bitmap gainBitmapFromView(View view) {
        if (view == null) {
            return null;
        }

        int maxWidth = ScreenUtils.getScreenWidth();
        int maxHeight = ScreenUtils.getScreenHeight();

        int width = view.getWidth();
        int height = view.getHeight();

        if (width <= 0 || height <= 0) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(
                width > maxWidth ? maxWidth : width,
                height > maxHeight ? maxHeight : height,
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

//    /**
//     * 从ViewGroup中获取Bitmap
//     * 使用RGB_565的色彩模式，减少Bitmap的大小
//     *
//     * @param viewGroup 获取Bitmap的ViewGroup
//     * @return 获取到的bitmap.或者，当ViewGroup为空的时候，返回null
//     */
//    public static Bitmap gainBitmapFromView(ViewGroup viewGroup) {
//        if (viewGroup == null) {
//            return null;
//        }
//
//        int height = 0;
//        int width = 0;
//
//        for (int i = 0; i < viewGroup.getChildCount(); i++) {
//            View view = viewGroup.getChildAt(i);
//            height = height + view.getWidth();
//            width = width + view.getHeight();
//        }
//
//        if (width <= 0 || height <= 0) {
//            return null;
//        }
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bitmap);
//        viewGroup.draw(canvas);
//
//        return bitmap;
//    }

    /**
     * 转化Bitmap为圆角型Bitmap
     *
     * @param bitmap      待转换的Bitmap
     * @param radius      圆角
     * @param needRecycle 是否需要回收原Bitmap
     * @return 转化成功的Bitmap，或者，当原Bitmap为空或已经被回收，返回null
     */
    public static Bitmap gainRoundedCornerBitmap(Bitmap bitmap, float radius,
                                                 boolean needRecycle) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        if (needRecycle && bitmap != output && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        return output;
    }

    /**
     * 转化Bitmap为圆型Bitmap
     *
     * @param bitmap      待转换的Bitmap
     * @param needRecycle 是否需要回收原Bitmap
     * @return 转化成功的Bitmap，或者，当原Bitmap为空或已经被回收，返回null
     */
    public static Bitmap gainCircularBitmap(Bitmap bitmap, boolean needRecycle) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());

        int halfH = bitmap.getHeight() >> 2;
        int halfW = bitmap.getWidth() >> 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(halfW, halfH, halfW > halfH ? halfH : halfW, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        if (needRecycle && bitmap != output && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        return output;
    }

    /**
     * 将bitmap转化为byte[]
     * 转化时采用png格式，并且不进行压缩处理
     *
     * @param bitmap      要转化的bitmap
     * @param needRecycle 是否回收bitmap
     * @return 转化成功的byte[]，或null
     */
    public static byte[] transBitmapToByte(Bitmap bitmap, boolean needRecycle) {
        return transBitmapToByte(bitmap, Bitmap.CompressFormat.PNG, needRecycle);
    }

    /**
     * 将bitmap转化为byte[]
     * 不进行压缩处理
     *
     * @param bitmap      要转化的bitmap
     * @param needRecycle 是否回收bitmap
     * @return 转化成功的byte[]，或null
     */
    public static byte[] transBitmapToByte(Bitmap bitmap, Bitmap.CompressFormat format, boolean needRecycle) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(format, 100, out);

        if (needRecycle) {
            bitmap.recycle();
        }

        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return out.toByteArray();
    }

    /**
     * 缩放Bitmap
     * 不会改变原文件中的大小，只改变内存中的Bitmap的大小
     *
     * @param bitmap      原Bitmap
     * @param newWidth    目标Bitmap的宽
     * @param newHeight   目标Bitmap的高
     * @param needRecycle 是否需要回收原Bitmap
     * @return 缩放后的Bitmap ， 或者，如果原Bitmap为null或已经被回收，返回null
     */
    private static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight,
                                      boolean needRecycle) {

        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= 0 || height <= 0) {
            return null;
        }

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        return scaleBitmap(bitmap, scaleWidth, scaleHeight, needRecycle);
    }

    /**
     * 缩放Bitmap
     * 不会改变原文件中的大小，只改变内存中的Bitmap的大小
     *
     * @param bitmap      原Bitmap
     * @param scale       缩放比例
     * @param needRecycle 是否需要回收原Bitmap
     * @return 缩放后的Bitmap ， 或者，如果原Bitmap为null或已经被回收，返回null
     */
    private static Bitmap scaleBitmap(Bitmap bitmap, float scale,
                                      boolean needRecycle) {

        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        return scaleBitmap(bitmap, scale, scale, needRecycle);
    }

    /**
     * 压缩Bitmap，返回压缩后的Bitmap的byte数组
     * 不会改变内存中Bitmap的大小
     *
     * @param bitmap      待压缩的Bitmap
     * @param quality     压缩质量。[0,100]，100表示不压缩
     * @param needRecycle 是否需要回收原Bitmap
     * @return 压缩后的byte数组。或者，如果原Bitmap为null或已经被回收，或者压缩质量小于0，将返回null
     */
    public static byte[] compressBitmapToByte(Bitmap bitmap, int quality,
                                              boolean needRecycle) {
        if (bitmap == null || bitmap.isRecycled() || quality <= 0) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

        if (needRecycle && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        return baos.toByteArray();
    }

    /**
     * 压缩Bitmap，返回压缩后的Bitmap的byte数组
     * 不会改变内存中Bitmap的大小
     *
     * @param bitmap      待压缩的Bitmap
     * @param maxSize     压缩后的大小
     * @param needRecycle 是否需要回收原Bitmap
     * @return 压缩后的byte数组。或者，如果原Bitmap为null或已经被回收，或者压缩质量小于0，将返回null
     */
    public static byte[] compressBitmapToByte(Bitmap bitmap, long maxSize,
                                              boolean needRecycle) {
        if (bitmap == null || bitmap.isRecycled() || maxSize <= 0) {
            return null;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] output = null;

        maxSize = maxSize << 20;

        for (int quality = 100; quality >= 0; quality = quality - 5) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            output = baos.toByteArray();
            baos.reset();

            if (output.length <= maxSize) {
                break;
            }
        }

        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (needRecycle && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        return output;
    }

    /**
     * 压缩Bitmap，返回压缩后的Bitmap
     * 不会改变内存中Bitmap的大小
     *
     * @param bitmap      待压缩的Bitmap
     * @param quality     压缩质量。[0,100]，100表示不压缩
     * @param needRecycle 是否需要回收原Bitmap
     * @return 压缩后的Bitmap。或者，如果原Bitmap为null或已经被回收，或者压缩质量小于0，将返回null
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int quality,
                                        boolean needRecycle) {
        return gainBitmapFromByte(compressBitmapToByte(bitmap, quality, needRecycle));
    }

    /**
     * 压缩Bitmap，返回压缩后的Bitmap
     * 不会改变内存中Bitmap的大小
     *
     * @param bitmap      待压缩的Bitmap
     * @param maxSize     压缩后的大小
     * @param needRecycle 是否需要回收原Bitmap
     * @return 压缩后的Bitmap。或者，如果原Bitmap为null或已经被回收，或者压缩质量小于0，将返回null
     */
    public static Bitmap compressBitmap(Bitmap bitmap, long maxSize,
                                        boolean needRecycle) {
        return gainBitmapFromByte(compressBitmapToByte(bitmap, maxSize, needRecycle));
    }

    /**
     * 保存Bitmap到文件
     *
     * @param file        需要保存到的文件
     * @param bitmap      需要保存的Bitmap
     * @param needRecycle 是否需要回收原Bitmap
     * @return 保存成功：true；保存失败：false
     */
    public static boolean saveBitmap(File file, Bitmap bitmap, boolean needRecycle) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return false;
        }

        String format = FileUtils.getFileFormatFromPath(file.getPath());

        if (StringUtils.equalsIgnoreCase("PNG", format)) {
            return saveBitmap(file, bitmap, Bitmap.CompressFormat.PNG, needRecycle);
        } else if (StringUtils.equalsIgnoreCase("JPG", format)
                || StringUtils.equalsIgnoreCase("JPEG", format)) {
            return saveBitmap(file, bitmap, Bitmap.CompressFormat.JPEG, needRecycle);
        }
        return false;
    }

    /**
     * 保存Bitmap到文件
     *
     * @param file        需要保存到的文件
     * @param bitmap      需要保存的Bitmap
     * @param format      需要保存的格式
     * @param needRecycle 是否需要回收原Bitmap
     * @return 保存成功：true；保存失败：false
     */
    public static boolean saveBitmap(File file, Bitmap bitmap, Bitmap.CompressFormat format, boolean needRecycle) {
        return IOUtils.writeFile(file, transBitmapToByte(bitmap, format, needRecycle));
    }

    /**
     * 计算bitmap的采样率，既压缩率。
     *
     * @param options   bitmap的参数
     * @param reqWidth  实际需要的bitmap的宽
     * @param reqHeight 实际需要的bitmap的高
     * @return 采样率
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        return calculateInSampleSize(options.outWidth,
                options.outHeight, reqWidth, reqHeight);
    }

    /**
     * 计算bitmap的采样率，既压缩率。
     *
     * @param originalWidth  原bitmap的宽
     * @param originalHeight 原bitmap的高
     * @param reqWidth       实际需要的bitmap的宽
     * @param reqHeight      实际需要的bitmap的高
     * @return 采样率
     */
    private static int calculateInSampleSize(int originalWidth, int originalHeight,
                                             int reqWidth, int reqHeight) {
        int inSampleSize = 1;

        //计算采样率，取较小值
        if (originalHeight > reqHeight || originalWidth > reqWidth) {
            final int heightRatio = Math.round((float) originalHeight / (float) reqHeight);
            final int widthRatio = Math.round((float) originalWidth / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        //针对某些宽或高特别大的bitmap，再计算采样率
        final float totalPixels = originalWidth * originalHeight;
        final float totalReqPixelsCap = (reqWidth * reqHeight) << 1;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize = inSampleSize << 1;
        }

        return inSampleSize;
    }

    /**
     * 缩放Bitmap
     *
     * @param bitmap      原Bitmap
     * @param scaleWidth  宽的缩放比例
     * @param scaleHeight 高的缩放比例
     * @param needRecycle 是否需要回收原Bitmap
     * @return 缩放后的Bitmap ， 或者，如果原Bitmap为null或已经被回收，返回null
     */
    private static Bitmap scaleBitmap(Bitmap bitmap, float scaleWidth, float scaleHeight,
                                      boolean needRecycle) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap output = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        if (needRecycle && bitmap != output && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return output;

    }
    /**
     * 从path中获取图片信息,在通过BitmapFactory.decodeFile(String path)方法将突破转成Bitmap时，
     * 遇到大一些的图片，我们经常会遇到OOM(Out Of Memory)的问题。所以用到了我们上面提到的BitmapFactory.Options这个类。
     *
     * @param path   文件路径
     * @param width  想要显示的图片的宽度
     * @param height 想要显示的图片的高度
     * @return
     */
    public static Bitmap decodeBitmap(String path, int width, int height) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        // inJustDecodeBounds如果设置为true,仅仅返回图片实际的宽和高,宽和高是赋值给opts.outWidth,opts.outHeight;
        op.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, op); //获取尺寸信息
        //获取比例大小
        int wRatio = (int) Math.ceil(op.outWidth / width);
        int hRatio = (int) Math.ceil(op.outHeight / height);
        //如果超出指定大小，则缩小相应的比例
        if (wRatio > 1 && hRatio > 1) {
            if (wRatio > hRatio) {
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, op);
        return bmp;
    }
}
