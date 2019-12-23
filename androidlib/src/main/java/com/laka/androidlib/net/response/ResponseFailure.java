package com.laka.androidlib.net.response;


/**
 * @Author Lyf
 * @CreateTime 2018/2/9
 * @Description An HTTP Result which contains cod、msg when request is failed.
 **/
public class ResponseFailure {

    private int code;

    private String msg;

    public ResponseFailure() {
    }

    public ResponseFailure(String msg) {
        this.msg = msg;
    }

    public ResponseFailure(int code, String msg) {
        this.code = code;
        this.msg = msg;
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

    @Override
    public String toString() {
        return "ResponseFailure{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
