package com.laka.androidlib.util.rx.exception;

import com.google.gson.JsonSyntaxException;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.network.NetworkUtils;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @Author:Rayman
 * @Date:2019/1/17
 * @Description:针对OKHttp网络请求异常处理类
 */

public class HttpExceptionHandler {

    /**
     * 统一错误处理 -> 汉化了提示，以下错误出现的情况 (ps:不一定百分百按我注释的情况，可能其他情况)
     */
    public static Throwable unifiedError(Throwable e) {
        Throwable throwable;
        if (e instanceof UnknownHostException) {
            //无网络的情况，或者主机挂掉了。返回，对应消息
            // Unable to resolve host "m.app.haosou.com": No address associated with hostname
            if (!NetworkUtils.isNetworkAvailable()) {
                //无网络
                throwable = new Throwable("hello?好像没网络啊！", e.getCause());
            } else {
                //主机挂了，也就是你服务器关了
                throwable = new Throwable("服务器开小差,请稍后重试！", e.getCause());
            }
            LogUtils.error("当前无网络或者服务器挂掉了：" + e.getMessage());
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof SocketException) {
            //连接超时等
            throwable = new Throwable("网络连接超时，请检查您的网络状态！", e.getCause());
            LogUtils.error("网络连接失败：" + e.getMessage());
        } else if (e instanceof NumberFormatException || e instanceof IllegalArgumentException || e instanceof JsonSyntaxException) {
            //也就是后台返回的数据，与你本地定义的Gson类，不一致，导致解析异常 (ps:当然这不能跟客户这么说)
            throwable = new Throwable("未能请求到数据，攻城狮正在修复!", e.getCause());
            LogUtils.error("Gson解析数据异常：" + e.getMessage());
        } else if (e instanceof NetWorkException) {
            throwable = new NetWorkException(BaseException.NETWORK_ERROR, "网络请求失败，请检查您的网络状态！");
            LogUtils.error("OkHttp网络拦截器抛出无网络异常");
        } else if (e instanceof ApiException) {
            ApiException exception = (ApiException) e;
            throwable = new ApiException(exception.getCode(), exception.getErrorMsg());
            LogUtils.error("业务逻辑Api错误异常：" + exception.getCode() + "\n错误信息：" + exception.getErrorMsg());
        } else {
            //其他 未知
            throwable = new Throwable("哎呀故障了，攻城狮正在修复！", e.getCause());
        }
        return throwable;
    }
}
