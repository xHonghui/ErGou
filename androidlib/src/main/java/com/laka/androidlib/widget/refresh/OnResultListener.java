package com.laka.androidlib.widget.refresh;

/**
 * @Author Lyf
 * @CreateTime 2018/5/2
 * @Description 请求结果的回调
 **/
public interface OnResultListener {

    /**
     * 失败的回调
     * @param errorCode 错误码
     * @param errorMsg 错误信息
     */
    void onFailure(int errorCode, String errorMsg);

    /**
     * 成功的回调
     * @param response
     */
    void onResponse(BaseListBean response);
}
