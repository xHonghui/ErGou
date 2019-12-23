package com.laka.androidlib.features.update.util;

import android.support.annotation.NonNull;

/**
 * @Author Lyf
 * @CreateTime 2018/5/14
 * @Description A protocol of downloading task.
 * @Note You can learn how to implement it with this class{@link DefaultDownloadTaskImpl}
 **/
public interface IDownloadTask extends OnDownloadListener {

    void startDownloadApk();

    void cancelDownloadTask();

    /**
     * @return true if a certain file is downloading.
     */
    boolean isDownloading();

    void setBuilder(@NonNull DownloadApkUtil.Builder builder);
}
