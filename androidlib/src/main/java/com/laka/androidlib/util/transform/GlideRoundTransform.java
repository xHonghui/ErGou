package com.laka.androidlib.util.transform;

/**
 * @Author:summer
 * @Date:2019/7/23
 * @Description:
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.laka.androidlib.util.screen.ScreenUtils;

/**
 * 加载圆角图片转换器，进行圆角裁剪，则不能用 RGB_565 的格式，否则会出现黑边
 */
public class GlideRoundTransform extends BitmapTransformation {

    private static int radius = ScreenUtils.dp2px(5);

    public GlideRoundTransform(Context context, int radius) {
        super(context);
        this.radius = ScreenUtils.dp2px(radius);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }
        cropFilletBitmap(source, result, radius);
//        cropBitmap(source, result, radius);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }

    /**
     * 裁剪圆角图片
     */
    private static void cropFilletBitmap(Bitmap source, Bitmap result, float radius) {
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void cropBitmap(Bitmap source, Bitmap result, float radius) {
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        Path path = new Path();
        path.moveTo(radius, 0);
        path.lineTo(source.getWidth() - radius, 0);
        path.arcTo(source.getWidth() - 2 * radius, 0, source.getWidth(), 2 * radius, 0, -90, false);
        path.lineTo(source.getWidth(), source.getHeight() - radius);
        path.arcTo(source.getWidth() - 2 * radius, source.getHeight() - 2 * radius, source.getWidth(), source.getHeight(), 0, 90, false);
        path.lineTo(radius, source.getHeight());
        path.arcTo(0, source.getHeight() - 2 * radius, 2 * radius, source.getHeight(), -90, -180, false);
        path.lineTo(100, radius);
        path.arcTo(0, 0, 2 * radius, 2 * radius, -180, -270, false);
        path.close();

        path.addCircle(source.getWidth() / 2, source.getHeight() / 2, source.getWidth() / 2, Path.Direction.CW);

        canvas.drawPath(path, paint);
    }
}
