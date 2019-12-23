package com.laka.androidlib.util.retrofit.download;

/**
 * @Author:Rayman
 * @Date:2019/1/22
 * @Description:下载事件回调
 */

public class DownloadEvent {

    public static final String DOWNLOAD_START = "DOWNLOAD_START";
    public static final String DOWNLOAD_FINISH = "DOWNLOAD_FINISH";
    public static final String DOWNLOAD_FAILED = "DOWNLOAD_FAILED";
    public static final String DOWNLOAD_PROGRESS = "DOWNLOAD_PROGRESS";

    private long downloadReadByte;
    private long downloadTotalByte;
    private int downloadProgress;

    public DownloadEvent(long downloadReadByte, long downloadTotalByte, int downloadProgress) {
        this.downloadReadByte = downloadReadByte;
        this.downloadTotalByte = downloadTotalByte;
        this.downloadProgress = downloadProgress;
    }

    public long getDownloadReadByte() {
        return downloadReadByte;
    }

    public void setDownloadReadByte(long downloadReadByte) {
        this.downloadReadByte = downloadReadByte;
    }

    public long getDownloadTotalByte() {
        return downloadTotalByte;
    }

    public void setDownloadTotalByte(long downloadTotalByte) {
        this.downloadTotalByte = downloadTotalByte;
    }

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }
}
