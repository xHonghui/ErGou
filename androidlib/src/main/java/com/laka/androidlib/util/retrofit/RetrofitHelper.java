package com.laka.androidlib.util.retrofit;

import android.content.Context;

import com.laka.androidlib.constant.AppConstant;
import com.laka.androidlib.net.utils.parse.GsonUtil;
import com.laka.androidlib.util.InterceptorUtils;
import com.laka.androidlib.util.retrofit.download.ProgressHelper;
import com.laka.androidlib.util.rx.constant.ApiType;
import com.laka.androidlib.util.rx.convert.JsonConverterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author:Rayman
 * @Date:2018/12/18
 * @Description:Retrofit 工具类
 **/
public class RetrofitHelper {
    private static Builder mInstance;
    private Context context;
    private Retrofit mRetrofit;

    /**
     * description:常规参数配置
     **/
    private String HOST = "";
    private boolean isTokenRequest = false;
    private boolean isNetWorkInterceptor = false;
    private boolean isDownloadRequest = false;
    private int mApi = ApiType.TAOBAO_API;

    private RetrofitHelper(Builder builder) {
        this.context = builder.context;
        this.isTokenRequest = builder.isTokenRequest;
        this.isNetWorkInterceptor = builder.isNetWorkInterceptor;
        this.isDownloadRequest = builder.isDownloadRequest;
        this.HOST = builder.requestHost;
        this.mApi = builder.apiType;
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

        if (isDownloadRequest) {
            builder = ProgressHelper.addProgress(builder);
        }

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

        builder.addNetworkInterceptor(new LoggingInterceptor())
                .cache(cache)
                .retryOnConnectionFailure(true)
                .connectTimeout(AppConstant.MAX_CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(AppConstant.MAX_READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(AppConstant.MAX_WRITE_TIME_OUT, TimeUnit.SECONDS);

        // 创建OKHttp对象
        OkHttpClient okHttpClient = builder.build();
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient);
        if (mApi == ApiType.CUSTOM_API) {
            retrofitBuilder.addConverterFactory(JsonConverterFactory.create(new GsonUtil().getDefaultGson()));
        } else {
            retrofitBuilder.addConverterFactory(GsonConverterFactory.create(new GsonUtil().getDefaultGson()));
        }
        mRetrofit = retrofitBuilder.build();
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
        private boolean isDownloadRequest = false;
        private String requestHost = "";
        private Context context;
        private int apiType = ApiType.TAOBAO_API;

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

        public Builder setDownloadRequest(boolean isDownloadRequest) {
            this.isDownloadRequest = isDownloadRequest;
            return this;
        }

        public Builder setRequestHost(String requestHost) {
            this.requestHost = requestHost;
            return this;
        }

        public Builder setApiType(int type) {
            this.apiType = type;
            return this;
        }

        public RetrofitHelper build() {
            return new RetrofitHelper(this);
        }
    }
}