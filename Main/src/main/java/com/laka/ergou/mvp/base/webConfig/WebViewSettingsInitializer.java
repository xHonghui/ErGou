package com.laka.ergou.mvp.base.webConfig;

import android.annotation.SuppressLint;
import android.os.Build;

import com.laka.androidlib.util.ApplicationUtils;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

/**
 * @function 对传入的webView进行各种settings，返回setting好的webView
 * Created by sming
 */

public class WebViewSettingsInitializer {

    @SuppressLint("SetJavaScriptEnabled")
    public WebView createWebView(final WebView webView) {
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);//设置WebView是否允许执行JavaScript脚本,默认false
        setting.setSupportZoom(false);//WebView是否支持使用屏幕上的缩放控件和手势进行缩放,默认值true
        setting.setBuiltInZoomControls(true);//是否使用内置的缩放机制
        setting.setDisplayZoomControls(false);//使用内置的缩放机制时是否展示缩放控件,默认值true

        setting.setUseWideViewPort(true);//是否支持HTML的“viewport”标签或者使用wide viewport
        setting.setLoadWithOverviewMode(true);//是否允许WebView度超出以概览的方式载入页面,默认false
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//设置布局,会引起WebView的重新布局(relayout),默认值NARROW_COLUMNS

        setting.setRenderPriority(WebSettings.RenderPriority.HIGH);//线程优先级(在API18以上已废弃。不建议调整线程优先级，未来版本不会支持这样做)
        setting.setEnableSmoothTransition(true);//已废弃,将来会成为空操作（no-op）,设置当panning或者缩放或者持有当前WebView的window没有焦点时是否允许其光滑过渡,若为true,WebView会选择一个性能最大化的解决方案。例如过渡时WebView的内容可能不更新。若为false,WebView会保持精度（fidelity）,默认值false。
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);//重写使用缓存的方式，默认值LOAD_DEFAULT
        setting.setPluginState(WebSettings.PluginState.ON);//在API18以上已废弃。未来将不支持插件,不要使用
        setting.setJavaScriptCanOpenWindowsAutomatically(true);//让JavaScript自动打开窗口,默认false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting.setMixedContentMode(2);
        }
        //webview 中localStorage无效的解决方法
        setting.setDomStorageEnabled(true);//DOM存储API是否可用,默认false
        setting.setAppCacheMaxSize(1024 * 1024 * 8);//设置应用缓存内容的最大值
        String appCachePath = ApplicationUtils.getContext().getCacheDir().getAbsolutePath();
        setting.setAppCachePath(appCachePath);//设置应用缓存文件的路径
        setting.setAllowFileAccess(true);//是否允许访问文件,默认允许
        setting.setAppCacheEnabled(true);//应用缓存API是否可用,默认值false,结合setAppCachePath(String)使用
        return webView;
    }
}
