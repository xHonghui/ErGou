package com.laka.ergou.common.interceptor;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.laka.androidlib.util.LogUtils;
import com.laka.ergou.BuildConfig;
import com.laka.ergou.common.util.device.DeviceInfoUtils;
import com.laka.ergou.mvp.user.UserUtils.UserUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * @Author:summer
 * @Date:2019/3/25
 * @Description:公共参数拦截器，与AuthorizationInterceptor不同，当前拦截器涉及一些业务逻辑
 */
public class GlobalParamsInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String method = request.method();

        Map<String, String> commonParams = new HashMap<>();//公共参数
        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;
        commonParams.put("build", versionCode + "");
        commonParams.put("version", versionName);
        commonParams.put("platform", "Android");

        String deviceId = DeviceInfoUtils.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) deviceId = "";
        String phoneProducerAndModel = DeviceInfoUtils.getPhoneProducerAndModel();
        if (TextUtils.isEmpty(phoneProducerAndModel)) phoneProducerAndModel = "";
        String uuidAndroidId = DeviceInfoUtils.getUUIDAndroidId();
        if (TextUtils.isEmpty(uuidAndroidId)) uuidAndroidId = "";
        commonParams.put("deviceIDFA", deviceId);
        commonParams.put("deviceInfo", phoneProducerAndModel);
        commonParams.put("deviceUUID", uuidAndroidId);
        commonParams.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        LogUtils.info("deviceUUID:" + DeviceInfoUtils.getUUIDAndroidId() + DeviceInfoUtils.getDeviceId() + "-------deviceIDFA;" + DeviceInfoUtils.getAndroidId());

        if (UserUtils.INSTANCE.isLogin() && !TextUtils.isEmpty(UserUtils.INSTANCE.getUserToken())) {
            commonParams.put("token", UserUtils.INSTANCE.getUserToken());
        }
        if (UserUtils.INSTANCE.isLogin() && !TextUtils.isEmpty(UserUtils.INSTANCE.getUserId() + "")) {
            commonParams.put("user_id", UserUtils.INSTANCE.getUserId() + "");
        }
        if (method.equals("GET")) {
            return chainForGet(chain, request, commonParams);
        } else if (method.equals("POST")) {
            return chainForPost(chain, request, commonParams);
        }

        return chainForPost(chain, request, commonParams);
    }

    @NonNull
    private Response chainForGet(Chain chain, Request request, Map<String, String> commonParams) throws IOException {
        Set<String> keySet = commonParams.keySet();
        HttpUrl.Builder builder = request.url().newBuilder();
        for (String key : keySet) {
            String value = commonParams.get(key);
            builder.addQueryParameter(key, value);
        }
        HttpUrl newUrl = builder.build();
        Request newRequest = request.newBuilder()
                .url(newUrl)
                .build();
        return chain.proceed(newRequest);
    }

    /**
     * post方法请求链路
     */
    private Response chainForPost(Chain chain, Request request, Map<String, String> commonParams) throws IOException {
        String bodyParams = bodyToString(request.body());
        StringBuilder buff = new StringBuilder();
        Set<String> addParamsSet = commonParams.keySet();
        if (!TextUtils.isEmpty(bodyParams)) {
            buff.append(bodyParams);
            buff.append("&");
        }
        for (String key : addParamsSet) {
            buff.append(key + "=" + commonParams.get(key) + "&");
        }
        String resultParams = "";
        if (!TextUtils.isEmpty(buff)) {
            resultParams = buff.toString().substring(0, buff.toString().length() - 1);
        }
        LogUtils.info("allParams：" + resultParams);
        // 將公共参数添加到表单中
        Request newRequst = request.newBuilder().post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), resultParams)).build();
        return chain.proceed(newRequst);
    }

    private String bodyToString(final RequestBody requestBody) {
        try {
            final RequestBody copy = requestBody;
            final Buffer buffer = new Buffer();
            if (copy != null) {
                copy.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
