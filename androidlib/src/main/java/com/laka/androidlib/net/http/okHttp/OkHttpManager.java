package com.laka.androidlib.net.http.okHttp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.laka.androidlib.net.consts.HttpCodes;
import com.laka.androidlib.net.header.IHeaderManager;
import com.laka.androidlib.net.http.HttpParameterBuilder;
import com.laka.androidlib.net.response.BaseBean;
import com.laka.androidlib.net.response.Callback;
import com.laka.androidlib.net.response.DownloadCallback;
import com.laka.androidlib.net.response.ResponseDownload;
import com.laka.androidlib.net.response.ResponseFailure;
import com.laka.androidlib.net.thread.ThreadManager;
import com.laka.androidlib.net.utils.Util;
import com.laka.androidlib.net.utils.parse.ParseUtil;
import com.laka.androidlib.util.LogUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @Author Lyf
 * @CreateTime 2018/2/8
 * @Description 不符合接口协定的请求，一律用旧项目之前的AsyncTask或AsyncUtils类。
 **/
public final class OkHttpManager implements IOkHttpManager {

    private final static String TAG = OkHttpManager.class.getSimpleName();

    private static Headers mOkHttpHeaders;
    private HttpParameterBuilder mHttpParameterBuilder;

    // It's better to use a single instance of OkHttpClient in a certain project.
    private final OkHttpClient mOkHttpClient = new OkHttpClient();

    // Manager Headers.
    private IHeaderManager mHeaderManager;

    // Sets some settings of http to OkHttpClient with a HttpBuilder.
    public OkHttpManager(HttpParameterBuilder httpParameterBuilder, IHeaderManager mHeaderManager) {

        this.mHeaderManager = mHeaderManager;
        this.mHttpParameterBuilder = httpParameterBuilder;

        OkHttpClient.Builder okHttpBuilder = mOkHttpClient.newBuilder();
        okHttpBuilder.readTimeout(httpParameterBuilder.getReadTimeOut(), TimeUnit.MILLISECONDS)
                .writeTimeout(httpParameterBuilder.getWriteTimeOut(), TimeUnit.MILLISECONDS)
                .connectTimeout(httpParameterBuilder.getConnectTimeOut(), TimeUnit.MILLISECONDS)
                .cookieJar(CookieManager.getCookieManager());
        // Add ssl here.
        // HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        // okHttpBuilder.sslSocketFactory(sslParams.sSLSocketFactory,sslParams.trustManager);

        okHttpBuilder.build();
    }

    @Override
    public <T> void doGet(@NonNull String tag, @NonNull String url, @Nullable ArrayMap<String, Object> params, final @Nullable Callback<T> responseCallback) {
        // Creates a basic request
        final Request request = new Request
                .Builder()
                .tag(tag)
                .headers(getOkHttpHeaders(mHeaderManager.getHeaders()))
                .url(Util.composeParams(url, params))
                .cacheControl(CacheControl.FORCE_CACHE)
                .build();

        handleNetTask(request, responseCallback);
    }

    @Override
    public <T> void doPost(@NonNull String tag, @NonNull String url, @Nullable ArrayMap<String, Object> params,
                           @Nullable Callback<T> responseCallback) {

        // 传Json
//        RequestBody body = RequestBody.create(MediaType.parse("text/html;charset=utf-8"),
//                ParseUtil.toJson(params));
        // 传表单
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                Util.composeParams(params));

//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            LogUtils.error(TAG, "输出参数:" + entry.getKey() + "\nValue：" + entry.getValue());
//        }

        Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .post(body)
                .build();

