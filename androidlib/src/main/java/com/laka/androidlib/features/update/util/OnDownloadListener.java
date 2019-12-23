package com.laka.androidlib.features.update.util;

/**
 * @Author Lyf
 * @CreateTime 2018/5/14
 * @Description listen the progress of downloading a file.
 **/
public interface OnDownloadListener {

    // Return a progress.
    void onProgress(int progress);

    /**
     * Called when The downloading task is now finished.
     *
     * @param filePath The path of apk file.
     */
    void onFinish(String filePath);

    // Called when the downloading task is canceled.
    void onCancel();

    // Called If any exceptions thrown.
    void onError(Exception e);

}
