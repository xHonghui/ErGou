package com.laka.androidlib.util.retrofit;

import android.content.Context;

import com.laka.androidlib.net.utils.parse.GsonUtil;
import com.laka.androidlib.util.InterceptorUtils;
import com.laka.androidlib.util.rx.convert.JsonConverterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @Author:Rayman
 * @Date:2018/12/18
 * @Description:Retrofit 工具类
 **/
public class RetrofitCustomHelper {

    private static Builder mInstance;
    private Context context;
    private Retrofit mRetrofit;

    /**
     * description:常规参数配置
     **/
    private String HOST = "";
    private boolean isTokenRequest = false;
    private boolean isNetWorkInterceptor = false;

    private RetrofitCustomHelper(Builder builder) {
        this.context = builder.context;
        this.isTokenRequest = builder.isTokenRequest;
        this.isNetWorkInterceptor = builder.isNetWorkInterceptor;
        this.HOST = builder.requestHost;
        initRetrofit();
    }

    public static Builder getInstance(Context context) {
        if (mInstance == null) {
            synchronized (Builder.class) {
                if (mInstance == null) {
                    mInstance = new Builder(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    /**
     * 配置Retrofit信息
     */
    private void initRetrofit() {
        // 指定缓存路径,缓存大小100Mb
        Cache cache = new Cache(new File(context.getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (isNetWorkInterceptor) {
            builder.addInterceptor(new NetWorkInterceptor(context));
        }

        //custom 设置的拦截器
        if (InterceptorUtils.isInit()) {
            for (Interceptor interceptor : InterceptorUtils.getInterceptorList()) {
                builder.addInterceptor(interceptor);
            }
        }

        if (isTokenRequest) {
            builder.addInterceptor(new AuthorizationInterceptor(context));
        }

        builder.addInterceptor(new LoggingInterceptor())
                .cache(cache)
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);

        // 创建OKHttp对象
        OkHttpClient okHttpClient = builder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(JsonConverterFactory.create(new GsonUtil().getDefaultGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    /**
     * 通过create函数获取到具体的Service
     *
     * @param reqServer
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> reqServer) {
        return mRetrofit.create(reqServer);
    }

    /**
     * 构建者类
     */
    public static class Builder {

        private boolean isTokenRequest = false;
        private boolean isNetWorkInterceptor = false;
        private String requestHost = "";
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTokenRequest(boolean tokenRequest) {
            isTokenRequest = tokenRequest;
            return this;
        }

        public Builder setNetWorkInterceptor(boolean netWorkInterceptor) {
            isNetWorkInterceptor = netWorkInterceptor;
            return this;
        }

        public Builder setRequestHost(String requestHost) {
            this.requestHost = requestHost;
            return this;
        }

        public RetrofitCustomHelper build() {
            return new RetrofitCustomHelper(this);
        }
    }
}