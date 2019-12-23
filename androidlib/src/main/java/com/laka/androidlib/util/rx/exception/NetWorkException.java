package com.laka.androidlib.util.rx.exception;

import java.io.IOException;

/**
 * @Author:Rayman
 * @Date:2019/1/16
 * @Description:网络错误Exception类
 */

public class NetWorkException extends IOException {

    private int code;

    public NetWorkException(int code, String errorMsg) {
        super(errorMsg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
