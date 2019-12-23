package com.laka.androidlib.util;

import android.text.TextUtils;

/**
 * @Author:Rayman
 * @Date:2018/12/21
 * @Description:数字类型转换
 */

public class NumberUtils {

    public static boolean isNumber(String string) {
        return TextUtils.isDigitsOnly(string);
    }

    /**
     * 转换成销量（销量/10000,float保留一位小数，并且加上"万"）
     *
     * @param sellCount
     * @return
     */
    public static String convertNumberToSellCount(int sellCount) {
        if (sellCount > 10000) {
            float count = ((float) sellCount / (float) 10000);
            float countOne = (float) (Math.round(count * 10)) / 10;
            return countOne + "万";
        }
        return sellCount + "";
    }

}
