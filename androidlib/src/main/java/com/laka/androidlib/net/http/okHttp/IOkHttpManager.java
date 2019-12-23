package com.laka.androidlib.net.http.okHttp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.laka.androidlib.net.http.IHttpManager;
import com.laka.androidlib.net.response.DownloadCallback;

import okhttp3.Headers;

/**
 * @Author Lyf
 * @CreateTime 2018/2/23
 * @Description An IOkHttpManager interface defines What the okHttpManager can do.
 * It's better to keep this interface Since You can learn quickly what the OkHttpManager does in here.
 * Instead of reading entirely codes of OkHttpManager.
 **/
public interface IOkHttpManager extends IHttpManager {


    /**
     * Transfers the original headers(Map<String, String> type) to OkHttp's headers.
     *
     * @param originalHeaders original headers
     * @return Headers.
     */
    Headers getOkHttpHeaders(ArrayMap<String, String> originalHeaders);

//    /**
//     *
//     * @return
//     */
//    Request createRequest();

}
