package com.laka.androidlib.util.retrofit.download;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @Author:Rayman
 * @Date:2019/1/22
 * @Description:自定义Download类型响应体，主要获取进度
 */

public class DownloadResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private DownloadListener listener;
    private BufferedSource bufferedSource;

    public DownloadResponseBody(ResponseBody responseBody, DownloadListener listener) {
        this.responseBody = responseBody;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        if (responseBody == null) {
            return 0L;
        }
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(createSource(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * 读取请求体数据，通过类似AOP的方式，添加转接回调
     *
     * @param bufferedSource
     * @return
     */
    private Source createSource(BufferedSource bufferedSource) {
        return new ForwardingSource(bufferedSource) {

            // 当前读取的总进度
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long byteRead = super.read(sink, byteCount);
                totalBytesRead += byteRead != -1 ? byteRead : 0;
                if (listener != null) {
                    listener.onProgress(totalBytesRead, responseBody.contentLength(),
                            (int) (totalBytesRead * 100 / responseBody.contentLength()));
                }
                return byteRead;
            }
        };
    }
}
