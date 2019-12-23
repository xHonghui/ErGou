package com.laka.androidlib.net.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.laka.androidlib.net.response.Callback;
import com.laka.androidlib.net.response.DownloadCallback;


/**
 * @Author Lyf
 * @CreateTime 2018/2/8
 * @Description IHttpManager defines a set of basic http request's methods.
 * When you change your Http library to another, The class of enclosing the new library should be implemented this interface.
 * If do so, you will change nothing to the rest of project.
 **/
public interface IHttpManager {

    /**
     * @param url  A uniform resource locator (URL) with a scheme of either {@code http} or {@code https}.
     * @param params A set of params which will be sent to remote server when a request is sent.
     * @param responseCallback<T> The response returned by remote server.
     */
    <T> void doGet(@NonNull String tag, @NonNull String url,
                                    @Nullable ArrayMap<String, Object> params, @Nullable Callback<T> responseCallback);

    /**
     * @param url  A uniform resource locator (URL) with a scheme of either {@code http} or {@code https}.
     * @param params A set of params which will be sent to remote server when a request is sent.
     * @param responseCallback<T> The response returned by remote server.
     */
    <T> void doPost(@NonNull String tag, @NonNull String url,
                                     @Nullable ArrayMap<String, Object> params, @Nullable Callback<T> responseCallback);

    /**
     * 下载文件
     *
     * @param tag              tag
     * @param url              A uniform resource locator (URL) with a scheme of either {@code http} or {@code https}.
     * @param params           A set of params which will be sent to remote server when a request is sent.
     * @param responseCallback The response returned by remote server.
     */
    void downloadFile(@NonNull String tag, @NonNull String url,
                      @Nullable ArrayMap<String, Object> params,
                      @NonNull DownloadCallback responseCallback);

    /**
     * Cancel requests by a tag.
     * @param tag a tag indicates requests.
     */
    void cancelRequestWithTag(Object tag);

    /**
     * Cancel all requests
     */
    void cancelAllRequests();
}
