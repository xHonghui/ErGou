package com.laka.androidlib.listener;

import android.graphics.Bitmap;

/**
 * @ClassName: IBitmapGainListener
 * @Description: 获取bitmap的结果回调
 * @Author: chuan
 * @Date: 22/01/2018
 */

public interface IBitmapGainListener {
    int FAIL_CODE_URL_NULL = 1;
    int FAIL_CODE_OTHER = 2;

    /**
     * 获取bitmap成功
     *
     * @param bitmap 获取的bitmap
     */
    void onSuccess(Bitmap bitmap);

    /**
     * 获取bitmap失败
     *
     * @param code      失败码
     * @param throwable 错误原因，可为null
     */
    void onFail(int code, Throwable throwable);
}
