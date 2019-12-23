package com.laka.androidlib.util.image;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.laka.androidlib.constant.AppConstant;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @Author:Rayman
 * @Date:2019/1/24
 * @Description:Glide加载模块类---改用OKHttp加载 .
 * 主要添加超时时间约束
 */

public class GlideOkHttpModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(AppConstant.MAX_READ_TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(AppConstant.MAX_WRITE_TIME_OUT, TimeUnit.SECONDS);
        builder.connectTimeout(AppConstant.MAX_CONNECT_TIME_OUT, TimeUnit.SECONDS);

        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(builder.build()));

    }
}