        handleNetTask(request, responseCallback);
    }

    @Override
    public void downloadFile(@NonNull String tag, @NonNull String url,
                             @Nullable ArrayMap<String, Object> params,
                             @NonNull DownloadCallback responseCallback) {
        final Request request = new Request
                .Builder()
                .tag(tag)
                .headers(getOkHttpHeaders(mHeaderManager.getHeaders()))
                .url(Util.composeParams(url, params))
                .build();

        handleDownloadTask(request, responseCallback);
    }

    @Override
    public void cancelAllRequests() {
        if (mOkHttpClient != null) {

            for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
                call.cancel();
            }

            for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
                call.cancel();
            }
        }
    }

    @Override
    public void cancelRequestWithTag(Object tag) {

        if (mOkHttpClient != null && tag != null) {

            for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
                if (call.request().tag().equals(tag))
                    call.cancel();
            }

            for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
                if (call.request().tag().equals(tag))
                    call.cancel();
            }
        }

    }

    @Override
    public Headers getOkHttpHeaders(ArrayMap<String, String> originalHeaders) {

        Headers.Builder headers = new Headers.Builder();

        for (String headerName : originalHeaders.keySet()) {
            headers.set(headerName.trim(), originalHeaders.get(headerName).trim());
        }

        mOkHttpHeaders = headers.build();

        return mOkHttpHeaders;
    }

    private void handleDownloadTask(Request request, final DownloadCallback responseCallback) {
        mOkHttpClient.newCall(request)
                .enqueue(new okhttp3.Callback() {

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) {
                        if (responseCallback == null) {
                            return;
                        }

                        ResponseBody body;

                        try {
                            if (response == null || response.code() != 200 || (body = response.body()) == null
                                    || body.byteStream() == null) {
                                handleDownloadFailure(responseCallback);
                                return;
                            }

                            ResponseDownload responseDownload = new ResponseDownload();
                            responseDownload.setInputStream(body.byteStream());
                            responseDownload.setContentLength(body.contentLength());
                            responseCallback.onResponse(responseDownload);

                        } catch (Exception e) {
                            LogUtils.debug(TAG, "requesting failed = " + e.toString());
                            handleDownloadFailure(responseCallback);
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.debug(TAG, "requesting failed = " + e.toString());

                        handleDownloadFailure(responseCallback);
                    }

                });

    }


    private void handleDownloadFailure(DownloadCallback responseCallback) {

        ResponseFailure failureResponse = new ResponseFailure();
        failureResponse.setCode(HttpCodes.UNKNOWN_HTTP_ERROR);
        failureResponse.setMsg("下载失败，服务器异常");
        onFailureResponse(failureResponse, responseCallback);
    }

    private <T> void handleNetTask(Request request, final Callback<T> responseCallback) {
        mOkHttpClient.newCall(request)
                .enqueue(new okhttp3.Callback() {

                    @Override
                    public void onResponse(Call call, okhttp3.Response response) {
                        handleResponse(response, responseCallback);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtils.debug(TAG, "requesting failed = " + e.toString());

                        ResponseFailure failureResponse = new ResponseFailure();
                        failureResponse.setCode(HttpCodes.UNKNOWN_HTTP_ERROR);
                        failureResponse.setMsg(mHttpParameterBuilder.getServerErrorMsg());
                        onFailureResponse(failureResponse, responseCallback);
                    }

                });

    }

    private <T> void handleResponse(okhttp3.Response response, Callback<T> responseCallback) {

        if (responseCallback != null) {

            ResponseFailure failureResponse = new ResponseFailure();

            try {
                if (response != null) {

                    if (response.code() == 200 && response.body() != null) {

                        try {

                            T bean = null;

                            // Get the raw json from server.
                            String json = response.body().string();

                            // Prints the raw json.
                            LogUtils.debug(TAG, "Server response = " + json);

                            // Parse raw json into BaseBean
                            BaseBean baseBean = ParseUtil.parseJson(json, BaseBean.class);

                            // BaseBean is a wrapping class. The T is a one of fields of it.
                            if (baseBean != null) {

                                if (baseBean.getCode() == HttpCodes.CODE_SUCCESS) {

                                    LogUtils.debug(TAG, "BaseBean = " + ParseUtil.toJson(baseBean));

                                    json = ParseUtil.toJson(baseBean.getData());

                                    if (json != null && !TextUtils.isEmpty(json)) {

                                        LogUtils.debug(TAG, "Json = " + json);

                                        // Parse  data into bean
                                        bean = ParseUtil.parseJson(json, responseCallback);
                                    }

                                    LogUtils.debug(TAG, "bean = " + ParseUtil.toJson(bean));

                                    // bean can't be null if isAllowedEmptyData is false.
                                    if (!mHttpParameterBuilder.isAllowedEmptyData() && bean == null) {

                                        LogUtils.debug(TAG, "requesting failed ，" +
                                                "bean == null and isAllowedEmptyData == false");

                                        failureResponse.setMsg(mHttpParameterBuilder.getParseErrorMsg());
                                        failureResponse.setCode(baseBean.getCode());
                                        onFailureResponse(failureResponse, responseCallback);
                                    } else {
                                        // Return the data.
                                        onSuccessResponse(bean, responseCallback);
                                    }

                                } else {
                                    // Errors of Business
                                    failureResponse.setMsg(baseBean.getMsg());
                                    failureResponse.setCode(baseBean.getCode());
                                    onFailureResponse(failureResponse, responseCallback);
                                }

                            } else {

                                LogUtils.debug(TAG, "parsing failed!");
                                // Result a null bean without any errors.
                                failureResponse.setCode(HttpCodes.UNKNOWN_HTTP_ERROR);
                                failureResponse.setMsg(mHttpParameterBuilder.getParseErrorMsg());
                                onFailureResponse(failureResponse, responseCallback);
                            }

                        } catch (Exception e) {
                            LogUtils.debug(TAG, "Parsing json failed = " + e.toString());
                            failureResponse.setMsg(mHttpParameterBuilder.getParseErrorMsg());
                            onFailureResponse(failureResponse, responseCallback);
                        }

                    } else {

                        // Prints error info if the requesting is failed.
                        LogUtils.debug(TAG, "requesting failed = " + response.message().toString());

                        failureResponse.setCode(response.code());
                        failureResponse.setMsg(response.message());
                        onFailureResponse(failureResponse, responseCallback);
                    }
                }
            } catch (Exception e) {
                LogUtils.debug(TAG, "requesting failed = " + e.toString());

                failureResponse.setCode(HttpCodes.UNKNOWN_HTTP_ERROR);
                failureResponse.setMsg(mHttpParameterBuilder.getServerErrorMsg());
                onFailureResponse(failureResponse, responseCallback);
            }
        }


    }

    private <T> void onFailureResponse(final ResponseFailure failureResponse, final Callback<T> responseCallback) {

        if (responseCallback != null) {
            ThreadManager.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.debug(TAG, "onFailure = " + ParseUtil.toJson(failureResponse));
                    responseCallback.onFailure(failureResponse);
                }
            });

        }
    }

    private <T> void onSuccessResponse(final T bean, final Callback<T> responseCallback) {

        if (responseCallback != null) {
            ThreadManager.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.debug(TAG, "onResponse = " + ParseUtil.toJson(bean));
                    responseCallback.onResponse(bean);
                }
            });

        }

    }

}