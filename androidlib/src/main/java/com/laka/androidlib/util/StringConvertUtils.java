package com.laka.androidlib.util;

import android.text.TextUtils;

/**
 * @Author:Rayman
 * @Date:2019/1/19
 * @Description:业务逻辑常用字符串转换工具
 */

public class StringConvertUtils {

    /**
     * 将Phone加密后返回
     *
     * @param phone
     * @return
     */
    public static String convertEncryptionPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return "";
        }
        if (TextUtils.isDigitsOnly(phone)) {
            StringBuffer buffer = new StringBuffer(phone);
            return buffer.replace(3, buffer.length() - 4, "****").toString();
        } else {
            return phone;
        }
    }

}
