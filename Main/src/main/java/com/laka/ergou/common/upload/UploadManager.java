package com.laka.ergou.common.upload;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.laka.androidlib.util.ApplicationUtils;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.Utils;
import com.laka.androidlib.util.toast.ToastHelper;

import java.util.Date;


/**
 *  
 *  * @ClassName: UploadManager
 *  * @Description: 上传文件工具了
 *  * @Author: 关健   
 *  * @Version: 1.0.0
 *  * @Date 2016/11/16
 */

public class UploadManager {

    private OSS oss;
    // 运行sample前需要配置以下字段为有效的值
    private static final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
    // 先获取id/secret/token
    public static String accessKeyId;
    public static String accessKeySecret;
    public static String accessKeyToken;
    private OSSAsyncTask ossAsyncTask;

//     重置key,这样下次使用，才会重新创建。
//    public static void resetKey() {
//        self = null;
//    }

//    private UploadManager self;
//    public static UploadManager getInstance() {
//        if (self == null) {
//            self = new UploadManager();
//        }
//        return self;
//    }

    public UploadManager() {

        try {
            //OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, accessKeyToken);
            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
            conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
            conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
            conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
            OSSLog.enableLog();
            oss = new OSSClient(ApplicationUtils.getApplication(), endpoint, credentialProvider, conf);
        } catch (Exception e) {
            LogUtils.info("UploadManager:" + e.toString());
        }

    }

    /**
     * 上传图片
     */
    public void uploadFile(String bucketName, String objectKey, String uploadFilePath, final UploadCallBack callBack) {
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, uploadFilePath);
        // 异步上传时可以设置进度回调。
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        if (ossAsyncTask != null && !ossAsyncTask.isCompleted()) ossAsyncTask.cancel();
        ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
                if (callBack != null) {
                    callBack.onSuccess(request, result);
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常。
                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
                    ToastHelper.showCenterToast(clientExcepion.getMessage());
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    ToastHelper.showCenterToast(serviceException.getRawMessage());
                }
                if (callBack != null) {
                    callBack.onFail(clientExcepion.getMessage());
                }
            }
        });
    }

    public void cancelUpload() {
        if (ossAsyncTask != null && !ossAsyncTask.isCompleted()) {
            ossAsyncTask.cancel();
        }
    }

}
