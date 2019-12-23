package com.laka.ergou.common.interceptor

import android.text.TextUtils
import com.laka.androidlib.eventbus.EventBusManager
import com.laka.androidlib.util.LogUtils
import com.laka.androidlib.util.SPHelper
import com.laka.androidlib.util.rx.constant.RequestCommonCode
import com.laka.ergou.mvp.login.constant.LoginConstant
import com.laka.ergou.mvp.login.model.event.UserEvent
import com.laka.ergou.mvp.user.UserUtils.UserUtils
import com.laka.ergou.mvp.user.constant.UserConstant
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject
import java.io.EOFException

/**
 * @Author:summer
 * @Date:2019/8/15
 * @Description:token验证拦截器，读取响应体数据，如果code为token失效或者未登录，则发送响应event消息
 */
class IllegalTokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response? {
        val request = chain?.request()
        val response = chain?.proceed(request)
        val responseBody = response?.body()
        val contentLength = responseBody?.contentLength()
        try {
            if (!bodyEncoded(response?.headers())) {
                val source = responseBody?.source()
                source?.request(Long.MAX_VALUE)
                val buffer = source?.buffer()
                var charset = Charsets.UTF_8
                val contentType = responseBody?.contentType()
                if (contentType != null) {
                    try {
                        charset = contentType.charset(Charsets.UTF_8)!!
                    } catch (e: Exception) {
                        return response
                    }
                }
                if (!isPlaintext(buffer!!)) {
                    return response
                }
                if (contentLength != 0L) {
                    //读取响应数据，
                    val result = buffer.clone().readString(charset)
                    if (!TextUtils.isEmpty(result)) {
                        try {
                            val jsonObject = JSONObject(result)
                            if (jsonObject.has("code")) {
                                val code = jsonObject.optInt("code")
                                if (code == RequestCommonCode.LK_WRONG_USER_TOKEN
                                        || code == RequestCommonCode.LK_NOT_LOGIN) {
                                    LogUtils.info("登录失效，清除本地用户信息")
                                    //清除用户信息，如果当前页面时UserInfoActivity，则更新当前页面信息
                                    SPHelper.clearByFileName(LoginConstant.USER_INFO_FILENAME)
                                    UserUtils.clearUserInfo()
                                    EventBusManager.postEvent(UserEvent(UserConstant.LOGOUT_EVENT))
                                    EventBusManager.postEvent(UserEvent(UserConstant.EDIT_USER_INFO))
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    private fun bodyEncoded(headers: Headers?): Boolean {
        val contentEncoding = headers?.get("Content-Encoding")
        return contentEncoding != null && contentEncoding.toLowerCase() != "identity"
    }

    private fun isPlaintext(buffer: Buffer): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = if (buffer.size() < 64) buffer.size() else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0 until 16) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (e: EOFException) {
            return false
        }
    }

}