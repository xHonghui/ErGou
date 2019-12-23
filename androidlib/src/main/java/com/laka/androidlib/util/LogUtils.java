package com.laka.androidlib.util;

import android.util.Log;

import com.orhanobut.logger.Logger;

import java.util.Locale;

/**
 * @ClassName: LogUtils
 * @Description: log工具类
 * @Author: chuan
 * @Date: 08/01/2018
 */

public final class LogUtils {

    private final static String TAG = LogUtils.class.getSimpleName();

    private LogUtils() {
        throw new UnsupportedOperationException("do not instantiate me , please.");
    }

    /**
     * 使用默认tag打印debug类别的log
     * 普通log
     *
     * @param msg 打印内容
     */
    public static void log(String msg) {
        if (ApplicationUtils.isDebug()) {
            Log.d(TAG, msg);
        }
    }

    /**
     * 使用默认tag打印debug类别的log
     * 普通log
     *
     * @param msg 打印内容
     */
    public static void log(String tag, String msg) {
        if (ApplicationUtils.isDebug()) {
            Log.d(tag, msg);
        }
    }

    /**
     * 使用默认tag打印debug类别的log
     *
     * @param msg 打印内容
     */
    public static void Logger(String msg) {
        debug(TAG, msg);
    }

    /**
     * 打印verbose级别的log
     *
     * @param tag 打印标识
     * @param msg 打印内容
     */
    public static void verbose(String tag, String msg) {
        if (ApplicationUtils.isDebug()) {
            Logger.v(tag, msg);
        }
    }

    /**
     * 打印verbose级别的log
     *
     * @param tag    打印标识
     * @param format 打印格式
     * @param args   打印内容
     */
    public static void verbose(String tag, String format, Object... args) {
        if (ApplicationUtils.isDebug()) {
            Logger.v(tag, buildMessage(format, args));
        }
    }

    /**
     * 打印info级别的log
     *
     * @param msg 打印内容
     */
    public static void info(String msg) {
        if (ApplicationUtils.isDebug()) {
            Logger.i(msg);
        }
    }

    /**
     * 打印info级别的log
     *
     * @param tag 打印标识
     * @param msg 打印内容
     */
    public static void info(String tag, String msg) {
        if (ApplicationUtils.isDebug()) {
            Logger.i(tag, msg);
        }
    }

    /**
     * 打印info级别的log
     *
     * @param tag    打印标识
     * @param format 打印格式
     * @param args   打印内容
     */
    public static void info(String tag, String format, Object... args) {
        if (ApplicationUtils.isDebug()) {
            Logger.i(tag, buildMessage(format, args));
        }
    }

    /**
     * 打印debug级别的log
     *
     * @param msg 打印内容
     */
    public static void debug(String msg) {
        if (ApplicationUtils.isDebug()) {
            Logger.d(msg);
        }
    }

    /**
     * 打印debug级别的log
     *
     * @param tag 打印标识
     * @param msg 打印内容
     */
    public static void debug(String tag, String msg) {
        if (ApplicationUtils.isDebug()) {
            Logger.d(tag, msg);
        }
    }

    /**
     * 打印debug级别的log
     *
     * @param tag    打印标识
     * @param format 打印格式
     * @param args   打印内容
     */
    public static void debug(String tag, String format, Object... args) {
        if (ApplicationUtils.isDebug()) {
            Logger.d(tag, buildMessage(format, args));
        }
    }

    /**
     * 打印warn级别的log
     *
     * @param msg 打印内容
     */
    public static void warn(String msg) {
        if (ApplicationUtils.isDebug()) {
            Logger.w(msg);
        }
    }

    /**
     * 打印warn级别的log
     *
     * @param tag 打印标识
     * @param msg 打印内容
     */
    public static void warn(String tag, String msg) {
        if (ApplicationUtils.isDebug()) {
            Logger.w(tag, msg);
        }
    }

    /**
     * 打印warn级别的log
     *
     * @param tag    打印标识
     * @param format 打印格式
     * @param args   打印内容
     */
    public static void warn(String tag, String format, Object... args) {
        if (ApplicationUtils.isDebug()) {
            Logger.w(tag, buildMessage(format, args));
        }
    }

    /**
     * 打印warn级别的log并抛出错误
     *
     * @param tag 打印标识
     * @param msg 打印内容
     * @param tr  错误抛出
     */
    public static void warn(String tag, String msg, Throwable tr) {
        if (ApplicationUtils.isDebug()) {
            Logger.e(tag, msg, (Exception) tr);
        }
    }

    /**
     * 打印warn级别的log，并抛出错误
     *
     * @param tag    打印标识
     * @param tr     错误抛出
     * @param format 打印格式
     * @param args   打印内容
     */
    public static void warn(String tag, Throwable tr, String format, Object... args) {
        if (ApplicationUtils.isDebug()) {
            Logger.e(tag, buildMessage(format, args), (Exception) tr);
        }
    }

    /**
     * 打印error级别的log
     *
     * @param exception 具体的错误
     */
    public static void error(Exception exception) {
        if (ApplicationUtils.isDebug()) {
            Logger.e(exception);
        }
    }

    /**
     * 打印error级别的log
     *
     * @param msg 打印内容
     */
    public static void error(String msg) {
        if (ApplicationUtils.isDebug()) {
            Logger.e(msg);
        }
    }

    /**
     * 打印error级别的log
     *
     * @param tag 打印标识
     * @param msg 打印内容
     */
    public static void error(String tag, String msg) {
        if (ApplicationUtils.isDebug()) {
            Logger.e(tag, msg);
        }
    }

    /**
     * 打印error级别的log
     *
     * @param tag    打印标识
     * @param format 打印格式
     * @param args   打印内容
     */
    public static void error(String tag, String format, Object... args) {
        if (ApplicationUtils.isDebug()) {
            Logger.e(tag, buildMessage(format, args));
        }
    }

    /**
     * 打印error级别的log并抛出错误
     *
     * @param tag 打印标识
     * @param msg 打印内容
     * @param tr  错误抛出
     */
    public static void error(String tag, String msg, Throwable tr) {
        if (ApplicationUtils.isDebug()) {
            Logger.e(tag, msg, (Exception) tr);
        }
    }

    /**
     * 打印error级别的log，并抛出错误
     *
     * @param tag    打印标识
     * @param tr     错误抛出
     * @param format 打印格式
     * @param args   打印内容
     */
    public static void error(String tag, Throwable tr, String format, Object... args) {
        if (ApplicationUtils.isDebug()) {
            Logger.e(tag, buildMessage(format, args), (Exception) tr);
        }
    }

    /**
     * 输出log在控制台
     *
     * @param tag 输出标识
     * @param msg 输出内容
     */
    public static void print(String tag, String msg) {
        if (ApplicationUtils.isDebug()) {
            System.out.print(tag + "," + msg);
        }
    }


    /**
     * 构建自定义的打印格式
     *
     * @param format 打印格式
     * @param args   打印内容
     * @return 按自定义格式生成的log String
     */
    private static String buildMessage(String format, Object... args) {
        String msg = (args == null) ? format : String.format(Locale.US, format, args);
        StackTraceElement[] traces = new Throwable().fillInStackTrace().getStackTrace();

        String caller;

        if (traces == null || traces.length == 0) {
            caller = "<unknown>";
        } else {
            String callingClass = traces[0].getClassName();
            callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
            callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

            caller = callingClass + "." + traces[0].getMethodName();
        }

        return String.format(Locale.US, "[%d] %s: %s",
                Thread.currentThread().getId(), caller, msg);
    }
}
