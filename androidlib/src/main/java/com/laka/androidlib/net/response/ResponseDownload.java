package com.laka.androidlib.net.response;

import java.io.InputStream;

/**
 * @ClassName: ResponseDownload
 * @Description: 下载成功时的返回值
 * @Author: chuan
 * @Date: 09/04/2018
 */
public class ResponseDownload {
    private InputStream inputStream;  //输入流

    private long contentLength;  //文件大小

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }
}
