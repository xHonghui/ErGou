package com.laka.androidlib.util.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.laka.androidlib.util.ApplicationUtils;

/**
 * @ClassName: NetworkUtils
 * @Description: 判断网络状态
 * @Author: chuan
 * @Date: 09/01/2018
 */

public class NetworkUtils {
    private final static String NETWORK_NAME_UNKNOWN = "unknown";
    private final static String NETWORK_NAME_WIFI = "wifi";
    private final static String NETWORK_NAME_2G = "2G";
    private final static String NETWORK_NAME_3G = "3G";
    private final static String NETWORK_NAME_4G = "4G";

    /**
     * 当前网络状态为：无网络
     */
    public static final int NETWORK_STATE_NONE = 0;
    /**
     * 当前网络状态为：wifi
     */
    public static final int NETWORK_STATE_WIFI = 1;
    /**
     * 当前网络状态为：2g
     */
    public static final int NETWORK_STATE_2G = 2;
    /**
     * 当前网络状态为：3g
     */
    public static final int NETWORK_STATE_3G = 3;
    /**
     * 当前网络状态为：4g
     */
    public static final int NETWORK_STATE_4G = 4;
    /**
     * 当前网络状态为：未知网络
     */
    public static final int NETWORK_STATE_UNKNOWN = 5;

    private NetworkUtils() {
        throw new UnsupportedOperationException("do not instantiate me , please.");
    }

    /**
     * 获取网络名称
     *
     * @return {@link NetworkUtils#NETWORK_NAME_UNKNOWN},
     * {@link NetworkUtils#NETWORK_NAME_2G},
     * {@link NetworkUtils#NETWORK_NAME_3G},
     * {@link NetworkUtils#NETWORK_NAME_4G},
     * {@link NetworkUtils#NETWORK_NAME_WIFI}
     */
    public static String getNetWorkName() {
        int net = getNetworkState();
        if (net == NETWORK_STATE_NONE) {
            return NETWORK_NAME_UNKNOWN;
        } else if (net == NETWORK_STATE_WIFI) {
            return NETWORK_NAME_WIFI;
        } else if (net == NETWORK_STATE_2G) {
            return NETWORK_NAME_2G;
        } else if (net == NETWORK_STATE_3G) {
            return NETWORK_NAME_3G;
        } else if (net == NETWORK_STATE_4G) {
            return NETWORK_NAME_4G;
        }
        return NETWORK_NAME_UNKNOWN;
    }

    /**
     * 获取当前网络状态
     *
     * @return {@link NetworkUtils#NETWORK_STATE_NONE},
     * {@link NetworkUtils#NETWORK_STATE_WIFI},
     * {@link NetworkUtils#NETWORK_STATE_2G},
     * {@link NetworkUtils#NETWORK_STATE_3G},
     * {@link NetworkUtils#NETWORK_STATE_4G},
     * {@link NetworkUtils#NETWORK_STATE_UNKNOWN}
     */
    public static int getNetworkState() {
        ConnectivityManager connManager = (ConnectivityManager) ApplicationUtils.getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connManager == null) {
            return NETWORK_STATE_UNKNOWN;
        }

        NetworkInfo netInfo = connManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            switch (netInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return NETWORK_STATE_WIFI;
                case ConnectivityManager.TYPE_MOBILE:
                    switch (netInfo.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_GPRS: //联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: //电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: //移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NETWORK_STATE_2G;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: //电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NETWORK_STATE_3G;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NETWORK_STATE_4G;
                        default:
                            return NETWORK_STATE_UNKNOWN;
                    }

                default:
                    return NETWORK_STATE_UNKNOWN;
            }
        }
        return NETWORK_STATE_NONE;
    }

    /**
     * 当前网络是否为WiFi
     *
     * @return true, 表示当前网络是wifi。false,表示当前网络不为wifi，或没有网络。
     */
    public static boolean isWifi() {
        return getNetworkState() == NETWORK_STATE_WIFI;
    }

    /**
     * 当前网络是否为移动网络
     *
     * @return true, 表示当前网络是手机流量。false,表示当前网络不为手机流量，或没有网络。
     */
    public static boolean isMobile() {
        int state = getNetworkState();
        return state == NETWORK_STATE_2G
                || state == NETWORK_STATE_3G
                || state == NETWORK_STATE_4G;
    }

    /**
     * @return true, 表示当前网络可用。false,表示当前网络不可用。
     */
    public static boolean isNetworkAvailable() {
        return getNetworkState() != NETWORK_STATE_NONE;
    }
}
