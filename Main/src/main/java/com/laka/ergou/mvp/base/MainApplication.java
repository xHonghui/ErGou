package com.laka.ergou.mvp.base;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.ImageView;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.laka.androidlib.base.application.BaseApplication;
import com.laka.androidlib.util.InterceptorUtils;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.image.GlideImageLoader;
import com.laka.androidlib.util.image.ImageLoader;
import com.laka.ergou.BuildConfig;
import com.laka.ergou.common.interceptor.GlobalParamsInterceptor;
import com.laka.ergou.common.interceptor.IllegalTokenInterceptor;
import com.laka.ergou.common.util.MetaDataUtils;
import com.laka.ergou.common.util.share.WxHelper;
import com.laka.ergou.mvp.db.ChatDbEngine;
import com.lqr.emoji.IImageLoader;
import com.lqr.emoji.LQREmotionKit;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.view.CropImageView;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @Author:Rayman
 * @Date:2018/12/15
 * @Description:项目Application
 */

public class MainApplication extends BaseApplication {

    public static final String TAG = "MainApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        initAliBaiChuan();
        initImageLoader();
        initBugly();
        initUMeng();
        initImUI();
        initDb();
        initJpush();
        initJverification();
        initWechatSDK();
        initCustomInterceptor();
        closeAndroidPDialog();
        initLeakCanary();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {//1
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    /**
     * 初始化公共拦截器，解决androidlib 不能反向调用 main 应用的痛点
     */
    private void initCustomInterceptor() {
        InterceptorUtils.addCommonInterceptor(new GlobalParamsInterceptor());
        InterceptorUtils.addCommonInterceptor(new IllegalTokenInterceptor());
    }

    private void initWechatSDK() {
        WxHelper wxHelper = new WxHelper(this);
    }

    private void initJpush() {
        String channel = MetaDataUtils.getMateDataForApplicationInfo("UMENG_CHANNEL");
        LogUtils.info("application_channel:" + channel);
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);
        JPushInterface.setChannel(this, channel);
        //设置标签
        if (BuildConfig.DEBUG) {
            Set<String> tags = new HashSet<>();
            //tags.add("Rayman");
            JPushInterface.setTags(this, tags, new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                    Log.d(TAG, "setTags . set = " + set.toString() + " ;result = " + i);
                }
            });
        }
    }

    private void initJverification() {
        JVerificationInterface.init(this);
        JVerificationInterface.setDebugMode(BuildConfig.DEBUG);
    }

    private void initDb() {
        ChatDbEngine.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initImageLoader() {
        ImageLoader.getInstance()
                .setLoader(new GlideImageLoader(this));
    }

    /**
     * 初始化阿里百川
     */
    private void initAliBaiChuan() {
        AlibcTradeSDK.asyncInit(this, new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                LogUtils.error("成功");
            }

            @Override
            public void onFailure(int i, String s) {
                LogUtils.error(i + ":" + s);
            }
        });
    }

    private void initUMeng() {
        /**
         * 由于基类的 activity 存放于 androidlib 中，所以将友盟SDK放到 androidlib 中，这样就可以直接在基类中
         * 进行友盟的配置了
         * */
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "db6fd476b6", BuildConfig.DEBUG);
    }

    private void initImUI() {
        // LitePal.initialize(this);
        //初始化融云
        // initRongCloud();
        //初始化红包
        // initRedPacket();
        //初始化仿微信控件ImagePicker
        initImagePicker();
        //初始化表情控件
        LQREmotionKit.init(this, new IImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            }
        });
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new com.lqr.imagepicker.loader.ImageLoader() {
            @Override
            public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
                Glide.with(getApplicationContext()).load(Uri.parse("file://" + path).toString()).centerCrop().into(imageView);
            }

            @Override
            public void clearMemoryCache() {

            }
        });   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }


    /**
     * 禁止android P 非官方sdk 访问限制弹窗，下一个版本再进行兼容
     */
    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
