package com.laka.ergou.common.upload

import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult

/**
 * @Author:summer
 * @Date:2019/1/14
 * @Description:
 */
interface UploadCallBack {
    fun onSuccess(request: PutObjectRequest, result: PutObjectResult)
    fun onFail(message:String)
}