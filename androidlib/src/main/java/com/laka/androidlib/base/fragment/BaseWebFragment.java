package com.laka.androidlib.base.fragment;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.laka.androidlib.base.activity.AbstractWebViewActivity;
import com.laka.androidlib.util.ActivityManager;

/**
 * @Author:Rayman
 * @Date:2019/1/7
 * @Description:基于BaseFragment的网页Fragment基类 .
 * 主要运用在部分Fragment中加载WEB页面，内核使用原生浏览器
 */
public abstract class BaseWebFragment extends BaseFragment {

    private final static String TAG = AbstractWebViewActivity.class.getCanonicalName();

    private final static String NOT_INIT_WEB_VIEW = "请先初始化WebView";

    protected WebView mWebView;
    protected _WebViewClient mWebViewClient;
    protected _WebChromeClient mWebChromeClient;
    protected ActivityManager mActivityManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVariable();
    }

    /**
     * 初始化WebView相关属性
     */
    protected void initVariable() {
        if (mWebView == null) {
            throw new IllegalStateException(NOT_INIT_WEB_VIEW);
        }

        initWebSettings(mWebView.getSettings());
        mWebView.setWebViewClient(
                mWebViewClient == null ? new _WebViewClient() : mWebViewClient);
        mWebView.setWebChromeClient(
                mWebChromeClient == null ? new _WebChromeClient() : mWebChromeClient);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.stopLoading();
            mWebView.clearHistory();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }

    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.mActivity.onBackPressed();
        }
    }

    /**
     * 加载url
     *
     * @param url url
     */
    protected void loadUrl(String url) {
        if (mWebView == null) {
            throw new IllegalStateException(NOT_INIT_WEB_VIEW);
        }
//        mX5WebView.setLayerType(LAYER_TYPE_NONE);
        //  mX5WebView.setDrawingCacheEnabled(true);

        mWebView.loadUrl(url);
    }

    /**
     * 初始化Cookie
     *
     * @param url url
     */
    protected void syncCookie(String url) {

    }

    /**
     * 开始加载的回调
     */
    protected void onLoadStart() {

    }

    /**
     * 加载进度变化的回调
     *
     * @param progress 加载进度
     */
    protected void onLoadProgress(int progress) {
    }

    /**
     * 加载结束的回调
     */
    protected void onLoadFinish(String title) {
    }

    /**
     * 加载出错的回调
     *
     * @param error 错误 {@link WebResourceError}
     */
    protected void onLoadError(WebResourceError error) {
    }

    /**
     * 设置自定义的WebViewClient
     *
     * @param webViewClient {@link _WebViewClient}
     */
    protected void setWebViewClient(_WebViewClient webViewClient) {
        this.mWebViewClient = webViewClient;
    }

    /**
     * 设置自定义的WebChromeClient
     *
     * @param webChromeClient {@link _WebChromeClient}
     */
    protected void setWebChromeClient(_WebChromeClient webChromeClient) {
        this.mWebChromeClient = webChromeClient;
    }

    /**
     * 初始化WebSettings，如需修改设置，可复写该方法
     */
    protected void initWebSettings(@NonNull WebSettings webSetting) {

        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }


    //处理通知请求事件
    protected class _WebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            onLoadStart();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            onLoadFinish(view.getTitle());
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }
    }

    //处理与JS的交互
    protected class _WebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            onLoadProgress(newProgress);
        }
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mActivity.getWindow();
            window.setStatusBarColor(color);
        } else {
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

    }
}
