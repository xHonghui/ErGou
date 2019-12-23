package com.laka.androidlib.net.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @Author Lyf
 * @CreateTime 2018/3/24
 * @Description BaseBean is a class for enclosing the response json of remote server.
 * If the code is meaning successful,
 * You may will invoke getData() method to get a string json of Bean parse it.
 **/
public class BaseBean {

    @Expose
    @SerializedName("ret")
    private int code;

    @Expose
    @SerializedName("data")
    private Object data;

    @Expose
    @SerializedName("msg")
    private String msg;

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
