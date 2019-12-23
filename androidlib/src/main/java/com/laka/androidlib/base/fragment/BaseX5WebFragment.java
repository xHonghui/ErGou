package com.laka.androidlib.base.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;

import com.laka.androidlib.BuildConfig;
import com.laka.androidlib.util.LogUtils;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import com.laka.androidlib.base.activity.AbstractWebViewActivity;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @Author:Rayman
 * @Date:2019/1/7
 * @Description:基于BaseFragment的网页Fragment基类 .
 * 主要运用在部分Fragment中加载WEB页面，内核使用腾讯的X5WebView
 */

public abstract class BaseX5WebFragment extends BaseFragment {

    private final static String TAG = AbstractWebViewActivity.class.getCanonicalName();

    private final static String NOT_INIT_NATIVE_WEB_VIEW = "请先初始化NativeWebView，通过设置isNativeWeb = true";
    private final static String NOT_INIT_X5_WEB_VIEW = "请先初始化X5WebView，通过设置isNativeWeb = false";
    private final static String NOT_INIT_COMPLETE = "当前为设置WebView初始化监听函数，不允许当前操作";

    protected WebView mX5WebView;
    protected X5WebViewClient mX5WebViewClient;
    protected X5WebChromeClient mX5WebChromeClient;

    protected android.webkit.WebView mNativeWebView;
    protected NativeWebViewClient mNativeWebViewClient;
    protected NativeWebChromeClient mNativeWebChromeClient;

    /**
     * description:因为考虑到部分Activity可能在一初始化的时候就直接调用WebView然后去设置什么。
     * 但是这个是不允许的，因为Activity的initViews和Fragment的initViews不是完全同步的，存在毫秒级的差别
     * 所以不能直接提供使用，这边多加一个回调表示WebView在Fragment中已经初始化完毕了。只有初始化完毕的WebView才能被获取
     **/
    private WebViewEventCallBack mWebViewEventCallback;

    private boolean isNativeKernel = true;

