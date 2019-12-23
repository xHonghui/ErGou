package com.laka.androidlib.util.network;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.FrameLayout;

import com.laka.androidlib.net.thread.ThreadManager;
import com.laka.androidlib.util.ContextUtil;
import com.laka.androidlib.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

/**
 * @Author:Rayman
 * @Date:2018/7/24
 * @Description:网络状态变化工具类，嵌入到Activity里面
 */

public class NetWorkHelper {

    private final String TAG = this.getClass().getSimpleName();
    public static final String NET_WORK_BROAD_CAST_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    private Context context;

    /**
     * description:参数设置
     **/
    private int netState = -1;
    protected static boolean isLostInternet = false;

    /**
     * description:UI配置
     **/
    private View mNetWorkErrorView;

    /**
     * description:网络监听
     **/
    private BroadcastReceiver mNetWorkChangeReceiver;
    private IntentFilter intentFilter;
    private boolean isRegister = false;


    /**
     * description:回调
     **/
    private NetWorkChangeListener netWorkChangeListener;
    private NetWorkReloadListener netWorkReloadListener;

    public NetWorkHelper(Context context) {
        WeakReference weakReference = new WeakReference(context);
        this.context = (Context) weakReference.get();
    }

    /**
     * 初始化工具类
     */
    public void init() {
        mNetWorkChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                netState = NetworkUtils.getNetworkState();
                //控制第一次打开页面不进入onInterNetChange，只有当从无网络状态回到有网络状态时，才调用
                ThreadManager.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (netState == NetworkUtils.NETWORK_STATE_NONE) {
                            if (netWorkChangeListener != null) {
                                isLostInternet = true;
                                netWorkChangeListener.onNetWorkChange(netState, !isLostInternet);
                            }
                        } else {
                            if (isLostInternet) {
                                isLostInternet = false;
                                if (netWorkChangeListener != null) {
                                    netWorkChangeListener.onNetWorkChange(netState, !isLostInternet);
                                }
                            }
                        }
                    }
                });
            }
        };

        intentFilter = new IntentFilter();
        intentFilter.addAction(NET_WORK_BROAD_CAST_ACTION);
        if (ContextUtil.isValidContext(context)) {
            context.registerReceiver(mNetWorkChangeReceiver, intentFilter);
        }
        isRegister = true;
    }

    /**
     * 释放当前广播监听器
     */
    public void release() {
        try {
            if (context != null) {
                context.unregisterReceiver(mNetWorkChangeReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        netWorkChangeListener = null;
        netWorkReloadListener = null;
    }

    /**
     * 获取当前Activity的ContentView，然后添加一个无网络页面
     * 当前页面可以通过点击，然后重新加载数据
     *
     * @param netWorkError
     */
    public void showNetWorkErrorView(boolean netWorkError) {
        if (mNetWorkErrorView != null) {
            FrameLayout contentParent = null;
            if (ContextUtil.isValidContext(context)) {
                Activity activity = (Activity) context;
                contentParent = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            }
            mNetWorkErrorView.setVisibility(View.GONE);
            if (mNetWorkErrorView.getParent() == null) {
                contentParent.addView(mNetWorkErrorView);
            }
        }

        if (mNetWorkErrorView != null) {
            if (netWorkError) {
                mNetWorkErrorView.setVisibility(View.VISIBLE);
            } else {
                mNetWorkErrorView.setVisibility(View.GONE);
            }
        }
    }

    public void setNetWorkErrorView(View view) {
        this.mNetWorkErrorView = view;
        mNetWorkErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (netWorkReloadListener != null) {
                    netWorkReloadListener.onReload();
                }
            }
        });
    }

    public void setNetWorkChangeListener(NetWorkChangeListener netWorkChangeListener) {
        this.netWorkChangeListener = netWorkChangeListener;
    }

    public void setNetWorkReloadListener(NetWorkReloadListener netWorkReloadListener) {
        this.netWorkReloadListener = netWorkReloadListener;
    }

    public static boolean isLostInternet() {
        return isLostInternet;
    }

    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getLocalHostIp() {
        String hostIp = "";
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return hostIp;
    }

    /**
     * 获取IP地址
     *
     * @return
     */
    public static String getNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String line = "";
        try {
            infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                while ((line = reader.readLine()) != null) strber.append(line + "\n");
                inStream.close();                // 从反馈的结果中提取出IP地址
                int start = strber.indexOf("{");
                int end = strber.indexOf("}");
                String json = strber.substring(start, end + 1);
                if (json != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        line = jsonObject.optString("cip");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    public interface NetWorkChangeListener {

        /**
         * 网络监听回调，仅有断网和网络正常的情况
         *
         * @param netWorkState 当前网络状态下的TAG
         * @param isConnective 是否可用
         */
        void onNetWorkChange(int netWorkState, boolean isConnective);
    }

    public interface NetWorkReloadListener {

        void onReload();

    }

}
