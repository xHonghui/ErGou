package com.laka.androidlib.features.update.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.InputStream;

/**
 * @Author Lyf
 * @CreateTime 2018/5/14
 * @Description A util for downloading Apk file.
 * <p>
 * You can use this util like this way:
 * <code> new DownLoadApkUtil.Builder()
 * .setDownloadUrl(mUpdateBean.getUrl())
 * .setFileName(mFileName)
 * .setCallback(new OnDownloadListener() {
 * public void onError(Exception e) {}
 * public void onProgress(int progress) {}
 * public void onCancel() {}
 * public void onFinish(String filePath) {}
 * }).build().startDownloadApk();
 * </code>
 * </p>
 * <p>
 * also, You can specify an impl of downloading task, like this:
 * <code> new DownLoadApkUtil.Builder()...
 * // You can specify an impl here.
 * .setDownLoadTaskImpl(new DefaultDownloadTaskImpl())
 * ....startDownloadApk();
 * </code>
 * </p>
 **/
public class DownloadApkUtil {

    // A protocol of downloading task.
    private final IDownloadTask downLoadTask;

    private DownloadApkUtil(Builder builder) {

        checkAttributes(builder);

        if (builder.getDownLoadTaskImpl() == null) {
            downLoadTask = new DefaultDownloadTaskImpl();
        } else {
            // Use the default impl for downloading If no any impl is specified.
            downLoadTask = builder.getDownLoadTaskImpl();
        }

        downLoadTask.setBuilder(builder);

    }

    /**
     * Start to download
     */
    public DownloadApkUtil startDownloadApk() {
        downLoadTask.startDownloadApk();
        return this;
    }

    /**
     * Cancel a downloading task.
     */
    public void cancelDownloadTask() {
        downLoadTask.cancelDownloadTask();
    }

    /**
     * @return true if a certain file is downloading.
     */
    public boolean isDownloading() {
        return downLoadTask.isDownloading();
    }

    /**
     * Check attributes and throw an RuntimeException If any attr is illegal.
     */
    private void checkAttributes(Builder builder) {

        if (TextUtils.isEmpty(builder.getFilePath())) {
            throw new RuntimeException("The filePath can't be empty or null.");
        }

        /*
         * These attributes aren't a must.
         */

//        if (TextUtils.isEmpty(builder.getDownloadUrl())) {
//            throw new RuntimeException("The downloadUrl can't be empty or null.");
//        }
//        if (builder.getCallback() == null) {
//            throw new RuntimeException("The callback can't be null. ");
//        }
//
//        if (builder.getInputStream() == null) {
//            throw new RuntimeException("The inputStream can't be null. ");
//        }

    }

    public static class Builder {

        /*
         * A protocol of downloading task.
         * Note: You can change the implementation of downloading by setting this attr.
         */
        private IDownloadTask downLoadTaskImpl;
        // A path of the apk file.
        private String filePath;
        // A url is an address of resource on the remote server.
        private String downloadUrl;
        // A total length of the apk file.
        private long contentLength;
        // A stream of the resource.
        @Nullable
        private InputStream inputStream;
        // A listener for downloading task.
        @Nullable
        private OnDownloadListener callback;


        public String getFilePath() {
            return filePath;
        }

        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public Builder setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public Builder setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
            return this;
        }

        public OnDownloadListener getCallback() {
            return callback;
        }

        public Builder setCallback(OnDownloadListener callback) {
            this.callback = callback;
            return this;
        }

        public IDownloadTask getDownLoadTaskImpl() {
            return downLoadTaskImpl;
        }

        public Builder setDownLoadTaskImpl(IDownloadTask downLoadTaskImpl) {
            this.downLoadTaskImpl = downLoadTaskImpl;
            return this;
        }

        public long getContentLength() {
            return contentLength;
        }

        public Builder setContentLength(long contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public DownloadApkUtil build() {
            return new DownloadApkUtil(this);
        }

    }

}