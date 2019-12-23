package com.laka.androidlib.util.rx.exception;

import java.io.IOException;

/**
 * @Author:Rayman
 * @Date:2018/12/18
 * @Description:基于RxJava封装的基础Exception类
 */

public class BaseException extends IOException {

    /**
     * description:网络请求错误（根据RxJava onError的Exception类型划分）
     **/
    public static final int NETWORK_ERROR = 0x11;

    private String errorMsg;
    private int code;

    public BaseException(int code, String errorMsg) {
        super(errorMsg);
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg == null ? "" : errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
