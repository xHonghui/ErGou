package com.laka.androidlib.net.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.androidlib.constant.AppConstant;

/**
 * @Author Rayman
 * @CreateTime 2018/12/13
 * @Description 请求Response
 **/
public class BaseResponse<T> {

    @Expose
    @SerializedName(value = "resultcode", alternate = {"code"})
    private int code;

    @Expose
    @SerializedName(value = "reason", alternate = {"msg"})
    private String msg;

    @Expose
    @SerializedName(value = "result", alternate = {"data"})
    private T data;

    @Expose
    @SerializedName(value = "timestamp")
    private long timeStamp;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isSuccess() {
        return code == AppConstant.API_SUCCESS_CODE;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
