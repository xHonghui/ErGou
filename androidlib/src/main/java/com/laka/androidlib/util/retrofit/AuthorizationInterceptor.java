package com.laka.androidlib.util.retrofit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.laka.androidlib.features.user.IUser;
import com.laka.androidlib.util.EncryptUtils;
import com.laka.androidlib.util.LogUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
 * desc:
 * Token权限拦截器(需要获取到用户的UserID和UserToken进行请求)
 *
 * @author Rayman
 * @date 2017/12/15
 */

public class AuthorizationInterceptor<D extends IUser> implements Interceptor {


    private static final String SIGN_METHOD_MD5 = "md5";
    private static final String LAKA_APP_SECRET = "Lakatv@!#$B2416a^%&Z*";
    private static final String REQUEST_DATA_FORMAT = "json";
    private static final String REQUEST_API_VERSION = "2.0";
    private static final String REQUEST_SIMPLIFY = "false";
    private static final String REQUEST_PLATFORM = "Android";
    private Context context;

    public AuthorizationInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        LogUtils.info("request url " + chain.request().url().toString());
        Request request = chain.request();
        String method = request.method();
        Map<String, String> signParams = new HashMap<>();//需要签名的参数
        Map<String, String> addParams = new HashMap<>();//添加的公共参数

        //1、通过 query 方式添加参数，直接拼接到了 url 中，获取出来
        HttpUrl httpUrl = request.url();
        Set<String> paramsKey = httpUrl.queryParameterNames();
        for (String key : paramsKey) {
            String value = httpUrl.queryParameter(key);
            signParams.put(key, value);
            LogUtils.info("query_params:" + key + "=" + value);
        }

        //2、通过 Field 方式添加参数到 RequestBody 中，获取出来
        StringBuilder postBodyAllParams = new StringBuilder();
        String bodyStr = bodyToString(request.body());
        LogUtils.info("body_:" + bodyStr);
        if (!TextUtils.isEmpty(bodyStr) && method.equals("POST")) {//post请求才有RequestBody
            String[] split = bodyStr.split("&");
            for (String temp : split) {
                String[] keyValueArry = temp.split("=");
                if (keyValueArry.length == 2) {
                    /*
                    * keyValue[0]: key   keyValue[1]: value
                    * URLDecoder.decode 使用 utf-8 进行解码，由于参数是添加到 RequestBody 中，okhttp 默认会使用 utf-8 对 RequestBody 的所有参数进行编码，
                    * 假如参数A的值是中文，则会被编码成为16进制表示的字符串 “A = %E6%A9%98%E5%AD%90%E6%B4%B2%E4%BB%80%E4%B9%88”，客户端使用这个字符串进行签名，
                    * 那么后台在验证参数的时候就会不通过，因为http 在响应我们的请求后，首先会对RequestBody的参数进行解码，参数 A 恢复原来的值，然后后台
                    * 使用原来的值去验证 sign 参数，就会出错。
                    * */
                    signParams.put(keyValueArry[0], URLDecoder.decode(keyValueArry[1], "utf-8"));
                    LogUtils.info("body_params:" + keyValueArry[0] + "=" + keyValueArry[1]);
                } else if (keyValueArry.length == 1) {
                    //有可能的参数是 key=  这种形式，这样keyValueArry.length==1
                    signParams.put(keyValueArry[0], URLDecoder.decode("", "utf-8"));
                }
            }
            //请求体有参数，则添加 ‘&’符号
            postBodyAllParams.append(bodyStr);
            postBodyAllParams.append("&");
        }
        //加密参数
        String sign = signTopRequest(signParams, LAKA_APP_SECRET, SIGN_METHOD_MD5);
        LogUtils.info("sign:" + sign);
        if (method.equals("GET")) {
            addParams.put("sign", sign);
            return chainForGet(chain, request, addParams);
        } else if (method.equals("POST")) {
            //如果是 post 请求方式，则将公共参数添加到表单中
            LogUtils.info("request_body_params:" + postBodyAllParams.toString());
            postBodyAllParams.append("&sign=" + sign);
            Request newRequst = request.newBuilder().post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), postBodyAllParams.toString())).build();
            return chain.proceed(newRequst);
        }
        return chain.proceed(chain.request());
    }

    @NonNull
    private Response chainForGet(Chain chain, Request request, Map<String, String> addParams) throws IOException {
        Set<String> keySet = addParams.keySet();
        HttpUrl.Builder builder = request.url().newBuilder();
        for (String key : keySet) {
            String value = addParams.get(key);
            builder.addQueryParameter(key, value);
        }
        HttpUrl newUrl = builder.build();
        Request newRequest = request.newBuilder()
                .url(newUrl)
                .build();
        return chain.proceed(newRequest);
    }

    /**
     * 参数签名
     *
     * @param params
     * @param secret
     * @param signMethod
     * @return
     * @throws IOException
     */
    public String signTopRequest(Map<String, String> params, String secret, String signMethod) throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        if (SIGN_METHOD_MD5.equals(signMethod)) { // MD5 签名方式
            query.append(secret);
        }
        for (String key : keys) {
            String value = params.get(key);
            query.append(key).append(value);
//            if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
//                query.append(key).append(value);
//            }
        }

        // 第三步：使用MD5加密
        if (SIGN_METHOD_MD5.equals(signMethod)) {
            query.append(secret);
            LogUtils.info("query:" + query);
            return new String(EncryptUtils.encryptMD5(query.toString())).toUpperCase();
        } else {
            throw new RuntimeException("md5 encrypt error");
        }
    }


    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
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
