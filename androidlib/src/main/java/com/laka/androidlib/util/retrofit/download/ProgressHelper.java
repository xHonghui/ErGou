package com.laka.androidlib.util.retrofit.download;

import com.laka.androidlib.eventbus.EventBusManager;
import com.laka.androidlib.util.LogUtils;

import okhttp3.OkHttpClient;

/**
 * @Author:Rayman
 * @Date:2019/1/22
 * @Description:针对OKHttp下载帮助类 当前的回调是通过EventBus的方式回调的，可以统一封装回调。
 */

public class ProgressHelper {

    public static OkHttpClient.Builder addProgress(OkHttpClient.Builder builder) {
        if (builder == null) {
            builder = new OkHttpClient.Builder();
        }

        // 进度回调，通过EventBus的方式回调
        DownloadListener downloadListener = new DownloadListener() {
            @Override
            public void onStart() {
                // 发送EventBus
                EventBusManager.postEvent(DownloadEvent.DOWNLOAD_START);
                LogUtils.info("开始下载");
            }

            @Override
            public void onProgress(long progressByte, long totalByte, int progress) {
                if (totalByte > 0) {
                    EventBusManager.postEvent(DownloadEvent.DOWNLOAD_PROGRESS,
                            new DownloadEvent(progressByte, totalByte, progress));
                    LogUtils.info("下载回调：" + progressByte + "\n总文件大小：" + totalByte + "\n当前进度：" + progress);
                }
            }

            @Override
            public void onFinish() {
                EventBusManager.postEvent(DownloadEvent.DOWNLOAD_START);
                LogUtils.info("下载结束");
            }

            @Override
            public void onFailed(String errMsg) {
                EventBusManager.postEvent(DownloadEvent.DOWNLOAD_FAILED, errMsg);
                LogUtils.info("下载错误：" + errMsg);
            }
        };

        builder.addInterceptor(new DownloadInterceptor(downloadListener));
        return builder;
    }
}
