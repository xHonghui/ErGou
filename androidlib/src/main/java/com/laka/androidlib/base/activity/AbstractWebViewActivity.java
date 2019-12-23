package com.laka.androidlib.base.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.laka.androidlib.util.StringUtils;

/**
 * @ClassName: AbstractWebViewActivity
 * @Description: 加载WebView的Activity
 * @Author: chuan
 * @Date: 09/03/2018
 */

public abstract class AbstractWebViewActivity extends BaseActivity {
    private final static String TAG = AbstractWebViewActivity.class.getCanonicalName();

    private final static String NOT_INIT_WEB_VIEW = "请先初始化WebView";

    protected WebView mWebView;
    private _WebViewClient mWebViewClient;
    private _WebChromeClient mWebChromeClient;

    @Override
    protected void initVariable() {
        super.initVariable();

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
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
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

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
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

        if (StringUtils.isTrimEmpty(url)) {
            return;
        }

        syncCookie(url);
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
     *
     * @param webSetting {@link WebSettings}
     */
    protected void initWebSettings(@NonNull WebSettings webSetting) {
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSetting.setAllowFileAccess(true);
//       //webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        webSetting.setSupportZoom(true);
//        webSetting.setBuiltInZoomControls(true);
//        webSetting.setSupportMultipleWindows(true);
//        webSetting.setAppCacheEnabled(true);
//        webSetting.setDomStorageEnabled(true);
//        webSetting.setGeolocationEnabled(true);
//        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
//        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
//        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置载入页面自适应手机屏幕，居中显示
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= 21) {//适配https中打不开图片问题
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
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
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            onLoadError(error);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();  //等待授权。用于进行https请求
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//            syncCookie(url);
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

}
