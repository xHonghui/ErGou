package com.laka.androidlib.base.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.webkit.WebView;

import com.laka.androidlib.R;
import com.laka.androidlib.eventbus.Event;
import com.laka.androidlib.eventbus.EventBusManager;
import com.laka.androidlib.listener.EventListener;
import com.laka.androidlib.util.ActivityManager;
import com.laka.androidlib.util.LogUtils;
import com.laka.androidlib.util.StatusBarUtil;
import com.laka.androidlib.util.network.NetWorkHelper;
import com.laka.androidlib.util.screen.DensityUtils;
import com.laka.androidlib.widget.dialog.LoadingDialog;
import com.laka.androidlib.widget.swipebacklayout.SwipeBackActivity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * @Author:Rayman
 * @Date:2018/5/7
 * @Description：Activity基类 .
 * 整合Activity规范、侧滑手势Activity、网络层统一监听处理、
 **/
public abstract class BaseActivity extends SwipeBackActivity {

    private final String TAG = this.getClass().getSimpleName();
    protected Activity mContext;
    protected ActivityManager mActivityManager;
    private LoadingDialog mLoadingDialog;

    /**
     * description:网络异常处理，默认是托管给Activity处理（创建一个View添加到contentView上面）。
     * 假若不需要托管，例如一些列表的页面。就可以设置isActivityHandleNoNetWork去设置，然后在InterNetChange里面
     * 做判断
     **/
    private NetWorkHelper netWorkHelper;
    private boolean isLostInternet = false;
    protected boolean isActivityHandleNoNetWork = true;
    private NetWorkHelper.NetWorkChangeListener netWorkChangeListener;
    private NetWorkHelper.NetWorkReloadListener netWorkReloadListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DensityUtils.setCustomDensity(this, getApplication());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
        this.setContentView(setContentView());
        mContext = this;
        setStatusBarColor(R.color.black);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mActivityManager = ActivityManager.getInstance();
        mActivityManager.addActivity(this);
        ButterKnife.bind(this);
        EventBusManager.register(this);
        initLoadingDialog();
        initIntent();
        initViews();
        initData();
        initEvent();
        initVariable();
    }

    private void initLoadingDialog() {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        mLoadingDialog = new LoadingDialog(this);
    }

    /**
     * 显示加载框
     */
    public void showLoading() {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        if (!mLoadingDialog.isShowing() && mLoadingDialog != null) {
            mLoadingDialog.show();
        }
    }

    /**
     * 隐藏加载框
     */
    public void dismissLoading() {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 防止Activity销毁，自动保存Fragment状态。
     * 因为在Activity销毁的时候，会保存Fragment的状态
     * 然后在重启Activity的时候，Activity自动恢复之前的Fragment状态
     * 但是前一个Activity可能被释放了，所以会引起getActivity为空的问题
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 0);
            StatusBarUtil.setLightModeNotFullScreen(this, true);
        } else {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, color), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟
        MobclickAgent.onResume(this);
        initNetWorkHelper();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 友盟
        MobclickAgent.onPause(this);
        netWorkHelper.release();
    }

    /**
     * 设置布局Id
     *
     * @return 当前布局Id
     */
    public abstract int setContentView();

    /**
     * 初始化Intent
     */
    public abstract void initIntent();

    /**
     * 初始化UI
     */
    protected abstract void initViews();

    /**
     * 初始化页面数据
     */
    protected abstract void initData();

    /**
     * 初始化页面事件
     */
    protected abstract void initEvent();

    /**
     * 初始化变量
     */
    protected void initVariable() {

    }

    /**
     * 默认实现onEvent方法，子类可按需重载
     *
     * @param action
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String action) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {

    }

    @Override
    public void finish() {
        // 在 finish 中将窗口关闭，防止窗体泄漏
        dismissLoading();
        mLoadingDialog = null;
        super.finish();
    }

    @Override
    protected void onDestroy() {
        // 取消事件通知，放在基类，确保每次都有取消。
        EventBusManager.unRegister(this);
        mActivityManager.removeActivity(this);
        super.onDestroy();
        // LogUtils.info("destory", isDestroyed() + ":" + isFinishing()); // 都为 true
    }

    /**
     * 初始化网路状态切换工具类
     */
    private void initNetWorkHelper() {
        netWorkHelper = new NetWorkHelper(this);
        netWorkHelper.init();
        netWorkChangeListener = new NetWorkHelper.NetWorkChangeListener() {
            @Override
            public void onNetWorkChange(int netWorkState, boolean isConnective) {
                isLostInternet = !isConnective;
                LogUtils.error("输出NetWorkState：" + netWorkState + "\nConnective：" + isConnective);
                if (isActivityHandleNoNetWork) {
                    netWorkHelper.showNetWorkErrorView(isLostInternet);
                }
                onInternetChange(isLostInternet);
            }
        };
        netWorkReloadListener = new NetWorkHelper.NetWorkReloadListener() {
            @Override
            public void onReload() {
                BaseActivity.this.onReload();
            }
        };

        netWorkHelper.setNetWorkChangeListener(netWorkChangeListener);
        netWorkHelper.setNetWorkReloadListener(netWorkReloadListener);
    }

    /**
     * 获取没网络下显示的View
     *
     * @return
     */
    protected void setNetWorkErrorView(View view) {
        if (view == null) {
            // 这里可以设置通用的Vie显示
            return;
        }
        netWorkHelper.setNetWorkErrorView(view);
    }

    /**
     * 网络出错的情况下，重新加载页面数据
     */
    protected void onReload() {

    }

    /**
     * 网络发生变化之后的回调
     */
    protected void onInternetChange(boolean isLostInternet) {
    }

    /**
     * Find出来的View，自带防抖点击功能
     */
    public <T extends View> T setClickView(int id) {

        T view = findViewById(id);
        view.setOnClickListener(new EventListener(this));
        return view;
    }
}
