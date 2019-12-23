package com.laka.androidlib.util;

import android.os.PatternMatcher;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: StringUtils
 * @Description: 字符串工具类
 * @Author: chuan
 * @Date: 10/01/2018
 */

public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("do not instantiate me , please.");
    }

    public final static String FILTER_NON_CHINESE = "[\\u4e00-\\u9fa5]";
    public final static String FILTER_REGEX_ONLY_ENGLISH_AND_NUMBER = "[^a-zA-Z0-9]";
    public static final String URL_PATTERN = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";

    /**
     * 判断字符串是否不为空（“null”字符串返回false）
     *
     * @param str 待判断字符串
     * @return true, 字符串不为空；false,字符串为空
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 待判断字符串
     * @return true, 字符串为空；false,字符串不为空
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0 || str.equals("null")||str.equals("NULL");
    }

    /**
     * 判断字符串是否为空，不包括" "
     *
     * @param str 待判断字符串
     * @return true, 字符串为空；false,字符串不为空
     */
    public static boolean isTrimEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断值是否有效
     *
     * @param tmp 具体值
     * @return
     */
    public static final boolean isValueString(String tmp) {
        boolean flag = false;
        flag = tmp != null && !"".equals(tmp) && !"null".equals(tmp);
        return flag;
    }

    /**
     * 判断两个String是否相等
     *
     * @param strA String
     * @param strB String
     * @return true ， 相等；false，不相等
     */
    public static boolean equals(String strA, String strB) {
        return (strA == null && strB == null)
                || (strA != null) && (strB != null) && strA.equals(strB);
    }

    /**
     * 判断两个String是否相等
     * 忽略大小写
     *
     * @param strA String
     * @param strB String
     * @return true ， 相等；false，不相等
     */
    public static boolean equalsIgnoreCase(String strA, String strB) {
        return (strA == null && strB == null)
                || (strA != null) && (strB != null) && strA.equalsIgnoreCase(strB);
    }

    /**
     * 转化String为int值
     *
     * @param str String值
     * @return String转化的int值或0
     */
    public static int parseInt(String str) {
        return parseInt(str, 0);
    }

    /**
     * 转化String为int值
     *
     * @param str      String值
     * @param defValue 转化失败时的默认值
     * @return String转化的int值或defValue
     */
    public static int parseInt(String str, int defValue) {
        int result = defValue;

        // 判断是否为空，同时判断是否为数字类型
        if (isTrimEmpty(str) || !TextUtils.isDigitsOnly(str)) {
            return result;
        }

        try {
            result = Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
            result = defValue;
        }

        return result;
    }

    /**
     * 转化String值为float值
     *
     * @param str String值
     * @return String转化成功的float值或0f
     */
    public static float parseFloat(String str) {
        return parseFloat(str, 0f);
    }

    /**
     * 转化String值为float值
     *
     * @param str      String值
     * @param defValue 转化失败时返回的默认值
     * @return String转化成功的float值或defValue
     */
    public static float parseFloat(String str, float defValue) {
        float result = defValue;

        if (isTrimEmpty(str) || !TextUtils.isDigitsOnly(str)) {
            return result;
        }

        try {
            result = Float.parseFloat(str);
        } catch (Exception e) {
            e.printStackTrace();
            result = defValue;
        }

        return result;
    }

    /**
     * 转化String值为Double值
     *
     * @param str String值
     * @return String转化成功的Double值或defValue
     */
    public static double parseDouble(String str) {
        return parseDouble(str, 0L);
    }

    /**
     * 转化String值为long值
     *
     * @param str      String值
     * @param defValue 转化失败时的默认值
     * @return String转化成功的double值或defValue
     */
    public static double parseDouble(String str, long defValue) {
        double result = defValue;

        if (isTrimEmpty(str) || !TextUtils.isDigitsOnly(str)) {
            return result;
        }

        try {
            result = Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
            result = defValue;
        }

        return result;
    }

    /**
     * 转化String值为long值
     *
     * @param str String值
     * @return String转化成功的long值或0L
     */
    public static long parseLong(String str) {
        return parseLong(str, 0L);
    }

    /**
     * 转化String值为long值
     *
     * @param str      String值
     * @param defValue 转化失败时的默认值
     * @return String转化成功的long值或defValue
     */
    public static long parseLong(String str, long defValue) {
        long result = defValue;

        if (isTrimEmpty(str) || !TextUtils.isDigitsOnly(str)) {
            return result;
        }

        try {
            result = Long.parseLong(str);
        } catch (Exception e) {
            e.printStackTrace();
            result = defValue;
        }

        return result;
    }


    /**
     * 判断是否为Http链接
     *
     * @param str
     * @return
     */
    public static boolean isHttpUrl(String str) {
        boolean result = false;
        if (TextUtils.isEmpty(str)) {
            return result;
        }
        Pattern pattern = Pattern.compile(URL_PATTERN);
        Matcher matcher = pattern.matcher(str);
        result = matcher.matches();
        return result;
    }

    /**
     * String值转化为boolean值
     *
     * @param str String值
     * @return boolean值
     */
    public static boolean parseBoolean(String str) {
        return "true".equalsIgnoreCase(str);
    }

    /**
     * 判断两个输入的String字符串的值是否有效
     * 有效判断：两者不为空时，max > min
     * 同时为空，两者随意一个不为空
     *
     * @param minValueStr 最低值
     * @param maxValueStr 最高值
     * @return
     */
    public static boolean compareValueValid(String minValueStr, String maxValueStr) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(minValueStr) && !TextUtils.isEmpty(maxValueStr)) {
            double minValue = Double.parseDouble(minValueStr);
            double maxValue = Double.parseDouble(maxValueStr);
            if (minValue > maxValue) {
                isValid = false;
            } else {
                isValid = true;
            }
        } else if (!TextUtils.isEmpty(minValueStr) || !TextUtils.isEmpty(maxValueStr)) {
            isValid = true;
        } else if (TextUtils.isEmpty(minValueStr) && TextUtils.isEmpty(maxValueStr)) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 判断传入的若干个String是否为空字符串
     *
     * @param strings
     * @return
     */
    public static boolean isStringNotEmpty(String... strings) {
        boolean result = true;
        for (String str : strings) {
            if (TextUtils.isEmpty(str)) {
                result = false;
            }
        }
        return result;
    }

    public static String stringFilter(String str, String regEx) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 电话号码正则表达式
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * description:将Span Style类型String转换成Html支持的格式
     *
     * @param spanStyleString Span类型标签
     **/
    public static String convertSpanStyleStr2HtmlStr(String spanStyleString) {
        if (TextUtils.isEmpty(spanStyleString)) {
            return "";
        } else {
            String result = spanStyleString.replace("span style=\"color:", "font color='")
                    .replace("span style=\"font-weight:", "font weight='")
                    .replace("span style=\"font-size:", "font size='")
                    .replace("color:", "color='")
                    .replace("font-weight:", "weight='")
                    .replace("font-size:", "size='")
                    .replace(";", "' ")
                    .replace("\">", "'>")
                    .replace("</span>", "</font>");
            //LogUtils.info("输出元数据：" + spanStyleString + "\n输出修改后数据：" + result);
            return result;
        }
    }
}
