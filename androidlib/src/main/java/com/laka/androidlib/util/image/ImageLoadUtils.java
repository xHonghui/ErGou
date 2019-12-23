package com.laka.androidlib.util.image;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.laka.androidlib.util.screen.ScreenUtils;
import com.laka.androidlib.util.StringUtils;
import com.laka.androidlib.util.ViewUtils;

/**
 * @ClassName: ImageLoadUtils
 * @Description: 图片加载工具
 * @Author: chuan
 * @Date: 22/01/2018
 */
public final class ImageLoadUtils {
    private static final String TAG = ImageLoadUtils.class.getSimpleName();

    //淡入动画时间
    private static final int FADE_IN_DURATION_MILLIS = 250;

    //本地uri
    private static final String PREFIX_URI_LOCAL = "file://";
    //res uri
    private static final String PREFIX_URI_RES = "res:///";
    //asset uri
    private static final String PREFIX_URI_ASSET = "asset:///";

    /**
     * 七牛服务器域名
     */
    public static final String QINIU_CDN_HOST_NAME = "7xrkjc.com2.z0.glb.qiniucdn.com";

    private ImageLoadUtils() {
        throw new UnsupportedOperationException("do not instantiate me , please.");
    }

    /**
     * 清除指定url的图片缓存
     *
     * @param imageUri 图片uri
     */
    public static void clearCache(String imageUri) {
        if (StringUtils.isTrimEmpty(imageUri)) {
            return;
        }

        Fresco.getImagePipeline().evictFromCache(Uri.parse(imageUri));
    }

    /**
     * 加载res资源
     *
     * @param imageView SimpleDraweeView
     * @param resId     res资源
     */
    public static void loadResImage(SimpleDraweeView imageView, int resId) {
        loadResImage(imageView, resId, false);
    }

    /**
     * 加载res资源
     *
     * @param imageView     SimpleDraweeView
     * @param resId         res资源
     * @param playAnimation 是否播放加载动画效果
     */
    public static void loadResImage(SimpleDraweeView imageView, int resId,
                                    boolean playAnimation) {
        loadResImage(imageView, resId, playAnimation, null);
    }

    /**
     * 加载res资源
     *
     * @param imageView     SimpleDraweeView
     * @param resId         res资源id
     * @param playAnimation 是否播放加载动画效果
     * @param listener      图片加载控制监听
     */
    public static void loadResImage(SimpleDraweeView imageView, int resId,
                                    boolean playAnimation, ControllerListener listener) {
        loadImage(imageView, PREFIX_URI_RES + resId, playAnimation, listener);
    }

    /**
     * 加载asset资源
     *
     * @param imageView SimpleDraweeView
     * @param path      res资源路径
     */
    public static void loadAssetsImage(SimpleDraweeView imageView, String path) {
        loadAssetsImage(imageView, path, false);
    }

    /**
     * 加载asset资源
     *
     * @param imageView     SimpleDraweeView
     * @param path          res资源路径
     * @param playAnimation 是否播放加载动画效果
     */
    public static void loadAssetsImage(SimpleDraweeView imageView, String path,
                                       boolean playAnimation) {
        loadAssetsImage(imageView, path, playAnimation, null);
    }

    /**
     * 加载asset资源
     *
     * @param imageView     SimpleDraweeView
     * @param path          asset资源路径
     * @param playAnimation 是否播放加载动画效果
     * @param listener      图片加载控制监听
     */
    public static void loadAssetsImage(SimpleDraweeView imageView, String path,
                                       boolean playAnimation, ControllerListener listener) {
        loadImage(imageView, PREFIX_URI_ASSET + path, playAnimation, listener);
    }

    /**
     * 加载本地资源
     *
     * @param imageView SimpleDraweeView
     * @param uri       本地资源uri
     */
    public static void loadLocalImage(SimpleDraweeView imageView, String uri) {
        loadLocalImage(imageView, uri, false);
    }

    /**
     * 加载本地资源
     *
     * @param imageView     SimpleDraweeView
     * @param uri           本地资源uri
     * @param playAnimation 是否播放加载动画效果
     */
    public static void loadLocalImage(SimpleDraweeView imageView, String uri,
                                      boolean playAnimation) {
        loadLocalImage(imageView, uri, playAnimation, null);
    }

    /**
     * 加载本地资源
     *
     * @param imageView     SimpleDraweeView
     * @param uri           本地资源uri
     * @param playAnimation 是否播放加载动画效果
     * @param listener      图片加载控制监听
     */
    public static void loadLocalImage(SimpleDraweeView imageView, String uri,
                                      boolean playAnimation, ControllerListener listener) {
        loadImage(imageView, PREFIX_URI_LOCAL + uri, playAnimation, listener);
    }

    /**
     * 加载图片资源
     *
     * @param imageView SimpleDraweeView
     * @param url       资源url
     */
    public static void loadImage(SimpleDraweeView imageView, String url) {
        loadImage(imageView, url, false);
    }

    /**
     * 加载图片资源
     *
     * @param imageView     SimpleDraweeView
     * @param url           资源url
     * @param playAnimation 是否播放加载动画效果
     */
    public static void loadImage(SimpleDraweeView imageView, String url, boolean playAnimation) {
        loadImage(imageView, url, playAnimation, null);
    }

    /**
     * 加载图片资源
     *
     * @param imageView SimpleDraweeView
     * @param url       资源url
     * @param listener  图片加载控制监听
     */
    public static void loadImage(SimpleDraweeView imageView, String url, boolean playAnimation,
                                 ControllerListener listener) {
        loadImage(imageView, url, playAnimation, listener, false, null);
    }

    /**
     * 加载图片资源
     *
     * @param imageView         SimpleDraweeView
     * @param url               资源url
     * @param playAnimation     是否播放加载动画效果
     * @param listener          图片加载控制监听
     * @param tapToRetryEnabled 是否重试
     * @param processor         加载进度条
     */
    public static void loadImage(final SimpleDraweeView imageView, String url, boolean playAnimation,
                                 ControllerListener listener, boolean tapToRetryEnabled,
                                 BasePostprocessor processor) {
        if (StringUtils.isTrimEmpty(url) || imageView == null) {
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

        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();
        if (!StringUtils.isTrimEmpty(scheme)
                && ((scheme.equalsIgnoreCase("http") ||
                scheme.equalsIgnoreCase("https")))
                && StringUtils.equals(QINIU_CDN_HOST_NAME, uri.getHost())) {
            uri = buildQiniuUri(url, width, height);
        }

        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uri)
                .setRotationOptions(RotationOptions.autoRotate())
                .setPostprocessor(processor)
                .setLocalThumbnailPreviewsEnabled(true)
                .setResizeOptions(new ResizeOptions(width, height));


        GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
        hierarchy.setFadeDuration(FADE_IN_DURATION_MILLIS);
        AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(imageView.getController())
                .setControllerListener(listener)
                .setImageRequest(imageRequestBuilder.build())
                .setAutoPlayAnimations(playAnimation)
                .setTapToRetryEnabled(tapToRetryEnabled)
                .build();
        imageView.setController(controller);
    }

    /**
     * 创建七牛的服务器uri，添加图片的宽和高
     *
     * @param url    图片的原url
     * @param width  图片的宽
     * @param height 图片的高
     * @return 添加图片宽和高的uri
     */
    public static Uri buildQiniuUri(String url, int width, int height) {
        return Uri.parse(new StringBuilder()
                .append(url)
                .append("?imageView2/0/w/")
                .append(width)
                .append("/h/")
                .append(height)
                .toString());
    }
}