    /**
     * 初始化WebView相关属性
     */
    public void switchWebViewKernel(boolean isNativeKernel) {
        mWebViewEventCallback = getWebViewEventCallback();
        if (mWebViewEventCallback == null) {
            throw new IllegalArgumentException(NOT_INIT_COMPLETE);
        }

        this.isNativeKernel = isNativeKernel;
        if (isNativeKernel) {
            if (mNativeWebView == null) {
                throw new IllegalStateException(NOT_INIT_NATIVE_WEB_VIEW);
            }
            mNativeWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mNativeWebView.setDrawingCacheEnabled(true);
            initNativeWebSettings(mNativeWebView.getSettings());
            mNativeWebViewClient = new NativeWebViewClient();
            mNativeWebChromeClient = new NativeWebChromeClient();
            mNativeWebView.setWebViewClient(mNativeWebViewClient);
            mNativeWebView.setWebChromeClient(mNativeWebChromeClient);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
                mX5WebView.setWebContentsDebuggingEnabled(true); //设置调试模式
            }
        } else {
            if (mX5WebView == null) {
                throw new IllegalStateException(NOT_INIT_X5_WEB_VIEW);
            }
            mX5WebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            initX5WebSettings(mX5WebView.getSettings());
            mX5WebViewClient = new X5WebViewClient();
            mX5WebChromeClient = new X5WebChromeClient();
            mX5WebView.setWebViewClient(mX5WebViewClient);
            mX5WebView.setWebChromeClient(mX5WebChromeClient);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && BuildConfig.DEBUG) {
                mX5WebView.setWebContentsDebuggingEnabled(true);
            }
        }
        mWebViewEventCallback.onWebViewInitCompleted();
        //LogUtils.info("BaseWebFragment初始化Web控件时间：" + System.currentTimeMillis());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNativeKernel) {
            if (mNativeWebView != null) {
                mNativeWebView.onResume();
            }
        } else {
            if (mX5WebView != null) {
                mX5WebView.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isNativeKernel) {
            if (mNativeWebView != null) {
                mNativeWebView.onPause();
            }
        } else {
            if (mX5WebView != null) {
                mX5WebView.onPause();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isNativeKernel) {
            if (mNativeWebView != null) {
                ((ViewGroup) mNativeWebView.getParent()).removeView(mNativeWebView);
                mNativeWebView.stopLoading();
                mNativeWebView.clearHistory();
                mNativeWebView.removeAllViews();
                mNativeWebView.destroy();
                mNativeWebView = null;
            }
        } else {
            if (mX5WebView != null) {
                ((ViewGroup) mX5WebView.getParent()).removeView(mX5WebView);
                mX5WebView.stopLoading();
                mX5WebView.clearHistory();
                mX5WebView.removeAllViews();
                mX5WebView.destroy();
                mX5WebView = null;
            }
        }
    }

    public void onBackPressed() {
        if (isNativeKernel) {
            if (mNativeWebView != null && mNativeWebView.canGoBack()) {
                mNativeWebView.goBack();
            } else {
                super.mActivity.onBackPressed();
            }
        } else {
            if (mX5WebView != null && mX5WebView.canGoBack()) {
                mX5WebView.goBack();
            } else {
                super.mActivity.onBackPressed();
            }
        }
    }

    /**
     * 加载url
     *
     * @param url url
     */
    public void loadUrl(String url) {
        if (isNativeKernel) {
            if (mNativeWebView == null) {
                throw new IllegalStateException(NOT_INIT_NATIVE_WEB_VIEW);
            }
            mNativeWebView.loadUrl(url);
        } else {
            if (mX5WebView == null) {
                throw new IllegalStateException(NOT_INIT_X5_WEB_VIEW);
            }
            mX5WebView.loadUrl(url);
        }
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
     * @param webViewClient {@link X5WebViewClient}
     */
    protected void setX5WebViewClient(X5WebViewClient webViewClient) {
        this.mX5WebViewClient = webViewClient;
        this.mX5WebView.setWebViewClient(mX5WebViewClient);
    }

    /**
     * 设置自定义的WebChromeClient
     *
     * @param webChromeClient {@link X5WebChromeClient}
     */
    protected void setX5WebChromeClient(X5WebChromeClient webChromeClient) {
        this.mX5WebChromeClient = webChromeClient;
        this.mX5WebView.setWebChromeClient(mX5WebChromeClient);
    }

    /**
     * 初始化X5WebSettings，如需修改设置，可复写该方法
     */
    protected void initX5WebSettings(@NonNull WebSettings webSetting) {

        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setBlockNetworkImage(false);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
    }

    /**
     * 设置自定义的WebViewClient
     *
     * @param webViewClient {@link X5WebViewClient}
     */
    protected void setNativeWebViewClient(NativeWebViewClient webViewClient) {
        this.mNativeWebViewClient = webViewClient;
        this.mNativeWebView.setWebViewClient(mNativeWebViewClient);
    }

    /**
     * 设置自定义的WebChromeClient
     *
     * @param webChromeClient {@link X5WebChromeClient}
     */
    protected void setNativeWebChromeClient(NativeWebChromeClient webChromeClient) {
        this.mNativeWebChromeClient = webChromeClient;
        this.mNativeWebView.setWebChromeClient(mNativeWebChromeClient);
    }

    /**
     * 初始化原生内核WebSettings，如需修改设置，可复写该方法
     */
    protected void initNativeWebSettings(@NonNull android.webkit.WebSettings webSetting) {

        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setBlockNetworkImage(false);
        webSetting.setLayoutAlgorithm(android.webkit.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(android.webkit.WebSettings.PluginState.ON_DEMAND);
        webSetting.setCacheMode(android.webkit.WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
    }

    /**
     * X5内核，处理通知请求事件
     */
    protected class X5WebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mWebViewEventCallback.onWebViewLoadStart();
            onLoadStart();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mWebViewEventCallback.onWebViewLoadFinish();
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

        @Override
        public void onReceivedError(WebView webView, int i, String s, String s1) {
            super.onReceivedError(webView, i, s, s1);
            // 页面加载失败时候的回调。当前回调是优先于onPageFinish的，所以在这里做统一错误处理
            if (mWebViewEventCallback != null) {
                LogUtils.info("load error-------：s=" + s + ",s1=" + s1);
                mWebViewEventCallback.onWebViewLoadError();
            }
        }

    }

    /**
     * X5内核，处理与JS的交互
     */
    protected class X5WebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mWebViewEventCallback.onWebViewLoading(newProgress);
            onLoadProgress(newProgress);
        }
    }

    /**
     * 原生内核，处理通知请求事件
     */
    protected class NativeWebViewClient extends android.webkit.WebViewClient {

        @Override
        public void onPageStarted(android.webkit.WebView view, String url, Bitmap favicon) {
            mWebViewEventCallback.onWebViewLoadStart();
            onLoadStart();
        }

        @Override
        public void onPageFinished(android.webkit.WebView view, String url) {
            mWebViewEventCallback.onWebViewLoadFinish();
            onLoadFinish(view.getTitle());
        }

        @Override
        public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public android.webkit.WebResourceResponse shouldInterceptRequest(android.webkit.WebView view, String url) {
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public void onReceivedError(android.webkit.WebView view, WebResourceRequest request, android.webkit.WebResourceError error) {
            super.onReceivedError(view, request, error);
            // 页面加载失败时候的回调。当前回调是优先于onPageFinish的，所以在这里做统一错误处理
            if (mWebViewEventCallback != null) {
                LogUtils.info("load error-------：error=" + error);
                mWebViewEventCallback.onWebViewLoadError();
            }
        }
    }

    /**
     * 原生内核，处理与JS的交互
     */
    protected class NativeWebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(android.webkit.WebView view, int newProgress) {
            mWebViewEventCallback.onWebViewLoading(newProgress);
            onLoadProgress(newProgress);
        }
    }

    public WebView getX5WebView() {
        if (mWebViewEventCallback == null) {
            throw new IllegalArgumentException(NOT_INIT_COMPLETE);
        }
        return mX5WebView;
    }

    public X5WebViewClient getX5WebViewClient() {
        if (mWebViewEventCallback == null) {
            throw new IllegalArgumentException(NOT_INIT_COMPLETE);
        }
        return mX5WebViewClient;
    }

    public X5WebChromeClient getX5WebChromeClient() {
        if (mWebViewEventCallback == null) {
            throw new IllegalArgumentException(NOT_INIT_COMPLETE);
        }
        return mX5WebChromeClient;
    }

    public android.webkit.WebView getNativeWebView() {
        if (mWebViewEventCallback == null) {
            throw new IllegalArgumentException(NOT_INIT_COMPLETE);
        }
        return mNativeWebView;
    }

    public NativeWebViewClient getNativeWebViewClient() {
        if (mWebViewEventCallback == null) {
            throw new IllegalArgumentException(NOT_INIT_COMPLETE);
        }
        return mNativeWebViewClient;
    }

    public NativeWebChromeClient getNativeWebChromeClient() {
        if (mWebViewEventCallback == null) {
            throw new IllegalArgumentException(NOT_INIT_COMPLETE);
        }
        return mNativeWebChromeClient;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mActivity.getWindow();
            window.setStatusBarColor(color);
        } else {
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

    }

    /**
     * 设置当前WebView加载监听器
     *
     * @return
     */
    public abstract WebViewEventCallBack getWebViewEventCallback();

    public interface WebViewEventCallBack {

        /**
         * WebView初始化完毕
         */
        void onWebViewInitCompleted();

        /**
         * WebView开始加载
         */
        void onWebViewLoadStart();

        /**
         * WebView正在加载
         *
         * @param progress 加载进度
         */
        void onWebViewLoading(int progress);

        /**
         * WebView加载完毕
         */
        void onWebViewLoadFinish();

        /**
         * WebView加载失败
         */
        void onWebViewLoadError();
    }
}
