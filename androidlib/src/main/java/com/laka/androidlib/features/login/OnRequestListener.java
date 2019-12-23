package com.laka.androidlib.features.login;

/**
 * @Author Lyf
 * @CreateTime 2018/5/2
 * @Description 网络请求的监听者，实际的网络请求，在这个方法里面执行。
 * 相当于封装了refresh和loadmore关于页数的回调
 **/
public interface OnRequestListener<T> {

    /**
     * @param page            请求的页码
     * @param resultListener 请求的参数
     * @return 返回完整的请求地址.不管get还是post，都拼成get的形式，该值仅做判断用。
     */
    String onRequest(int page, T resultListener);
}
