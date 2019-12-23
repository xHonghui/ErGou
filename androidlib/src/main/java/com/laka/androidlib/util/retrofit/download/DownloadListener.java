package com.laka.androidlib.util.retrofit.download;

/**
 * @Author:Rayman
 * @Date:2019/1/22
 * @Description:网络下载回调
 */

public interface DownloadListener {

    /**
     * 下载开始
     */
    void onStart();

    /**
     * 下载进度回调
     *
     * @param progressByte 当前读取到的byte
     * @param totalByte    下载文件大小
     * @param progress     当前进度
     */
    void onProgress(long progressByte, long totalByte, int progress);

    /**
     * 下载完毕
     */
    void onFinish();

    /**
     * 下载失败
     *
     * @param errMsg
     */
    void onFailed(String errMsg);
}
