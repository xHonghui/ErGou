package com.laka.androidlib.util.retrofit;

import com.laka.androidlib.util.LogUtils;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * desc:
 * RequestParams,ResponseParams输出拦截器
 *
 * @author Rayman
 * @date 2017/12/15
 */

public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        // 这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();

        // 请求发起的时间
        long t1 = System.nanoTime();
        String method = request.method();
        String params = "";
        if ("POST".equals(method)) {
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ", ");
                }
                sb.delete(sb.length() - 1, sb.length());
            }
            params = sb.toString();
        }

        LogUtils.info(String.format("发送请求 %s " +
                        "\naction:%s " +
                        "\nparams:%s " +
                        "\non %s" +
                        "\n%s",
                request.url() == null ? "请求URL为空" : request.url(),
                method == null ? "请求方法为空" : method,
                params == null ? "Params参数列表为空" : params,
                chain.connection() == null ? "Request请求Connection连接为空" : chain,
                request.headers() == null ? "Request请求头为空" : request.headers()));

        // 响应返回，
        Response response = chain.proceed(request);
        // 收到响应的时间
        long t2 = System.nanoTime();
        // 这里不能直接使用response.body().string()的方式输出日志
        // 因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024);

        LogUtils.info("body",
                String.format("接收响应: [%s]" +
                                "\n返回json:%s " +
                                "\n响应时间:%.1fms " +
                                "\n响应头：%n%s",
                        response.request().url() == null ? "响应的URL为空" : response.request().url(),
                        responseBody.string() == null ? "响应体Body为空" : responseBody.string(),
                        (t2 - t1) / 1e6d,
                        response.headers() == null ? "响应体Header为空" : response.headers()));
        return response;
    }
}
