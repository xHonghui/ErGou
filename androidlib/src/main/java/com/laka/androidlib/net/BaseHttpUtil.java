package com.laka.androidlib.net;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.laka.androidlib.net.http.IHttpManager;
import com.laka.androidlib.net.response.Callback;
import com.laka.androidlib.net.response.DownloadCallback;


/**
 * @Author Lyf
 * @CreateTime 2018/1/23
 * @Description you can use BaseHttpUtil to do requests directly.
 * Note that: You may should rewrite the getSignParams() method to sign your params before doing request.
 * Return the original params if you don't need to sign your params.
 **/
public abstract class BaseHttpUtil implements IHttpManager {

    private IHttpManager mHttpManager = HttpManager.getHttpManager();

    /**
     * Add algorithms of sign.
     *
     * @param params the original requesting params.
     * @return The params with sign or the original params if you don't need to sign the params.
     */
    protected abstract ArrayMap<String, Object> getSignParams(ArrayMap<String, Object> params);

    @Override
    public <T> void doGet(@NonNull String tag, @NonNull String url,
                          @Nullable ArrayMap<String, Object> params, @Nullable Callback<T> responseCallback) {
        mHttpManager.doGet(tag, url, getSignParams(params), responseCallback);
    }

    @Override
    public <T> void doPost(@NonNull String tag, @NonNull String url, @Nullable ArrayMap<String, Object> params,
                           @Nullable Callback<T> responseCallback) {
        mHttpManager.doPost(tag, url, getSignParams(params), responseCallback);
    }

    // These methods below do request without params and listen.

    /**
     * This method uses url as a default tag.
     */
    public <T> void doGet(@NonNull String url, @Nullable ArrayMap<String, Object> params,
                          @Nullable Callback<T> responseCallback) {
        doGet(url, url, params, responseCallback);
    }

    public <T> void doGet(@NonNull String url, @Nullable Callback<T> responseCallback) {
        doGet(url, url, null, responseCallback);
    }

    /**
     * This method uses url as a default tag.
     */
    public <T> void doPost(@NonNull String url, @Nullable ArrayMap<String, Object> params,
                           @Nullable Callback<T> responseCallback) {
        doPost(url, url, params, responseCallback);
    }

    public <T> void doPost(@NonNull String url,
                           @Nullable Callback<T> responseCallback) {
        doPost(url, url, null, responseCallback);
    }

    public void downloadFile(@NonNull String tag, @NonNull String url,
                             @Nullable ArrayMap<String, Object> params,
                             @NonNull DownloadCallback responseCallback) {
        mHttpManager.downloadFile(tag, url, params, responseCallback);
    }

    public void cancelRequestWithTag(Object tag) {
        mHttpManager.cancelAllRequests();
    }

    @Override
    public void cancelAllRequests() {
        mHttpManager.cancelAllRequests();
    }

}
