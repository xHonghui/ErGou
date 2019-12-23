package com.laka.androidlib.util;

import android.text.TextUtils;

import com.laka.androidlib.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @ClassName: DateUtils
 * @Description: 用于处理日期时间的工具类
 * @Author: chuan
 * @Date: 26/03/2018
 */
public class DateUtils {

    private final static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);

    private DateUtils() {
        throw new UnsupportedOperationException("do not instantiate me , please.");
    }

    /**
     * 时间转化为显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getTimeStr(long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        if (calendar.before(inputTime)) {
            //今天23:59在输入时间之前，解决一些时间误差，把当天时间显示到这里
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + ResourceUtils.getString(R.string.picker_year) + "MM" + ResourceUtils.getString(R.string.picker_month) + "dd" + ResourceUtils.getString(R.string.picker_day));
            return sdf.format(currenTimeZone);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            return ResourceUtils.getString(R.string.time_yesterday);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("M" + ResourceUtils.getString(R.string.picker_month) + "d" + ResourceUtils.getString(R.string.picker_day));
                return sdf.format(currenTimeZone);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + ResourceUtils.getString(R.string.picker_year) + "MM" + ResourceUtils.getString(R.string.picker_month) + "dd" + ResourceUtils.getString(R.string.picker_day));
                return sdf.format(currenTimeZone);
            }
        }
    }

    public static String convertYearTime(String s) {
        if (TextUtils.isEmpty(s)) {
            s = "0";
        }
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        long n = Long.parseLong(s);

        return f.format(new Date(n * 1000L));
    }


    /**
     * 时间戳转换成日期格式
     *
     * @param timeStr 时间戳字符串
     * @param format  日期格式，如：yyyy-MM-dd
     * @return
     */
    public static String timeStamp2DateTime(String timeStr, String format) {

        SimpleDateFormat f = new SimpleDateFormat(format);
        long n = Long.parseLong(timeStr);

        return f.format(new Date(n * 1000L));
    }

    /**
     * 时间戳转换成日期格式
     *
     * @param timeStr 时间戳字符串
     * @param format  日期格式，如：yyyy-MM-dd
     * @return
     */
    public static String timeStamp2DateTime(long timeStr, String format) {

        SimpleDateFormat f = new SimpleDateFormat(format);
        return f.format(new Date(timeStr));
    }

    /**
     * 返回倒计时格式的时间
     * 例子：2天18小时29分33秒
     * by leiwei 订单的剩余时间
     */
    public static String countDownTime2(String s) {
        StringBuffer buffer = new StringBuffer();

        if (StringUtils.isValueString(s)) {
            long n = Long.parseLong(s) * 1000L;
            long now = System.currentTimeMillis();
            if (n <= now) {
                return "0秒";
            }

            long l = (n - now);
            int day = (int) (l / (24 * 60 * 60 * 1000L));
            int hour = (int) ((l - day * (24 * 60 * 60 * 1000L)) / (60 * 60 * 1000L));
            int minute = (int) (((l - day * (24 * 60 * 60 * 1000L) - hour * (60 * 60 * 1000L)) / (60 * 1000L)));
            int second = (int) (((l - day * (24 * 60 * 60 * 1000L) - hour * (60 * 60 * 1000L) - minute * (60 * 1000L))) / 1000L);

            if (day > 0) {
                buffer.append(day + "天");
            } else {
                buffer.append("");
            }
            if (hour > 0) {
                if (hour <= 9) {
                    buffer.append("0" + hour + "小时");
                } else {
                    buffer.append(hour + "小时");
                }
            } else {
                buffer.append("00小时");
            }
            if (minute > 0) {
                if (minute <= 9) {
                    buffer.append("0" + minute + "分钟");
                } else {
                    buffer.append(minute + "分钟");
                }
            } else {
                buffer.append("00分钟");
            }
            if (second > 0) {
                if (second <= 9) {
                    buffer.append("0" + second + "秒");
                } else {
                    buffer.append(second + "秒");
                }
            } else {
                buffer.append("00秒");
            }

        } else {
            buffer.append("");
        }

        return buffer.toString();
    }

    /**
     * 返回倒计时格式的时间
     * 例子：2天18小时29分33秒
     */
    public static String getCountDownTime(long timeStamp, String appendText) {

        StringBuilder builder = new StringBuilder();

        if (timeStamp != 0) {

            long day = timeStamp / (24 * 60 * 60 * 1000);
            long hour = (timeStamp / (60 * 60 * 1000) - day * 24);
            long minute = ((timeStamp / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long second = (timeStamp / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

            builder.append(appendTime(Math.min(day, 99), "天"))
                    .append(appendTime(hour, "时"))
                    .append(appendTime(minute, "分"))
                    .append(appendTime(second, "秒"))
                    .append(appendText);
        }

        return builder.toString();
    }

    /**
     * @param time
     * @param appendText
     * @return
     */
    private static String appendTime(long time, String appendText) {

        StringBuilder builder = new StringBuilder();

        if (time > 0) {

            // 天数不足2位，不需额外补0.
            if (!"天".equals(appendText) && time < 10) {
                builder.append("0");
            }

            builder.append(String.valueOf(time));
            builder.append(appendText);
        } else if (!"天".equals(appendText)) {
            builder.append("00").append(appendText);
        }

        return builder.toString();
    }

    /**
     * 返回倒计时格式的时间
     * 例:01:11:45:55
     */
    public static String getCountDownTimeWithColon(long timeStamp, String appendText) {

        StringBuilder builder = new StringBuilder();

        if (timeStamp != 0) {

            long day = timeStamp / (24 * 60 * 60 * 1000);
            long hour = (timeStamp / (60 * 60 * 1000) - day * 24);
            long minute = ((timeStamp / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long second = (timeStamp / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

            if (day != 0) {
                builder.append(appendTime(Math.min(day, 99), ":"));
            }

            if (hour != 0) {
                builder.append(appendTime(hour, ":"));
            }

            builder.append(appendTime(minute, ":"));
            builder.append(appendTime(second, ""));
        }

        return builder.toString();
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据后台返回的时间戳与本地时间戳比对，转换格式 .
     * < 1 min：刚刚
     * 1 <= x <= 60：多少分钟
     * 60 <= x <= 24*60：多少小时
     * 24 * 60 < x <= 7 * 24 * 60：多少天
     *
     * @param timetamp 后台时间戳
     * @return
     */
    public static String getTime(String timetamp) {

        if (TextUtils.isEmpty(timetamp)) {
            return "";
        }

        Long date = Long.parseLong(timetamp);

        // 测试代码
//        if (timetamp.length() > 12) {
//            date = Long.parseLong(timetamp) / 1000;
//        }

        Long now = System.currentTimeMillis() / 1000;
        long diff = now - date;
        String timevalue = "";
        int time = (int) (diff / 60);
        if (time < 1) {
            timevalue = "刚刚";
        } else if (time >= 1 && time < 60) {
            timevalue = time + "分钟前";
        } else if (time >= 60 && time <= 24 * 60) {
            timevalue = (time / 60) + "小时前";
        } else if (time > 24 * 60 && time <= 7 * 24 * 60) {
            timevalue = (time / 60 / 24) + "天前";
        } else {
            Date d = new Date(date * 1000);
            timevalue = new SimpleDateFormat("yyyy年MM月dd日").format(d);
        }
        return timevalue;
    }

    /**
     * 获取今天0点的Timestamp
     *
     * @return
     */
    public static long getDayBeginTimestamp() {
        Date date = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        Date date2 = new Date(date.getTime() - gc.get(gc.HOUR_OF_DAY) * 60 * 60
                * 1000 - gc.get(gc.MINUTE) * 60 * 1000 - gc.get(gc.SECOND)
                * 1000);
        return new Timestamp(date2.getTime()).getTime();
    }

    /**
     * 判断是否为今天
     *
     * @param timeStamp 传入的时间戳需要按照Java的毫秒级规范
     * @return
     */
    public static boolean isToday(long timeStamp) {
        long todayTimeStamp = getDayBeginTimestamp();
        if ((timeStamp * 1000L - todayTimeStamp) < 3600000 * 24) {
            return true;
        } else {
            return false;
        }
    }
}
