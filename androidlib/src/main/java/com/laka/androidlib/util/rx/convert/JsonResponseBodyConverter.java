package com.laka.androidlib.util.rx.convert;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.rx.constant.RequestCommonCode;
import com.laka.androidlib.util.rx.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @Author:summer
 * @Date:2019/1/17
 * @Description:统一返回格式，只返回data节点的数据
 */

final class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private static final String TAG = "JsonConverter";

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String message = "";
        try {
            String body = value.string();
            JSONObject jsonObj = new JSONObject(body);
            int code = jsonObj.optInt("code");
            String msg = jsonObj.optString("msg", RequestCommonCode.REQUEST_FAIL);
            Log.i(TAG, "======code=====" + String.valueOf(code));
            Log.i(TAG, "====== msg =====" + msg);
            if (code == RequestCommonCode.LK_OK) {
                if (jsonObj.has("data")) {
                    Object data = jsonObj.get("data");
                    body = data.toString();
                    Log.i(TAG, "body:" + body);
                    return adapter.fromJson(body);
                } else {
                    //return (T) msg;//只要adapter.fromJson() 出错的时候才会走到这里，所以这里应该抛一个异常
                    Log.i(TAG, "只要adapter.fromJson() 出错的时候才会走到这里，所以这里应该抛一个异常");
                    message = RequestCommonCode.DATA_FORMAT_EXCEPTION_MSG;
                }
            } else if (code == RequestCommonCode.LK_ERROR_REDIS_CONNECT_FATLED
                    || code == RequestCommonCode.LK_ERROR_MYSQL_CONNECT_FATLED
                    || code == RequestCommonCode.LK_ERROR_MYSQL_QUERY_FATLED) { // 连接失败 查询失败
                message = RequestCommonCode.LOADING_ERROR_MSG;
            } else if (code == RequestCommonCode.LK_NOT_LOGIN) { // 未登录
                message = RequestCommonCode.USER_NOT_LOGIN_MSG;
            } else if (code == RequestCommonCode.LK_WRONG_USER_TOKEN) {  // token失效
                message = RequestCommonCode.USER_TOKEN_INVALID_MSG;
            } else if (code == RequestCommonCode.LK_ERROR_SOURCH_EMPTRY) { // 搜索为空
                message = RequestCommonCode.EMPTRY_DATA_MSG;
            } else if (code == RequestCommonCode.LK_PRODUCT_DETAIL_NO_DATA) { //产品详情页无数据或者商品已下架
                message = RequestCommonCode.PRODUCT_DETAIL_NO_DATA_ERROR;
            } else {
                message = msg;
            }
            throw new ApiException(code, message);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            value.close();
        }
    }
}
